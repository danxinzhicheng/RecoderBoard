package com.weibu.recoderboard.logic;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.weibu.recoderboard.util.MediaUtils;

import java.util.UUID;


/**
 * Created by blueberry on 2017/7/17.
 */

public class RecordRepeatManager {
    private static RecordRepeatManager mInstance = null;
    private Activity context;
    private long durion = 30*1000,interval = 500;
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
    }
    public RecordRepeatManager setRecordDurion(long recordDurion)
    {
        this.durion = recordDurion;
        return this;
    }
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
    }

}
