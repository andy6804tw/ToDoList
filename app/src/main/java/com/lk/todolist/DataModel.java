package com.lk.todolist;

/**
 * Created by andy6804tw on 2016/12/26.
 */

public class DataModel {

    String id,title,date,time;
    public DataModel(){

    }
    public DataModel(String id,String title,String date,String time){
        this.id=id;
        this.title=title;
        this.date=date;
        this.time=time;
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
}
