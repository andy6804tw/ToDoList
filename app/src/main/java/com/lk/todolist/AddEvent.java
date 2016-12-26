package com.lk.todolist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddEvent extends AppCompatActivity {

    DBAccess access;
    EditText edtTitle,edtCategory,edtDesc;
    TextView tvDate,tvTime;
    DateFormat formatDateTime;
    Calendar dateTime ;
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
            Toast.makeText(AddEvent.this,id+" "+position,Toast.LENGTH_LONG).show();
            Intent intent=new Intent();
            intent.setClass(AddEvent.this, modify.class); //設定新活動視窗類別
            Bundle bundle=new Bundle();
            bundle.putString("id", id+"");//將id傳遞到新的活動視窗中
            intent.putExtras(bundle);
            startActivity(intent); //開啟新的活動視窗
        }
    });
        //時間日期初始化
        formatDateTime= DateFormat.getDateTimeInstance();
        dateTime = Calendar.getInstance();
        tvDate=(TextView)findViewById(R.id.tvDate);
        tvTime=(TextView)findViewById(R.id.tvTime);
        //時間、日期PickerDialog監聽事件

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddEvent.this,"Click",Toast.LENGTH_LONG).show();
                new DatePickerDialog(AddEvent.this, d, dateTime.get(Calendar.YEAR), dateTime.get(Calendar.MONTH),
                        dateTime.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(AddEvent.this, t, dateTime.get(Calendar.HOUR_OF_DAY), dateTime.get(Calendar.MINUTE), true).show();
            }
        });


}
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateTime.set(Calendar.YEAR, year);
            dateTime.set(Calendar.MONTH, monthOfYear);
            dateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String Date=formatDateTime.format(year);
            tvDate.setText(year+"/"+(monthOfYear+1)+"/"+dayOfMonth);
            //updateTextLabel();
        }
    };
    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateTime.set(Calendar.MINUTE, minute);
            tvTime.setText(hourOfDay+":"+minute);
            //updateTextLabel();
        }
    };

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
        edtCategory=(EditText)findViewById(R.id.edtCategory);
        edtDesc=(EditText)findViewById(R.id.edtDesc);


        long result =access.add(edtTitle.getText().toString(),tvDate.getText().toString(),tvTime.getText().toString(),edtCategory.getText().toString(),edtDesc.getText().toString(),"未完成");
        if(result>=0){
            Toast.makeText(AddEvent.this,"成功!",Toast.LENGTH_LONG).show();
            Cursor c=access.getData(null,DBAccess.DATE_FIELD);
            adapter.changeCursor(c);
        }else{
            Toast.makeText(AddEvent.this,"失敗!",Toast.LENGTH_LONG).show();
        }
        edtTitle.setText("");tvDate.setText("");tvTime.setText("");edtCategory.setText("");edtDesc.setText("");
    }



}