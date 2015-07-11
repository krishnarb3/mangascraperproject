package com.dev.pro.noob.rb.mangaproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by RB on 09-07-2015.
 */
public class chapterlistclass
{
    getpopmangalistTask task;
    public static String TAG="TAG";
    ArrayList<ArrayList<String>> results = new ArrayList<>();
    String manganame;
    Context context;
    public chapterlistclass(Activity activity)
    {
        this.context = activity;
    }
    public ArrayList<ArrayList<String>> returndata()
    {
    task = new getpopmangalistTask();
        try
        {
            results = task.execute().get();
        } catch (Exception e)
        {
        }
        return results;
    }
    public class getpopmangalistTask extends AsyncTask<Void,Void,ArrayList<ArrayList<String>>>
    {
        @Override
        protected void onPreExecute()
        {
            Log.d(TAG,"ON PREEXECUTE");
            super.onPreExecute();
        }

        @Override
    protected ArrayList<ArrayList<String>> doInBackground(Void... voids)
    {
        ArrayList<ArrayList<String>> newarraylist=new ArrayList<>();
        String mangalisturl = "http://www.mangareader.net";
        try
        {
            URL url = new URL(mangalisturl);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            InputStream inputStream = httpURLConnection.getInputStream();
            newarraylist = parseforpopular(inputStream);
        } catch (Exception e)
        {

        }
        return newarraylist;
    }

        @Override
        protected void onPostExecute(ArrayList<ArrayList<String>> arrayLists)
        {

            super.onPostExecute(arrayLists);
        }
    }
    public ArrayList<ArrayList<String>> parseforpopular(InputStream inputStream)
    {
        ArrayList<ArrayList<String>> finalresult = new ArrayList<>();
        ArrayList<String> results=new ArrayList<>(),manganames=new ArrayList<>();
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
        NodeList nodeList = root.getElementsByTagName("b");
        Node node;
        for(int i=0;i<40;i++)
        {
            Log.d(TAG, i + "");
            if(!nodeList.item(i).hasChildNodes())
                break;
            node = nodeList.item(i).getFirstChild();
            manganames.add(node.getFirstChild().getNodeValue());
            results.add(node.getAttributes().item(1).getNodeValue()+"");
            Log.d(TAG,results.toString());
        }
        finalresult.add(manganames);
        finalresult.add(results);
        Log.d(TAG,results.toString());
        return finalresult;
    }
}