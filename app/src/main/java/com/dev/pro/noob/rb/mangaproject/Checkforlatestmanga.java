package com.dev.pro.noob.rb.mangaproject;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

public class Checkforlatestmanga extends Service
{
    NotificationManager notificationmanager;
    Builder builder;
    public String TAG="TAG";
    public databaseclass helper;
    notifyNewChapter task;
    ArrayList<ArrayList<String>> dataarray = new ArrayList<>();
    ArrayList<String> manganames = new ArrayList<>();
    int id=1;
    public Checkforlatestmanga()
    {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        helper = new databaseclass(this);
        dataarray = helper.getAllData();
        notificationmanager = (NotificationManager)Checkforlatestmanga.this.getSystemService(Context.NOTIFICATION_SERVICE);
        manganames = dataarray.get(0);
        task = new notifyNewChapter();
        task.execute();
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {
        AlarmManager alarm = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarm.set(alarm.RTC_WAKEUP, System.currentTimeMillis() + (2 * 60 * 60 * 1000), PendingIntent.getService(this,0,new Intent(this,Checkforlatestmanga.class),0));
        super.onDestroy();
    }

    public class notifyNewChapter extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            ArrayList<String> parsedarray = new ArrayList<>();
            String urlasString = "http://www.mangareader.net";
            try
            {
                URL url = new URL(urlasString);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                InputStream inputStream = httpURLConnection.getInputStream();
                parsedarray = parseforlatest(inputStream);
                builder = new Builder(getApplicationContext()).setContentTitle("New Chapter Available").setContentText("").setSmallIcon(R.drawable.mangaicon);
                HashSet<String> uniqueValues = new HashSet(manganames);
                ArrayList<String> manganames2 = new ArrayList<>(uniqueValues);
                ArrayList<String> temp = new ArrayList<>();
                for(int t=0;t<parsedarray.size();t++)
                {
                    temp.add(parsedarray.get(t).substring(0,4));
                }
                HashSet<String> uniqueValues2 = new HashSet(temp);
                ArrayList<String> parsedarray2 = new ArrayList<>(uniqueValues2);
                for(int i=0;i<parsedarray2.size();i++)
                {
                    for(int j=0;j<manganames2.size();j++)
                    {
                        if(manganames2.get(j).startsWith(parsedarray2.get(i).substring(0,4)))
                        {
                            id++;
                            Log.d(TAG,"TRUE - "+manganames2.get(j));
                            builder = new Builder(getApplicationContext()).setContentTitle("New Chapter Available").setContentText("").setSmallIcon(R.drawable.naruto);
                            builder.setContentText("New Chapter Available in "+manganames2.get(j));
                            notificationmanager.notify(id, builder.build());
                        }
                    }
                }

            } catch (Exception e)
            {
                Log.d(TAG, e + "");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }
    public ArrayList<String> parseforlatest(InputStream inputStream)
    {
        ArrayList<String> results = new ArrayList<>();
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
        org.w3c.dom.Element root = (org.w3c.dom.Element) document1.getDocumentElement();
        NodeList nodeList3 = root.getElementsByTagName("a");
        NamedNodeMap namedNodeMap2;
        int count=0;
        for(int k=0;k<nodeList3.getLength();k++)
        {
            namedNodeMap2 = nodeList3.item(k).getAttributes();
            for(int l=0;l<namedNodeMap2.getLength();l++)
            {
                if(namedNodeMap2.item(l).getNodeName().equals("class"))
                {
                    if(namedNodeMap2.item(l).getNodeValue().equals("chaptersrec"))
                    {
                        count++;
                        Log.d(TAG,"Service count - "+nodeList3.item(k).getFirstChild().getNodeValue());
                        results.add(nodeList3.item(k).getFirstChild().getNodeValue());
                    }
                }

            }
        }
        return results;
    }
    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
