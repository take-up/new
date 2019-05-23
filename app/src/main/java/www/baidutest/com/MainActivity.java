package www.baidutest.com;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;

import www.baidutest.com.Utils.Helper;
import www.baidutest.com.location.BaiduHelperUtil;
import www.baidutest.com.location.LocationAddress;

public class MainActivity extends AppCompatActivity implements LocationAddress.LocationImp{

    private final int BAIDU_READ_PHONE_STATE = 100;

    private LocationAddress locationAddress = null;

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private BaiduHelperUtil baiduMapUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        showAuthority();
        mMapView = findViewById(R.id.mmap);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        baiduMapUtil = new BaiduHelperUtil(mBaiduMap);
        init();

    }

    public void OnLocationClick(View view) {

        int id = view.getId();

        if(id == R.id.btn_location) {

            location();
        }

        if (id == R.id.btn_show_indoor) {
            baiduMapUtil.setIndoorEnable(true);
        }
    }

    private void location() {

        if (Helper.isGps(this)) {
            locationAddress.startLocation();
        }else {
            //提示用户开启GPS
            Toast.makeText(MainActivity.this, "请手动开启GPS",Toast.LENGTH_LONG).show();
        }
    }


    /**
     * 6.0以上用户动态授权
     */
    private void showAuthority() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED
                    || this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    || this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    || this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    || this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE }, BAIDU_READ_PHONE_STATE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode) {
            //requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case 1:
                BAIDU_READ_PHONE_STATE:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //获取到权限，做相应处理
                    //调用定位SDK应确保相关权限均被授权，否则会引起定位失败
                    init();
                } else{
                    //没有获取到权限，做特殊处理
                    Toast.makeText(MainActivity.this, "获取位置权限失败，请手动开启",Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }

    }

    private void init() {
        locationAddress = new LocationAddress(this);
        locationAddress.setLocationIpm(this);
//        location();
    }

    @Override
    public void getDbLocation(BDLocation bdLocation,boolean isCenter) {
        baiduMapUtil.startLocal(bdLocation,isCenter);
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        // 退出时销毁定位
        locationAddress.stopLocation();
        // 关闭定位图层
      baiduMapUtil.closeMyLoacation();
        mMapView.onDestroy();
        mMapView = null;
    }
}
