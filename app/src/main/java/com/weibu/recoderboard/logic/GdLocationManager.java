package com.weibu.recoderboard.logic;

import android.app.Activity;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.weibu.recoderboard.entity.CarNumberMsg;
import com.weibu.recoderboard.entity.LocationMsg;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by blueberry on 2017/7/17.
 * 高德地图，获取位置，速度
 */

public class GdLocationManager {
    private static GdLocationManager mInstance = null;
    private Activity context;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private LocationMsg msg;

    public static GdLocationManager getInstance(Activity context) {
        if (mInstance == null) {
            synchronized (GdLocationManager.class) {
                if (mInstance == null) {
                    mInstance = new GdLocationManager(context);
                }
            }
        }
        return mInstance;
    }
    private GdLocationManager(Activity activity) {
        this.context = activity;
        initLocation();
    }

    private void initLocation()
    {
        //初始化client
        locationClient = new AMapLocationClient(context);
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }
    /**
     * 默认的定位参数
     * @author blueberry
     *
     */
    private AMapLocationClientOption getDefaultOption(){
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    /**
     * 定位监听
     */
    private AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {
                if(location.getErrorCode() == 0) {
                    if(msg != null){
                        msg = null;
                    }
                    msg = new LocationMsg();
                    msg.setMsg_location(location.getAddress());
                    msg.setMsg_speed(location.getSpeed());

                }else{
                    msg = new LocationMsg();
                    msg.setMsg_location("无法定位");
                    msg.setMsg_speed(0);
                }

            } else {
                msg = new LocationMsg();
                msg.setMsg_location("无法定位");
                msg.setMsg_speed(0);
            }
            EventBus.getDefault().post(msg);//EventBus发送location speed,MakrkViewManager类负责接收
        }
    };

    /**
     * 开始定位
     * @author blueberry
     *
     */
    public void startLocation(){
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    /**
     * 停止定位
     * @author blueberry
     *
     */
    public void stopLocation(){
        // 停止定位
        locationClient.stopLocation();
    }

    /**
     * 销毁定位
     * @author blueberry
     *
     */
    public void destroyLocation(){
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

}
