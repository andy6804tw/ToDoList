package com.lk.todolist.Desktop;

/**
 * Created by andy6804tw on 2016/12/23.
 */

public class todoItem {
    private String event,desc;
    private  int imgId;
    public todoItem(String event, String desc,int id) {
        this.event = event;
        this.desc = desc;
        imgId=id;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
