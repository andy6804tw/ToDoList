package com.lk.todolist;

/**
 * Created by andy6804tw on 2016/12/21.
 */

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
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

}