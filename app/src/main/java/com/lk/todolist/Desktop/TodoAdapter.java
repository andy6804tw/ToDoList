package com.lk.todolist.Desktop;

/**
 * Created by andy6804tw on 2016/12/23.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lk.todolist.R;

import java.util.List;


public class TodoAdapter extends BaseAdapter {
    private Context context;//Activity
    private List<todoItem> list;
    private class ViewHolder{
        TextView tvEvent,tvDesc;
        ImageView ivIcon;
    }

    public TodoAdapter(Context context, List<todoItem> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        LayoutInflater layoutInflater=LayoutInflater.from(context);//負責把todoitem讀出來
        if(convertView==null){//假如版面為空
            convertView=layoutInflater.inflate(R.layout.todolist_item,null);
            viewHolder=new ViewHolder();
            viewHolder.tvEvent=(TextView)convertView.findViewById(R.id.tvEvent);
            viewHolder.tvDesc=(TextView)convertView.findViewById(R.id.tvDesc);
            viewHolder.ivIcon=(ImageView)convertView.findViewById(R.id.ivIcon);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        todoItem todoItem=list.get(position);
        viewHolder.tvEvent.setText(todoItem.getEvent());
        viewHolder.tvDesc.setText(todoItem.getDesc());
        viewHolder.ivIcon.setImageResource(todoItem.getImgId());
        return convertView;
    }
}
