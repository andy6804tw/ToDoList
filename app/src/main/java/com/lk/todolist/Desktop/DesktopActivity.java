package com.lk.todolist.Desktop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lk.todolist.MainActivity;
import com.lk.todolist.R;

import java.util.ArrayList;

public class DesktopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desktop);
        //更改狀態欄顏色
        WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
        localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        ListView listView=(ListView)findViewById(R.id.listView);
        //設定資料
        ArrayList<todoItem> list=new ArrayList<>();
        list.add(new todoItem("代辦事項","Schedule",R.drawable.img4));
        list.add(new todoItem("回顧過去","Search",R.drawable.img2));
        list.add(new todoItem("鬧鍾","Clock",R.drawable.img4));
        TodoAdapter todoAdapter=new TodoAdapter(this,list);
        listView.setAdapter(todoAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    startActivity(new Intent(DesktopActivity.this, MainActivity.class));
                }
            }
        });
    }
}
