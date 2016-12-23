package com.lk.todolist;

/**
 * Created by andy6804tw on 2016/12/21.
 */

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> implements View.OnClickListener{
    ArrayList<String>list=new ArrayList<String>();
    String Title[]={"Title1","Title2","Title3","Title4","Title5"};
    String Date[]={"2016/12/3","2016/12/3","2016/12/3","2016/12/3","2016/12/3"};
    String Time[]={"13:15","14:20","5:20","14:20","12:00"};
    static int count=0;
    public RecyclerAdapter(){

    }
    public RecyclerAdapter(ArrayList<String>list){
        this.list=list;
    }

    class ViewHolder extends RecyclerView.ViewHolder{


        public TextView tvTitle,tvDate,tvTime;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView)itemView.findViewById(R.id.tvTitle);
            tvDate = (TextView)itemView.findViewById(R.id.tvDate);
            tvTime =(TextView)itemView.findViewById(R.id.tvTime);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();

                    Snackbar.make(v, "Click detected on item " + position,
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return false;
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.card_layout, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(v);

        // 对每一个cell注册点击事件
        v.setOnClickListener(this);

        // 取消viewHolder的重用机制（很重要）
        viewHolder.setIsRecyclable(false);

            return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder,int i) {


        if(list.size()==0){
            viewHolder.tvTitle.setText(MainActivity.title.get(i));
            viewHolder.tvDate.setText(MainActivity.date.get(i));
            viewHolder.tvTime.setText(MainActivity.time.get(i));
        }else{
            viewHolder.tvTitle.setText(MainActivity.title.get(HomeFragment.index.get(i)));
            viewHolder.tvDate.setText(MainActivity.date.get(HomeFragment.index.get(i)));
            viewHolder.tvTime.setText(MainActivity.time.get(HomeFragment.index.get(i)));
        }

    }

    @Override
    public int getItemCount() {
        if(list.size()==0)
            return MainActivity.time.size();
        else
            return list.size();
    }
    @Override
    public void onClick(View v) {

        int index;

        LinearLayout linearLayout = (LinearLayout)v.findViewById(R.id.linearLayout);
        View subView = LayoutInflater.from(v.getContext()).inflate(R.layout.card_add_layout, (ViewGroup)v, false);

        // 利用cell控件的Tag值来标记cell是否被点击过,因为已经将重用机制取消，cell退出当前界面时就会被销毁，Tag值也就不存在了。
        // 如果不取消重用，那么将会出现未曾点击就已经添加子视图的效果，再点击的时候会继续添加而不是收回。
        if (v.findViewById(R.id.linearLayout).getTag() == null) {
            index = 1;
        } else {
            index = (int)v.findViewById(R.id.linearLayout).getTag();
        }

        Log.i("yichu", "onClick: " + index);

        // close状态: 添加视图
        if (index == 1) {
            linearLayout.addView(subView);
            subView.setTag(1000);
            v.findViewById(R.id.linearLayout).setTag(2);
        } else {
            // open状态： 移除视图
            linearLayout.removeView(v.findViewWithTag(1000));
            v.findViewById(R.id.linearLayout).setTag(1);
        }
    }

}