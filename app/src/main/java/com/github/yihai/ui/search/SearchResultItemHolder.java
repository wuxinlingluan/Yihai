package com.github.yihai.ui.search;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.yihai.R;
import com.github.yihai.base.recycle.BaseViewHolder;
import com.github.yihai.base.recycle.RecycleItemBean;
import com.github.yihai.bean.GoodsFloorBean;
import com.github.yihai.bean.GoodsLocationBean;
import com.github.yihai.bean.GoodsShelfBean;
import com.github.yihai.bean.MaterialBean;
import com.github.yihai.bean.SearchResultBean;
import com.github.yihai.manager.callback.LoginBean;
import com.github.yihai.ui.goods.GoodsHolder;

import java.util.List;


/**
 * Created by zyq on 2017/12/7.
 */

public class SearchResultItemHolder extends BaseViewHolder {
    private TextView tv_hw;
    private TextView tv_hc;
    private TextView tv_hj;
    private TextView tv_materail;
    private TextView tv_goods;
    private TextView tv_identitymark;

    public SearchResultItemHolder(Context context, View itemView) {
        super(context, itemView);
        tv_hw = (TextView) itemView.findViewById(R.id.tv_hw);
        tv_hc = (TextView) itemView.findViewById(R.id.tv_hc);
        tv_hj = (TextView) itemView.findViewById(R.id.tv_hj);
        tv_materail = (TextView) itemView.findViewById(R.id.tv_material);
        tv_goods = (TextView) itemView.findViewById(R.id.tv_goods_right);
        tv_identitymark = (TextView) itemView.findViewById(R.id.tv_identitymark);
    }

    @Override
    public void onBindViewHolder(int position, RecycleItemBean recycleItemBean) {
        SearchResultBean searchResultBean = ((SearchResultBean) recycleItemBean.getData());
        if (!TextUtils.isEmpty(searchResultBean.getHW_NO())) {
            tv_hw.setText(searchResultBean.getHW_NO());
        }
        if (!TextUtils.isEmpty(searchResultBean.getHC_NO())) {
            tv_hc.setText(searchResultBean.getHC_NO());
        }
        if (!TextUtils.isEmpty(searchResultBean.getHJ_NO())) {
            tv_hj.setText(searchResultBean.getHJ_NO());
        }
        if (!TextUtils.isEmpty(searchResultBean.getMaterialName())) {
            tv_materail.setText(searchResultBean.getMaterialName());
        }
        if (!TextUtils.isEmpty(searchResultBean.getCompanyName())) {
            tv_goods.setText(searchResultBean.getCompanyName());
        }
        if (!TextUtils.isEmpty(searchResultBean.getIdentityMark())) {
            tv_identitymark.setText(searchResultBean.getIdentityMark());
        }
    }
}
