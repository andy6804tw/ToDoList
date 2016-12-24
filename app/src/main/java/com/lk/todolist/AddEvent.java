package com.lk.todolist;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class AddEvent extends AppCompatActivity {

    DBAccess access;
    EditText edtTitle,edtDate,edtTime;
    long id;//資料ID
    ListView listView;
    SimpleCursorAdapter adapter=null;
    public static ArrayList<String> title,date,time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
    access=new DBAccess(this,"schedule",null,1);
    listView=(ListView)findViewById(R.id.listView);
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(AddEvent.this,id+""+position,Toast.LENGTH_LONG).show();
            Intent intent=new Intent();
            intent.setClass(AddEvent.this, modify.class); //設定新活動視窗類別
            Bundle bundle=new Bundle();
            bundle.putString("id", id+"");//將id傳遞到新的活動視窗中
            intent.putExtras(bundle);
            startActivity(intent); //開啟新的活動視窗
        }
    });


}

    @Override
    protected void onResume() {//撈資料資料更新
        Cursor c=access.getData(null,DBAccess.DATE_FIELD);
        if(adapter==null){
            adapter=new SimpleCursorAdapter(this,R.layout.todo_item,c
                    ,new String[]{DBAccess.TITLE_FIELD,DBAccess.DATE_FIELD,DBAccess.TIME_FIELD}
                    ,new int[]{R.id.tvTitle,R.id.tvDate,R.id.tvTime},0);
            listView.setAdapter(adapter);
        }else {
            adapter.changeCursor(c);
            listView.setAdapter(adapter);
        }

        super.onResume();

    }

    @Override
    protected void onDestroy() {
        access.close();
        super.onDestroy();
    }

    public void addData(View view) {
        edtTitle=(EditText)findViewById(R.id.edtTitle);
        edtDate=(EditText)findViewById(R.id.edtDate);
        edtTime=(EditText)findViewById(R.id.edtTime);
        long result =access.add(edtTitle.getText().toString(),edtDate.getText().toString(),edtTime.getText().toString());
        if(result>=0){
            Toast.makeText(AddEvent.this,"成功!",Toast.LENGTH_LONG).show();
            Cursor c=access.getData(null,DBAccess.DATE_FIELD);
            adapter.changeCursor(c);
        }else{
            Toast.makeText(AddEvent.this,"失敗!",Toast.LENGTH_LONG).show();
        }
        edtTitle.setText("");edtDate.setText("");edtTime.setText("");
    }
}