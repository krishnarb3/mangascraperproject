package com.dev.pro.noob.rb.mangaproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by RB on 16-07-2015.
 */
public class ChaptersAdapter extends ArrayAdapter<String>
{
    public static String TAG="TAG";
    public ArrayList<String> newarray = new ArrayList<>();
    ProgressBar pb;
    public ChaptersAdapter(Context context,ArrayList<String> arrayList)
    {
        super(context,R.layout.chaptersrow,arrayList);
        this.newarray=arrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View view = inflater.inflate(R.layout.chaptersrow,parent,false);
        TextView textview = (TextView)view.findViewById(R.id.chaptersrow_text);
        textview.setText(newarray.get(position));
        //pb=(ProgressBar)view.findViewById(R.id.chaptersrow_progressbar);
        /*view.setFocusable(true);
        view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                pb.setProgress(50);
            }
        });*/
        return view;
    }
    public void setProgressBarvisibility(Boolean b)
    {
        if(b)
        pb.setVisibility(View.VISIBLE);
    }
}
