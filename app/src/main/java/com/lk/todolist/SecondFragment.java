package com.lk.todolist;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import at.grabner.circleprogress.AnimationState;
import at.grabner.circleprogress.AnimationStateChangedListener;
import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.TextMode;

import static com.lk.todolist.HomeFragment.count;


/**
 * A simple {@link Fragment} subclass.
 */
public class SecondFragment extends Fragment {

    private CircleProgressView mCircleView;
    private Boolean mShowUnit = true;
    private TextView tvWork,tvFinish,tvUnFinish;
    private  int count_finish=0;
    private int finisg_count=0;
    private CardView card_View;
    private DateFormat formatDateTime;
    private Calendar dateTime ;
    TextView tvDate;
    String mDate="";

    public SecondFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.fragment_second, container, false);
        mCircleView = (CircleProgressView) view.findViewById(R.id.circleView);
        tvWork=(TextView)view.findViewById(R.id.tvWork);
        tvFinish=(TextView)view.findViewById(R.id.tvFinish);
        tvUnFinish=(TextView)view.findViewById(R.id.tvUnFinish);
        card_View=(CardView)view.findViewById(R.id.card_view);

        mCircleView.setShowTextWhileSpinning(true); // Show/hide text in spinning mode
        mCircleView.setText("Loading...");
        mCircleView.setOnAnimationStateChangedListener(
                new AnimationStateChangedListener() {
                    @Override
                    public void onAnimationStateChanged(AnimationState _animationState) {
                        switch (_animationState) {
                            case IDLE:
                            case ANIMATING:
                            case START_ANIMATING_AFTER_SPINNING:
                                mCircleView.setTextMode(TextMode.PERCENT); // show percent if not spinning
                                mCircleView.setUnitVisible(mShowUnit);
                                break;
                            case SPINNING:
                                mCircleView.setTextMode(TextMode.TEXT); // show text while spinning
                                mCircleView.setUnitVisible(false);
                            case END_SPINNING:
                                break;
                            case END_SPINNING_START_ANIMATING:
                                break;

                        }
                    }
                }
        );
        card_View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //客製化dialog
                final Dialog dialog = new Dialog(getActivity());
                dialog.setTitle("查詢歷史");
                dialog.setContentView(R.layout.search_todo_count);
                dialog.setCancelable(false);
                Button btnYes = (Button) dialog.findViewById(R.id.btnYes);
                Button btnNo = (Button) dialog.findViewById(R.id.btnNo);

                //時間日期初始化
                formatDateTime = DateFormat.getDateTimeInstance();
                dateTime = Calendar.getInstance();
                tvDate = (TextView) dialog.findViewById(R.id.tvDate);
                //時間、日期PickerDialog監聽事件
                tvDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DatePickerDialog(getActivity(), d1, dateTime.get(Calendar.YEAR), dateTime.get(Calendar.MONTH),
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

                        if(mDate.equals(""))
                            Toast.makeText(getActivity(),"請正確選擇日期哦!",Toast.LENGTH_SHORT).show();
                        else {
                            DBAccess access;
                            finisg_count=0;
                            access=new DBAccess(getActivity(),"schedule",null,1);
                            final Cursor c=access.getData(DBAccess.DATE_FIELD+" ='"+mDate+"'",DBAccess.DATE_FIELD);
                            c.moveToFirst();
                            for(int i=0;i<c.getCount();i++){
                                if(c.getString(6).equals("完成"))
                                    finisg_count++;
                                c.moveToNext();
                            }
                            tvWork.setText(mDate+"工作量: "+c.getCount());
                            tvFinish.setText("完成: "+finisg_count);
                            tvUnFinish.setText("未完成: "+(c.getCount()-finisg_count));
                            mCircleView.spin();
                            //Handler.postDelayed
                            //Android 才提供 ( android.os.Handler.postDelayed(Runnable r, long delayMillis) )
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable(){

                                @Override
                                public void run() {

                                    //過兩秒後要做的事情
                                    mCircleView.stopSpinning();
                                    mCircleView.setValue(Float.parseFloat(Double.toString(finisg_count*1.0/c.getCount()*100)));
                                }}, 1000);
                            dialog.cancel();
                        }
                        }
                });
                dialog.show();
            }
        });

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //相當於Fragment的onResume
            DBAccess access;
            count_finish=0;
            //取得現在時間
            SimpleDateFormat f=new SimpleDateFormat("yyyy/MM/dd");
            Date curDate =new Date(System.currentTimeMillis());
            String str=f.format(curDate);
            count=0;
            access=new DBAccess(getActivity(),"schedule",null,1);
            //Cursor c=access.getData(null,DBAccess.DATE_FIELD);
            Cursor c=access.getData(DBAccess.DATE_FIELD+" ='"+str+"'",DBAccess.TIME_FIELD);
            c.moveToFirst();
            for(int i=0;i<c.getCount();i++){
                if(c.getString(6).equals("完成"))
                    count_finish++;
                c.moveToNext();
            }
            tvWork.setText("今日工作量: "+c.getCount());
            tvFinish.setText("完成: "+count_finish);
            tvUnFinish.setText("未完成: "+(c.getCount()-count_finish));
            mCircleView.spin();
            //Handler.postDelayed
            //Android 才提供 ( android.os.Handler.postDelayed(Runnable r, long delayMillis) )
            Handler handler = new Handler();
            handler.postDelayed(new Runnable(){

                @Override
                public void run() {

                    //過兩秒後要做的事情
                    mCircleView.stopSpinning();
                    mCircleView.setValue(Float.parseFloat(Double.toString(count_finish*1.0/HomeFragment.list.size()*100)));
                }}, 1000);
        }
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
                    mDate=(year+"/"+str_monthOfYear+"/"+str_dayOfMonth);
                    tvDate.setText("     "+mDate);
                    //updateTextLabel();
                }
            };

}
