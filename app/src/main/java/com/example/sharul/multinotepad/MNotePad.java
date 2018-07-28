package com.example.sharul.multinotepad;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MNotePad extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{

    private RecyclerView recyclerView;
    private NoteAdapter myAdapter;
    private List<Note> noteslist = new ArrayList<>();
    private static final int REQ=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mnote_pad);

        recyclerView = (RecyclerView)findViewById(R.id.recycler);
        myAdapter = new NoteAdapter(noteslist, this);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initializeFiles();
        new AsyncNotes(this).execute();


    }

    private void initializeFiles()
    {
        File f = this.getFileStreamPath("Note.json");
        if(!f.exists())
        {
            try
            {
                FileOutputStream fout = getApplicationContext().openFileOutput("Note.json",Context.MODE_PRIVATE);

                OutputStreamWriter writer = new OutputStreamWriter(fout);
                writer.write("[]");
                writer.close();
            }
            catch (Exception e)
            {

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.about:
                Intent intent_about = new Intent(this, About.class);
                startActivity(intent_about);
                return true;

            case R.id.add:
                Intent intent_add = new Intent(this, NewNote.class);
                intent_add.putExtra("",-1);
                startActivityForResult(intent_add, REQ);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        writeJson();
    }

    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildAdapterPosition(v);
        Note n = noteslist.get(pos);
        Intent intent = new Intent(MNotePad.this,NewNote.class);
        intent.putExtra("",pos);
        intent.putExtra(Note.class.getName(),n);
        startActivityForResult(intent, REQ);

    }


    @Override
    public boolean onLongClick(View v) {
        final int pos = recyclerView.getChildLayoutPosition(v);


        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                noteslist.remove(pos);
                myAdapter.notifyDataSetChanged();

            }
        });
        build.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // dialog cancelled
            }
        });
        build.setMessage("Delete Note '"+noteslist.get(pos).getTitle()+"'?");
        AlertDialog dialog = build.create();
        dialog.show();
        return false;
    }


    @Override
    public void onActivityResult(int req, int res, Intent data) {
        if (req == REQ) {
            if (res == RESULT_OK) {

                if (data.hasExtra("USER_MESSAGE")){
                    Toast.makeText(this,data.getStringExtra("USER_MESSAGE"),Toast.LENGTH_SHORT).show();
                }else{
                    String title = data.getStringExtra("USER_TITLE");
                    String datetime = data.getStringExtra("USER_DATETIME");
                    String note = data.getStringExtra("USER_NOTES");
                    int pos = data.getIntExtra("NOTE_INDEX", -1);

                    if(pos != -1 && noteslist.size() > 0) {
                        Note n = noteslist.get(pos);
                        n.setTitle(title);
                        n.setDatetime(datetime);
                        n.setNote(note);
                    }
                    else noteslist.add(new Note(note, title, datetime));
                    writeJson();
                }

            }
        }
    }

    private void writeJson() {
        try {
            FileOutputStream file_out_stream = getApplicationContext().openFileOutput("Note.json", Context.MODE_PRIVATE);
            JsonWriter jwrite = new JsonWriter(new OutputStreamWriter(file_out_stream, "UTF-8"));
            jwrite.setIndent("  ");
            jwrite.beginArray();

            for (Note n: this.noteslist) {
                jwrite.beginObject();
                jwrite.name("title").value(n.getTitle());
                jwrite.name("datetime").value(n.getDatetime());
                jwrite.name("notes").value(n.getNote());
                jwrite.endObject();
            }

            jwrite.endArray();
            jwrite.close();
        } catch (Exception e) { Log.e("Exception", "Happened", e); }
    }

    public void update(ArrayList<Note> uplist) {
        if(uplist == null) return;
        myAdapter.updateList(uplist);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeFiles();
        new AsyncNotes(this).execute();

    }
}

