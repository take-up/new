package www.baidutest.com.location;

import com.baidu.location.BDLocation;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;

public class LocationNotify {

    private LocationClient locationClient;

    private BDNotifyListener myNotifyListener;

    public LocationNotify(LocationClient locationClient) {

        if (this.locationClient == null) {
            this.locationClient = locationClient;
        }

        if (myNotifyListener == null) {
            myNotifyListener = new MyNotifyListener();
        }

        locationClient.registerNotify(myNotifyListener);
    }

    /**
     * 设置位置提醒
     * @param latitude
     * @param longitude
     * @param radius
     */
    public void setAddressNotify(double latitude, double longitude,float radius) {
        //设置位置提醒，四个参数分别是：纬度、经度、半径、坐标类型
       myNotifyListener.SetNotifyLocation(latitude,longitude,radius,locationClient.getLocOption().getCoorType());
    }

    /**
     *   取消位置提醒
     */
    public void removeNotify() {
        locationClient.removeNotifyEvent(myNotifyListener);
    }

    private class MyNotifyListener extends BDNotifyListener {

        /**
         * 已达到设置位置附近
         * @param bdLocation
         * @param v
         */
        @Override
        public void onNotify(BDLocation bdLocation, float v) {
            super.onNotify(bdLocation, v);
        }

        @Override
        public void SetNotifyLocation(double v, double v1, float v2, String s) {
            super.SetNotifyLocation(v, v1, v2, s);
        }
    }
}
