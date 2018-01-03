package com.github.yihai.ui.check;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.yihai.R;
import com.github.yihai.base.BaseSpinnerAdapter;
import com.github.yihai.bean.CheckPlanBean;

/**
 * Created by dzl on 2017/12/13.
 */

public class CheckPlanListAdapter extends BaseSpinnerAdapter<CheckPlanBean> {

    public CheckPlanListAdapter(Context context) {
        super(context, context.getString(R.string.action_select_plan));
        this.context = context;
    }

    @Override
    protected void initFirst(String firstTitle) {
        CheckPlanBean checkPlanBean = new CheckPlanBean();
        checkPlanBean.setID("-1");
        checkPlanBean.setPanKuPlanName(firstTitle);
        mDatas.add(checkPlanBean);
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public CheckPlanBean getItem(int position) {
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
        viewHolder.textView.setText(mDatas.get(position).getPanKuPlanName());
        return convertView;
    }

    static class ViewHolder {
        TextView textView;
    }
}
