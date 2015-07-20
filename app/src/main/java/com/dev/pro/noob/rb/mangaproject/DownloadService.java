package com.dev.pro.noob.rb.mangaproject;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import android.support.v4.app.NotificationCompat.Builder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by RB on 11-07-2015.
 */
public class DownloadService extends IntentService
{
    databaseclass helper;
    NotificationManager notifyManager;
    Builder builder;
    int id=1;
    Imagetask task;
    ArrayList<String> pagesurl = new ArrayList<>();
    ArrayList<String> imagessrcurl = new ArrayList<>();
    public String TAG="TAG";
    String chapterpg1;
    String manganame;
    Integer chapterno;
    public DownloadService()
    {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        File path;
        String chapterpage1 = intent.getStringExtra("page1");
        manganame = intent.getStringExtra("manganame");
        chapterpg1 = "http://www.mangareader.net"+chapterpage1;
        chapterno = intent.getIntExtra("chapterno",0);
        helper = new databaseclass(this);
        URL url = null;
        try
        {
            notifyManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
            builder = new Builder(getApplicationContext()).setContentTitle("Manga Downloading - "+manganame+" - "+chapterno).setContentText("Image Downloading").setSmallIcon(R.drawable.naruto);
            url = new URL(chapterpg1);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            InputStream inputStream = urlConnection.getInputStream();
            pagesurl = parseforPagesurl(inputStream);
            if(pagesurl.size()==0)
                pagesurl = parseforPagesurlxmldirty(inputStream);
            Log.d(TAG,"Pagesurl - "+pagesurl.toString());
            imagessrcurl = parseforImagesurl(pagesurl);
            Log.d(TAG,"Imagessrcurl"+imagessrcurl.toString());
            path = new File(Environment.getExternalStorageDirectory().toString()+"/MangaDownloader/"+manganame+"/");
            if(!path.exists())
                path.mkdirs();
            task = new Imagetask(imagessrcurl,path,chapterno);
            task.execute();
            Toast.makeText(getApplicationContext(),"Download complete "+imagessrcurl.size(),Toast.LENGTH_LONG).show();
        } catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Service connection Problem",Toast.LENGTH_SHORT).show();
        }

    }

    public class Imagetask extends AsyncTask<Void,Integer,Void>
    {
        int chapterno;
        File path;
        InputStream in;
        ArrayList<String> imagesurl = new ArrayList<>();
        Bitmap bitmap;
        public Imagetask(ArrayList<String> imagesurl,File path,int chapterno)
        {
            this.imagesurl = imagesurl;
            this.path = path;
            this.chapterno = chapterno;
        }

        @Override
        protected void onPreExecute()
        {

            builder.setProgress(imagesurl.size(),0,false);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values)
        {
            builder.setContentText("Downloading Image :     "+values[0]+"/"+imagesurl.size());
            builder.setProgress(imagesurl.size(),values[0],false);
            notifyManager.notify(id,builder.build());
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            for(int i=0;i<imagesurl.size();i++)
            {
                publishProgress(i);
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
                        sendprogress(i,imagesurl.size());
                        //Log.d(TAG,"BITAPPROGRESS  "+i);
                    } catch (Exception e)
                    {
                    }

                } catch (Exception e)
                {
                    Log.d(TAG,e+"");
                }
            }
            publishresults();
            return null;
        }

        @Override
        protected void onPostExecute(Void avoid)
        {

            long id2 = helper.insert(manganame,chapterno);
            Toast.makeText(getApplicationContext(),"ALL Images Saved Successfully",Toast.LENGTH_SHORT).show();
            super.onPostExecute(avoid);
            builder.setContentText("Download complete");
            // Removes the progress bar
            builder.setProgress(0, 0, false);
            notifyManager.notify(id, builder.build());
        }
    }
    public ArrayList<String> parseforPagesurl(InputStream inputStream)
    {
        ArrayList<String> imagesurl = new ArrayList<>();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try
        {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            org.w3c.dom.Element root = document.getDocumentElement();
            NodeList pagesurlnodelist = root.getElementsByTagName("option");
            Node node;
            for(int i=0;i<pagesurlnodelist.getLength();i++)
            {
                node = pagesurlnodelist.item(i);
                Log.d(TAG,node.getAttributes().item(0).getTextContent());
                imagesurl.add(node.getAttributes().item(0).getTextContent());

            }
        } catch (Exception e)
        {
            Log.d(TAG,e+"" );
        }
        return imagesurl;
    }
    public ArrayList<String> parseforPagesurlxmldirty(InputStream inputStream)
    {
        ArrayList<String> imagesurl = new ArrayList<>();
        OutputStream stream= new OutputStream()
        {
            @Override
            public void write(int i) throws IOException
            {

            }
        };
        Document document = null;
        Tidy tidy = new Tidy();
        tidy.setXHTML(true);
        document=tidy.parseDOM(inputStream,stream);
        Document document1 = (org.w3c.dom.Document)document;
        Log.d(TAG,"Dirty");
        org.w3c.dom.Element root = (org.w3c.dom.Element) document1.getDocumentElement();
            NodeList pagesurlnodelist = root.getElementsByTagName("option");
            Node node;
        Log.d(TAG,pagesurlnodelist.getLength()+"");
            for(int i=0;i<pagesurlnodelist.getLength();i++)
            {
                node = pagesurlnodelist.item(i);
                Log.d(TAG,node.getAttributes().item(0).getTextContent());
                imagesurl.add(node.getAttributes().item(0).getTextContent());

            }

        return imagesurl;
    }
    public ArrayList<String> parseforImagesurl(ArrayList<String> arrayList)
    {
        ArrayList<String> imagesurl = new ArrayList<>();
        for(int i=0;i<arrayList.size();i++)
        {
            try
            {
                URL url = new URL("http://www.mangareader.net"+arrayList.get(i));
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                InputStream inputStream = urlConnection.getInputStream();
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document document = documentBuilder.parse(inputStream);
                org.w3c.dom.Element root = document.getDocumentElement();
                NodeList nodeList = root.getElementsByTagName("img");
                Node node=nodeList.item(0);
                imagesurl.add(node.getAttributes().item(3).getTextContent());
            } catch (Exception e)
            {
                Log.d(TAG,e+"");
            }
        }
        Log.d(TAG,imagesurl.toString());
        return  imagesurl;
    }
    public void publishresults()
    {
        Intent intent = new Intent(NOTIFICATION_SERVICE);
        intent.putExtra("imagesurl",imagessrcurl);
        intent.putExtra("manganame",manganame);
        intent.putExtra("chapterno",chapterno);
        sendBroadcast(intent);
    }
    public void sendprogress(int i,int j)
    {
        Intent intent = new Intent(NOTIFICATION_SERVICE);
        intent.putExtra("progress",i+1);
        intent.putExtra("totalimages",j);
        intent.putExtra("chapterno",chapterno);
        Log.d(TAG,i+"progress");
        sendBroadcast(intent);
    }
}
