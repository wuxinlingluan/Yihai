package com.github.yihai.base.recycle;

/**
 * Created by dzl on 2017/12/7.
 */

public class RecycleItemBean<T> {

    private int id;
    private int type;
    private String title;
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
