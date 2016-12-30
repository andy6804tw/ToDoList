package com.lk.todolist;

/**
 * Created by andy6804tw on 2016/12/21.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lk.todolist.Search.SearchActivity;

import java.util.ArrayList;

import me.leolin.shortcutbadger.ShortcutBadger;

import static com.lk.todolist.MainActivity.count;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    ArrayList<DataModel>list=new ArrayList<DataModel>();

    private  static Context mContext;//給卡片選項用的HomeFragment.class
    public RecyclerAdapter(Context c){
        mContext=c;
    }
    public RecyclerAdapter(ArrayList<DataModel>list,Context c){
        this.list=list;
        mContext=c;
    }

    class ViewHolder extends RecyclerView.ViewHolder{


        public TextView tvTitle,tvDate,tvTime,tvOption;
        public ImageView ivStatute;
        public CardView card_view;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView)itemView.findViewById(R.id.tvTitle);
            tvDate = (TextView)itemView.findViewById(R.id.tvDate);
            tvTime =(TextView)itemView.findViewById(R.id.tvTime);
            tvOption =(TextView)itemView.findViewById(R.id.tvOption);
            ivStatute=(ImageView)itemView.findViewById(R.id.ivStatute);
            card_view=(CardView)itemView.findViewById(R.id.card_view);
            if(mContext.toString().contains("SearchActivity")){
                tvOption.setVisibility(View.GONE);//設定隱藏且不佔空間，動態隱藏顯示元件
            }
           /* itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();

                    Snackbar.make(v, "Click detected on item " + position+" "+MainActivity.list.get(Integer.parseInt(MainActivity.list.get(position).getId())).getCategory()+".",
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();

                    Snackbar.make(v, "Click detected on item " + position+" "+MainActivity.list.get(Integer.parseInt(MainActivity.list.get(position).getId())).getCategory()+" "+MainActivity.list.get(Integer.parseInt(MainActivity.list.get(position).getId())).getDesc(),
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return true;
                }
            });*/
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup,final int i) {

            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.card_layout, viewGroup, false);
            final ViewHolder viewHolder = new ViewHolder(v);

        // 对每一个cell注册点击事件
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index;
                LinearLayout linearLayout = (LinearLayout)v.findViewById(R.id.linearLayout);
                View subView = LayoutInflater.from(v.getContext()).inflate(R.layout.card_add_layout, (ViewGroup)v, false);
                TextView tvDesc=(TextView)subView.findViewById(R.id.tvDesc);
                //viewHolder.getAdapterPosition()取得現在list的位置取得描述資料
                if(list.size()==0) {
                    if (mContext.toString().contains("MainActivity")) {
                        tvDesc.setText("備註: " + MainActivity.list.get(viewHolder.getAdapterPosition()).getDesc());
                    } else {
                        tvDesc.setText("備註: " + SearchActivity.list.get(viewHolder.getAdapterPosition()).getDesc());
                    }
                }else{
                    tvDesc.setText("備註: " +list.get(viewHolder.getAdapterPosition()).getDesc());
                }

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
        });

        // 取消viewHolder的重用机制（很重要）
        viewHolder.setIsRecyclable(false);

            return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {


        if(list.size()==0){
            //設定狀態圖片
            if(MainActivity.list.get(i).getStatue().equals("完成")){
                viewHolder.ivStatute.setImageResource(R.drawable.done2);
            }else{
                viewHolder.ivStatute.setImageResource(R.drawable.undone2);
            }
            /*viewHolder.tvTitle.setText(MainActivity.title.get(i));
            viewHolder.tvDate.setText(MainActivity.date.get(i));
            viewHolder.tvTime.setText(MainActivity.time.get(i));*/
            viewHolder.tvTitle.setText(MainActivity.list.get(i).getTitle());
            viewHolder.tvDate.setText(MainActivity.list.get(i).getDate());
            viewHolder.tvTime.setText(MainActivity.list.get(i).getTime());

        }else{
            //判斷是否SearchActivity 要變換cardView顏色
            if(mContext.toString().contains("SearchActivity")){
                viewHolder.card_view.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorSearchCard));
            }
                //設定狀態圖片
                if(list.get(i).getStatue().equals("完成")){
                    viewHolder.ivStatute.setImageResource(R.drawable.done2);
                }else{
                    viewHolder.ivStatute.setImageResource(R.drawable.undone2);

            }
            /*viewHolder.tvTitle.setText(MainActivity.title.get(HomeFragment.index.get(i)));
            viewHolder.tvDate.setText(MainActivity.date.get(HomeFragment.index.get(i)));
            viewHolder.tvTime.setText(MainActivity.time.get(HomeFragment.index.get(i)));*/
            viewHolder.tvTitle.setText(list.get(i).getTitle());
            viewHolder.tvDate.setText(list.get(i).getDate());
            viewHolder.tvTime.setText(list.get(i).getTime());
        }
        //設定卡片選項item option
        viewHolder.tvOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //Display option menu

                final PopupMenu popupMenu = new PopupMenu(mContext, viewHolder.tvOption);
                popupMenu.inflate(R.menu.card_option_menu);//畫一個menu
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        final DBAccess access =new DBAccess(mContext,"schedule",null,1);
                        /*int id=0;
                        Cursor c=access.getData(null, null);//資料查詢，無條件(按照資料放入順序排列、原始順序)
                        c.moveToFirst();
                        z://找出原始id順序
                        for(id=1;id<=c.getCount();id++){
                            if(c.getString(1).equals(MainActivity.title.get(i))&&c.getString(2).equals(MainActivity.date.get(i))&&c.getString(3).equals(MainActivity.time.get(i))){
                                Toast.makeText(mContext, "Saved"+" "+id, Toast.LENGTH_LONG).show();
                                id=Integer.parseInt(c.getString(0));//取得id值(偷吃步作法) *若曾經刪除也算一筆紀錄 所以不能用Cursor的索引值當id
                                break z;
                            }
                            c.moveToNext();
                        }*/

                        switch (item.getItemId()) {
                            case R.id.mnu_item_modify://修改
                                Toast.makeText(mContext, "modify"+" "+MainActivity.list.get(i).getId(), Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent();
                                    intent.setClass(mContext, modify.class); //設定新活動視窗類別
                                    Bundle bundle=new Bundle();
                                    bundle.putString("id",MainActivity.list.get(i).getId());//將id傳遞到新的活動視窗中 從1開始?
                                    intent.putExtras(bundle);
                                    mContext.startActivity(intent); //開啟新的活動視窗
                                break;
                            case R.id.mnu_item_delete://刪除
                                //Delete item
                               //access.delete(MainActivity.list.get(i).getId());
                               /* final String title=MainActivity.title.get(i);
                                final String date=MainActivity.date.get(i);
                                final String time=MainActivity.time.get(i);
                                MainActivity.title.remove(i);
                                MainActivity.date.remove(i);
                                MainActivity.time.remove(i);*/
                                String delete_id=MainActivity.list.get(i).getId();
                                final int position_delete=viewHolder.getAdapterPosition();
                                final DataModel remove_data=MainActivity.list.get(i);
                                //確定刪除
                                access.delete(delete_id);
                                MainActivity.list.remove(i);
                                if(remove_data.getStatue().equals("未完成"))
                                    ShortcutBadger.applyCount(mContext.getApplicationContext(), count-1);//桌面未完成次數
                                notifyDataSetChanged();
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

                                                        //DBAccess access=new DBAccess(mContext,"schedule",null,1);
                                                        //access.add(remove_data.getTitle(),remove_data.getDate(),remove_data.getTime(),remove_data.getCategory(),remove_data.getDesc(),remove_data.getStatue());
                                                        access.add(remove_data.getTitle(),remove_data.getDate(),remove_data.getTime(),remove_data.getCategory(),remove_data.getDesc(),remove_data.getStatue());
                                                        ArrayList<DataModel>temp_list=new ArrayList<DataModel>();
                                                        remove_data.id=Integer.toString(Integer.parseInt(remove_data.id)+1);
                                                        Toast.makeText(mContext,position_delete+"",Toast.LENGTH_LONG).show();
                                                        for(int j=0;j<=MainActivity.list.size();j++){
                                                            if(j==position_delete)
                                                                temp_list.add(remove_data);
                                                            else if(j>position_delete)
                                                                temp_list.add(MainActivity.list.get(j-1));
                                                            else
                                                                temp_list.add(MainActivity.list.get(j));
                                                        }
                                                        MainActivity.list=temp_list;
                                                        if(remove_data.getStatue().equals("未完成"))
                                                            ShortcutBadger.applyCount(mContext.getApplicationContext(), count);//桌面未完成次數
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
                            case R.id.mnu_item_done:
                                final DataModel updae_data=MainActivity.list.get(i);
                                //確定刪除
                                MainActivity.list.get(i).statue="完成";
                                ShortcutBadger.applyCount(mContext.getApplicationContext(), MainActivity.count-1);//桌面未完成次數
                                notifyDataSetChanged();
                                access.update(updae_data.getTitle(),updae_data.getDate(),updae_data.getTime(),updae_data.getCategory(),updae_data.getDesc(),"完成",DBAccess.ID_FIELD+" ="+updae_data.getId());
                                Snackbar.make(v, "事件完成" ,
                                        Snackbar.LENGTH_LONG)
                                        .setAction("取消完成", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                AlertDialog.Builder dialog=new AlertDialog.Builder(mContext);
                                                dialog.setMessage("確定復原?");
                                                dialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        MainActivity.list.get(i).statue="未完成";
                                                        ShortcutBadger.applyCount(mContext.getApplicationContext(), MainActivity.count);//桌面未完成次數
                                                        notifyDataSetChanged();
                                                        access.update(updae_data.getTitle(),updae_data.getDate(),updae_data.getTime(),updae_data.getCategory(),updae_data.getDesc(),"未完成",DBAccess.ID_FIELD+" ="+updae_data.getId());
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

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Snackbar.make(v, "Click detected on item " + i+" "+MainActivity.list.get(i).getCategory()+" "+MainActivity.list.get(i).getDesc(),
                        Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        if(list.size()==0)
            return MainActivity.list.size();
        else
            return list.size();
    }



}