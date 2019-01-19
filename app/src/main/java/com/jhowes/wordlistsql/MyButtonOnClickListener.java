package com.jhowes.wordlistsql;

import android.view.View;

/**
 * Instantiated for the Edit and Delete buttons in WordListAdapter.
 */
public class MyButtonOnClickListener implements View.OnClickListener {
    private static final String TAG = View.OnClickListener.class.getSimpleName();

    int id;
    String word, definition;

    public MyButtonOnClickListener(int id, String word, String def) {
        this.id = id;
        this.word = word;
        this.definition = def;
    }

    public void onClick(View v) {
        // Implemented in WordListAdapter
    }
}