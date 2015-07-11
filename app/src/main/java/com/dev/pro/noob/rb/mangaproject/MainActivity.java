package com.dev.pro.noob.rb.mangaproject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
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
        final ArrayList<String> arrayList2 = new ArrayList<String>(Arrays.asList("Naruto"));
        final int[] imagesarray2 = {R.drawable.naruto};
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                if(i==1)
                {
                    Toast.makeText(getApplicationContext(),"Toast onclick",Toast.LENGTH_SHORT).show();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    chapterlistclass chapterlist = new chapterlistclass(MainActivity.this);
                    ArrayList<ArrayList<String>> arrayList1= new ArrayList<ArrayList<String>>();
                    arrayList1=chapterlist.returndata();
                    ItemFragment fragment = ItemFragment.newInstance(arrayList1.get(0),arrayList1.get(1));
                    ft.add(R.id.mainfragment,fragment,"Main Fragment");
                    ft.commit();
                }
            }
        });

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

