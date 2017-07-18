package com.weibu.recoderboard.viewmanager;

import android.content.Context;

import com.weibu.recoderboard.MainActivity;

/**
 * Created by blueberry on 2017/7/14.
 */

public interface IViewManager  {
    public IViewManager initView();
    public IViewManager initData();
    public void show();
    public void hide();
    public void hideDelay(long delay);
    public void showContinue();
    public void stop();
}
