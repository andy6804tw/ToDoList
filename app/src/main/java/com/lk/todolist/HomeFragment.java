package com.lk.todolist;


import android.content.Intent;
import android.database.Cursor;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import me.leolin.shortcutbadger.ShortcutBadger;

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
    //public  static ArrayList<String>list;
    //public  static ArrayList<Integer>index;
    public static ArrayList<DataModel>list;
    public  ArrayList<DataModel>myList;
    private String[] mVals =new String[]{
            "家庭","開會","學校","功課","全部"
    };
    DBAccess access;
    public static int count=0;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.fragment_home, container, false);
        mFlowLayout = (FlowLayout) view.findViewById(R.id.flowLayout);
        //設定RecyclerView
        recyclerView =(RecyclerView) view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        listInit();
        initData();//標籤點擊
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

    public void  listInit(){
        //取得現在時間
        SimpleDateFormat f=new SimpleDateFormat("yyyy/MM/dd");
        Date curDate =new Date(System.currentTimeMillis());
        String str=f.format(curDate);
        /*title=new ArrayList<String>();
        date=new ArrayList<String>();
        time=new ArrayList<String>();*/
        count=0;
        list=new ArrayList<DataModel>();
        access=new DBAccess(getActivity(),"schedule",null,1);
        //Cursor c=access.getData(null,DBAccess.DATE_FIELD);
        Cursor c=access.getData(DBAccess.DATE_FIELD+" ='"+str+"'",DBAccess.TIME_FIELD);
        c.moveToFirst();
        for(int i=0;i<c.getCount();i++){
            /*title.add(c.getString(1)+"");
            date.add(c.getString(2)+"");
            time.add(c.getString(3)+"");*/
            if(c.getString(6).equals("未完成"))
                count++;
            list.add(new DataModel(c.getString(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6)));
            c.moveToNext();
        }
        //設定桌面icon今日代辦事項的個數
        ShortcutBadger.applyCount(getActivity().getApplicationContext(), count);
        //設定RecyclerView
        adapter = new RecyclerAdapter(list,getActivity());
        recyclerView.setAdapter(adapter);
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

            //初始化
            listInit();
            newText=newText.toLowerCase();
           /* ArrayList<String> Mylist=new ArrayList<String>();
            for(String s:MainActivity.title){
                if(s.contains(newText)){
                    Mylist.add(s);
                }

            }*/

            //list=new ArrayList<String>();
            //index=new ArrayList<Integer>();
            //逐一比對搜尋
            ArrayList<DataModel>myList=new ArrayList<DataModel>();
            for(int i=0;i<list.size();i++){
                if(list.get(i).getTitle().contains(newText)||list.get(i).getDesc().contains(newText)||list.get(i).getCategory().contains(newText)){
                    myList.add(list.get(i));
                    //index.add(i);
                }
            }
            adapter = new RecyclerAdapter(myList,getActivity());
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
        //listInit();
        for (int i = 0; i < mVals.length; i++) {
            final TextView tv = (TextView) mInflater.inflate(R.layout.textview, mFlowLayout, false);
            tv.setText(mVals[i]);
            mFlowLayout.addView(tv);

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(),tv.getText().toString()+"",Toast.LENGTH_SHORT).show();
                    listInit();
                    myList=new ArrayList<DataModel>();
                    for(int i=0;i<list.size();i++){
                        if(list.get(i).getCategory().equals(tv.getText().toString())){
                            myList.add(list.get(i));
                           // index.add(i);
                        }
                    }
                    if(tv.getText().toString().equals("全部"))
                        myList.addAll(list);
                    else if(myList.size()==0)
                        Toast.makeText(getActivity(),"查無"+tv.getText().toString()+"行程", Toast.LENGTH_SHORT).show();
                    adapter = new RecyclerAdapter(myList,getActivity());
                    recyclerView.setAdapter(adapter);
                }
            });

        }

    }

}
