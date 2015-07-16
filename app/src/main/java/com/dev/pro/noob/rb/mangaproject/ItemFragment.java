package com.dev.pro.noob.rb.mangaproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.dev.pro.noob.rb.mangaproject.dummy.DummyContent;

import java.util.ArrayList;

public class ItemFragment extends Fragment implements AbsListView.OnItemClickListener
{
    ArrayList<String> temp_param1= new ArrayList<String>();
    ArrayList<String> temp_param2= new ArrayList<>();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<String> mParam1 = new ArrayList<>();
    private ArrayList<String> mParam2 = new ArrayList<>();
    private OnFragmentInteractionListener mListener;
    private ListView mListView;
    public EditText editText;
    public String TAG="TAG";
    public ItemFragment()
    {

    }

    // TODO: Rename and change types of parameters
    public static ItemFragment newInstance(ArrayList<String> param1,ArrayList<String> param2)
    {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM1, param1);
        args.putStringArrayList(ARG_PARAM2, param2);
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
            mParam2 = getArguments().getStringArrayList(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        mListView = (ListView)view.findViewById(R.id.list);
        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,mParam1);
        mListView.setAdapter(adapter);
        mListView.setFastScrollEnabled(true);
        mListView.setOnItemClickListener(this);
        editText = (EditText)view.findViewById(R.id.edittext);
        editText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                temp_param1.clear();
                temp_param2.clear();
                for(int k=0;k<mParam1.size();k++)
                {

                    if(mParam1.get(k).toLowerCase().startsWith(editable.toString().toLowerCase()))
                    {
                        Log.d(TAG,mParam1.get(k));
                        temp_param1.add(mParam1.get(k));
                        temp_param2.add(mParam2.get(k));
                    }
                   ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,temp_param1);
                   mListView.setAdapter(arrayAdapter);


                }
            }
        });
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
            Log.d(TAG,temp_param1.toString());
            if(temp_param1.size()==0)
            {
                String TAG = "TAG";
                Log.d(TAG, mParam1.get(position).toLowerCase());
                Intent intent = new Intent(getActivity(), MangaSelected.class);
                intent.putExtra("manganame", mParam1.get(position).toLowerCase());
                intent.putExtra("mangalink", mParam2.get(position).toLowerCase());
                startActivity(intent);
            }
            else
            {
                String TAG = "TAG";
                Log.d(TAG, temp_param1.get(position).toLowerCase());
                Intent intent = new Intent(getActivity(), MangaSelected.class);
                intent.putExtra("manganame", temp_param1.get(position).toLowerCase());
                intent.putExtra("mangalink", temp_param2.get(position).toLowerCase());
                startActivity(intent);
            }
            //mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
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
