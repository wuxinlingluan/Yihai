package com.github.yihai.ui.in;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.yihai.R;
import com.github.yihai.base.BaseSpinnerAdapter;
import com.github.yihai.bean.GoodsRightBean;

/**
 * Created by dzl on 2017/12/13.
 */

public class GoodsRightListAdapter extends BaseSpinnerAdapter<GoodsRightBean> {

    public GoodsRightListAdapter(Context context) {
        super(context, context.getString(R.string.hint_select));
    }

    public GoodsRightListAdapter(Context context, String firstTitle) {
        super(context, firstTitle);
    }

    @Override
    protected void initFirst(String firstTitle) {
        GoodsRightBean goodsRightBean = new GoodsRightBean();
        goodsRightBean.setCompanyCode("-1");
        goodsRightBean.setCompanyName(firstTitle);
        mDatas.add(goodsRightBean);
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public GoodsRightBean getItem(int position) {
        return mDatas == null ? null : mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_spinner, null);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(mDatas.get(position).getCompanyName());
        return convertView;
    }

    static class ViewHolder {
        TextView textView;
    }
}
