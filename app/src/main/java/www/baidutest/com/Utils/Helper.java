package www.baidutest.com.Utils;

import android.content.Context;
import android.location.LocationManager;

public class Helper {

    /**
     * 检测是否开启GPS
     * @param context
     * @return
     */
    public static boolean isGps(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true;
        }
        return false;
    }
}
