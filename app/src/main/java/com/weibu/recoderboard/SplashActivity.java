package com.weibu.recoderboard;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import java.util.List;
import com.werb.permissionschecker.PermissionChecker;

/**
 * Created by blueberry on 2017/7/13.
 * 预留闪屏页，为了在6.0以上机子上测试，相机，录像权限兼容等
 */

public class SplashActivity extends BaseActivity {

    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };
    private PermissionChecker permissionChecker;

    @Override
    protected int getContentLayout() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        permissionChecker = new PermissionChecker(this); // initialize，must need
        if (permissionChecker.isLackPermissions(PERMISSIONS)) {
            permissionChecker.requestPermissions();
        } else {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionChecker.PERMISSION_REQUEST_CODE:
                if (permissionChecker.hasAllPermissionsGranted(grantResults)) {
                    startActivity(new Intent(this,MainActivity.class));
                    finish();
                } else {
                    permissionChecker.showDialog();
                }
                break;
        }
    }
}