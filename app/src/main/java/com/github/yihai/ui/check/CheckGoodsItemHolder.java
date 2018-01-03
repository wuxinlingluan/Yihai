package com.github.yihai.ui.check;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.yihai.R;
import com.github.yihai.base.recycle.BaseViewHolder;
import com.github.yihai.base.recycle.RecycleItemBean;
import com.github.yihai.bean.CheckPlanDetailBean;


/**
 * Created by dzl on 2017/12/7.
 */

public class CheckGoodsItemHolder extends BaseViewHolder {
    public CheckGoodsItemHolder(Context context, View itemView) {
        super(context, itemView);
    }

    @Override
    public void onBindViewHolder(int position, RecycleItemBean recycleItemBean) {
        CheckPlanDetailBean checkPlanDetailBean = (CheckPlanDetailBean) recycleItemBean.getData();
        if (checkPlanDetailBean != null) {
            TextView tvRfid = (TextView) itemView.findViewById(R.id.tv_rfid);
//            TextView tvShelf = (TextView) itemView.findViewById(R.id.tv_goods_shelf);
//            TextView tvFloor = (TextView) itemView.findViewById(R.id.tv_goods_floor);
            TextView tvLocation = (TextView) itemView.findViewById(R.id.tv_goods_location);
            TextView tvMaterial = (TextView) itemView.findViewById(R.id.tv_material);
//            TextView tvRight = (TextView) itemView.findViewById(R.id.tv_goods_right);
//            TextView tvAlias = (TextView) itemView.findViewById(R.id.tv_goods_alias);
            ImageView ivLight = (ImageView) itemView.findViewById(R.id.iv_light);

            if (checkPlanDetailBean.isSuccess){
                ivLight.setImageResource(R.drawable.ic_scan_success);
            } else {
                ivLight.setImageResource(R.drawable.ic_scan_error);
            }
//            if (!TextUtils.isEmpty(checkPlanDetailBean.getHuoJia())) {
//                tvShelf.setText(checkPlanDetailBean.getHuoJia());
//            }
            if (!TextUtils.isEmpty(checkPlanDetailBean.getMark1())) {
                tvRfid.setText(checkPlanDetailBean.getMark1());
            }
//            if (!TextUtils.isEmpty(checkPlanDetailBean.getHuoCeng())) {
//                tvFloor.setText(checkPlanDetailBean.getHuoCeng());
//            }
            if (!TextUtils.isEmpty(checkPlanDetailBean.getHuoWei())) {
                tvLocation.setText(checkPlanDetailBean.getHuoWei());
            }
            if (!TextUtils.isEmpty(checkPlanDetailBean.getHuoWei())) {
                tvMaterial.setText(checkPlanDetailBean.getMaterialName());
            }
//            if (!TextUtils.isEmpty(checkPlanDetailBean.getCompanyName())) {
//                tvRight.setText(checkPlanDetailBean.getCompanyName());
//            }
//            if (!TextUtils.isEmpty(checkPlanDetailBean.getIdentityMark())) {
//                tvAlias.setText(checkPlanDetailBean.getIdentityMark());
//            }
        }
    }
}
