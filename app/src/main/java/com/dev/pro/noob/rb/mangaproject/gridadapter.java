package com.dev.pro.noob.rb.mangaproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by RB on 08-07-2015.
 */
public class gridadapter extends ArrayAdapter<String>
{
    ArrayList<String> manganames = new ArrayList<>();
    Context context;
    public gridadapter(Context context, ArrayList<String> manganames)
    {
        super(context,R.layout.layout_mangagrid,manganames);
        this.context=context;
        this.manganames=manganames;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.layout_mangagrid,parent,false);
        TextView textView = (TextView)view.findViewById(R.id.gridtext);
        textView.setText(manganames.get(position));
        return view;
    }
}
