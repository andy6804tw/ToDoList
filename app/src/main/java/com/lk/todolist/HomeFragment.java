package com.lk.todolist;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


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


        return view;
    }
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
