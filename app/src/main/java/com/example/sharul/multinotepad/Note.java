package com.example.sharul.multinotepad;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sharul on 27-02-2017.
 */

public class Note implements Serializable
{
    private String note;
    private String title;
    private String datetime;

    public Note(String n,String t, String d)
    {
        note = n;
        title = t;
        datetime = d;
    }

    public String getDatetime() { return datetime; }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
