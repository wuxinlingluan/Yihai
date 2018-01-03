package com.github.yihai.ui.main;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.yihai.R;
import com.github.yihai.ui.MyApplication;
import com.github.yihai.zebra.ZebarHolder;
import com.zebra.rfid.api3.RFIDReader;
import com.zebra.rfid.api3.ReaderDevice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${sheldon} on 2017/12/15.
 */

public class ReaderListAdapter extends ArrayAdapter<ReaderDevice> {
    private List<ReaderDevice> readersList;
    private final Context context;
    private final int resourceId;
    public CheckedTextView checkedTextView;

    public ReaderListAdapter(Context context, int resourceId, List<ReaderDevice> readersList) {
        super(context, resourceId, readersList);
        this.context = context;
        this.resourceId = resourceId;
        this.readersList = readersList;
    }

    public void setData(List<ReaderDevice> readersList) {
        this.readersList = readersList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return readersList == null ? 0 : readersList.size();
    }

    @Override
    public ReaderDevice getItem(int position) {
        return readersList == null ? null : readersList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ReaderDevice reader = readersList.get(position);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(resourceId, null);
        }
        checkedTextView = (CheckedTextView) convertView.findViewById(R.id.reader_checkedtextview);
        checkedTextView.setText(reader.getName() + "\n" + reader.getAddress());

        LinearLayout readerDetail = (LinearLayout) convertView.findViewById(R.id.reader_detail);
        RFIDReader rfidReader = reader.getRFIDReader();
        if (rfidReader != null && rfidReader.isConnected()) {
            checkedTextView.setChecked(true);
            readerDetail.setVisibility(View.VISIBLE);
//            System.out.println("rfidReader " + rfidReader.ReaderCapabilities.getSerialNumber() +" "+  Application.isBatchModeInventoryRunning);
            if (ZebarHolder.isBatchModeInventoryRunning != null && ZebarHolder.isBatchModeInventoryRunning) {
                ((TextView) convertView.findViewById(R.id.tv_model)).setText(context.getResources().getString(R.string.batch_mode_running_title));
                ((TextView) convertView.findViewById(R.id.tv_serial)).setText(context.getResources().getString(R.string.batch_mode_running_title));
            } else if (rfidReader.ReaderCapabilities.getModelName() != null && rfidReader.ReaderCapabilities.getSerialNumber() != null) {
                ((TextView) convertView.findViewById(R.id.tv_model)).setText(rfidReader.ReaderCapabilities.getModelName());
                ((TextView) convertView.findViewById(R.id.tv_serial)).setText(rfidReader.ReaderCapabilities.getSerialNumber());
            }

        } else {
            readerDetail.setVisibility(View.GONE);
            checkedTextView.setChecked(false);
        }
        return convertView;
    }
}
