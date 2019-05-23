package www.baidutest.com.location;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationListener;
import android.media.ThumbnailUtils;
import android.util.Log;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapBaseIndoorMapInfo;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

import www.baidutest.com.R;
import www.baidutest.com.Utils.PictureUtil;
import www.baidutest.com.entity.MarkerBean;

public class BaiduHelperUtil {

    private final String TAG = BaiduHelperUtil.class.getSimpleName();

    private BaiduMap baiduMap = null;

    private MapBaseIndoorMapInfo mMapBaseIndoorMapInfo = null;

    public BaiduHelperUtil(BaiduMap baiduMap) {
        this.baiduMap = baiduMap;
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        initListener();
    }

    private void initListener() {
        baiduMap.setOnMarkerClickListener(markerClickListener);
    }

    /**
     * 切换地图类型
     *
     * @param mapType
     */
    public void switchMapType(MapType mapType) {
        switch (mapType) {
            case NONE:
                baiduMap.setMapType(BaiduMap.MAP_TYPE_NONE);
                break;
            case SATELLITE:
                baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;
            case NORMAL:
                baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                break;
            default:
                break;
        }
    }


    public enum MapType {
        NORMAL("普通地图", 0), SATELLITE("卫星图", 1), NONE("空白图", 2);
        String name;

        int code;

        MapType(String name, int code) {
            this.name = name;
            this.code = code;
        }
    }

    /**
     * 实时路况图
     *
     * @param enable
     */
    public void setTrafficEnabled(boolean enable) {
        baiduMap.setTrafficEnabled(enable);
    }

    /**
     * 百度城市热力图
     *
     * @param enable
     */
    public void setBaiduHeatMapEnabled(boolean enable) {
        baiduMap.setBaiduHeatMapEnabled(enable);
    }

    /**
     * 自定义定位模式
     *
     * @param locationMode
     * @param enableDirection
     * @param imgRef
     * @param accuracyCircleFillColor
     * @param accuracyCircleStrokeColor
     */
    public void setLocationConfiguration(MyLocationConfiguration.LocationMode locationMode, boolean enableDirection, int imgRef,
                                         int accuracyCircleFillColor,
                                         int accuracyCircleStrokeColor) {
        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(imgRef);

        MyLocationConfiguration myLocationConfiguration;
        if (accuracyCircleFillColor == 0 && accuracyCircleStrokeColor == 0) {
            myLocationConfiguration = new MyLocationConfiguration(locationMode, enableDirection, mCurrentMarker);
        } else {
            myLocationConfiguration = new MyLocationConfiguration(locationMode, enableDirection, mCurrentMarker,
                    accuracyCircleFillColor, accuracyCircleStrokeColor);
        }
        baiduMap.setMyLocationConfiguration(myLocationConfiguration);
    }


    public void startLocal(BDLocation location, boolean isShowLoc) {

        //mapView 销毁后不在处理新接收的位置
        if (location == null || baiduMap == null) {
            return;
        }

// 构造定位数据
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();

// 设置定位数据
        baiduMap.setMyLocationData(locData);

// 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
        setLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, R.mipmap.ic_launcher, 0, 0);

        if (isShowLoc) {
//            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
//            MapStatus.Builder builder = new MapStatus.Builder();
//            builder.target(ll).zoom(18.0f);
//            baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            updateLocal(location.getLatitude(), location.getLongitude());
        }
    }

    /**
     * 更新位置
     *
     * @param v1 经度
     * @param v2 纬度
     */
    public void updateLocal(double v1, double v2) {
        LatLng ll = new LatLng(v1, v2);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(18.0f);
        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    /**
     * 当不需要定位图层时关闭定位图层
     */
    public void closeMyLoacation() {
        if (baiduMap != null)
            baiduMap.setMyLocationEnabled(false);
    }

    /**
     * 设置覆盖物
     *
     * @param markerList
     */
    public void setMarker(List<MarkerBean> markerList) {
        if (markerList == null || markerList.size() < 1) {
            return;
        }
        for (int i = 0; i < markerList.size(); i++) {
            MarkerBean markerBean = markerList.get(i);
            LatLng point = markerBean.getLatLng();
            //构建market图标
            BitmapDescriptor bitmap = null;
            Object imgObj = markerBean.getImgUrl();
            if (imgObj instanceof Bitmap) {
                Bitmap bm = (Bitmap) markerBean.getImgUrl();
                bitmap = BitmapDescriptorFactory.fromBitmap(bm);
            } else if (imgObj instanceof String) {
                String img = (String) markerBean.getImgUrl();
                if (img.startsWith("file:///")) {
                    bitmap = BitmapDescriptorFactory.fromAsset(img);
                } else if (img.startsWith("http") || img.startsWith("https")) {
                    bitmap = BitmapDescriptorFactory.fromPath(img);
                } else {
                    bitmap = BitmapDescriptorFactory.fromFile(img);
                }
            } else if (imgObj instanceof View) {
                View imgView = (View) markerBean.getImgUrl();
                BitmapDescriptorFactory.fromView(imgView);
            } else if (imgObj instanceof Integer) {
                int resouceId = (int) markerBean.getImgUrl();
                bitmap = BitmapDescriptorFactory.fromResource(resouceId);
            }

            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap);
            //在地图上添加Marker，并显示
            baiduMap.addOverlay(option);
        }
    }

    /**
     * 显示室内地图
     */
    public void setIndoorEnable(boolean enable) {
        if (baiduMap != null)
            baiduMap.setIndoorEnable(enable);
    }

    /**
     * 楼层
     *
     * @param strFloor SwitchFloorError.SWITCH_OK:     //切换成功
     *                 SwitchFloorError.FLOOR_INFO_ERROR:   //切换楼层, 室内ID信息错误
     *                 SwitchFloorError.FLOOR_OVERLFLOW:    //楼层溢出 即当前室内图不存在该楼层
     *                 SwitchFloorError.FOCUSED_ID_ERROR:  //切换楼层室内ID与当前聚焦室内ID不匹配
     *                 SwitchFloorError.SWITCH_ERROR: //切换楼层失败
     */
    public String getIndoorFloorInfo(String strFloor) {
        String strID = mMapBaseIndoorMapInfo.getID();
//        String strFloor = mMapBaseIndoorMapInfo.getCurFloor();
        MapBaseIndoorMapInfo.SwitchFloorError switchFloorError = baiduMap.switchBaseIndoorMapFloor(strFloor, strID);
        return switchFloorError.name();
    }

//    public void setOutIndoor() {
//        baiduMap.
//    }
    /**
     * 监听室内
     */
    private BaiduMap.OnBaseIndoorMapListener onBaseIndoorMapListener = new BaiduMap.OnBaseIndoorMapListener() {
        @Override
        public void onBaseIndoorMapMode(boolean on, MapBaseIndoorMapInfo mapBaseIndoorMapInfo) {

            if (mapBaseIndoorMapInfo != null) {
                mMapBaseIndoorMapInfo = mapBaseIndoorMapInfo;
            }
            if (on) {
                // 进入室内图
                // 通过获取回调参数 mapBaseIndoorMapInfo 便可获取室内图信
                //息，包含楼层信息，室内ID等
            } else {
                // 移除室内图
            }

        }
    };

    /**
     * marker的点击事件
     */

    private BaiduMap.OnMarkerClickListener markerClickListener = new BaiduMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            Log.i(TAG, "onMarkerClick: ");
            return false;
        }
    };
}
