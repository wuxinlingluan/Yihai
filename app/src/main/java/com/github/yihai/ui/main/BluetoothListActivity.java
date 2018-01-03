package com.github.yihai.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.yihai.R;
import com.github.yihai.base.BaseActivity;
import com.github.yihai.zebra.ZebarHolder;
import com.github.yihai.zebra.ZebarUtil;
import com.zebra.rfid.api3.InvalidUsageException;
import com.zebra.rfid.api3.OperationFailureException;
import com.zebra.rfid.api3.RFIDResults;
import com.zebra.rfid.api3.ReaderDevice;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.github.yihai.zebra.ZebarHolder.mConnectedReader;

/*
* 蓝牙列表
* */
public class BluetoothListActivity extends BaseActivity {
    private static final int REQUEST_OPEN_BLUETOOTH = 100;

    @BindView(R.id.custom_simple_bar)
    Toolbar toolbar;
    @BindView(R.id.custom_title)
    TextView tvTitle;
    @BindView(R.id.custom_back)
    TextView customBack;

    private CustomProgressDialog progressDialog;
    private ListView pairedListView;
    private TextView tv_emptyView;
    private ReaderListAdapter readerListAdapter;

    private DeviceConnectTask deviceConnectTask;
    private static Timer t;

    public List<ReaderDevice> readersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_list);
        ButterKnife.bind(this);
    }

    @Override
    public void initView() {
        setSupportActionBar(toolbar);
        tvTitle.setText(R.string.title_list);
        pairedListView = (ListView) findViewById(R.id.bondedReadersList);
        tv_emptyView = (TextView) findViewById(R.id.empty);

        readerListAdapter = new ReaderListAdapter(this, R.layout.readers_list_item, readersList);
        pairedListView.setAdapter(readerListAdapter);
    }

    @Override
    public void initData() {
        if (ZebarUtil.openBluetooth(this, REQUEST_OPEN_BLUETOOTH)) {
            startScan();
        }
    }

    @Override
    public void bindEvent() {
        pairedListView.setOnItemClickListener(mDeviceClickListener);
        pairedListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_OPEN_BLUETOOTH) {
            startScan();
        }
    }

    private void startScan() {
        readersList.clear();
        readersList = ZebarUtil.loadPairedDevices();
        if (ZebarHolder.mConnectedDevice != null) {
            //再次扫描时蓝牙配对设备中没有当前的记录的设备时，重置
            //TODO 未经测试（多个设备连接状况）
            int index = readersList.indexOf(ZebarHolder.mConnectedDevice);
            if (index != -1) {
                readersList.remove(index);
                readersList.add(index, ZebarHolder.mConnectedDevice);
            } else {
                ZebarHolder.mConnectedDevice = null;
                mConnectedReader = null;
            }
        }
//        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
//        Set<BluetoothDevice> devices = adapter.getBondedDevices();
//        for (int i = 0; i < devices.size(); i++) {
//            BluetoothDevice device = (BluetoothDevice) devices.iterator().next();
//            Log.i("1111111111111", device.getName());
//        }

//        {
//            BluetoothDevice device = (BluetoothDevice) devices.iterator().next();
//            System.out.println(device.getName());
//        }

        if (readersList.size() == 0) {
            pairedListView.setEmptyView(tv_emptyView);
        } else
            readerListAdapter.setData(readersList);
    }

    //------------华丽的分割线  以上代码已经抽取整理 ---------------------

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> av, View v, int pos, long arg3) {
            if (ZebarUtil.isBluetoothEnabled()) {
                // Get the device MAC address, which is the last 17 chars in the View
                ReaderDevice readerDevice = readerListAdapter.getItem(pos);
                if (mConnectedReader == null) {
                    if (deviceConnectTask == null || deviceConnectTask.isCancelled()) {
                        ZebarHolder.is_connection_requested = true;
                        if (readerDevice != null) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                deviceConnectTask = new DeviceConnectTask(readerDevice, "连接" + readerDevice.getName(), getReaderPassword(readerDevice.getName()));
                                deviceConnectTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            } else {
                                deviceConnectTask = new DeviceConnectTask(readerDevice, "连接" + readerDevice.getName(), getReaderPassword(readerDevice.getName()));
                                deviceConnectTask.execute();
                            }
                        }
                    }
                } else {
                    {
                        if (mConnectedReader.isConnected()) {
                            ZebarHolder.is_disconnection_requested = true;
                            readerListAdapter.checkedTextView.setClickable(true);
                            readerListAdapter.notifyDataSetChanged();
                            try {
                                mConnectedReader.disconnect();
                            } catch (InvalidUsageException e) {
                                e.printStackTrace();
                            } catch (OperationFailureException e) {
                                e.printStackTrace();
                            }
                            //
                            ReaderDevice readerDevice1 = new ReaderDevice();
                            bluetoothDeviceDisConnected(readerDevice1);
                            if (ZebarHolder.NOTIFY_READER_CONNECTION)
                                //    sendNotification(Constants.ACTION_READER_DISCONNECTED, "Disconnected from " + Application.mConnectedReader.getHostName());
                                //
                                ZebarUtil.clearSettings();
                        }
                        if (!mConnectedReader.getHostName().equalsIgnoreCase(readerDevice.getName())) {
                            mConnectedReader = null;
                            if (deviceConnectTask == null || deviceConnectTask.isCancelled()) {
                                ZebarHolder.is_connection_requested = true;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                    deviceConnectTask = new DeviceConnectTask(readerDevice, "正在连接" + readerDevice.getName(), getReaderPassword(readerDevice.getName()));
                                    deviceConnectTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                } else {
                                    deviceConnectTask = new DeviceConnectTask(readerDevice, "正在连接" + readerDevice.getName(), getReaderPassword(readerDevice.getName()));
                                    deviceConnectTask.execute();
                                }
                            }
                        } else {
                            mConnectedReader = null;
                        }
                    }
                }

                // Create the result Intent and include the MAC address
            } else
                Toast.makeText(BluetoothListActivity.this, "请打开蓝牙", Toast.LENGTH_SHORT).show();
        }
    };

    @OnClick(R.id.custom_back)
    public void onViewClicked() {
        finish();
    }

    /**
     * async task to go for BT connection with reader
     */
    private class DeviceConnectTask extends AsyncTask<Void, String, Boolean> {
        private final ReaderDevice connectingDevice;
        private String prgressMsg;
        private OperationFailureException ex;
        private String password;

        DeviceConnectTask(ReaderDevice connectingDevice, String prgressMsg, String Password) {
            this.connectingDevice = connectingDevice;
            this.prgressMsg = prgressMsg;
            password = Password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new CustomProgressDialog(BluetoothListActivity.this, prgressMsg);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... a) {
            try {
                if (password != null)
                    connectingDevice.getRFIDReader().setPassword(password);
                connectingDevice.getRFIDReader().connect();
                if (password != null) {
                    SharedPreferences.Editor editor = getSharedPreferences(Constants.READER_PASSWORDS, 0).edit();
                    editor.putString(connectingDevice.getName(), password);
                    editor.commit();
                }
            } catch (InvalidUsageException e) {
                e.printStackTrace();
            } catch (OperationFailureException e) {
                e.printStackTrace();
                ex = e;
            }
            if (connectingDevice.getRFIDReader().isConnected()) {
                mConnectedReader = connectingDevice.getRFIDReader();
                try {
                    ZebarHolder.mConnectedReader.Events.addEventsListener(ZebarHolder.eventHandler);
                } catch (InvalidUsageException e) {
                    e.printStackTrace();
                } catch (OperationFailureException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
                try {
                    readerListAdapter.checkedTextView.setChecked(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                connectingDevice.getRFIDReader().Events.setBatchModeEvent(true);
                connectingDevice.getRFIDReader().Events.setReaderDisconnectEvent(true);
                connectingDevice.getRFIDReader().Events.setBatteryEvent(true);
                connectingDevice.getRFIDReader().Events.setInventoryStopEvent(true);
                connectingDevice.getRFIDReader().Events.setInventoryStartEvent(true);
                // if no exception in connect
                if (ex == null) {
                    try {
                        ZebarUtil.UpdateReaderConnection(false);
                        startTimer();
                    } catch (InvalidUsageException e) {
                        e.printStackTrace();
                    } catch (OperationFailureException e) {
                        e.printStackTrace();
                    }
                } else {
                    ZebarUtil.clearSettings();
                }
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            ZebarHolder.NOTIFY_READER_CONNECTION = result;
            progressDialog.cancel();
            if (ex != null) {
                if (ex.getResults() == RFIDResults.RFID_CONNECTION_PASSWORD_ERROR) {
                    //showPasswordDialog(connectingDevice);
                    bluetoothDeviceConnected(connectingDevice);
                } else if (ex.getResults() == RFIDResults.RFID_BATCHMODE_IN_PROGRESS) {
                    ZebarHolder.isBatchModeInventoryRunning = true;
                    ZebarHolder.mIsInventoryRunning = true;
                    bluetoothDeviceConnected(connectingDevice);
                    if (ZebarHolder.NOTIFY_READER_CONNECTION) {
                    }
                } else if (ex.getResults() == RFIDResults.RFID_READER_REGION_NOT_CONFIGURED) {
                    bluetoothDeviceConnected(connectingDevice);
                    ZebarHolder.regionNotSet = true;
                } else
                    bluetoothDeviceConnFailed(connectingDevice);
            } else {
                if (result) {
                    if (ZebarHolder.NOTIFY_READER_CONNECTION)
                        bluetoothDeviceConnected(connectingDevice);
                } else {
                    bluetoothDeviceConnFailed(connectingDevice);
                }
            }
            deviceConnectTask = null;
//            try {
//                ZebarHolder.mConnectedReader.Actions.Inventory.perform();
//            } catch (InvalidUsageException e) {
//                e.printStackTrace();
//            } catch (OperationFailureException e) {
//                e.printStackTrace();
//            }
        }

        @Override
        protected void onCancelled() {
            deviceConnectTask = null;
            super.onCancelled();
        }

        public ReaderDevice getConnectingDevice() {
            return connectingDevice;
        }
    }

    private String getReaderPassword(String address) {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.READER_PASSWORDS, 0);
        return sharedPreferences.getString(address, null);
    }


    public static void startTimer() {
        if (t == null) {
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    try {
                        if (mConnectedReader != null)
                            mConnectedReader.Config.getDeviceStatus(true, false, false);
                        else
                            stopTimer();
                    } catch (InvalidUsageException e) {
                        e.printStackTrace();
                    } catch (OperationFailureException e) {
                        e.printStackTrace();
                    }
                }
            };
            t = new Timer();
            t.scheduleAtFixedRate(task, 0, 60000);
        }
    }

    public static void stopTimer() {
        if (t != null) {
            t.cancel();
            t.purge();
        }
        t = null;
    }

    public void bluetoothDeviceDisConnected(ReaderDevice device) {
        if (deviceConnectTask != null && !deviceConnectTask.isCancelled() && deviceConnectTask.getConnectingDevice().getName().equalsIgnoreCase(device.getName())) {
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
            if (deviceConnectTask != null)
                deviceConnectTask.cancel(true);
        }
        if (device != null) {
            changeTextStyle(device);
        } else
            Constants.logAsMessage(Constants.TYPE_ERROR, "ReadersListFragment", "deviceName is null or empty");
    }

    private void changeTextStyle(ReaderDevice device) {
        int i = readerListAdapter.getPosition(device);
        if (i >= 0) {
            readerListAdapter.remove(device);
            readerListAdapter.insert(device, i);
            readerListAdapter.notifyDataSetChanged();
        }
    }


    public void bluetoothDeviceConnected(ReaderDevice device) {
        if (device != null) {
            ZebarHolder.mConnectedDevice = device;
            ZebarHolder.is_connection_requested = false;
            changeTextStyle(device);

        } else
            Constants.logAsMessage(Constants.TYPE_ERROR, "ReadersListFragment", "deviceName is null or empty");
    }

    public void bluetoothDeviceConnFailed(ReaderDevice device) {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
        if (deviceConnectTask != null)
            deviceConnectTask.cancel(true);
        if (device != null)
            changeTextStyle(device);
        else
            Constants.logAsMessage(Constants.TYPE_ERROR, "ReadersListFragment", "deviceName is null or empty");
        mConnectedReader = null;
        ZebarHolder.mConnectedDevice = null;
    }


    @Override
    protected void onResume() {
        super.onResume();
        capabilitiesRecievedforDevice();
    }

    /**
     * method to update serial and model of connected reader device
     */
    public void capabilitiesRecievedforDevice() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (readersList.size() > 0 && readerListAdapter != null) {
                    readerListAdapter.notifyDataSetChanged();
                } else {
                    readersList = ZebarUtil.loadPairedDevices();
                    readerListAdapter.setData(readersList);
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
