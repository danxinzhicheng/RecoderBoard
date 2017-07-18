package com.weibu.recoderboard.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.weibu.recoderboard.R;
import com.weibu.recoderboard.entity.CarNumberMsg;
import com.weibu.recoderboard.entity.RecoderMsg;
import com.weibu.recoderboard.util.Constant;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by blueberry on 2017/7/14.
 * 设置主界面
 */

public class SettingPrefsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{

    private ListPreference listPreference;
    private EditTextPreference editTextPreference;

    //获取实例
    public static SettingPrefsFragment newInstance()
    {
        SettingPrefsFragment settingPrefsFragment = new SettingPrefsFragment();
        return settingPrefsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        view.setBackgroundResource(android.R.color.white);//设置背景色为白色，否则默认透明
//        view.setAlpha(0.5f);

        editTextPreference = (EditTextPreference)findPreference(Constant.prefs_key_car_number);
        listPreference = (ListPreference)findPreference(Constant.prefs_key_recoder_time);
        editTextPreference.setOnPreferenceChangeListener(this);
        listPreference.setOnPreferenceChangeListener(this);

        initPreferencesValue();//初始化设置界面参数数据
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting_preferences);
    }
    private void initPreferencesValue(){
        SharedPreferences shp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String car_number = shp.getString(Constant.prefs_key_car_number,"");
        String recoder_time = shp.getString(Constant.prefs_key_recoder_time,"");
            if (!TextUtils.isEmpty(car_number)) {
                editTextPreference.setSummary(car_number);
            }

            if(!TextUtils.isEmpty(recoder_time)) {
                int i = Integer.parseInt(recoder_time);
                String[] array = getActivity().getResources().getStringArray(R.array.recoder_time);
                listPreference.setSummary(array[i]);
                listPreference.setValue(recoder_time);
            }


    }
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
            return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        if(newValue != null){

            if(Constant.prefs_key_car_number.equals(preference.getKey())){
                String value = newValue.toString();
                if(!TextUtils.isEmpty(value)) {
                    editTextPreference.setSummary(value);
                    EventBus.getDefault().post(new CarNumberMsg(value));//EventBus发送car_no,MakrkViewManager类负责接收
                    return true;
                }

            }else if(Constant.prefs_key_recoder_time.equals(preference.getKey())){
                if(!TextUtils.isEmpty(newValue.toString())) {
                    int i = Integer.parseInt(newValue.toString());
                    String[] array = getActivity().getResources().getStringArray(R.array.recoder_time);
                    listPreference.setSummary(array[i]);
                    EventBus.getDefault().post(new RecoderMsg(newValue.toString()));
                    return true;
                }
            }
        }
        return false;
    }
}
