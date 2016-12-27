package com.lk.todolist;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import me.leolin.shortcutbadger.ShortcutBadger;

public class MainActivity extends AppCompatActivity {

    private  Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    //public static ArrayList<String> title,date,time;
    public static ArrayList<DataModel>list;
    DBAccess access;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=(Toolbar)findViewById(R.id.app_bar);
        toolbar.setTitle("代辦事項");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //更改狀態欄顏色
       // WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
        //localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);


        tabLayout=(TabLayout)findViewById(R.id.tabLayout);
        viewPager=(ViewPager)findViewById(R.id.viewPager);
        viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new HomeFragment(),"今日行程");
        viewPagerAdapter.addFragments(new SecondFragment(),"First");
        viewPagerAdapter.addFragments(new ThirdFragment(),"Second");


        //Toast.makeText(MainActivity.this,title.size()+" "+title.get(2),Toast.LENGTH_LONG).show();



        //setupTabIcons();
    }

    @Override
    protected void onResume() {
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        //取得現在時間
        SimpleDateFormat f=new SimpleDateFormat("yyyy/MM/dd");
        Date curDate =new Date(System.currentTimeMillis());
        String str=f.format(curDate);

        /*title=new ArrayList<String>();
        date=new ArrayList<String>();
        time=new ArrayList<String>();*/
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
        }Toast.makeText(this,str+" "+c.getCount(),Toast.LENGTH_LONG).show();
        //設定桌面icon今日代辦事項的個數
        ShortcutBadger.applyCount(getApplicationContext(), list.size());
        super.onResume();
    }

    /* private void setupTabIcons() {
         tabLayout.getTabAt(0).setIcon(R.drawable.img2);
         tabLayout.getTabAt(1).setIcon(R.drawable.img2);
         tabLayout.getTabAt(2).setIcon(R.drawable.img2);
     }*/
   /* private void setupTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("ONE");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img2, 0, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("TWO");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img2, 0, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("THREE");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img2, 0, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);
    }*/
}
