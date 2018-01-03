package com.github.yihai.ui.out;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.yihai.R;
import com.github.yihai.base.BaseSpinnerAdapter;
import com.github.yihai.bean.OutPlanBean;

/**
 * Created by dzl on 2017/12/13.
 */

public class OutPlanListAdapter extends BaseSpinnerAdapter<OutPlanBean> {

    public OutPlanListAdapter(Context context) {
        super(context, context.getString(R.string.action_select_plan));
        this.context = context;
    }

    @Override
    protected void initFirst(String firstTitle) {
        OutPlanBean outPlanBean = new OutPlanBean();
        outPlanBean.setID("-1");
        outPlanBean.setChuKuPlanName(firstTitle);
        mDatas.add(outPlanBean);
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public OutPlanBean getItem(int position) {
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
        viewHolder.textView.setText(mDatas.get(position).getChuKuPlanName());
        if (mDatas.get(position).getMark1()==null || mDatas.get(position).getMark1().equals("")) {
            if (mDatas.get(position).getMaterialName()==null || mDatas.get(position).getMaterialName().equals("")) {
                viewHolder.textView.setText(mDatas.get(position).getChuKuPlanName());
            } else {
                viewHolder.textView.setText(mDatas.get(position).getChuKuPlanName()
                        + "-" + mDatas.get(position).getMaterialName());
            }

        } else {
            viewHolder.textView.setText(mDatas.get(position).getChuKuPlanName()
                    + "-" + mDatas.get(position).getMaterialName()
                    + "-" + mDatas.get(position).getMark1());
        }
        return convertView;
    }

    static class ViewHolder {
        TextView textView;
    }
}
