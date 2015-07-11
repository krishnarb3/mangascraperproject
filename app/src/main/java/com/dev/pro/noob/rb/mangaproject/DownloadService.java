package com.dev.pro.noob.rb.mangaproject;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.renderscript.Element;
import android.util.Log;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by RB on 11-07-2015.
 */
public class DownloadService extends IntentService
{
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
        String chapterpage1 = intent.getStringExtra("page1");
        manganame = intent.getStringExtra("manganame");
        chapterpg1 = "http://www.mangareader.net"+chapterpage1;
        chapterno = intent.getIntExtra("chapterno",0);
        URL url = null;
        try
        {
            url = new URL(chapterpg1);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            InputStream inputStream = urlConnection.getInputStream();
            pagesurl = parseforPagesurl(inputStream);
            imagessrcurl = parseforImagesurl(pagesurl);
        } catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Service connection Problem",Toast.LENGTH_SHORT).show();
        } finally
        {
            publishresults();
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
}
