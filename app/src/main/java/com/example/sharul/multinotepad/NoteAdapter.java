package com.example.sharul.multinotepad;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Sharul on 27-02-2017.
 */

public class NoteAdapter extends RecyclerView.Adapter<MyViewHolder>
{
    private List<Note> notesList;
    private MNotePad mactivity;


    public NoteAdapter(List<Note> notes, MNotePad main)
    {
        this.notesList = notes;
        this.mactivity = main;
    }

    public void updateList(List<Note> nlist)
    {
        this.notesList.clear();
        this.notesList.addAll(nlist);
        notifyDataSetChanged();
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup view, int i)
    {
        View v = LayoutInflater.from(view.getContext()).inflate(R.layout.card_note, view, false);
        v.setOnClickListener(mactivity);
        v.setOnLongClickListener(mactivity);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int pos)
    {
        holder.title.setText(this.notesList.get(pos).getTitle());
        holder.dt.setText(this.notesList.get(pos).getDatetime());

        String s = this.notesList.get(pos).getNote();
        if (s.length()>80)
        {
            s = s.substring(0,79)+"...";
        }
        holder.note.setText(s);
    }

    @Override
    public int getItemCount()
    {
        return this.notesList.size();
    }



}
