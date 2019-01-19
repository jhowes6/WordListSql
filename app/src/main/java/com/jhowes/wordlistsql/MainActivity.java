package com.jhowes.wordlistsql;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

/**
 * Implements a RecyclerView that displays a list of words from a SQL database.
 * - Clicking the fab button opens a second activity to add a word to the database.
 * - Clicking the Edit button opens an activity to edit the current word in the database.
 * - Clicking the Delete button deletes the current word from the database.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final int WORD_EDIT = 1;
    public static final int WORD_ADD = -1;
    public static final int DEFINITION_EDIT = 2;

    private RecyclerView mRecyclerView;
    private WordListAdapter mAdapter;

    private WordListOpenHelper DB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the database
        DB = new WordListOpenHelper(this);



        // Create recycler view.
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        // Create an mAdapter and supply the data to be displayed.
        mAdapter = new WordListAdapter(this, DB);
        // Connect the mAdapter with the recycler view.
        mRecyclerView.setAdapter(mAdapter);
        // Give the recycler view a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Add a floating action click handler for creating new entries.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start empty edit activity.
                Intent intent = new Intent(getBaseContext(), EditWordActivity.class);
                startActivityForResult(intent, WORD_EDIT);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check that the result is from the correct activity and get the word
        // from the extras
        if(requestCode == WORD_EDIT){
            if(resultCode == RESULT_OK){
                String word = data.getStringExtra(EditWordActivity.EXTRA_REPLY);
                // Check if we have been passed an id with the extras
                if(!TextUtils.isEmpty(word)){
                    int id = data.getIntExtra(WordListAdapter.EXTRA_ID, -99);
                    if(id == WORD_ADD){
                        DB.insert(word);
                    } else if( id >= 0){
                        if(data.getIntExtra(WordListAdapter.EXTRA_DEFINITION, -1 ) == 10){
                            DB.updateDefinition(id, word);
                        } else{
                            DB.update(id, word);
                        }

                    }
                    // Notify the adapter that the data has changed
                    mAdapter.notifyDataSetChanged();
                } else{
                    Toast.makeText(getApplicationContext(), R.string.empty_not_saved,
                            Toast.LENGTH_SHORT).show();
                }
            }
        } else if(requestCode == DEFINITION_EDIT){

        }
    }
}
