package com.jhowes.wordlistsql;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Implements a simple Adapter for a RecyclerView.
 * Demonstrates how to add a click handler for each item in the ViewHolder.
 */
public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {

    WordListOpenHelper mDB;

    /**
     *  Custom view holder with a text view and two buttons.
     */
    class WordViewHolder extends RecyclerView.ViewHolder {
        public final TextView wordItemView;
        public final TextView definitionItemView;
        Button delete_button;
        Button edit_word_button;
        Button edit_definition_button;

        public WordViewHolder(View itemView) {
            super(itemView);
            wordItemView = (TextView) itemView.findViewById(R.id.word);
            definitionItemView = (TextView) itemView.findViewById(R.id.definition);
            delete_button = (Button)itemView.findViewById(R.id.delete_button);
            edit_word_button = (Button)itemView.findViewById(R.id.edit_word_button);
            edit_definition_button = (Button) itemView.findViewById(R.id.edit_definition_button);
        }
    }

    private static final String TAG = WordListAdapter.class.getSimpleName();

    public static final String EXTRA_ID = "ID";
    public static final String EXTRA_WORD = "WORD";
    public static final String EXTRA_DEFINITION = "DEFINITION";
    public static final String EXTRA_POSITION = "POSITION";

    private final LayoutInflater mInflater;
    Context mContext;

    public WordListAdapter(Context context, WordListOpenHelper db) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mDB = db;
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.wordlist_item, parent, false);
        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        // Get an item from the database and display it
        WordItem current = mDB.query(position);
        holder.wordItemView.setText(current.getWord());
        holder.definitionItemView.setText(current.getDefinition());

        final WordViewHolder h = holder;
        // Attach a click listener to the DELETE button
        holder.delete_button.setOnClickListener(
                new MyButtonOnClickListener(current.getId(), null, null){
                    @Override
                    public void onClick(View v){
                        int deleted = mDB.delete(id);
                        if(deleted >= 0) notifyItemRemoved(h.getAdapterPosition());
                    }
                });
        holder.edit_word_button.setOnClickListener(
                new MyButtonOnClickListener(current.getId(), current.getWord(),
                        current.getDefinition()){
                    @Override
                    public void onClick(View v){
                        Intent intent = new Intent(mContext, EditWordActivity.class);
                        intent.putExtra(EXTRA_ID, id);
                        intent.putExtra(EXTRA_POSITION, h.getAdapterPosition());
                        intent.putExtra(EXTRA_WORD, word);
                        // Start an empty edit activity
                        ((Activity) mContext).startActivityForResult(
                                intent, MainActivity.WORD_EDIT);
                    }
                }
        );
        holder.edit_definition_button.setOnClickListener(
                new MyButtonOnClickListener(current.getId(), current.getWord(),
                        current.getDefinition()){
                    @Override
                    public void onClick(View v){
                        Intent intent = new Intent(mContext, EditWordActivity.class);
                        intent.putExtra(EXTRA_ID, id);
                        intent.putExtra(EXTRA_POSITION, h.getAdapterPosition());
                        intent.putExtra(EXTRA_WORD, definition);
                        intent.putExtra(EXTRA_DEFINITION, 10);
                        ((Activity) mContext).startActivityForResult(
                                intent, MainActivity.WORD_EDIT);
                    }
                }
        );

    }

    @Override
    public int getItemCount() {
        return (int) mDB.count();
    }
}