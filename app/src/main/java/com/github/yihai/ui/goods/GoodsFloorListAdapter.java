package com.github.yihai.ui.goods;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.yihai.R;
import com.github.yihai.base.BaseSpinnerAdapter;
import com.github.yihai.bean.GoodsFloorBean;

/**
 * Created by dzl on 2017/12/13.
 */

public class GoodsFloorListAdapter extends BaseSpinnerAdapter<GoodsFloorBean> {

    public GoodsFloorListAdapter(Context context) {
        super(context, context.getString(R.string.hint_select));
    }

    @Override
    protected void initFirst(String firstTitle) {
        GoodsFloorBean goodsFloorBean = new GoodsFloorBean();
        goodsFloorBean.setID("-1");
        goodsFloorBean.setHC_NO(firstTitle);
        mDatas.add(goodsFloorBean);
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public GoodsFloorBean getItem(int position) {
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
        viewHolder.textView.setText(mDatas.get(position).getHC_NO());
        return convertView;
    }

    static class ViewHolder {
        TextView textView;
    }

}
