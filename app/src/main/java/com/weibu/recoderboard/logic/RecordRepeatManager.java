package com.weibu.recoderboard.logic;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.weibu.recoderboard.entity.CarNumberMsg;
import com.weibu.recoderboard.entity.RecoderMsg;
import com.weibu.recoderboard.util.Constant;
import com.weibu.recoderboard.util.MediaUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.UUID;


/**
 * Created by blueberry on 2017/7/17.
 */

public class RecordRepeatManager {
    private static RecordRepeatManager mInstance = null;
    private Activity context;
    private long durion = 25*1000,interval = 500;
    private MediaUtils mediaUtils;
    private static final int RECORD_START = 0;
    private static final int RECORD_STOP = 1;
    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case RECORD_START:
                    mediaUtils.setTargetName(UUID.randomUUID() + ".mp4");
                    mediaUtils.record();
                    mHandler.sendEmptyMessageDelayed(RECORD_STOP,durion);
                    break;
                case RECORD_STOP:
                    if( mediaUtils.isRecording()){
                        mediaUtils.stopRecordSave();//录制结束
                        mHandler.sendEmptyMessageDelayed(RECORD_START,interval);
                        Toast.makeText(context,"保存成功",Toast.LENGTH_LONG).show();

                    }else{
                        Toast.makeText(context,"保存失败",Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    };

    /**
     * @param msg
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEventBus(RecoderMsg msg) {
        Log.i("xxxx","onMainEventBus...getMsg_durion:"+msg.getMsg_durion());
        if(!TextUtils.isEmpty(msg.getMsg_durion())) {
            durion = getReateRecoderDurion(msg.getMsg_durion());
        }
    }
    private long getReateRecoderDurion(String recoder_time) {
            int i = Integer.parseInt(recoder_time);
            switch (i) {
                case 0:
                    return 1*60*1000;//1分钟
                case 1:
                    return 3*60*1000;//3分钟
                case 2:
                    return 5*60*1000;//5分钟
            }
            return durion;
    }
    public static RecordRepeatManager getInstance(Activity context) {
        if (mInstance == null) {
            synchronized (RecordRepeatManager.class) {
                if (mInstance == null) {
                    mInstance = new RecordRepeatManager(context);
                }
            }
        }
        return mInstance;
    }
    private RecordRepeatManager(Activity activity) {
        this.context = activity;
        mediaUtils = MediaUtils.getInstance(activity);
        EventBus.getDefault().register(this);
    }
    public RecordRepeatManager setRecordDurion(long recordDurion)
    {
        this.durion = recordDurion;
        return this;
    }

    /**
     * 目前必须设置为0，才能保证循环录制不漏帧，待确认。
     * @param recordInterval
     * @return
     */
    public RecordRepeatManager setRecordInterval(long recordInterval)
    {
        this.interval = recordInterval;
        return this;
    }

    public void startRecordAuto()
    {
        mHandler.sendEmptyMessageDelayed(RECORD_START,500);
    }

    public void stop()
    {
        mHandler.removeCallbacksAndMessages(null);
        EventBus.getDefault().unregister(this);
    }

}
