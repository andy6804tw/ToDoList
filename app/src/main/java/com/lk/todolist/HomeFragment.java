package com.lk.todolist;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    private FlowLayout mFlowLayout;
    private FloatingActionButton fab;
    public  static ArrayList<String>list;
    public  static ArrayList<Integer>index;
    private String[] mVals =new String[]{
            "吃飯","開會","Welcome","Music"
    };


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.fragment_home, container, false);
        mFlowLayout = (FlowLayout) view.findViewById(R.id.flowLayout);
        initData();
        recyclerView =(RecyclerView) view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        fab=(FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),AddEvent.class));
            }
        });
        setHasOptionsMenu(true);//來告知這個fragment有另外的OptionsMenu(參考)
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_item, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(queryListener);

    }
    final private android.support.v7.widget.SearchView.OnQueryTextListener queryListener = new android.support.v7.widget.SearchView.OnQueryTextListener() {

        @Override
        public boolean onQueryTextChange(String newText) {

            //直接丟給filter
            newText=newText.toLowerCase();
           /* ArrayList<String> Mylist=new ArrayList<String>();
            for(String s:MainActivity.title){
                if(s.contains(newText)){
                    Mylist.add(s);
                }

            }*/

            list=new ArrayList<String>();
            index=new ArrayList<Integer>();
            for(int i=0;i<MainActivity.title.size();i++){
                if(MainActivity.title.get(i).contains(newText)){
                    list.add(MainActivity.title.get(i));
                    index.add(i);
                }
            }
            adapter = new RecyclerAdapter(list,getActivity());
            recyclerView.setAdapter(adapter);

               /* RecyclerAdapter adapter;
                adapter = new RecyclerAdapter(list,getActivity());
                adapter.setFilter(list);*/
                //recyclerView.setAdapter(adapter);
            //adapter.notifyDataSetChanged();
            Toast.makeText(getActivity(),list.size()+"",Toast.LENGTH_LONG).show();
            return true;
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            Log.d(TAG, "submit:"+query);
            return false;
        }
    };

    public void initData(){
        LayoutInflater mInflater = LayoutInflater.from(getActivity());

        for (int i = 0; i < mVals.length; i++) {
            final TextView tv = (TextView) mInflater.inflate(R.layout.textview, mFlowLayout, false);
            tv.setText(mVals[i]);
            mFlowLayout.addView(tv);

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), tv.getText().toString(), Toast.LENGTH_SHORT).show();
                    list=new ArrayList<String>();
                    index=new ArrayList<Integer>();
                    for(int i=0;i<MainActivity.title.size();i++){
                        if(MainActivity.title.get(i).equals(tv.getText().toString())){
                            list.add(MainActivity.title.get(i));
                            index.add(i);
                        }
                    }
                    adapter = new RecyclerAdapter(list,getActivity());
                    recyclerView.setAdapter(adapter);
                }
            });

        }

    }

}
