package com.github.yihai.base.recycle;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by dzl on 2017/6/8.
 */

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    protected Context context;
    public View itemView;
    protected OnItemClickListener itemClickListener;

    public BaseViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        this.itemView = itemView;
        //添加Item的点击事件
//        if (itemClickListener != null) {
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    itemClickListener.onItemClick(getAdapterPosition());
//                }
//            });
//        }
    }

    public abstract void onBindViewHolder(int position, RecycleItemBean recycleItemBean);
}
