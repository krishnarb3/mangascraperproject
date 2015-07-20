package com.dev.pro.noob.rb.mangaproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class MainActivity extends ActionBarActivity implements ItemFragment.OnFragmentInteractionListener,ItemFragment_downloaded.OnFragmentInteractionListener,HomeFragment.OnFragmentInteractionListener,SettingsFragment.OnFragmentInteractionListener
{
    public databaseclass helper;
    public String TAG="TAG";
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    public Navbarfragment navbarfragment;
    private ActionBarDrawerToggle drawerlistener;
    private ArrayList<String> navbarimagelist;
    private ArrayList<String> curfrag_manganames = new ArrayList<>();
    private ArrayList<String> curfrag_mangalinks = new ArrayList<>();
    private ArrayList<ArrayList<String>> dataarray=new ArrayList<>();
    private ArrayList<String> manganames=new ArrayList<>(),chapternosarraylist = new ArrayList<>();
    private int[] chapternos;
    EditText editText;
    Boolean mostpopular=true;
    int imagequality;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar)findViewById(R.id.app_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_drawer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        startService(new Intent(MainActivity.this,Checkforlatestmanga.class));
        preferences = getSharedPreferences("fullmangalistnames", Context.MODE_PRIVATE);
        helper = new databaseclass(this);
        dataarray = helper.getAllData();
        manganames = dataarray.get(0);
        Collections.reverse(manganames);
        editText = (EditText)findViewById(R.id.edittext);
        editText.getLayoutParams().height=0;
        editText.setInputType(0);
        chapternos = new int[manganames.size()];
        chapternosarraylist = dataarray.get(1);
        Collections.reverse(chapternosarraylist);
        for(int i=0;i<manganames.size();i++)
            chapternos[i]=Integer.parseInt(chapternosarraylist.get(i));
        navbarfragment=(Navbarfragment)getSupportFragmentManager().findFragmentById(R.id.navbarfragment);
        navbarfragment.setUp((DrawerLayout)findViewById(R.id.drawerlayout),toolbar);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerlayout);
        drawerlistener = new ActionBarDrawerToggle(this,drawerLayout,R.drawable.ic_drawer,R.string.drawertoggleopen,R.string.drawertoggleclose)
        {
            @Override
            public void onDrawerOpened(View drawerView)
            {
                toolbar.setNavigationIcon(R.drawable.ic_back);
                Toast.makeText(getApplicationContext(),"Drawer opened",Toast.LENGTH_SHORT).show();
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView)
            {
                toolbar.setNavigationIcon(R.drawable.ic_drawer);
                Toast.makeText(getApplicationContext(),"Drawer closed",Toast.LENGTH_SHORT).show();
                super.onDrawerClosed(drawerView);
            }

            //@Override
            /*public void onDrawerSlide(View drawerView, float slideOffset)
            {
                if(slideOffset)
                super.onDrawerSlide(drawerView, slideOffset);
            }*/
        };
        drawerLayout.setDrawerListener(drawerlistener);
        drawerlistener.setDrawerIndicatorEnabled(true);
        drawerLayout.openDrawer(Gravity.LEFT);
        drawerLayout.post(new Runnable()
        {
            @Override
            public void run()
            {
                drawerlistener.syncState();
            }
        });
        final ListView listView = (ListView)findViewById(R.id.list_fragment);
        final ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.NavbarlistArray)));
        final int[] imagesarray = {R.drawable.home,R.drawable.trending,R.drawable.yingyang,R.drawable.download,R.drawable.settings,R.drawable.bug,R.drawable.exit};
        navbaradapter adapter = new navbaradapter(getApplicationContext(),arrayList,imagesarray);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                for (int z = 0; z < adapterView.getChildCount(); z++) {
                    View listItem = adapterView.getChildAt(z);
                    listItem.setBackgroundColor(Color.parseColor("#1A237E"));
                    TextView textView=(TextView)listItem.findViewById(R.id.listtext);
                    textView.setTextColor(Color.WHITE);
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }
                if(i==0)
                {
                    view.setBackgroundColor(getResources().getColor(R.color.accentcolor));
                    Toast.makeText(MainActivity.this,"Click on Most Downloaded for mangareader.net's popular manga\nManga List for Full Manga list\nDownloaded Manga for viewing the downloaded Manga",Toast.LENGTH_LONG).show();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                    HomeFragment fragment = HomeFragment.newInstance(manganames);
                    ft.replace(R.id.mainfragment,fragment,"Main Fragment");
                    ft.commit();
                }
                if(i==1)
                {
                    if(mostpopular)
                    {
                        getpopmangalistTask task = new getpopmangalistTask();
                        task.execute();
                        mostpopular = false;
                        view.setBackgroundColor(getResources().getColor(R.color.accentcolor));
                        //editText.getLayoutParams().height=30;
                        //editText.setHint("ENNAMA");
                    }
                }
                if(i==2)
                {
                    view.setBackgroundColor(getResources().getColor(R.color.accentcolor));
                    editText.getLayoutParams().height=30;
                    editText.setHint("ENNAMA");
                    editText.addTextChangedListener(watcher);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                    if(preferences!=null)
                    {
                        Set<String> set = preferences.getStringSet("sharedString_manganames", new HashSet<String>());
                        ArrayList<String> fulllist_manganames = new ArrayList<String>(set);
                        Set<String> set2 = preferences.getStringSet("sharedString_mangalinks", new HashSet<String>());
                        ArrayList<String> fulllist_mangalinks = new ArrayList<String>(set2);
                        ItemFragment fragment = ItemFragment.newInstance(fulllist_manganames, fulllist_mangalinks);
                        curfrag_manganames = fulllist_manganames;
                        curfrag_mangalinks = fulllist_mangalinks;
                        ft.replace(R.id.mainfragment, fragment, "Main Fragment");
                        ft.commit();

                    }
                    getfullmangalistTask task = new getfullmangalistTask();
                    task.execute();
                }
                if(i==3)
                {
                    dataarray = helper.getAllData();
                    manganames = dataarray.get(0);
                    Collections.reverse(manganames);
                    chapternos = new int[manganames.size()];
                    chapternosarraylist = dataarray.get(1);
                    Collections.reverse(chapternosarraylist);
                    for(int t=0;t<manganames.size();t++)
                        chapternos[t]=Integer.parseInt(chapternosarraylist.get(t));
                    view.setBackgroundColor(getResources().getColor(R.color.accentcolor));
                    editText.getLayoutParams().height=30;
                    editText.addTextChangedListener(new TextWatcher()
                    {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3)
                        {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3)
                        {
                        Log.d(TAG,"WATCHING");
                        }

                        @Override
                        public void afterTextChanged(Editable editable)
                        {

                        }
                    });
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                    ItemFragment_downloaded ifd = ItemFragment_downloaded.newInstance(manganames,chapternos);
                    ft.replace(R.id.mainfragment,ifd,"Main Fragment");
                    ft.commit();
                }
                if(i==4)
                {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                    SettingsFragment sf = SettingsFragment.newInstance("","");
                    ft.replace(R.id.mainfragment,sf,"Main Fragment");
                    ft.commit();
                    imagequality = sf.imagequality;
                }
                if(i==5)
                {
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", "krishnarb3@gmail.com", null));
                    intent.putExtra(Intent.EXTRA_SUBJECT,"Bug Report - Manga Downloader");
                    startActivity(Intent.createChooser(intent, "Choose an Email client :"));
                }
                if(i==6)
                {
                    view.setBackgroundColor(getResources().getColor(R.color.accentcolor));
                    finish();
                    System.exit(0);
                }
            }
        });

    }
    private final TextWatcher watcher = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3)
        {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3)
        {
        Log.d(TAG,"OnTextChanged");
        }

        @Override
        public void afterTextChanged(Editable editable)
        {
            Log.d(TAG,"Aftertextchanged");
            /*ArrayList<String> newfrag_manganames=new ArrayList<>();
            ArrayList<String> newfrag_mangalinks=new ArrayList<>();
               for(int i=0;i<curfrag_manganames.size();i++)
               {
                   if(curfrag_manganames.get(i).startsWith(editable.toString()));
                   {
                       newfrag_manganames.add(curfrag_manganames.get(i));
                       newfrag_mangalinks.add(curfrag_mangalinks.get(i));
                   }
               }
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ItemFragment fragment = ItemFragment.newInstance(curfrag_manganames, curfrag_mangalinks);
            ft.replace(R.id.mainfragment, fragment, "Main Fragment");
            ft.commit();*/
        }
    };

    public class getfullmangalistTask extends AsyncTask<Void,Void,ArrayList<ArrayList<String>>>
    {
        //ProgressDialog pd;

        @Override
        protected void onPreExecute()
        {
            Toast.makeText(MainActivity.this,"Refreshing...",Toast.LENGTH_SHORT).show();
            //pd = ProgressDialog.show(MainActivity.this,"","Loading...",true);
            super.onPreExecute();
        }
        @Override
        protected ArrayList<ArrayList<String>> doInBackground(Void... voids)
        {
            ArrayList<ArrayList<String>> newarraylist = new ArrayList<>();
            String mangalisturl = "http://www.mangareader.net/alphabetical";
            try
            {
                URL url = new URL(mangalisturl);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                InputStream inputStream = httpURLConnection.getInputStream();
                newarraylist = parseforfulllist(inputStream);
            } catch (Exception e)
            {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MainActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return newarraylist;
        }

        @Override
        protected void onPostExecute(ArrayList<ArrayList<String>> arrayLists)
        {
            //pd.dismiss();
            try
            {
                SharedPreferences.Editor editor=preferences.edit();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                ItemFragment fragment = ItemFragment.newInstance(arrayLists.get(0), arrayLists.get(1));
                curfrag_manganames=arrayLists.get(0);
                curfrag_mangalinks=arrayLists.get(1);
                Set<String> sharedString_manganames = new HashSet<String>();
                for(int i=0;i<arrayLists.get(0).size();i++)
                {
                    sharedString_manganames.add(arrayLists.get(0).get(i));
                }
                Set<String> sharedString_mangalinks = new HashSet<String>();
                for(int i=0;i<arrayLists.get(0).size();i++)
                {
                    sharedString_mangalinks.add(arrayLists.get(1).get(i));
                }
                editor.putStringSet("sharedString_manganames",sharedString_manganames);
                editor.putStringSet("sharedString_mangalinks",sharedString_mangalinks);
                editor.apply();
                Log.d(TAG,sharedString_manganames.toString());
                ft.replace(R.id.mainfragment, fragment, "Main Fragment");
                ft.commit();
            }catch (Exception e)
            {
            Toast.makeText(MainActivity.this,"Connection Error",Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(arrayLists);
        }
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
                httpURLConnection.setRequestMethod("GET");
                InputStream inputStream = httpURLConnection.getInputStream();
                Log.d(TAG,"Success");
                newarraylist = parseforpopular(inputStream);
            } catch (Exception e)
            {
                runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(MainActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return newarraylist;
        }

        @Override
        protected void onPostExecute(ArrayList<ArrayList<String>> arrayLists)
        {
            pd.dismiss();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
            try
            {
                ItemFragment fragment = ItemFragment.newInstance(arrayLists.get(0), arrayLists.get(1));
                ft.replace(R.id.mainfragment, fragment, "Main Fragment");
                ft.commit();
                editText = (EditText)findViewById(R.id.edittext);
                editText.getLayoutParams().height=30;
                editText.setHint("ENNAMA");
            }catch (Exception e)
        {
            Toast.makeText(MainActivity.this,"Connection Error",Toast.LENGTH_SHORT).show();
        }
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
    public ArrayList<ArrayList<String>> parseforfulllist(InputStream inputStream)
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
        NodeList nodeList = root.getElementsByTagName("ul");
        NodeList itemchildren,itemchildren2;
        Node node;
        Node currentnode;
        Node currentattribute;
        Integer count=0;
        Log.d(TAG,"TOTAL MANGA NO. = "+count);
        Log.d(TAG,nodeList.getLength()+"");
        for(int i=0;i<nodeList.getLength();i++)
        {
            if(nodeList.item(i).hasAttributes())
            if(nodeList.item(i).getAttributes().item(0).getNodeName().equals("class"))
            {
                itemchildren = nodeList.item(i).getChildNodes();
                for(int j=0;j<itemchildren.getLength();j++)
                {
                    count++;
                    results.add(itemchildren.item(j).getFirstChild().getAttributes().item(0).getNodeValue());
                    manganames.add(itemchildren.item(j).getFirstChild().getFirstChild().getNodeValue());
                }
            }
        }
        Log.d(TAG,manganames.toString());
        Log.d(TAG,"TOTAL MANGA NO. = "+count);
        finalresult.add(manganames);
        finalresult.add(results);
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

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
    }
}

