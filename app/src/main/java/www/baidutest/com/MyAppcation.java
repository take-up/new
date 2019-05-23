package www.baidutest.com;

import android.app.ActivityManager;
import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Process;
import android.util.Log;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;

public class MyAppcation extends Application {

    private static Application appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
//        getChannel("com.baidu.lbsapi.API_KEY");
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }

    public static Application getAppContext() {
        return appContext;
    }


    private String getChannel(String key) {
        PackageManager packageManager = getPackageManager();
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(),PackageManager.GET_META_DATA);
            String value = applicationInfo.metaData.getString(key);
            Log.i("MyAppcation", "getChannel: key : "+key + "value : "+value);
            return value;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取当前进程名称
     * @return
     */
    private String getCurrentProcessName() {
        String currentProcessName = "";
        int pid = Process.myPid();
        ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo serviceInfo : activityManager.getRunningAppProcesses()) {
            if (serviceInfo.pid == pid) {
                currentProcessName = serviceInfo.processName;
                break;
            }
        }
        return currentProcessName;
    }
}
