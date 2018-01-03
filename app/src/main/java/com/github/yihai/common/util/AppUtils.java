package com.github.yihai.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ArrayRes;
import android.support.annotation.StringRes;
import android.view.WindowManager;

import com.github.yihai.base.ActivityPageManager;
import com.github.yihai.manager.YiHaiHttpConstaint;
import com.github.yihai.widget.PointBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by ${sheldon} on 2017/7/13.
 */

public class AppUtils {

    private static Context mContext;
    private static Thread mUiThread;
    private static Timer mTimer;

    private static Handler sHandler = new Handler(Looper.getMainLooper());

    public static void init(Context context) { //在Application中初始化
        mContext = context;
        mUiThread = Thread.currentThread();
        mTimer = new Timer();
    }

//    public static Handler getMainHandler() {
//        return BaseApplication.getMainHandler();
//    }
//
//    public static long getMainThreadId() {
//        return BaseApplication.getMainThreadId();
//    }
//
//    public static Context getContext() {
//        return BaseApplication.getContext();
//    }

    public static Context getAppContext() {
        return mContext;
    }

    public static AssetManager getAssets() {
        return mContext.getAssets();
    }

    public static float getDimension(int id) {
        return getResource().getDimension(id);
    }

    public static Resources getResource() {
        return mContext.getResources();
    }

    @SuppressWarnings("deprecation")
    public static Drawable getDrawable(int resId) {
        return mContext.getResources().getDrawable(resId);
    }

    @SuppressWarnings("deprecation")
    public static int getColor(int resId) {
        return mContext.getResources().getColor(resId);
    }

    public static String getString(@StringRes int resId) {
        return mContext.getResources().getString(resId);
    }

    public static String[] getStringArray(@ArrayRes int resId) {
        return mContext.getResources().getStringArray(resId);
    }

    public static boolean isUIThread() {
        return Thread.currentThread() == mUiThread;
    }

    public static void runOnUI(Runnable r) {
        sHandler.post(r);
    }

    public static void runOnUIDelayed(Runnable r, long delayMills) {
        sHandler.postDelayed(r, delayMills);
    }

    public static void runOnUITask(TimerTask r, long delay, long rate) {
        mTimer.schedule(r, delay, rate);
    }

    public static void runCancel() {
        mTimer.cancel();
    }

    public static void removeRunnable(Runnable r) {
        if (r == null) {
            sHandler.removeCallbacksAndMessages(null);
        } else {
            sHandler.removeCallbacks(r);
        }
    }

    public static ClassLoader getInstalledClassloader(Context context, String packageName) {
        Context packageContext = null;
        try {
            packageContext = context.createPackageContext(packageName,
                    Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageContext.getClassLoader();
    }

    /**
     * 获取资源ID
     *
     * @param packageName 包名
     * @param type        对应的资源类型, drawable mipmap等
     * @param fieldName
     * @return
     */
    public static int getResourceID(ClassLoader classLoader, String packageName, String type, String fieldName) {
        int resID = -1;
        String rClassName = packageName + ".R$" + type; // 根据匿名内部类的命名, 拼写出R文件的包名+类名
        try {
            Class cls = classLoader.loadClass(rClassName);    //  加载R文件
            resID = (Integer) cls.getField(fieldName).get(null);    //  反射获取R文件对应资源名的ID
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resID;
    }

    /**
     * 获取资源ID
     *
     * @param packageName 包名
     * @param type        对应的资源类型, drawable mipmap等
     * @param fieldName
     * @return
     */
    public static int getResourceID(Context context, String packageName, String type, String fieldName) {
        return getResourceID(getInstalledClassloader(context, packageName), packageName, type, fieldName);
    }

    /**
     * 将网络获取的时间字符串进行解析
     * 例：将2017-11-24T00:00:00
     *
     * @param date
     * @return
     */
    public static Date parseNetDate(String date) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-ddhh:mm:ss").parse(date.replace("T", ""));
    }

    /**
     * 获取用户名
     *
     * @return
     */
    public static String getUserName(Context context) {
        return SharedPreferencesUtils.getString(context, YiHaiHttpConstaint.USER_NAME);
    }

    public static void exit(Context context) {
        ActivityPageManager.getInstance().exit(context);
        System.exit(0);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static int getWindowWidth(Context context) {
        return ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getWidth();
    }

    public static int getWindowHeight(Context context) {
        return ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getHeight();
    }
    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public static void backgroundAlpha(float bgAlpha,Activity activity)
    {
        WindowManager.LayoutParams lp =activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }
    /**
     * 货位转换 A01-1-1
     */
    public static PointBean getGoodsSite(String huowei) {
        PointBean pointBean = new PointBean();
        int ihuowei = 0;
        int x=0;
        int y=0;
        int z=0;
        String sx="";
        String sy="";
        String sz="";
        int index = huowei.indexOf("-");
        sz=huowei.substring(index-1,index);
        huowei = huowei.substring(index+1);
        if (huowei.indexOf("-") > -1) {
            sx=huowei.substring(huowei.indexOf("-")-1,huowei.indexOf("-"));
            sy=huowei.substring(huowei.indexOf("-")+1,huowei.indexOf("-")+2);
        }
        if(CommonUtil.isNumeric(sx))
        {
            x = Integer.parseInt(sx);
        }
        if(CommonUtil.isNumeric(sy))
        {
            y = Integer.parseInt(sy);
        }
        if(CommonUtil.isNumeric(sz))
        {
            z = Integer.parseInt(sz);
        }
        pointBean.setX(x);
        pointBean.setY(y);
        pointBean.setZ(z);
        return pointBean;
    }
}
