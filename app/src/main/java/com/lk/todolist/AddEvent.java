package com.lk.todolist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
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
    public static ArrayList<String> title,date,time;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        //判斷Statute bar寬高 fitsSystemWindows="true"
        Window window = this.getWindow();
        // Followed by google doc.
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //window.setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
        //window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorBar2));
        //當API大於棉花糖5.0才能更改statue bar顏色
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1){
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorBar2));
        }
        // For not opaque(transparent) color.
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        access=new DBAccess(this,"schedule",null,1);
        //時間日期初始化
        formatDateTime= DateFormat.getDateTimeInstance();
        dateTime = Calendar.getInstance();
        tvDate=(TextView)findViewById(R.id.tvDate);
        tvTime=(TextView)findViewById(R.id.tvTime);
        //時間、日期PickerDialog監聽事件
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            //若月份為單位數補零
            String str_monthOfYear=Integer.toString(monthOfYear+1);//月份+1
            if(str_monthOfYear.length()==1)
                str_monthOfYear="0"+str_monthOfYear;
            //若日期為單位數補零
            String str_dayOfMonth=Integer.toString(dayOfMonth);
            if(str_dayOfMonth.length()==1)
                str_dayOfMonth="0"+str_dayOfMonth;
            tvDate.setText(year+"/"+str_monthOfYear+"/"+str_dayOfMonth);
            //updateTextLabel();
        }
    };
    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateTime.set(Calendar.MINUTE, minute);
            //小時若單位數補零
            String str_hourOfDay=Integer.toString(hourOfDay);
            if(str_hourOfDay.length()==1)
                str_hourOfDay="0"+str_hourOfDay;
            //分鐘若為個位數補零
            String str_minute=Integer.toString(minute);
            if(str_minute.length()==1)
                str_minute="0"+str_minute;
            tvTime.setText(str_hourOfDay+":"+str_minute);
            //updateTextLabel();
        }
    };

    public void addData(View view) {
        edtTitle=(EditText)findViewById(R.id.edtTitle);
        edtCategory=(EditText)findViewById(R.id.edtCategory);
        edtDesc=(EditText)findViewById(R.id.edtDesc);


        long result =access.add(edtTitle.getText().toString(),tvDate.getText().toString(),tvTime.getText().toString(),edtCategory.getText().toString(),edtDesc.getText().toString(),"未完成");
        if(result>=0){
            Toast.makeText(AddEvent.this,"成功!",Toast.LENGTH_LONG).show();
            finish();
        }else{
            Toast.makeText(AddEvent.this,"失敗!",Toast.LENGTH_LONG).show();
        }
        edtTitle.setText("");tvDate.setText("");tvTime.setText("");edtCategory.setText("");edtDesc.setText("");

    }



}