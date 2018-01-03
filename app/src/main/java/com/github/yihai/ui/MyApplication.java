package com.github.yihai.ui;

import android.app.Application;
import android.content.Context;

import com.github.yihai.zebra.ZebarHolder;

/**
 * Created by ${sheldon} on 2017/12/15.
 */

public class MyApplication extends Application {
//    public static RFIDReader mConnectedReader;
        public static Context context;
    public static int inPlanNo=1;
    public static int outPlanNo=1;
    public static int checkPlanNo=1;
//    //Variable to keep track of the unique tags seen
//    public static volatile int UNIQUE_TAGS = 0;

//    //variable to keep track of the total tags seen
//    public static volatile int TOTAL_TAGS = 0;
//    //Arraylist to keeptrack of the tagIDs to act as adapter for autocomplete text views
//    public static ArrayList<String> tagIDs;
//    //variable to store the tag read rate
//    public static volatile int TAG_READ_RATE = 0;
//    //Boolean to keep track of whether the inventory is running or not
//    public static volatile boolean mIsInventoryRunning;
//    public static int inventoryMode = 0;
//    public static int memoryBankId = -1;
//    public static Boolean isBatchModeInventoryRunning;
//    public static String accessControlTag;
//    public static String locateTag;
//    //Variable to maintain the RR started time to maintain the read rate
//    public static volatile long mRRStartedTime;
//    public static PreFilters[] preFilters = null;
//    public static boolean isAccessCriteriaRead = false;
//    public static int preFilterIndex = -1;
//    //For Notification
//    public static volatile int INTENT_ID = 100;
    //  public static MainActivity.EventHandler eventHandler;
//    public static TreeMap<String, Integer> inventoryList = new TreeMap<String, Integer>();
//    public static HashMap<String, String> versionInfo = new HashMap<>(5);

    //Arraylist to keeptrack of the tags read for Inventory
    //  public static ArrayList<InventoryListItem> tagsReadInventory = new MaxLimitArrayList();
//    public static boolean isGettingTags;
//    public static boolean EXPORT_DATA;
//    public static ReaderDevice mConnectedDevice;
//    public static boolean isLocatingTag;
    //
//    public static StartTrigger settings_startTrigger;
//    public static StopTrigger settings_stopTrigger;
//    public static short TagProximityPercent = -1;
//    public static TagStorageSettings tagStorageSettings;
//    public static int batchMode;
// //    public static BatteryData BatteryData = null;
//    public static DYNAMIC_POWER_OPTIMIZATION dynamicPowerSettings;
//    public static boolean is_disconnection_requested;
//    public static boolean is_connection_requested;
    //Application Settings
//    public static volatile boolean AUTO_DETECT_READERS;
//    public static volatile boolean AUTO_RECONNECT_READERS;
//    public static volatile boolean NOTIFY_READER_AVAILABLE;
//    public static volatile boolean NOTIFY_BATTERY_STATUS;
//    public static volatile boolean NOTIFY_READER_CONNECTION;
//    //Beeper
//    public static BEEPER_VOLUME beeperVolume = null;
//    // Singulation control
//    public static Antennas.SingulationControl singulationControl;
//    // regulatory
//    public static RegulatoryConfig regulatory;
//    public static Boolean regionNotSet = false;
//    // antenna
//    public static RFModeTable rfModeTable;
//    public static Antennas.AntennaRfConfig antennaRfConfig;
//    public static int[] antennaPowerLevel;
//    public static Readers readers;
//    private static boolean activityVisible;
//    public static ReaderDevice mReaderDisappeared;

    @Override
    public void onCreate() {
        super.onCreate();
        ZebarHolder.newInstance();
        context=this.getApplicationContext();
    }
    public static Context getContext(){
        return context;
    }

}
