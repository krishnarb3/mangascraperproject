package com.dev.pro.noob.rb.mangaproject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by RB on 17-07-2015.
 */
public class FullScreenImageAdapter extends PagerAdapter
{
    private Activity activity;
    private ArrayList<String> imagepaths;
    private LayoutInflater inflater;
    public FullScreenImageAdapter(Activity activity,ArrayList<String> arrayList)
    {
        this.activity=activity;
        this.imagepaths=arrayList;
    }
    @Override
    public int getCount()
    {
        return this.imagepaths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view == ((RelativeLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        ImageView imageView;
        inflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewlayout = inflater.inflate(R.layout.layout_fullscreen_image,container,false);
        imageView = (ImageView)viewlayout.findViewById(R.id.image);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(imagepaths.get(position), options);
        imageView.setImageBitmap(bitmap);
        ((ViewPager) container).addView(viewlayout);
        return viewlayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        ((ViewPager) container).removeView((RelativeLayout)object);
    }
}

