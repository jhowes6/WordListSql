package com.jhowes.wordlistsql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WordListOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = WordListOpenHelper.class.getSimpleName();
    private static final int DATABASE_VERSION = 1;
    private static final String WORD_LIST_TABLE = "word_entries";
    private static final String DATABASE_NAME = "wordlist";

    // Column names
    public static final String KEY_ID = "_id";
    public static final String KEY_WORD = "word";
    public static final String KEY_DEFINITION = "definition";

    // String array of columns
    private static final String[] COLUMNS = { KEY_ID, KEY_WORD, KEY_DEFINITION };

    // SQL query that creates the table
    private static final String WORD_LIST_TABLE_CREATE =
            "CREATE TABLE " + WORD_LIST_TABLE + " ( " + KEY_ID +
                    " INTEGER PRIMARY KEY, " + KEY_WORD + " TEXT, " +
                    KEY_DEFINITION + " TEXT);";

    // References to the database
    private SQLiteDatabase mWritableDB;
    private SQLiteDatabase mReadableDB;

    public WordListOpenHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(WORD_LIST_TABLE_CREATE);
        fillDatabaseWithData(db);

    }
    private void fillDatabaseWithData(SQLiteDatabase db){
        String[] words = { "Frogger", "Gex", "Mario", "Luigi", "Sonic", "Spyro",
                "Crash Bandicoot", "Rezz", "Jak", "Daxter", "Clank" };
        String[] definitions = { "frog boi", "lizard boi", "italian boi", "italian boi",
                "hedgehog boi", "dragon boi", "bandicoot boi", "robo boi", "white boi",
                "squirrel boi", "robo friend" };
        // Create a container for the data
        // (A ContentValues stores the data for one row as key-value pairs)
        // (The key is the name of the column; the value is the value for the column)
        ContentValues values = new ContentValues();
        for(int i = 0; i < words.length; i++){
            values.put(KEY_WORD, words[i]);
            values.put(KEY_DEFINITION, definitions[i]);
            db.insert(WORD_LIST_TABLE, null, values);
        }
    }

    /**
     * Returns a WordItem from the row at "position"
     * @param position
     * @return
     */
    public WordItem query(int position){
        // construct a query that returns only the nth row of the result
        String query = "SELECT * FROM " + WORD_LIST_TABLE + " ORDER BY " +
                KEY_WORD + " ASC " + "LIMIT " + position + ",1";
        // Instantiate a cursor variable to null to hold the result from the database
        Cursor cursor = null;
        // Instantiate a WordItem entry
        WordItem entry = new WordItem();
        try{
            if(mReadableDB == null) mReadableDB = getReadableDatabase();
            cursor = mReadableDB.rawQuery(query, null);
            // move the cursor to the first item
            cursor.moveToFirst();
            // Initialize WordItem based on the values returned by the cursor
            entry.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            entry.setWord(cursor.getString(cursor.getColumnIndex(KEY_WORD)));
            entry.setDefinition(cursor.getString(cursor.getColumnIndex(KEY_DEFINITION)));
        } catch(Exception e){
            Log.d(TAG, "EXCEPTION! " + e);
        } finally{
            cursor.close();
            return entry;
        }
    }
    public long insert(String word){
        long newId = 0;
        // Create a ContentValues for the row data
        ContentValues values = new ContentValues();
        values.put(KEY_WORD, word);
        values.put(KEY_DEFINITION, " ");
        try{
            if(mWritableDB == null) mWritableDB = getWritableDatabase();
            // Insert the row
            newId = mWritableDB.insert(WORD_LIST_TABLE, null, values);
        }catch(Exception e){
            Log.d(TAG, "INSERT EXCEPTION! " + e.getMessage());
        }finally {
            return newId;
        }
    }

    /**
     *
     * @return the number of entries in the database
     */
    public long count(){
        if(mReadableDB == null) mReadableDB = getReadableDatabase();
        return DatabaseUtils.queryNumEntries(mReadableDB, WORD_LIST_TABLE);
    }

    /**
     *  Deletes an entry from the database
     *
     * @param id - id of the item to delete
     * @return - number of rows deleted
     */
    public int delete(int id){
        int deleted = 0;
        try{
            if(mWritableDB == null) mWritableDB = getWritableDatabase();
            deleted = mWritableDB.delete(WORD_LIST_TABLE, KEY_ID + " = ? ",
                    new String[]{String.valueOf(id)});
        } catch(Exception e){
            Log.d(TAG, "DELETE EXCEPTION! " + e.getMessage());
        }
        return deleted;
    }

    /**
     *  Edits a word in the database
     *
     * @param id - id of word to be edited
     * @param word
     * @return
     */
    public int update(int id, String word){
        int numberRowsUpdated = -1;
        try{
            if(mWritableDB == null) mWritableDB = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_WORD, word);
            numberRowsUpdated = mWritableDB.update(WORD_LIST_TABLE,
                    values, KEY_ID + " = ?",
                    new String[]{String.valueOf(id)});
        } catch(Exception e){
            Log.d(TAG, "EDIT EXCEPTION! " + e.getMessage());
        }
        return numberRowsUpdated;
    }
    public int updateDefinition(int id, String word){
        int numberRowsUpdated = -1;
        try{
            if(mWritableDB == null) mWritableDB = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_DEFINITION, word);
            numberRowsUpdated = mWritableDB.update(WORD_LIST_TABLE,
                    values, KEY_ID + " = ?",
                    new String[]{String.valueOf(id)});
        } catch(Exception e){
            Log.d(TAG, "EDIT EXCEPTION! " + e.getMessage());
        }
        return numberRowsUpdated;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(WordListOpenHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to " +
                        newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + WORD_LIST_TABLE);
        onCreate(db);

    }
}
