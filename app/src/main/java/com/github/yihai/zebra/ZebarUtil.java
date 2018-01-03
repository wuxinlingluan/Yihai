package com.github.yihai.zebra;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.zebra.rfid.api3.Antennas;
import com.zebra.rfid.api3.InvalidUsageException;
import com.zebra.rfid.api3.OperationFailureException;
import com.zebra.rfid.api3.RFIDReader;
import com.zebra.rfid.api3.ReaderDevice;
import com.zebra.rfid.api3.Readers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${sheldon} on 2017/12/19.
 */

public class ZebarUtil {

    /*
    * 开始扫描
    * */
    public static void startScan(Context context, EventHandler eventHandler) {
        RFIDReader mConnectedReader = ZebarHolder.mConnectedReader;
        if (ZebarHolder.mConnectedReader != null && ZebarHolder.mConnectedReader.isConnected()) {
            try {
                bindEvent(eventHandler, mConnectedReader);
            } catch (InvalidUsageException e) {
                e.printStackTrace();
            } catch (final OperationFailureException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "连接失败", Toast.LENGTH_SHORT).show();
        }
    }

    public static void bindEvent(EventHandler eventHandler, RFIDReader mConnectedReader) throws InvalidUsageException, OperationFailureException {
        mConnectedReader.Actions.Inventory.perform();
        if (!ZebarHolder.isEventAdded) {
            ZebarHolder.mConnectedReader.Events.addEventsListener(eventHandler);
            ZebarHolder.isEventAdded = true;
        }
    }

    public static void unbindEvent(EventHandler eventHandler) throws InvalidUsageException, OperationFailureException {
        if (ZebarHolder.isEventAdded) {
            ZebarHolder.mConnectedReader.Events.removeEventsListener(eventHandler);
            ZebarHolder.isEventAdded = false;
        }
    }


    /*
    * 结束扫描
    * */
    public static void endScan() {
        if (ZebarHolder.mConnectedReader != null && ZebarHolder.mConnectedReader.isConnected()) {
            try {
                ZebarHolder.mConnectedReader.Actions.Inventory.stop();
            } catch (InvalidUsageException e) {
                e.printStackTrace();
            } catch (OperationFailureException e) {
                e.printStackTrace();
            }
        }

    }

    public static boolean openBluetooth(Activity activity, int requestCode) {
        if (!isBluetoothEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableIntent, requestCode);
            return false;
        }
        return true;
    }

    public static boolean isBluetoothEnabled() {
        return BluetoothAdapter.getDefaultAdapter().isEnabled();
    }

    /**
     * 获取所有可用RFID设备
     */
    public static List<ReaderDevice> loadPairedDevices() {
        Readers readers = new Readers();
        List<ReaderDevice> readerDevices = new ArrayList<>();
        readerDevices.addAll(readers.GetAvailableRFIDReaderList());
        return readerDevices;
    }

    /**
     * 更新读写器连接状态
     *
     * @param fullUpdate
     * @throws InvalidUsageException
     * @throws OperationFailureException
     */
    public static void UpdateReaderConnection(Boolean fullUpdate) throws InvalidUsageException, OperationFailureException {
        ZebarHolder.mConnectedReader.Events.setBatchModeEvent(true);
        ZebarHolder.mConnectedReader.Events.setReaderDisconnectEvent(true);
        ZebarHolder.mConnectedReader.Events.setInventoryStartEvent(true);
        ZebarHolder.mConnectedReader.Events.setInventoryStopEvent(true);
        ZebarHolder.mConnectedReader.Events.setTagReadEvent(true);
        ZebarHolder.mConnectedReader.Events.setHandheldEvent(true);
        ZebarHolder.mConnectedReader.Events.setBatteryEvent(true);
        ZebarHolder.mConnectedReader.Events.setPowerEvent(true);
        ZebarHolder.mConnectedReader.Events.setOperationEndSummaryEvent(true);

        if (fullUpdate)
            ZebarHolder.mConnectedReader.PostConnectReaderUpdate();

        ZebarHolder.regulatory = ZebarHolder.mConnectedReader.Config.getRegulatoryConfig();
        ZebarHolder.regionNotSet = false;
        ZebarHolder.rfModeTable = ZebarHolder.mConnectedReader.ReaderCapabilities.RFModes.getRFModeTableInfo(0);
        ZebarHolder.antennaRfConfig = ZebarHolder.mConnectedReader.Config.Antennas.getAntennaRfConfig(1);
        ZebarHolder.singulationControl = ZebarHolder.mConnectedReader.Config.Antennas.getSingulationControl(1);
        ZebarHolder.settings_startTrigger = ZebarHolder.mConnectedReader.Config.getStartTrigger();
        ZebarHolder.settings_stopTrigger = ZebarHolder.mConnectedReader.Config.getStopTrigger();
        ZebarHolder.tagStorageSettings = ZebarHolder.mConnectedReader.Config.getTagStorageSettings();
        ZebarHolder.dynamicPowerSettings = ZebarHolder.mConnectedReader.Config.getDPOState();
        ZebarHolder.beeperVolume = ZebarHolder.mConnectedReader.Config.getBeeperVolume();
        ZebarHolder.batchMode = ZebarHolder.mConnectedReader.Config.getBatchModeConfig().getValue();
        ZebarHolder.reportUniquetags = ZebarHolder.mConnectedReader.Config.getUniqueTagReport();
        ZebarHolder.mConnectedReader.Config.getDeviceVersionInfo(ZebarHolder.versionInfo);
    }

    /**
     * method to clear reader's settings on disconnection
     */
    public static void clearSettings() {
        ZebarHolder.antennaPowerLevel = null;
        ZebarHolder.antennaRfConfig = null;
        ZebarHolder.singulationControl = null;
        ZebarHolder.rfModeTable = null;
        ZebarHolder.regulatory = null;
        ZebarHolder.batchMode = -1;
        ZebarHolder.tagStorageSettings = null;
        ZebarHolder.reportUniquetags = null;
        ZebarHolder.dynamicPowerSettings = null;
        ZebarHolder.settings_startTrigger = null;
        ZebarHolder.settings_stopTrigger = null;
        ZebarHolder.beeperVolume = null;
        ZebarHolder.preFilters = null;
        if (ZebarHolder.versionInfo != null)
            ZebarHolder.versionInfo.clear();
        ZebarHolder.regionNotSet = false;
        ZebarHolder.isBatchModeInventoryRunning = null;
        //  Application.BatteryData = null;
        ZebarHolder.is_disconnection_requested = false;
        ZebarHolder.mConnectedDevice = null;
//        Application.mConnectedReader = null;
    }

    /*
    * 设置扫描强度
    * */
    public static void setAntenna(int number) {
        try {
            if (ZebarHolder.mConnectedReader != null && ZebarHolder.antennaRfConfig != null) {
                Antennas.AntennaRfConfig antennaRfConfig = ZebarHolder.mConnectedReader.Config.Antennas.getAntennaRfConfig(1);
                antennaRfConfig.setTransmitPowerIndex(number);
                ZebarHolder.mConnectedReader.Config.Antennas.setAntennaRfConfig(1, antennaRfConfig);
                ZebarHolder.antennaRfConfig = antennaRfConfig;
            }
        } catch (InvalidUsageException e) {
            e.printStackTrace();
        } catch (OperationFailureException e) {
            e.printStackTrace();
        }
    }


}
