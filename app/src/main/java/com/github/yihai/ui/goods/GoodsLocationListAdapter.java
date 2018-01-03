package com.github.yihai.ui.goods;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.yihai.R;
import com.github.yihai.base.BaseSpinnerAdapter;
import com.github.yihai.bean.GoodsLocationBean;

/**
 * Created by dzl on 2017/12/13.
 */

public class GoodsLocationListAdapter extends BaseSpinnerAdapter<GoodsLocationBean> {

    public GoodsLocationListAdapter(Context context) {
        super(context, context.getString(R.string.hint_select));
    }

    @Override
    protected void initFirst(String firstTitle) {
        GoodsLocationBean goodsLocationBean = new GoodsLocationBean();
        goodsLocationBean.setID("-1");
        goodsLocationBean.setHW_NO(firstTitle);
        mDatas.add(goodsLocationBean);
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public GoodsLocationBean getItem(int position) {
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
        viewHolder.textView.setText(mDatas.get(position).getHW_NO());
        return convertView;
    }

    static class ViewHolder {
        TextView textView;
    }
}
