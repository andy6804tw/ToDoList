package com.lk.todolist;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import at.grabner.circleprogress.AnimationState;
import at.grabner.circleprogress.AnimationStateChangedListener;
import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.TextMode;


/**
 * A simple {@link Fragment} subclass.
 */
public class SecondFragment extends Fragment {

    private CircleProgressView mCircleView;
    private Boolean mShowUnit = true;
    private TextView tvWork,tvFinish,tvUnFinish;
    private  int count_finish=0;
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



        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //相當於Fragment的onResume

            count_finish=0;
            for(int i =0;i<HomeFragment.list.size();i++){
                if(HomeFragment.list.get(i).getStatue().equals("完成"))
                    count_finish++;
            }
            tvWork.setText("今日工作量: "+HomeFragment.list.size());
            tvFinish.setText("完成: "+count_finish);
            tvUnFinish.setText("未完成: "+(HomeFragment.list.size()-count_finish));
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

}
