package com.github.yihai.zebra;

import com.zebra.rfid.api3.Antennas;
import com.zebra.rfid.api3.BEEPER_VOLUME;
import com.zebra.rfid.api3.DYNAMIC_POWER_OPTIMIZATION;
import com.zebra.rfid.api3.PreFilters;
import com.zebra.rfid.api3.RFIDReader;
import com.zebra.rfid.api3.RFModeTable;
import com.zebra.rfid.api3.ReaderDevice;
import com.zebra.rfid.api3.RegulatoryConfig;
import com.zebra.rfid.api3.StartTrigger;
import com.zebra.rfid.api3.StopTrigger;
import com.zebra.rfid.api3.TagStorageSettings;
import com.zebra.rfid.api3.UNIQUE_TAG_REPORT_SETTING;

import java.util.HashMap;

/**
 * Created by dzl on 2017/12/20.
 */

public class ZebarHolder {
    public static  boolean isEventAdded=false;
    private static ZebarHolder zebarHolder;
    public static RFIDReader mConnectedReader;
    public static ReaderDevice mConnectedDevice;
    //Boolean to keep track of whether the inventory is running or not
    public static volatile boolean mIsInventoryRunning;
    public static Boolean isBatchModeInventoryRunning;
    public static PreFilters[] preFilters = null;
    public static HashMap<String, String> versionInfo;
    public static StartTrigger settings_startTrigger;
    public static StopTrigger settings_stopTrigger;
    public static short TagProximityPercent;
    public static TagStorageSettings tagStorageSettings;
    public static int batchMode;
    public static DYNAMIC_POWER_OPTIMIZATION dynamicPowerSettings;
    public static boolean is_disconnection_requested;
    public static boolean is_connection_requested;
    public static volatile boolean NOTIFY_READER_CONNECTION;
    //Beeper
    public static BEEPER_VOLUME beeperVolume = null;
    // Singulation control
    public static Antennas.SingulationControl singulationControl;
    // regulatory
    public static RegulatoryConfig regulatory;
    public static Boolean regionNotSet;
    // public static BatteryData BatteryData = null;
    // antenna
    public static RFModeTable rfModeTable;
    public static Antennas.AntennaRfConfig antennaRfConfig;
    public static int[] antennaPowerLevel;
    public static UNIQUE_TAG_REPORT_SETTING reportUniquetags = null;
    public static EventHandler eventHandler = new EventHandler();

    private ZebarHolder() {
        versionInfo = new HashMap<>(5);
        TagProximityPercent = -1;
        regionNotSet = false;
    }

    public static ZebarHolder newInstance() {
        if (zebarHolder == null) {
            zebarHolder = new ZebarHolder();
        }
        return zebarHolder;
    }

//
//    //暂时没用---begin----
//    //Variable to keep track of the unique tags seen
//    public static volatile int UNIQUE_TAGS = 0;
//    //variable to keep track of the total tags seen
//    public static volatile int TOTAL_TAGS = 0;
//    //Arraylist to keeptrack of the tagIDs to act as adapter for autocomplete text views
//    public static ArrayList<String> tagIDs;
//    //variable to store the tag read rate
//    public static volatile int TAG_READ_RATE = 0;
//    public static int inventoryMode = 0;
//    public static int memoryBankId = -1;
//    public static String accessControlTag;
//    public static String locateTag;
//    //Variable to maintain the RR started time to maintain the read rate
//    public static volatile long mRRStartedTime;
//    public static boolean isAccessCriteriaRead = false;
//    public static int preFilterIndex = -1;
//    //For Notification
//    public static volatile int INTENT_ID = 100;
//    public static TreeMap<String, Integer> inventoryList = new TreeMap<String, Integer>();
//    public static boolean isGettingTags;
//    public static boolean EXPORT_DATA;
//    public static boolean isLocatingTag;
//    public static volatile boolean AUTO_DETECT_READERS;
//    public static volatile boolean AUTO_RECONNECT_READERS;
//    public static volatile boolean NOTIFY_READER_AVAILABLE;
//    public static volatile boolean NOTIFY_BATTERY_STATUS;
//
//    public static Readers readers;
//    private static boolean activityVisible;
//    public static ReaderDevice mReaderDisappeared;
//    //暂时没用---end----

    //clear saved data
    public static void reset() {
        mIsInventoryRunning = false;
        mConnectedDevice = null;
        antennaPowerLevel = null;
        //Triggers
        settings_startTrigger = null;
        settings_startTrigger = null;

        //Beeper
        beeperVolume = null;
        // reader settings
        regulatory = null;
        regionNotSet = false;

        preFilters = null;

        settings_startTrigger = null;
        settings_stopTrigger = null;

        if (versionInfo != null)
            versionInfo.clear();

        TagProximityPercent = -1;

        is_disconnection_requested = false;
        is_connection_requested = false;

//        //    BatteryData = null;
//        UNIQUE_TAGS = 0;
//        TOTAL_TAGS = 0;
//        TAG_READ_RATE = 0;
//        mRRStartedTime = 0;
//
////        if (tagsReadInventory != null)
////            tagsReadInventory.clear();
//        if (tagIDs != null)
//            tagIDs.clear();
//
//        inventoryMode = 0;
//        memoryBankId = -1;
//        if (inventoryList != null)
//            inventoryList.clear();
//
//        INTENT_ID = 100;
//
//        accessControlTag = null;
//        isAccessCriteriaRead = false;
//
//        preFilterIndex = -1;
//
//        isLocatingTag = false;
//        locateTag = null;
//
//        readers = null;
    }

}
