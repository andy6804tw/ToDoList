package com.lk.todolist;

/**
 * Created by andy6804tw on 2016/12/21.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> implements View.OnClickListener{

    ArrayList<String>list=new ArrayList<String>();

    private  static Context mContext;//給卡片選項用的HomeFragment.class
    public RecyclerAdapter(Context c){
        mContext=c;
    }
    public RecyclerAdapter(ArrayList<String>list,Context c){
        this.list=list;
        mContext=c;
    }

    class ViewHolder extends RecyclerView.ViewHolder{


        public TextView tvTitle,tvDate,tvTime,tvOption;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView)itemView.findViewById(R.id.tvTitle);
            tvDate = (TextView)itemView.findViewById(R.id.tvDate);
            tvTime =(TextView)itemView.findViewById(R.id.tvTime);
            tvOption =(TextView)itemView.findViewById(R.id.tvOption);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();

                    Snackbar.make(v, "Click detected on item " + position+" "+getItemId()+".",
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();

                    Snackbar.make(v, "Click detected on item " + position,
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return true;
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
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {


        if(list.size()==0){
            viewHolder.tvTitle.setText(MainActivity.title.get(i));
            viewHolder.tvDate.setText(MainActivity.date.get(i));
            viewHolder.tvTime.setText(MainActivity.time.get(i));
        }else{
            viewHolder.tvTitle.setText(MainActivity.title.get(HomeFragment.index.get(i)));
            viewHolder.tvDate.setText(MainActivity.date.get(HomeFragment.index.get(i)));
            viewHolder.tvTime.setText(MainActivity.time.get(HomeFragment.index.get(i)));
        }
        //設定卡片選項item option
        viewHolder.tvOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //Display option menu

                PopupMenu popupMenu = new PopupMenu(mContext, viewHolder.tvOption);
                popupMenu.inflate(R.menu.card_option_menu);//畫一個menu
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        DBAccess access =new DBAccess(mContext,"schedule",null,1);
                        int id=0;
                        Cursor c=access.getData(null, null);//資料查詢，無條件(按照資料放入順序排列、原始順序)
                        c.moveToFirst();
                        z://找出原始id順序
                        for(id=1;id<=c.getCount();id++){
                            if(c.getString(1).equals(MainActivity.title.get(i))&&c.getString(2).equals(MainActivity.date.get(i))&&c.getString(3).equals(MainActivity.time.get(i))){
                                //Toast.makeText(mContext, "Saved"+" "+id, Toast.LENGTH_LONG).show();
                                id=Integer.parseInt(c.getString(0));//取得id值(偷吃步作法) *若曾經刪除也算一筆紀錄 所以不能用Cursor的索引值當id
                                break z;
                            }
                            c.moveToNext();
                        }

                        switch (item.getItemId()) {
                            case R.id.mnu_item_modify://修改
                                Toast.makeText(mContext, "modify"+" "+id, Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent();
                                    intent.setClass(mContext, modify.class); //設定新活動視窗類別
                                    Bundle bundle=new Bundle();
                                    bundle.putString("id",Integer.toString(id));//將id傳遞到新的活動視窗中 從1開始?
                                    intent.putExtras(bundle);
                                    mContext.startActivity(intent); //開啟新的活動視窗
                                break;
                            case R.id.mnu_item_delete://刪除
                                //Delete item
                                access.delete(Integer.toString(id));
                                final String title=MainActivity.title.get(i);
                                final String date=MainActivity.date.get(i);
                                final String time=MainActivity.time.get(i);
                                MainActivity.title.remove(i);
                                MainActivity.date.remove(i);
                                MainActivity.time.remove(i);
                                notifyDataSetChanged();
                                //Toast.makeText(mContext, "Deleted "+i+" "+MainActivity.title.get(i), Toast.LENGTH_LONG).show();
                                Snackbar.make(v, "刪除成功" ,
                                        Snackbar.LENGTH_LONG)
                                        .setAction("取消刪除", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                AlertDialog.Builder dialog=new AlertDialog.Builder(mContext);
                                                dialog.setMessage("確定復原?");
                                                dialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        RecyclerView recyclerView;
                                                        RecyclerView.Adapter adapter;
                                                        MainActivity.title.add(i,title);//放回去原本的位置第一個參數i代表放入的索引值
                                                        MainActivity.date.add(i,date);
                                                        MainActivity.time.add(i,time);
                                                        DBAccess access=new DBAccess(mContext,"schedule",null,1);
                                                        access.add(title,date,date);
                                                       /* LayoutInflater inflater = LayoutInflater.from(mContext);
                                                        final View view=inflater.inflate(R.layout.fragment_home, null, false);
                                                        adapter = new RecyclerAdapter(list,mContext);
                                                        recyclerView =(RecyclerView) view.findViewById(R.id.recycler_view);
                                                        recyclerView.setAdapter(adapter);*/
                                                        notifyDataSetChanged();//監聽list是否有變動
                                                    }
                                                });
                                                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.cancel();
                                                    }
                                                });
                                                dialog.show();

                                            }
                                        }).show();
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

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

    public void setFilter(ArrayList<String>Mylist){

       // list.addAll(this.list);
        list=Mylist;
        notifyDataSetChanged();

    }

}