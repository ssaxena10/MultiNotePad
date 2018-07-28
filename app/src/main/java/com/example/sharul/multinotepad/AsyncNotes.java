package com.example.sharul.multinotepad;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Sharul on 03-03-2017.
 */

public class AsyncNotes extends AsyncTask<String, Integer, String>{

    private MNotePad mainAct;

    public AsyncNotes(MNotePad main)
    {
        mainAct = main;
    }
    @Override
    protected void onPreExecute()
    {

    }
    @Override
    protected void onPostExecute(String s)
    {
        ArrayList<Note> list = parseJSON(s);
        mainAct.update(list);
    }
    @Override
    protected String doInBackground(String... strings) {
        return read();
    }

    private String read()
    {
        try
        {
            InputStream inputStream = mainAct.getApplicationContext().openFileInput("Note.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

        return new String(buffer, "UTF-8");
    } catch (FileNotFoundException e) { return null;
    } catch (Exception e) { return null; }
    }

    private ArrayList<Note> parseJSON(String s) {
        ArrayList<Note> notesList = new ArrayList<>();

        try {
            JSONArray list = new JSONArray(s);
            JSONArray sortedArray = new JSONArray();
            List<JSONObject> jsonVals = new ArrayList<JSONObject>();
            for (int i = 0; i < list.length(); i++) {
                jsonVals.add(list.getJSONObject(i));
            }

            Collections.sort( jsonVals, new Comparator<JSONObject>() {
                private static final String KEY_NAME = "datetime";
                @Override
                public int compare(JSONObject a, JSONObject b) {
                    String valA = new String();
                    String valB = new String();

                    try {
                        valA = (String) a.get(KEY_NAME);
                        valB = (String) b.get(KEY_NAME);
                    }
                    catch (JSONException e) {

                    }
                    return -valA.compareTo(valB);
                }
            });

            for (int i = 0; i < list.length(); i++) {
                sortedArray.put(jsonVals.get(i));
            }

            for (int i = 0; i < sortedArray.length(); i++) {
                JSONObject jNotes = (JSONObject) sortedArray.get(i);
                String title = jNotes.getString("title");
                String datetime = jNotes.getString("datetime");
                String notes = jNotes.getString("notes");
                notesList.add(new Note(notes, title, datetime));
            }

            return notesList;
        } catch (Exception e) { return null; }
    }
}
