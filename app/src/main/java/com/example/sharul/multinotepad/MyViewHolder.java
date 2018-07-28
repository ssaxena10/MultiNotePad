package com.example.sharul.multinotepad;

import android.app.job.JobScheduler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Sharul on 27-02-2017.
 */
public class MyViewHolder extends RecyclerView.ViewHolder
{
    TextView note;
    TextView title;
    TextView dt;

    MyViewHolder(View item)
    {
        super(item);

        note = (TextView)item.findViewById(R.id.Note_Card);
        title = (TextView)item.findViewById(R.id.Title_Card);
        dt = (TextView)item.findViewById(R.id.DateTime);
    }
}
