package com.github.yihai.zebra;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.github.yihai.R;
import com.github.yihai.common.util.AppUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/20.
 */

public class ZebarPopUtil {
    private PopupWindow mRfidPopup;
    private ArrayAdapter<String> mRfidAdapter;
    private List<String> mRfidList;

    public void initPopup(Context context, AdapterView.OnItemClickListener listener) {
        if (mRfidList == null) {
            mRfidList = new ArrayList<>();
        } else {
            mRfidList.clear();
        }
        if (mRfidPopup == null) {
            if (mRfidAdapter == null) {
                mRfidAdapter = new ArrayAdapter<>(context, R.layout.item_rfid_device, R.id.text, mRfidList);
            }
            View view = LayoutInflater.from(context).inflate(R.layout.pop_rfid_list, null);
            ListView listView = (ListView) view.findViewById(R.id.list_item);
            listView.setAdapter(mRfidAdapter);
            mRfidPopup = new PopupWindow(context);
            mRfidPopup.setWidth(AppUtils.getWindowWidth(context.getApplicationContext()));
            mRfidPopup.setHeight(AppUtils.getWindowHeight(context.getApplicationContext()) * 2 / 5);
            mRfidPopup.setContentView(view);
            mRfidPopup.setFocusable(true);
            mRfidPopup.setBackgroundDrawable(new BitmapDrawable());
            mRfidPopup.update();
            listView.setOnItemClickListener(listener);
            mRfidPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    ZebarUtil.endScan();
                }
            });
        }
    }

    public void showPopup(View view) {
        mRfidList.clear();
        if(mRfidPopup!=null && !mRfidPopup.isShowing())
        {
            mRfidPopup.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
    }
    public void dismissPopup()
    {
        if(mRfidPopup!=null && mRfidPopup.isShowing())
        {
            mRfidPopup.dismiss();
        }
    }
    public List<String> getList()
    {
        return mRfidList;
    }
    public void addData(String data)
    {
        if(mRfidList!=null)
        {
            mRfidAdapter.add(data);
            mRfidAdapter.notifyDataSetChanged();
        }
    }
}
