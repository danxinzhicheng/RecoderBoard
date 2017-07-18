package com.weibu.recoderboard.viewmanager;

import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import com.weibu.recoderboard.MainActivity;
import com.weibu.recoderboard.R;

/**
 * Created by blueberry on 2017/7/14.
 * 设置按钮，主要做点击事件，延长delay秒消失及其他
 */

public class SettingViewManager implements IViewManager,View.OnClickListener{

    private MainActivity mainActivity;
    private ImageButton ibSetting;
    private Handler handler = new Handler();
    private long delay;
    public SettingViewManager(MainActivity context)
    {
        this.mainActivity = context;
    }
    @Override
    public SettingViewManager initView() {
        ibSetting = (ImageButton) mainActivity.findViewById(R.id.ib_setting);
        ibSetting.setOnClickListener(this);
        ibSetting.setAlpha(0.5f);
        return this;
    }

    @Override
    public SettingViewManager initData() {
        return this;
    }

    @Override
    public void show() {
        if(!ibSetting.isShown())
        ibSetting.setVisibility(View.VISIBLE);
    }

    @Override
    public void hide() {

        if(ibSetting.isShown())
        ibSetting.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideDelay(long delay) {
        this.delay = delay;
        handler.postDelayed(delayR,delay);
    }

    @Override
    public void showContinue() {
        handler.removeCallbacks(delayR);
        show();
        hideDelay(delay);
    }

    @Override
    public void stop() {
        handler.removeCallbacks(delayR);
    }

    private Runnable delayR = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    @Override
    public void onClick(View v) {
        if(v== ibSetting){
           mainActivity.showSettingLay();
        }

    }


}
