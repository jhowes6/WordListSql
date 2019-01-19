package com.jhowes.wordlistsql;

public class WordItem {

    private int mId;
    private String mWord;
    private String mDefinition;

    public WordItem(){

    }


    public int getId() {
        return mId;
    }

    public String getWord() {
        return mWord;
    }

    public String getDefinition() {
        return mDefinition;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setWord(String word) {
        mWord = word;
    }

    public void setDefinition(String definition) {
        mDefinition = definition;
    }
}
