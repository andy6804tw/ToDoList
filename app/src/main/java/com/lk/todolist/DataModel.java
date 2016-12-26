package com.lk.todolist;

/**
 * Created by andy6804tw on 2016/12/26.
 */

public class DataModel {

    String id,title,date,time,category,desc,statue;
    public DataModel(String id,String title,String date,String time,String category,String desc,String statue){
        this.id=id;
        this.title=title;
        this.date=date;
        this.time=time;
        this.category=category;
        this.desc=desc;
        this.statue=statue;
    }
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
    public String getCategory() {
        return category;
    }
    public String getDesc() {
        return desc;
    }
    public String getStatue() {
        return statue;
    }
}
