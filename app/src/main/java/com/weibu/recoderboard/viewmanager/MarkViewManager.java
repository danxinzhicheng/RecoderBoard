package com.weibu.recoderboard.viewmanager;

import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.weibu.recoderboard.MainActivity;
import com.weibu.recoderboard.R;
import com.weibu.recoderboard.entity.CarNumberMsg;
import com.weibu.recoderboard.entity.LocationMsg;
import com.weibu.recoderboard.util.Constant;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by blueberry on 2017/7/14.
 * 标记（车牌号，位置,天气...）显示管理类，负责数据显示
 */

public class MarkViewManager implements IViewManager {

    private MainActivity mainActivity;
    private TextView tvCarNumber,tvLocation,tvSpeed;

    public MarkViewManager(MainActivity context)
    {
        this.mainActivity = context;
    }
    @Override
    public MarkViewManager initView() {
        tvCarNumber = (TextView) mainActivity.findViewById(R.id.tv_no);
        tvLocation = (TextView) mainActivity.findViewById(R.id.tv_location);
        tvSpeed = (TextView) mainActivity.findViewById(R.id.tv_speed);
        return this;
    }

    @Override
    public MarkViewManager initData() {
        SharedPreferences shp = PreferenceManager.getDefaultSharedPreferences(mainActivity);
        String car_number = shp.getString(Constant.prefs_key_car_number,"");
        if(!TextUtils.isEmpty(car_number)) {
            tvCarNumber.setText("车牌号："+car_number);
        }else{
            hide();
        }
        EventBus.getDefault().register(this);//注册EventBus
        return this;
    }

    @Override
    public void show() {
        tvCarNumber.setVisibility(View.VISIBLE);
    }

    @Override
    public void hide() {
        tvCarNumber.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideDelay(long delay) {

    }

    @Override
    public void showContinue() {

    }

    @Override
    public void stop() {
        EventBus.getDefault().unregister(this);//解注册EventBus
    }

    /**
     * @param msg
     */
    @Subscribe (threadMode = ThreadMode.MAIN)
    public void onMainEventBus(CarNumberMsg msg) {
        Log.i("xxxx","onMainEventBus...msg:"+msg.getMessage());
        if(!TextUtils.isEmpty(msg.getMessage())) {
            tvCarNumber.setText("车牌号："+ msg.getMessage());
            show();
        }
    }

    /**
     * @param msg
     */
    @Subscribe (threadMode = ThreadMode.MAIN)
    public void onMainEventBus(LocationMsg msg) {
        if(!TextUtils.isEmpty(msg.getMsg_location())) {
            tvLocation.setText("位置："+ msg.getMsg_location());
        }

        if(!TextUtils.isEmpty(msg.getMsg_location())) {
            tvSpeed.setText("速度："+ msg.getMsg_speed());
        }
    }

}
