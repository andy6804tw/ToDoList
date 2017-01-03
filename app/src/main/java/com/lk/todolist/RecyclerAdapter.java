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
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import me.leolin.shortcutbadger.ShortcutBadger;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    ArrayList<DataModel>list=new ArrayList<DataModel>();
    int count=0;

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

        //對每一個cell註冊點擊事件
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index;
                LinearLayout linearLayout = (LinearLayout)v.findViewById(R.id.linearLayout);
                View subView = LayoutInflater.from(v.getContext()).inflate(R.layout.card_add_layout, (ViewGroup)v, false);
                TextView tvDesc=(TextView)subView.findViewById(R.id.tvDesc);
                TextView tvCategory=(TextView)subView.findViewById(R.id.tvCategory);
                String strDesc="";
                    strDesc=list.get(viewHolder.getAdapterPosition()).getDesc();
                    if(strDesc.equals(""))
                        strDesc="無";
                    tvDesc.setText("備註: " +strDesc);
                    tvCategory.setText("類別: " + list.get(viewHolder.getAdapterPosition()).getCategory());



                //利用單元控制的標記值就標記為單元格的單元格，而不是單元格的單元格。標記值也就不存在了。
                //如果不取消重用，那麼將會出現未曾點擊就已經添加子視圖的效果，再點擊的時間會繼續添加而不是收回。
                if (v.findViewById(R.id.linearLayout).getTag() == null) {
                    index = 1;
                } else {
                    index = (int)v.findViewById(R.id.linearLayout).getTag();
                }

                //Log.i("Card", "Card點擊: " + index);

                // close狀態: 增加內容
                if (index == 1) {
                    linearLayout.addView(subView);
                    subView.setTag(1000);
                    v.findViewById(R.id.linearLayout).setTag(2);
                } else {
                    // open狀態： 移除增加內容
                    linearLayout.removeView(v.findViewWithTag(1000));
                    v.findViewById(R.id.linearLayout).setTag(1);
                }
            }
        });
        // 取消viewHolder的重用機制（滑出View自動收回成預設狀態index=0 close）
        viewHolder.setIsRecyclable(false);

            return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

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
            viewHolder.tvTitle.setText(list.get(i).getTitle());
            viewHolder.tvDate.setText(list.get(i).getDate());
            viewHolder.tvTime.setText(list.get(i).getTime());

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

                        switch (item.getItemId()) {
                            case R.id.mnu_item_modify://修改
                                //Toast.makeText(mContext, "modify"+" "+MainActivity.list.get(i).getId(), Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent();
                                    intent.setClass(mContext, modify.class); //設定新活動視窗類別
                                    Bundle bundle=new Bundle();
                                    bundle.putString("id",list.get(i).getId());//將id傳遞到新的活動視窗中 從1開始?
                                    intent.putExtras(bundle);
                                    mContext.startActivity(intent); //開啟新的活動視窗
                                break;
                            case R.id.mnu_item_delete://刪除

                                String delete_id;
                                delete_id=list.get(i).getId();
                                final DataModel remove_data;
                                remove_data=list.get(i);
                                final int position_delete=viewHolder.getAdapterPosition();
                                //確定刪除
                                access.delete(delete_id);
                                list.remove(i);
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

                                                        access.add(remove_data.getTitle(),remove_data.getDate(),remove_data.getTime(),remove_data.getCategory(),remove_data.getDesc(),remove_data.getStatue());
                                                        ArrayList<DataModel>temp_list=new ArrayList<DataModel>();
                                                        remove_data.id=Integer.toString(Integer.parseInt(remove_data.id)+1);
                                                        listInit();
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
                                        listInit();
                                break;
                            case R.id.mnu_item_done:
                                final DataModel updae_data=list.get(i);
                                notifyDataSetChanged();
                                access.update(updae_data.getTitle(),updae_data.getDate(),updae_data.getTime(),updae_data.getCategory(),updae_data.getDesc(),"完成",DBAccess.ID_FIELD+" ="+updae_data.getId());
                               // list.get(i).statue="完成";
                                listInit();
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
                                                        access.update(updae_data.getTitle(),updae_data.getDate(),updae_data.getTime(),updae_data.getCategory(),updae_data.getDesc(),"未完成",DBAccess.ID_FIELD+" ="+updae_data.getId());
                                                        //list.get(i).statue="未完成";
                                                        listInit();
                                                        notifyDataSetChanged();
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

               /* Snackbar.make(v, "Click detected on item " + i+" "+list.get(i).getCategory()+" "+list.get(i).getDesc(),
                        Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
            return list.size();
    }
    public void listInit(){
        DBAccess access;
        count=0;
        //取得現在時間
        SimpleDateFormat f=new SimpleDateFormat("yyyy/MM/dd");
        Date curDate =new Date(System.currentTimeMillis());
        String str=f.format(curDate);
        count=0;
        list=new ArrayList<DataModel>();
        access=new DBAccess(mContext,"schedule",null,1);
        Cursor c=access.getData(DBAccess.DATE_FIELD+" ='"+str+"'",DBAccess.TIME_FIELD);
        c.moveToFirst();
        for(int i=0;i<c.getCount();i++){
            if(c.getString(6).equals("未完成"))
                count++;
            list.add(new DataModel(c.getString(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6)));
            c.moveToNext();
        }
        //設定桌面icon今日代辦事項的個數
        ShortcutBadger.applyCount(mContext.getApplicationContext(), count);
    }


}