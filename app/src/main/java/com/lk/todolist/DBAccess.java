package com.lk.todolist;

/**
 * Created by andy6804tw on 2016/12/21.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBAccess extends SQLiteOpenHelper {
    protected final static String TABLE_NAME="todolist";//建議字串常數
    protected final static String ID_FIELD="_id";
    protected final static String TITLE_FIELD="title";
    public final static String DATE_FIELD="date";
    public final static String TIME_FIELD="time";
    protected final static String CATRGORY_FIELD="category";
    protected final static String DESC_FIELD="desc";
    protected final static String STATUE_FIELD="statue";

    public DBAccess(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {//建構子1.哪個Activity呼叫2.資料庫名稱 3.資料庫物件4.版本
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {//第一次建構資料表呼叫,應用程式第一次執行會做
        /*
            create table todoList{
                integer primary key autoincrement,
                title text,
                date text,
                time text,
                catrgory text,
                desc text,
                statue text
            }
         */
        String sql="create table "+TABLE_NAME+"("+ID_FIELD+" integer primary key autoincrement,"
                +TITLE_FIELD+" text,"
                +DATE_FIELD+" text,"
                +TIME_FIELD+" text,"
                +CATRGORY_FIELD+" text,"
                +DESC_FIELD+" text,"
                +STATUE_FIELD+" text)";
        Log.e("SQLDB",sql);
        db.execSQL(sql);//不回傳資料的資料庫都能跑,更新新增刪除

    }

    @Override//當應用程式有更新再次開起來會檢查新app資料庫版本和舊資料是否一致
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exits "+TABLE_NAME);
        onCreate(db);

    }

    public long add(String title,String date,String time,String category,String desc,String statue){

        SQLiteDatabase db=getWritableDatabase();//物件可寫入資料
        ContentValues values=new ContentValues();
        if(title!=null)
            values.put(TITLE_FIELD,title);
        if(date!=null)
            values.put(DATE_FIELD,date);
        if(time!=null)
            values.put(TIME_FIELD,time);
        if(category!=null)
            values.put(CATRGORY_FIELD,category);
        if(desc!=null)
            values.put(DESC_FIELD,desc);
        if(statue!=null)
            values.put(STATUE_FIELD,statue);
        long result=db.insert(TABLE_NAME,null,values);
        db.close();

        return result;
    }
    public Cursor getData(String whereStr,String orderbyStr){
        /*
         select _id, title, date, time
          from todolist
          where _id=5        過濾條件
          order by date      日期排序
        */
        SQLiteDatabase db=getReadableDatabase();
        Cursor c=db.query(TABLE_NAME,new String[]{ID_FIELD,TITLE_FIELD,DATE_FIELD,TIME_FIELD,CATRGORY_FIELD,DESC_FIELD,STATUE_FIELD}
                ,whereStr,null,null,null,orderbyStr);//是在記憶體的空間 裡面包含很多筆資料 可走訪資料想像成陣列

        return c;

    }
    //修改
    long update(String title,String date, String time,String category,String desc,String statue,String whereClause) {
        SQLiteDatabase db=this.getWritableDatabase();//取得讀寫資料表物件
        ContentValues values =new ContentValues();
        if(date!=null)  values.put(DATE_FIELD, date);
        if(time!=null)  values.put(TIME_FIELD, time);
        if(title!=null)  values.put(TITLE_FIELD, title);
        if(category!=null)  values.put(CATRGORY_FIELD, category);
        if(desc!=null)  values.put(DESC_FIELD,desc);
        if(statue!=null)  values.put(STATUE_FIELD,statue);
        //執行更新資料
        long result=db.update(TABLE_NAME, values, whereClause, null);
        db.close();
        return result;//回傳更新資料筆數
    }
    //刪除
    int delete(String _id){
        SQLiteDatabase db=this.getWritableDatabase();//取得讀寫資料表物件
        int result=db.delete(TABLE_NAME, ID_FIELD+" ="+_id, null); //進行刪除
        db.close();
        return result;//回傳刪除筆數
    }



}