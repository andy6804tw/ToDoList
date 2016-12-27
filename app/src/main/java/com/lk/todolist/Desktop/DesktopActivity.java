package com.lk.todolist.Desktop;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lk.todolist.DBAccess;
import com.lk.todolist.DataModel;
import com.lk.todolist.MainActivity;
import com.lk.todolist.R;
import com.lk.todolist.Search.SearchActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import me.leolin.shortcutbadger.ShortcutBadger;

public class DesktopActivity extends AppCompatActivity {

    ArrayList<todoItem> item_list;
    ArrayList<DataModel>list;
    DBAccess access;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desktop);
        //更改狀態欄顏色
        WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
        localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        ListView listView=(ListView)findViewById(R.id.listView);
        //設定資料
        item_list=new ArrayList<>();
        item_list.add(new todoItem("代辦事項","Schedule",R.drawable.img4));
        item_list.add(new todoItem("回顧過去","Search",R.drawable.img2));
        item_list.add(new todoItem("關於","About",R.drawable.img5));
        TodoAdapter todoAdapter=new TodoAdapter(this,item_list);
        listView.setAdapter(todoAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    startActivity(new Intent(DesktopActivity.this, MainActivity.class));
                }
                else if(position==1){
                    startActivity(new Intent(DesktopActivity.this, SearchActivity.class));
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //取得現在時間
        SimpleDateFormat f=new SimpleDateFormat("yyyy/MM/dd");
        Date curDate =new Date(System.currentTimeMillis());
        String str=f.format(curDate);
        //建立今日事項
        list=new ArrayList<DataModel>();
        access=new DBAccess(this,"schedule",null,1);
        //Cursor c=access.getData(null,DBAccess.DATE_FIELD);
        Cursor c=access.getData(DBAccess.DATE_FIELD+" ='"+str+"'",DBAccess.TIME_FIELD);
        c.moveToFirst();
        for(int i=0;i<c.getCount();i++){
            /*title.add(c.getString(1)+"");
            date.add(c.getString(2)+"");
            time.add(c.getString(3)+"");*/
            list.add(new DataModel(c.getString(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6)));
            c.moveToNext();
        }
        //設定桌面icon今日代辦事項的個數
        ShortcutBadger.applyCount(getApplicationContext(), list.size());
    }
}
