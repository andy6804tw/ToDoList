package com.lk.todolist.Search;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.lk.todolist.DBAccess;
import com.lk.todolist.DataModel;
import com.lk.todolist.R;
import com.lk.todolist.RecyclerAdapter;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private Toolbar toolbar;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    public static ArrayList<DataModel> list;
    DBAccess access;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        toolbar=(Toolbar)findViewById(R.id.app_bar);
        toolbar.setTitle("代辦事項");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        list=new ArrayList<DataModel>();
        access=new DBAccess(this,"schedule",null,1);
        //Cursor c=access.getData(null,DBAccess.DATE_FIELD);
        Cursor c=access.getData(null,DBAccess.DATE_FIELD);
        c.moveToFirst();
        for(int i=0;i<c.getCount();i++){
            /*title.add(c.getString(1)+"");
            date.add(c.getString(2)+"");
            time.add(c.getString(3)+"");*/
            list.add(new DataModel(c.getString(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6)));
            c.moveToNext();
        }

        recyclerView =(RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter(list,this);
        recyclerView.setAdapter(adapter);



    }
}
