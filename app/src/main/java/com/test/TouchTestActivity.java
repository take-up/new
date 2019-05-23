package com.test;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.os.Build;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TimeUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.test.bean.User;
import com.test.handler.MyHandlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import www.baidutest.com.R;
import www.baidutest.com.databinding.ActivityTouchTestBinding;

public class TouchTestActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    ActivityTouchTestBinding activityTouchTestBinding = null;

    @Override
    protected void onStart() {
        //注册蓝牙广播
        IntentFilter bluetoothFilter = new IntentFilter();
        bluetoothFilter.addAction(BluetoothDevice.ACTION_FOUND);//每搜索到一个设备就发一个广播
        bluetoothFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//全部搜索完成后发送的广播
        registerReceiver(receiver,bluetoothFilter);
        super.onStart();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        super.onPause();
    }


    BluetoothAdapter mBluetoothAdapter = null;
    Set<BluetoothDevice> bondedDeviceSet = null;//配对的设备

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
       @Override
       public void onReceive(Context context, Intent intent) {

           String action = intent.getAction();
           if (BluetoothDevice.ACTION_FOUND.equals(action)) {

               BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
               if (bluetoothDevice.getBondState() != BluetoothDevice.BOND_BONDED && isSingleDevice(bluetoothDevice)) {
                   if (bondedDeviceSet == null) {
                       bondedDeviceSet = new HashSet<BluetoothDevice>();
                   }
                   bondedDeviceSet.add(bluetoothDevice);
                   //更新列表
               }

           }

           if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

           }


       }

    };

    /**
     * 判断是否存在了
     * @return
     */
    private boolean isSingleDevice(BluetoothDevice bluetoothDevice) {

        if (bluetoothDevice == null) {
            return true;
        }
        if (bondedDeviceSet != null && bondedDeviceSet.size() > 1) {
            for (BluetoothDevice bluetoothDevice1 : bondedDeviceSet) {
                if (bluetoothDevice.getAddress().equals(bluetoothDevice1.getAddress())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @param savedInstanceState 获取在onSaveInstanceState（）保存下来的数据
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_touch_test);
        activityTouchTestBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_touch_test);
        User user = new User("小明", 19);
        user.setAdult(true);
        activityTouchTestBinding.setUser(user);

    }

    public void onTestClick(View view) {

        int id = view.getId();

        if (id == R.id.btn_rxjava_map_test) {
            testMapRxjava2();
        }

        if (id == R.id.btn_rxjava_flatmap_test) {
            testFlatmapRxjava2();
        }

        if (id == R.id.btn_rxjava_interval_1_test) {
            testIntervalRxjava2_1();
        }

        if (id == R.id.btn_rxjava_interval_test) {
            testIntervalRxjava2_2();
        }

        if (id == R.id.btn_open_bluetooth) {

            //这种方式会让用户看到对话框
            //startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE),1);

            //静默打开蓝牙，6.0以及以上需要用户授权
            checkBlueToothPermission();

        }

        if (id == R.id.btn_close_bluetooth) {
            if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.disable();
            }
        }

        if (id == R.id.btn_passive_discovery) {
            if (mBluetoothAdapter != null) {
                if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                    Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    // 设置被发现时间，最大值是3600秒,0表示设备总是可以被发现的(小于0或者大于3600则会被自动设置为120秒)
                    discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
                    startActivity(discoverableIntent);
                }
            }
        }

        if (id == R.id.btn_active_discovery) {
            showBoundDevices();
        }

    }

    private void checkBlueToothPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            new RxPermissions(this).request(Manifest.permission.ACCESS_COARSE_LOCATION)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            if (aBoolean) {
                                openBluetooth();
                            } else {
                                Toast.makeText(TouchTestActivity.this, "请先授权才能使用", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            openBluetooth();
        }
    }

    private void openBluetooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.enable();
        } else {
            Toast.makeText(TouchTestActivity.this, "该设备不支持蓝牙", Toast.LENGTH_LONG).show();
        }
    }

    private void showBoundDevices() {

        if (mBluetoothAdapter == null) {
            return;
        }
        if (bondedDeviceSet == null) {
            bondedDeviceSet = mBluetoothAdapter.getBondedDevices();
        } else {
            bondedDeviceSet.addAll(mBluetoothAdapter.getBondedDevices());
        }
        for (BluetoothDevice bluetoothDevice : bondedDeviceSet) {
            Log.i(TAG, "showBoundDevices: name : " + bluetoothDevice.getName() + " , address : " + bluetoothDevice.getAddress());
        }
    }

    private void toDiscovery() {
        if (mBluetoothAdapter == null) {
            Toast.makeText(TouchTestActivity.this, "请先授权才能使用", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!mBluetoothAdapter.disable()) {
            mBluetoothAdapter.enable();
            mBluetoothAdapter.cancelDiscovery();
        }

        while (!mBluetoothAdapter.startDiscovery()) {
            Toast.makeText(TouchTestActivity.this, "尝试失败", Toast.LENGTH_SHORT).show();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void testIntervalRxjava2_1() {
        Flowable.interval(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.i(TAG, " interval accept: " + aLong);
                    }
                });
    }

    private void testIntervalRxjava2_2() {
        final long time = 5;
        Observable.interval(0, TimeUnit.SECONDS)
                .map(new Function<Long, Long>() {

                    @Override
                    public Long apply(Long aLong) throws Exception {
                        return time - aLong;
                    }
                })
                .take(time + 1)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.i(TAG, " interval的倒计时 accept: " + aLong);
                    }
                });
    }

    /**
     * flatMap ： 它可以把一个发射器Observable 通过某种方法转换为多个Observables，
     * 然后再把这些分散的Observables装进一个单一的发射器Observable
     */
    private void testFlatmapRxjava2() {

        Observable.just(1, 2, 3, 4)
                .flatMap(new Function<Integer, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Integer integer) throws Exception {
                        List<String> list = new ArrayList<>();
                        for (int i = 0; i < 3; i++) {
                            list.add("I am value " + integer);
                        }
                        //随机生成一个时间
                        int delayTime = (int) (1 + Math.random() * 10);
                        return Observable.fromIterable(list).delay(delayTime, TimeUnit.MILLISECONDS);
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.i(TAG, "flatMap apply: " + s + " 线程： " + Thread.currentThread().getName());
                    }
                });

    }

    /**
     * 测试rxjava2.x的map使用
     * map的作用： 它的作用是对发射时间发送的每一个事件应用一个函数
     */
    private void testMapRxjava2() {

        Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onNext(4);

            }
        })
                .map(new Function<Integer, String>() {

                    @Override
                    public String apply(Integer integer) throws Exception {
                        Log.i(TAG, "map apply: " + integer + " 线程： " + Thread.currentThread().getName());
                        return " the result : " + integer;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.i(TAG, "map accept: " + s + " 线程： " + Thread.currentThread().getName());
                    }
                });
    }

    /**
     * 保存数据
     *
     * @param outState
     * @param outPersistentState
     */

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    public void onStreamTest(View view) {

        List<Integer> intList = Arrays.asList(23, 45, 12, 65);
        final Double[] max = {Double.MAX_VALUE};

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: requestCode : " + requestCode + " resultCode : " + resultCode);
        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {

            }
        }
    }
}
