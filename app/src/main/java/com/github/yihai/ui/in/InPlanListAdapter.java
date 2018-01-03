package com.github.yihai.ui.in;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.yihai.R;
import com.github.yihai.base.BaseSpinnerAdapter;
import com.github.yihai.bean.InPlanBean;

/**
 * Created by dzl on 2017/12/13.
 */

public class InPlanListAdapter extends BaseSpinnerAdapter<InPlanBean> {

    public InPlanListAdapter(Context context) {
        super(context, context.getString(R.string.action_select_plan));
        this.context = context;
    }

    @Override
    protected void initFirst(String firstTitle) {
        InPlanBean inPlanBean = new InPlanBean();
        inPlanBean.setID("-1");
        inPlanBean.setRuKuPlanName(firstTitle);
        mDatas.add(inPlanBean);
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public InPlanBean getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_rfid_device, null);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (mDatas.get(position).getMark1()==null || mDatas.get(position).getMark1().equals("")) {
            if (mDatas.get(position).getMaterialName()==null || mDatas.get(position).getMaterialName().equals("")) {
                viewHolder.textView.setText(mDatas.get(position).getRuKuPlanName());
            } else {
                viewHolder.textView.setText(mDatas.get(position).getRuKuPlanName()
                        + "-" + mDatas.get(position).getMaterialName());
            }

        } else {
            viewHolder.textView.setText(mDatas.get(position).getRuKuPlanName()
                    + "-" + mDatas.get(position).getMaterialName()
                    + "-" + mDatas.get(position).getMark1());
        }

        return convertView;
    }

    static class ViewHolder {
        TextView textView;
    }
}
