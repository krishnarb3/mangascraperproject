package com.dev.pro.noob.rb.mangaproject;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Arrays;


public class MainActivity extends ActionBarActivity implements ItemFragment.OnFragmentInteractionListener
{
    public String TAG="TAG";
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    public Navbarfragment navbarfragment;
    private ActionBarDrawerToggle drawerlistener;
    private ArrayList<String> navbarimagelist;
    Boolean mostpopular=true;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        navbarfragment=(Navbarfragment)getSupportFragmentManager().findFragmentById(R.id.navbarfragment);
        navbarfragment.setUp((DrawerLayout)findViewById(R.id.drawerlayout),toolbar);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerlayout);
        drawerlistener = new ActionBarDrawerToggle(this,drawerLayout,R.drawable.drawertoggle,R.string.drawertoggleopen,R.string.drawertoggleclose)
        {
            @Override
            public void onDrawerOpened(View drawerView)
            {
                Toast.makeText(getApplicationContext(),"Drawer opened",Toast.LENGTH_SHORT).show();
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView)
            {
                Toast.makeText(getApplicationContext(),"Drawer closed",Toast.LENGTH_SHORT).show();
                super.onDrawerClosed(drawerView);
            }
        };
        drawerLayout.setDrawerListener(drawerlistener);
        ListView listView = (ListView)findViewById(R.id.list_fragment);
        final ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.NavbarlistArray)));
        final int[] imagesarray = {R.drawable.home,R.drawable.trending,R.drawable.yingyang,R.drawable.download,R.drawable.settings,R.drawable.exit};
        navbaradapter adapter = new navbaradapter(getApplicationContext(),arrayList,imagesarray);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                if(i==1)
                {
                    if(mostpopular)
                    {
                        getpopmangalistTask task = new getpopmangalistTask();
                        task.execute();
                        mostpopular = false;
                        view.setBackgroundColor(Color.CYAN);
                        TextView textView = (TextView) view.findViewById(R.id.listtext);
                        textView.setTextColor(Color.BLACK);
                    }
                }
            }
        });

    }
    public class getpopmangalistTask extends AsyncTask<Void,Void,ArrayList<ArrayList<String>>>
    {
        ProgressDialog pd;
        @Override
        protected void onPreExecute()
        {
            pd = ProgressDialog.show(MainActivity.this,"","Loading...",true);
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
            pd.dismiss();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ItemFragment fragment = ItemFragment.newInstance(arrayLists.get(0),arrayLists.get(1));
            ft.add(R.id.mainfragment,fragment,"Main Fragment");
            ft.commit();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public void onFragmentInteraction(String id)
    {

    }
}

