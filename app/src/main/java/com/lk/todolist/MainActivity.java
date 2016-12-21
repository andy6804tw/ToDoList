package com.lk.todolist;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private  Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    public static ArrayList<String> title,date,time;
    DBAccess access;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

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

        title=new ArrayList<String>();
        date=new ArrayList<String>();
        time=new ArrayList<String>();
        access=new DBAccess(this,"schedule",null,1);
        Cursor c=access.getData(null,DBAccess.DATE_FIELD);
        c.moveToFirst();
        for(int i=0;i<c.getCount();i++){
            title.add(c.getString(1)+"");
            date.add(c.getString(2)+"");
            time.add(c.getString(3)+"");
            c.moveToNext();
        }
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
