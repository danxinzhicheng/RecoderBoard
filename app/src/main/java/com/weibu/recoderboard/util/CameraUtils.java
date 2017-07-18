package com.weibu.recoderboard.util;

import android.annotation.TargetApi;
import android.hardware.Camera;
import android.view.Window;

import java.util.Iterator;
import java.util.List;

/**
 * Created by blueberry on 2017/7/14.
 */

public class CameraUtils {
    private CameraUtils()
    {

    }

    public static int getCameraBackId() {
        return getCameraId(0);
    }

    private static int getCameraId(int type) {
        int numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();

        for(int i = 0; i < numberOfCameras; ++i) {
            Camera.getCameraInfo(i, cameraInfo);
            if(cameraInfo.facing == type) {
                return i;
            }
        }

        return -1;
    }

    public static int getCameraFrontId() {
        return getCameraId(1);
    }

    public static int getCameraDisplayOrientation(Window window, int cameraId) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = window.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch(rotation) {
            case 0:
                degrees = 0;
                break;
            case 1:
                degrees = 90;
                break;
            case 2:
                degrees = 180;
                break;
            case 3:
                degrees = 270;
        }

        int result;
        if(info.facing == 1) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }

        return result;
    }

    public static Camera.Size getCameraPictureSize(Camera camera, int nearResolution) {
        return getCameraSize(camera.getParameters().getSupportedPictureSizes(), nearResolution);
    }

    private static Camera.Size getCameraSize(List<Camera.Size> sizes, int nearResolution) {
        Camera.Size pre = null;

        Camera.Size size;
        for(Iterator var4 = sizes.iterator(); var4.hasNext(); pre = size) {
            size = (Camera.Size)var4.next();
            int pixs = size.width * size.height;
            if(pixs == nearResolution) {
                return size;
            }

            if(pixs < nearResolution) {
                if(pre == null) {
                    return size;
                }

                int preDis = pre.width * pre.height - nearResolution;
                int curDis = nearResolution - pixs;
                if(preDis <= curDis) {
                    return pre;
                }

                return size;
            }
        }

        return pre;
    }

    public static Camera.Size getCameraPreviewSize(Camera camera, int nearResolution) {
        return getCameraSize(camera.getParameters().getSupportedPreviewSizes(), nearResolution);
    }


    public static Camera.Size getCameraVideoSize(Camera camera, int nearResolution) {
            Camera.Parameters parameters = camera.getParameters();
            List<Camera.Size> sizes = parameters.getSupportedVideoSizes();
            return sizes == null?getCameraSize(parameters.getSupportedPreviewSizes(), nearResolution):getCameraSize(sizes, nearResolution);
    }

    public static Camera.Size getOptimalVideoSize(List<Camera.Size> supportedVideoSizes,
                                                  List<Camera.Size> previewSizes, int w, int h) {
        // Use a very small tolerance because we want an exact match.
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;

        // Supported video sizes list might be null, it means that we are allowed to use the preview
        // sizes
        List<Camera.Size> videoSizes;
        if (supportedVideoSizes != null) {
            videoSizes = supportedVideoSizes;
        } else {
            videoSizes = previewSizes;
        }
        Camera.Size optimalSize = null;

        // Start with max value and refine as we iterate over available video sizes. This is the
        // minimum difference between view and camera height.
        double minDiff = Double.MAX_VALUE;

        // Target view height
        int targetHeight = h;

        // Try to find a video size that matches aspect ratio and the target view size.
        // Iterate over all available sizes and pick the largest size that can fit in the view and
        // still maintain the aspect ratio.
        for (Camera.Size size : videoSizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;
            if (Math.abs(size.height - targetHeight) < minDiff && previewSizes.contains(size)) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find video size that matches the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : videoSizes) {
                if (Math.abs(size.height - targetHeight) < minDiff && previewSizes.contains(size)) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    /**
     * 通过对比得到与宽高比最接近的尺寸（如果有相同尺寸，优先选择）
     *
     * @param surfaceWidth
     *            需要被进行对比的原宽
     * @param surfaceHeight
     *            需要被进行对比的原高
     * @param preSizeList
     *            需要对比的预览尺寸列表
     * @return 得到与原宽高比例最接近的尺寸
     */
    public static Camera.Size getCloselyPreSize(int surfaceWidth, int surfaceHeight,
                                          List<Camera.Size> preSizeList) {

        //因为预览相机图像需要旋转90度,所以在找相机预览size时切换长宽
        int ReqTmpWidth = surfaceHeight;
        int ReqTmpHeight = surfaceWidth;

        // 得到与传入的宽高比最接近的size
        float reqRatio = ((float) ReqTmpWidth) / ReqTmpHeight;
        float curRatio, deltaRatio;
        float deltaRatioMin = Float.MAX_VALUE;
        Camera.Size retSize = null;
        for (Camera.Size size : preSizeList) {
            if ((size.width == ReqTmpWidth) && (size.height == ReqTmpHeight)) {
                return size;
            }
            curRatio = ((float) size.width) / size.height;
            deltaRatio = Math.abs(reqRatio - curRatio);
            if (deltaRatio < deltaRatioMin) {
                deltaRatioMin = deltaRatio;
                retSize = size;
            }
        }

        return retSize;
    }

}
