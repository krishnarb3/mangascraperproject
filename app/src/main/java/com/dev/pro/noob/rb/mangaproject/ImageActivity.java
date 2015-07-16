package com.dev.pro.noob.rb.mangaproject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
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


public class ImageActivity extends Activity implements GestureDetector.OnGestureListener
{
    Integer i;
    String TAG="TAG";
    String manganame;
    int chapterno;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        Bundle bundle = getIntent().getExtras();
        manganame = bundle.getString("manganame");
        chapterno = bundle.getInt("chapterno");
        imageView = (ImageView) findViewById(R.id.image);
        Log.d(TAG, manganame + " - manganame");
        nextimage(1);

    }
    public void nextimage(Integer integer)
    {
        File path;
        Bitmap bitmap;

        try
        {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inDither = true;
            path = new File(Environment.getExternalStorageDirectory().toString() + "/MangaDownloader/" + manganame + "/");
            File file = new File(path, (chapterno) + " - " + 1 + ".jpg");
            if (file.exists())
            {
                String imagepath = Environment.getExternalStorageDirectory().toString() + "/MangaDownloader/" + manganame + "/"+Integer.toString(chapterno) + " - " + integer + ".jpg";
                bitmap = BitmapFactory.decodeFile(imagepath);
                if(bitmap!=null)
                imageView.setImageBitmap(bitmap);
                else
                    Toast.makeText(getApplicationContext(),"End of Chapter",Toast.LENGTH_SHORT).show();
                Log.d(TAG,imagepath);
                //Log.d(TAG,bitmap.toString());
            }


        } catch (Exception e)
        {
            Log.d(TAG, e + "");
        }
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
