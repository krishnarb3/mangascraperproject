package com.dev.pro.noob.rb.mangaproject;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;


public class Chapters extends ActionBarActivity
{
    ProgressDialog pd;
    public String TAG="TAG";
    ListView listView;
    File path;
    Bitmap bitmap;
    public class Imagetask extends AsyncTask<Void,Void,Void>
    {
        int chapterno;
        File path;
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        InputStream in;
        ArrayList<String> imagesurl = new ArrayList<>();
        public Imagetask(ArrayList<String> imagesurl,File path,int chapterno)
        {
            this.imagesurl = imagesurl;
            this.path = path;
            this.chapterno = chapterno;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pd = ProgressDialog.show(Chapters.this,"","Downloading...",true);
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            for(int i=0;i<imagesurl.size();i++)
            {
                try
                {
                    in=new URL(imagesurl.get(i)).openStream();
                    Log.d(TAG,imagesurl.get(i));
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = false;
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    options.inDither = true;
                    bitmap = BitmapFactory.decodeStream(in);
                        File file = new File(path,(chapterno)+" - "+(i+1)+".jpg");
                        FileOutputStream fileOutputStream = null;
                        try
                        {
                            fileOutputStream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG,80,fileOutputStream);
                        } catch (Exception e)
                        {
                        }

                } catch (Exception e)
                {
                    Log.d(TAG,e+"");
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void avoid)
        {

            pd.dismiss();
            Toast.makeText(getApplicationContext(),"ALL Images Saved Successfully",Toast.LENGTH_SHORT).show();
            super.onPostExecute(avoid);
        }
    }
    public BroadcastReceiver receiver = new BroadcastReceiver()
    {
        Imagetask task;
        @Override
        public void onReceive(Context context, Intent intent)
        {
            ArrayList<String> imagesurl = new ArrayList<>();
            Bundle bundle = intent.getExtras();
            String manganamefromservice;
            imagesurl = bundle.getStringArrayList("imagesurl");
            manganamefromservice = bundle.getString("manganame");
            Integer chapterno;
            chapterno = bundle.getInt("chapterno");
            path = new File(Environment.getExternalStorageDirectory().toString()+"/MangaDownloader/"+manganamefromservice+"/");
            if(!path.exists())
                path.mkdirs();
            task = new Imagetask(imagesurl,path,chapterno);
            task.execute();
            Toast.makeText(getApplicationContext(),"Download complete "+imagesurl.size(),Toast.LENGTH_LONG).show();
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters);

        Bundle bundle = getIntent().getExtras();
        listView = (ListView)findViewById(R.id.chapter);
        final ArrayList<String> chapterslinks = bundle.getStringArrayList("chapterslinks");
        String manganame = bundle.getString("manganame");
        manganame = manganame.substring(0,1).toUpperCase()+manganame.substring(1);
        ArrayList<String> chapterslist = new ArrayList<>();
        for(int i=0;i<chapterslinks.size();i++)
            chapterslist.add(manganame+" - "+(i+1));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,chapterslist);
        listView.setAdapter(adapter);
        final String finalManganame = manganame;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Intent intent = new Intent(Chapters.this,DownloadService.class);
                intent.putExtra("page1",chapterslinks.get(i));
                intent.putExtra("manganame", finalManganame);
                intent.putExtra("chapterno",i);
                startService(intent);
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(DownloadService.NOTIFICATION_SERVICE));
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        unregisterReceiver(receiver);
        if(pd!=null)
            pd.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chapters, menu);
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
}
