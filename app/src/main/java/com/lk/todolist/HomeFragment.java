package com.lk.todolist;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private FlowLayout mFlowLayout;

    private String[] mVals =new String[]{
            "Hello","Android","Welcome","Music"
    };

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.fragment_home, container, false);
        mFlowLayout = (FlowLayout) view.findViewById(R.id.flowLayout);
        initData();


        return view;
    }
    public void initData(){
        LayoutInflater mInflater = LayoutInflater.from(getActivity());

        for (int i = 0; i < mVals.length; i++) {
            final TextView tv = (TextView) mInflater.inflate(R.layout.textview, mFlowLayout, false);
            tv.setText(mVals[i]);
            mFlowLayout.addView(tv);

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), tv.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

}
