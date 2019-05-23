package www.baidutest.com.location;

import android.app.Activity;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.BMapManager;

import java.util.List;

public class LocationAddress {

    private final String TAG = LocationAddress.class.getSimpleName();

    private LocationClient locationClient = null;

    private LocationImp locationImp;

    //防止每次定位都重新设置中心点和marker
    private boolean isFirstLocation = true;

    public LocationAddress(Activity activity) {

        locationClient = new LocationClient(activity.getApplicationContext());

        //注册监听函数
        locationClient.registerLocationListener(myLocationListener);

        //配置SDK参数
        initSetting();
    }

    public void setLocationIpm(LocationImp locationIpm) {
        this.locationImp = locationIpm;
    }

    private void initSetting() {

        LocationClientOption option = new LocationClientOption();

        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

        //可选，设置返回经纬度坐标类型，默认GCJ02
        //GCJ02：国测局坐标；
        //BD09ll：百度经纬度坐标；
        //BD09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标
        option.setCoorType("bd09ll");

        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效
//        option.setScanSpan(1000);

        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true
        option.setOpenGps(true);

        //可选，设置是否需要地址信息，默认不需要
        option.setIsNeedAddress(true);
//可选，设置是否需要地址描述
        option.setIsNeedLocationDescribe(true);

        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIsNeedLocationPoiList(true);

        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false
        option.setLocationNotify(false);

        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)
        option.setIgnoreKillProcess(false);

        //可选，设置是否收集Crash信息，默认收集，即参数为false
        option.SetIgnoreCacheException(false);

        //可选，V7.2版本新增能力
        //如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位
        option.setWifiCacheTimeOut(5 * 60 * 1000);

        //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者，该模式下开发者无需再关心定位间隔是多少，定位SDK本身发现位置变化就会及时回调给开发者
//        option.setOpenAutoNotifyMode();

        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false
        option.setEnableSimulateGps(false);

        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        locationClient.setLocOption(option);
    }

    /**
     * 启动定位SDK
     */
    public void startLocation() {
        if (locationClient != null) {
            locationClient.start();
            locationClient.requestLocation();
            checkConnectHotSpotMessage();
        }
    }

    public void stopLocation() {
        if (locationClient != null) {
            locationClient.unRegisterLocationListener(myLocationListener);
            locationClient.stop();
        }
    }

    /**
     * 判断移动热点
     * @return
     */
    public boolean checkConnectHotSpotMessage() {
        return locationClient.requestHotSpotState();
    }

    private BDAbstractLocationListener myLocationListener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {

            if (location == null) {
                return;
            }

            //获取室内
            if (location.getFloor() != null) {
                // 当前支持高精度室内定位
                String buildingID = location.getBuildingID();// 百度内部建筑物ID
                String buildingName = location.getBuildingName();// 百度内部建筑物缩写
                String floor = location.getFloor();// 室内定位的楼层信息，如 f1,f2,b1,b2
                locationClient.startIndoorMode();// 开启室内定位模式（重复调用也没问题），
                // 开启后，定位SDK会融合各种定位信息（GPS,WI-FI，蓝牙，传感器等）连续平滑的输出定位结果；
                Log.i(TAG, "onReceiveLocation: 获取室内 buildingID: " + buildingID
                        + "\nbuildingName: " + buildingName
                        + "\nfloor: " + floor);
            }

            switch (location.getLocationWhere()) {//判断当前定位时国内还是国外
                case BDLocation.LOCATION_WHERE_IN_CN:
                    Log.i(TAG, "onReceiveLocation: where 在国内");
                    break;
                case BDLocation.LOCATION_WHERE_OUT_CN:
                    Log.i(TAG, "onReceiveLocation: where 在国外");
                    break;
                default:
                    Log.i(TAG, "onReceiveLocation: where 无法判定");
                    break;
            }
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            double latitude = location.getLatitude();    //获取纬度信息
            double longitude = location.getLongitude();    //获取经度信息
            float radius = location.getRadius();    //获取定位精度，默认值为0.0f

            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
            String coorType = location.getCoorType();

            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
            int errorCode = location.getLocType();

            String addr = location.getAddrStr();//获取详细地址信息
            String country = location.getCountry();//获取国家
            String province = location.getProvince();//获取省份
            String city = location.getCity();//获取城市
            String district = location.getDistrict();//获取区县
            String street = location.getStreet();    //获取街道信息

            Log.i(TAG, "onReceiveLocation: addr " + addr);
            Log.i(TAG, "onReceiveLocation: latitude: " + latitude);
            Log.i(TAG, "onReceiveLocation: longitude: " + longitude);
            Log.i(TAG, "onReceiveLocation: radius: " + radius);
            Log.i(TAG, "onReceiveLocation: coorType: " + coorType);
            //获取周边POI信息
            //POI信息包括POI ID、名称等，具体信息请参照类参考中POI类的相关说明
            List<Poi> poiList = location.getPoiList();
            if (poiList != null && poiList.size() > 0) {
                for (int i = 0; i < poiList.size(); i++) {
                    Poi poi = poiList.get(i);
                    Log.i(TAG, "onReceiveLocation: " + poi.getRank() + "  名称: " + poi.getName());
                }

            }

            //这个判断是为了防止每次定位都重新设置中心点和marker
            if (isFirstLocation) {
                //isFirstLocation = false;
                locationImp.getDbLocation(location, true);
            }

        }

        /**
         *
         * @param connectWifiMac 表示连接WI-FI的MAC地址，无连接或者异常时返回NULL
         * @param hotSpotState
         * LocationClient.CONNECT_HPT_SPOT_TRUE：连接的是移动热点
         * LocationClient.CONNECT_HPT_SPOT_FALSE：连接的非移动热点
         * LocationClient.CONNECT_HPT_SPOT_UNKNOWN：连接状态未知
         */
        @Override
        public void onConnectHotSpotMessage(String connectWifiMac, int hotSpotState) {
            super.onConnectHotSpotMessage(connectWifiMac, hotSpotState);
            Log.i(TAG, "onConnectHotSpotMessage: connectWifiMac : "+connectWifiMac
            +" hotSpotState : "+(hotSpotState == LocationClient.CONNECT_HOT_SPOT_TRUE ? "连接的是移动热点" :
            hotSpotState == LocationClient.CONNECT_HOT_SPOT_FALSE ? "连接的非移动热点" : "连接状态未知"));
        }
    };

    public interface LocationImp {
        void getDbLocation(BDLocation bdLocation, boolean isCenter);
    }
}
