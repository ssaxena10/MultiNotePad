package com.example.sharul.multinotepad;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Sharul on 27-02-2017.
 */

public class NewNote extends AppCompatActivity{

    private EditText Note;
    private EditText Title;
    private int loc;
    private String datetime;
    private Note n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_note);
        Title = (EditText) findViewById(R.id.new_title);
        Note = (EditText) findViewById(R.id.new_note);
        Note.setMovementMethod(new ScrollingMovementMethod());

        Intent intent = getIntent();
        if (intent.hasExtra(Note.class.getName())) {
            n = (Note) intent.getSerializableExtra(Note.class.getName());
            Title.setText(n.getTitle());
            Note.setText(n.getNote());
            datetime = (String) intent.getSerializableExtra(n.getDatetime());
        }
        loc = (Integer) intent.getSerializableExtra("");
    }

    public String DTFormat(){

        Date date  = new Date(System.currentTimeMillis());
        SimpleDateFormat sd = new SimpleDateFormat("EEE MMM dd,h:mm:ss a", Locale.US);
        return sd.format(date);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                Intent data = new Intent();

                if (Title.getText().toString().trim().equals("")){
                    data.putExtra("USER_MESSAGE", "Title Required: Not Saved");
                }
                else
                    {

                    if (n != null && n.getTitle().equals(Title.getText().toString()) && n.getNote().equals(Note.getText().toString())){
                        data.putExtra("USER_DATETIME", n.getDatetime());
                    }else{
                        data.putExtra("USER_DATETIME", DTFormat());
                    }
                    data.putExtra("USER_TITLE", Title.getText().toString());
                    data.putExtra("USER_NOTES", Note.getText().toString());
                }
                data.putExtra("NOTE_INDEX", loc);
                setResult(RESULT_OK, data);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {


        if(n != null && (!(n.getTitle().equals(Title.getText().toString())) || !(n.getNote().equals(Note.getText().toString())))){
            showDialogBox();
        }else if (n == null && ((Title.getText().toString().length() > 0 && Note.getText().toString().length() > 0)||
                (Title.getText().toString().length() > 0 || Note.getText().toString().length() < 0))){
            showDialogBox();
        }
        else if(Title.getText().toString().trim().equals("")){
            Toast.makeText(this, "Title Required: Not Saved", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }
    }

    public void showDialogBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent data = new Intent();
                if (n != null && n.getTitle().equals(Title.getText().toString()) && n.getNote().equals(Note.getText().toString())){
                    data.putExtra("USER_DATETIME", n.getDatetime());
                }else{
                    data.putExtra("USER_DATETIME", DTFormat());
                }
                data.putExtra("USER_TITLE", Title.getText().toString());
                data.putExtra("USER_NOTES", Note.getText().toString());
                data.putExtra("NOTE_INDEX", loc);
                setResult(RESULT_OK, data);
                finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent data = new Intent();
                finish();

            }
        });
        builder.setMessage("Note not saved! Save note '"+Title.getText().toString()+"'?");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
