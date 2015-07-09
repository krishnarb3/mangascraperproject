package com.dev.pro.noob.rb.mangaproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by RB on 06-07-2015.
 */
public class navbaradapter extends ArrayAdapter<String>
{
    Context context;
    ArrayList<String> arrayList = new ArrayList<>();
    public static String TAG="TAG";
    int[] imagesarray;
    public navbaradapter(Context context,ArrayList<String> arrayList,int[] imageslist)
    {
        super(context,R.layout.layout_navbarlist,arrayList);
        this.arrayList=arrayList;
        this.context=context;
        this.imagesarray=imageslist;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.layout_navbarlist,parent,false);
        TextView textView = (TextView)view.findViewById(R.id.listtext);
        ImageView imageView = (ImageView)view.findViewById(R.id.listimage);
        textView.setText(arrayList.get(position));
        imageView.setImageResource(imagesarray[position]);
        return view;
    }
}
