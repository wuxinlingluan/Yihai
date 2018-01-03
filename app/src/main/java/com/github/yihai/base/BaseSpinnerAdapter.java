package com.github.yihai.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dzl on 2017/12/15.
 */

public abstract class BaseSpinnerAdapter<T> extends BaseAdapter {
    protected Context context;
    protected String firstTitle;
    protected List<T> mDatas;

    public BaseSpinnerAdapter(Context context) {
        this.context = context;
    }

    public BaseSpinnerAdapter(Context context, String firstTitle) {
        this.context = context;
        this.firstTitle = firstTitle;
        mDatas = new ArrayList<>();
        initFirst(firstTitle);
    }

    public void setDatas(List<T> datas) {
        mDatas.clear();
        initFirst(firstTitle);
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    protected abstract void initFirst(String firstTitle);
}
