package com.lk.todolist.Search;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.lk.todolist.DBAccess;
import com.lk.todolist.DataModel;
import com.lk.todolist.R;
import com.lk.todolist.RecyclerAdapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SearchActivity extends AppCompatActivity {

    private Toolbar toolbar;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    public static ArrayList<DataModel> list;
    DBAccess access;
    DateFormat formatDateTime;
    Calendar dateTime ;
    TextView tvDate1,tvDate2;
    String mDate1,mDate2;
    long mTime1=0,mTime2=0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //判斷Statute bar寬高 fitsSystemWindows="true"
        Window window = this.getWindow();
        // Followed by google doc.
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //window.setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorBar));

        // For not opaque(transparent) color.
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        toolbar=(Toolbar)findViewById(R.id.app_bar);
        toolbar.setTitle("回顧過去");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        //客製化dialog
        final Dialog dialog=new Dialog(this);
        dialog.setTitle("查詢歷史");
        dialog.setContentView(R.layout.search_dialog);
        Button btnYes=(Button)dialog.findViewById(R.id.btnYes);
        Button btnNo=(Button)dialog.findViewById(R.id.btnNo);

        TextView tvDate,tvTime;
        //時間日期初始化
        formatDateTime= DateFormat.getDateTimeInstance();
        dateTime = Calendar.getInstance();
        tvDate1=(TextView)dialog.findViewById(R.id.tvDate1);
        tvDate2=(TextView)dialog.findViewById(R.id.tvDate2);
        //時間、日期PickerDialog監聽事件
        tvDate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(SearchActivity.this, d1, dateTime.get(Calendar.YEAR), dateTime.get(Calendar.MONTH),
                        dateTime.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        tvDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(SearchActivity.this, d2, dateTime.get(Calendar.YEAR), dateTime.get(Calendar.MONTH),
                        dateTime.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //定義好時間字串的格式
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                //將字串轉成Date型

                try {
                    Date  dt1 =sdf.parse(mDate1);
                    Date  dt2 =sdf.parse(mDate2);
                    mTime1=dt1.getTime();
                    mTime2=dt2.getTime();
                    Toast.makeText(SearchActivity.this, dt1.getTime()+" "+mDate2,Toast.LENGTH_SHORT).show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                list=new ArrayList<DataModel>();
                access=new DBAccess(SearchActivity.this,"schedule",null,1);
                //Cursor c=access.getData(null,DBAccess.DATE_FIELD);
                Cursor c=access.getData(null,DBAccess.DATE_FIELD);
                c.moveToFirst();
                for(int i=0;i<c.getCount();i++){
            /*title.add(c.getString(1)+"");
            date.add(c.getString(2)+"");
            time.add(c.getString(3)+"");*/
                    long dataTime=0;
                    try {
                        Date  dt =sdf.parse(c.getString(2));
                        dataTime=dt.getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(dataTime>=mTime1&&dataTime<=mTime2){
                        list.add(new DataModel(c.getString(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6)));
                    }
                    c.moveToNext();
                }

                recyclerView =(RecyclerView) findViewById(R.id.recycler_view);
                layoutManager = new LinearLayoutManager(SearchActivity.this);
                recyclerView.setLayoutManager(layoutManager);
                adapter = new RecyclerAdapter(list,SearchActivity.this);
                recyclerView.setAdapter(adapter);
                //adapter.notifyDataSetChanged();

                dialog.cancel();

            }
        });
        dialog.show();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    DatePickerDialog.OnDateSetListener d1 = new DatePickerDialog.OnDateSetListener() {
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
            mDate1=(year+"/"+str_monthOfYear+"/"+str_dayOfMonth);
            tvDate1.setText(mDate1);
            //updateTextLabel();
        }
    };
    DatePickerDialog.OnDateSetListener d2 = new DatePickerDialog.OnDateSetListener() {
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
            mDate2=(year+"/"+str_monthOfYear+"/"+str_dayOfMonth);
            tvDate2.setText(mDate2);
            //updateTextLabel();
        }
    };
}
