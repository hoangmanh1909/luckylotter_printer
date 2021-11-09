package com.mbl.lucklotterprinter.utils;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.afollestad.materialcamera.MaterialCamera;
import com.mbl.lucklotterprinter.R;

import java.io.File;
import java.util.Objects;

public class MediaUltis {
    public static void captureImage(Fragment fragment, CharSequence info, String orderCode) {
        File saveDir = null;
        if (ContextCompat.checkSelfPermission(fragment.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            saveDir = new File(Environment.getExternalStorageDirectory(), "MaterialCamera");
            saveDir.mkdirs();

        }
        MaterialCamera materialCamera = new MaterialCamera(fragment)
                .saveDir(saveDir)
                .showPortraitWarning(true)
                .allowRetry(false)
                .autoSubmit(true)
                .forceCamera1()
                .defaultToFrontFacing(false);

        materialCamera.stillShot(); // launches the Camera in stillshot mode
        materialCamera.labelRetry(R.string.cam_retry_capture);
        materialCamera.labelConfirm(R.string.cam_use_capture);
        materialCamera.start(Constants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

    }

    public static void captureImage(Fragment fragment, CharSequence info, String orderCode,boolean isAfter) {
        File saveDir = null;
        if (ContextCompat.checkSelfPermission(fragment.requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            saveDir = new File(Environment.getExternalStorageDirectory(), "MaterialCamera");
            saveDir.mkdirs();

        }
        MaterialCamera materialCamera = new MaterialCamera(fragment)
                .saveDir(saveDir)
                .showPortraitWarning(true)
                .allowRetry(true)
                .autoSubmit(true)
                .forceCamera1()
                .setInfo(info)
                .setIsAfter(isAfter)
                .setOrderCode(orderCode)
                .defaultToFrontFacing(false);

        materialCamera.stillShot(); // launches the Camera in stillshot mode
        materialCamera.labelRetry(R.string.cam_retry_capture);
        materialCamera.labelConfirm(R.string.cam_use_capture);
        materialCamera.start(Constants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

    }

    public static void captureImage(Fragment fragment) {
        File saveDir = null;
        if (ContextCompat.checkSelfPermission(fragment.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            saveDir = new File(Environment.getExternalStorageDirectory(), "MaterialCamera");
            saveDir.mkdirs();
        }
        MaterialCamera materialCamera = new MaterialCamera(fragment)
                .saveDir(saveDir)
                .showPortraitWarning(true)
                .allowRetry(false)
                .autoSubmit(true)
                .forceCamera1()
                .defaultToFrontFacing(false);

        materialCamera.stillShot(); // launches the Camera in stillshot mode
        materialCamera.labelRetry(R.string.cam_retry_capture);
        materialCamera.labelConfirm(R.string.cam_use_capture);
        materialCamera.start(Constants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        Log.d("mkdirs123", String.valueOf(materialCamera));
    }
}
