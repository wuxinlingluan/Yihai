package com.github.yihai.base.recycle;

import android.support.annotation.LayoutRes;

/**
 * Created by dzl on 2017/12/7.
 */

public class RecycleTypeBean<VH> {

    private int type;
    @LayoutRes
    private int layout;
    private Class<VH> viewHolderClass;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    public Class<VH> getViewHolderClass() {
        return viewHolderClass;
    }

    public void setViewHolderClass(Class<VH> viewHolderClass) {
        this.viewHolderClass = viewHolderClass;
    }
}
