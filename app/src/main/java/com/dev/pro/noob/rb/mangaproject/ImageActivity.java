package com.dev.pro.noob.rb.mangaproject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;


public class ImageActivity extends Activity implements GestureDetector.OnGestureListener
{
    ViewPager viewpager;
    Integer i=1;
    String TAG="TAG";
    String manganame;
    int chapterno;
    ImageView imageView;
    private FullScreenImageAdapter adapter;
    ArrayList<String> bitmapspaths = new ArrayList<>();
    Boolean t=true;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        Bundle bundle = getIntent().getExtras();
        manganame = bundle.getString("manganame");
        chapterno = bundle.getInt("chapterno");
        imageView = (ImageView) findViewById(R.id.image);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        Log.d(TAG, manganame + " - manganame");
        while(t)
        {
            t=nextimage(i);
            i++;
        }
        adapter=new FullScreenImageAdapter(ImageActivity.this,bitmapspaths);
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(0);
    }
    public Boolean nextimage(Integer integer)
    {
        File path;
        Bitmap bitmap;
        Boolean response=false;
        try
        {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inDither = true;
            path = new File(Environment.getExternalStorageDirectory().toString() + "/MangaDownloader/" + manganame + "/");
            File file = new File(path, (chapterno) + " - " + integer + ".jpg");
            if (file.exists())
            {
                response = true;
                String imagepath = Environment.getExternalStorageDirectory().toString() + "/MangaDownloader/" + manganame + "/"+Integer.toString(chapterno) + " - " + integer + ".jpg";
                if(!imagepath.isEmpty())
                {
                    bitmapspaths.add(imagepath);
                    Log.d(TAG,imagepath);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "End of Chapter", Toast.LENGTH_SHORT).show();
                    response = false;
                }
            }
            else
                response = false;
        } catch (Exception e)
        {
            Log.d(TAG, e + "");
        }
        return response;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent)
    {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent)
    {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent)
    {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2)
    {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent)
    {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2)
    {
        return false;
    }
}
