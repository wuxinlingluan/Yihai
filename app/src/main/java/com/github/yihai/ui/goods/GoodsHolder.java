package com.github.yihai.ui.goods;

import android.os.Bundle;

import com.github.yihai.bean.GoodsFloorBean;
import com.github.yihai.bean.GoodsLocationBean;
import com.github.yihai.bean.GoodsShelfBean;

import java.util.List;

/**
 * Created by dzl on 2017/12/15.
 */

public class GoodsHolder {
    private static GoodsHolder holder = null;

    public static GoodsHolder getInstance() {
        if (holder == null) {
            holder = new GoodsHolder();
        }
        return holder;
    }

    private List<GoodsShelfBean> mGoodsShelfList;
    private List<GoodsFloorBean> mGoodsFloorList;
    private List<GoodsLocationBean> mGoodsLocationList;

    public List<GoodsShelfBean> getmGoodsShelfList() {
        return mGoodsShelfList;
    }

    public void setmGoodsShelfList(List<GoodsShelfBean> mGoodsShelfList) {
        this.mGoodsShelfList = mGoodsShelfList;
    }

    public List<GoodsFloorBean> getmGoodsFloorList() {
        return mGoodsFloorList;
    }

    public void setmGoodsFloorList(List<GoodsFloorBean> mGoodsFloorList) {
        this.mGoodsFloorList = mGoodsFloorList;
    }

    public List<GoodsLocationBean> getmGoodsLocationList() {
        return mGoodsLocationList;
    }

    public void setmGoodsLocationList(List<GoodsLocationBean> mGoodsLocationList) {
        this.mGoodsLocationList = mGoodsLocationList;
    }
}
