package com.dev.pro.noob.rb.mangaproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.dev.pro.noob.rb.mangaproject.dummy.DummyContent;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class ItemFragment_downloaded extends Fragment implements AbsListView.OnItemClickListener, AdapterView.OnItemLongClickListener
{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    databaseclass helper;
    private ArrayList<String> mParam1=new ArrayList<>();
    private int[] mParam2;
    private ArrayList<String> arrayList=new ArrayList<>();
    private OnFragmentInteractionListener mListener;
    private AbsListView mListView;
    private ListAdapter mAdapter;

    public static ItemFragment_downloaded newInstance(ArrayList<String> param1, int[] param2)
    {
        ItemFragment_downloaded fragment = new ItemFragment_downloaded();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM1, param1);
        args.putIntArray(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment_downloaded()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        helper = new databaseclass(getActivity());
        if (getArguments() != null)
        {
            mParam1 = getArguments().getStringArrayList(ARG_PARAM1);
            mParam2 = getArguments().getIntArray(ARG_PARAM2);
            for(int i=0;i<mParam1.size();i++)
                arrayList.add(mParam1.get(i)+" - "+mParam2[i]);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_itemdownloaded, container, false);

        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,arrayList);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);
        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        if (null != mListener)
        {
            Intent intent = new Intent(getActivity(),ImageActivity.class);
            intent.putExtra("manganame",mParam1.get(position));
            intent.putExtra("chapterno",mParam2[position]);
            startActivity(intent);
            //mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
    }
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete Chapter?").setMessage("Do you want to delete all images in this chapter?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int t)
            {
                File path;
                path = new File(Environment.getExternalStorageDirectory().toString()+"/MangaDownloader/"+mParam1.get(i)+"/");
                arrayList.remove(i);
                long id = helper.delete(mParam1.get(i),mParam2[i]);
                Boolean res = true;
                for(int j=0;res!=false;j++)
                {
                    File file = new File(path, (mParam2[i]) + " - " + (j + 1) + ".jpg");
                    try
                    {
                        res = file.delete();
                    } catch (Exception e)
                    {
                    }
                    ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayList);
                    ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);
                    Toast.makeText(getActivity(),"Deleted Chapter",Toast.LENGTH_SHORT).show();
                    // Set OnItemClickListener so we can be notified on item clicks
                }
            }
        });
        builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        return false;
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText)
    {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView)
        {
            ((TextView) emptyView).setText(emptyText);
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener
    {
        public void onFragmentInteraction(String id);
    }

}
