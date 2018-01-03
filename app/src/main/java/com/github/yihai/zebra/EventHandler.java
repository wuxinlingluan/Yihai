package com.github.yihai.zebra;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.github.yihai.ui.MyApplication;
import com.zebra.rfid.api3.ACCESS_OPERATION_CODE;
import com.zebra.rfid.api3.ACCESS_OPERATION_STATUS;
import com.zebra.rfid.api3.RfidEventsListener;
import com.zebra.rfid.api3.RfidReadEvents;
import com.zebra.rfid.api3.RfidStatusEvents;
import com.zebra.rfid.api3.START_TRIGGER_TYPE;
import com.zebra.rfid.api3.STATUS_EVENT_TYPE;
import com.zebra.rfid.api3.STOP_TRIGGER_TYPE;
import com.zebra.rfid.api3.TagData;

/**
 * Created by ${sheldon} on 2017/12/19.
 */

public class EventHandler implements RfidEventsListener {

    private Handler handler;

    @Override
    public void eventReadNotify(RfidReadEvents e) {
        final TagData[] myTags = ZebarHolder.mConnectedReader.Actions.getReadTags(100);
        if (myTags != null) {
            for (int index = 0; index < myTags.length; index++) {
                if (myTags[index].getOpCode() == ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ &&
                        myTags[index].getOpStatus() == ACCESS_OPERATION_STATUS.ACCESS_SUCCESS) {
                }
                if (myTags[index].isContainsLocationInfo()) {
                    final int tag = index;
                    ZebarHolder.TagProximityPercent = myTags[tag].LocationInfo.getRelativeDistance();
                }
                if (myTags[index] != null && (myTags[index].getOpStatus() == null || myTags[index].getOpStatus() == ACCESS_OPERATION_STATUS.ACCESS_SUCCESS)) {
                    final int tag = index;
                    Log.i("888888888888888", myTags[tag].getTagID());
                    if (handler != null) {
                        Message message = Message.obtain();
                        Bundle bundle = new Bundle();
                        bundle.putString("data", myTags[tag].getTagID());
                        message.setData(bundle);
                        handler.sendMessage(message);
                    }
                }
            }
        }
    }

    @Override
    public void eventStatusNotify(RfidStatusEvents rfidStatusEvents) {
        if (rfidStatusEvents.StatusEventData.getStatusEventType() == STATUS_EVENT_TYPE.HANDHELD_TRIGGER_EVENT) {
            if ((ZebarHolder.settings_startTrigger.getTriggerType().toString().equalsIgnoreCase(START_TRIGGER_TYPE.START_TRIGGER_TYPE_IMMEDIATE.toString()))) {
                Log.i("11111111111111", "开始");
                ZebarHolder.settings_stopTrigger.setTriggerType(STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_IMMEDIATE);
                ZebarHolder.settings_startTrigger.setTriggerType(START_TRIGGER_TYPE.START_TRIGGER_TYPE_PERIODIC);
                ZebarUtil.startScan(MyApplication.getContext(), ZebarHolder.eventHandler);
            } else if ((ZebarHolder.settings_stopTrigger.getTriggerType().toString().equalsIgnoreCase(STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_IMMEDIATE.toString()))) {
                ZebarHolder.settings_startTrigger.setTriggerType(START_TRIGGER_TYPE.START_TRIGGER_TYPE_IMMEDIATE);
                ZebarHolder.settings_stopTrigger.setTriggerType(STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_DURATION);
                Log.i("11111111111111", "结束");
                ZebarUtil.endScan();
            }

        }
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void removeHandler() {
        this.handler = null;
    }

    public interface OnEventListener {
        void eventReadNotify(TagData myTag);
    }
}
