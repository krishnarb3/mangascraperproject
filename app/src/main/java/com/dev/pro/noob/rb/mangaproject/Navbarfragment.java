package com.dev.pro.noob.rb.mangaproject;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class Navbarfragment extends Fragment
{
    public static String TAG="TAG";
    private DrawerLayout ndrawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    public Navbarfragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        final View view =  inflater.inflate(R.layout.fragment_navbar, container, false);
        return view;
    }


    public void setUp(DrawerLayout drawerLayout,Toolbar toolbar)
    {
        ndrawerLayout = drawerLayout;
        actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar,R.string.drawertoggleopen,R.string.drawertoggleclose)
        {
            @Override
            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView)
            {
                super.onDrawerClosed(drawerView);
            }
        };
        ndrawerLayout.setDrawerListener(actionBarDrawerToggle);
    }
}
