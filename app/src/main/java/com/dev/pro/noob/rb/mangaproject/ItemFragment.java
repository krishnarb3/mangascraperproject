package com.dev.pro.noob.rb.mangaproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;


import com.dev.pro.noob.rb.mangaproject.dummy.DummyContent;

import java.util.ArrayList;

public class ItemFragment extends Fragment implements AbsListView.OnItemClickListener
{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<String> mParam1 = new ArrayList<>();
    private int[] mParam2;
    private OnFragmentInteractionListener mListener;
    private AbsListView mListView;
    private gridadapter mAdapter;

    public ItemFragment()
    {

    }

    // TODO: Rename and change types of parameters
    public static ItemFragment newInstance(ArrayList<String> param1,int[] param2)
    {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM1, param1);
        args.putIntArray(ARG_PARAM2,param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            mParam1 = getArguments().getStringArrayList(ARG_PARAM1);
            mParam2 = getArguments().getIntArray(ARG_PARAM2);
        }
        mAdapter = new gridadapter(getActivity(),mParam1,mParam2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_item_grid, container, false);
        mListView = (GridView) view.findViewById(R.id.grid);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
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
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            String TAG="TAG";
            Log.d(TAG,mParam1.get(position).toLowerCase());
            Intent intent = new Intent(getActivity(),chapterlistactivity.class);
            intent.putExtra("manganame",mParam1.get(position).toLowerCase());
            startActivity(intent);
            mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
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
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
