package com.mbl.lucklotterprinter.app;

import androidx.multidex.MultiDexApplication;

import com.facebook.drawee.backends.pipeline.Fresco;

public class ApplicationController extends MultiDexApplication {
    static ApplicationController applicationController;

    @Override
    public void onCreate() {
        super.onCreate();
//        if (!BuildConfig.DEBUG)
//            Fabric.with(this, new Crashlytics());
        Fresco.initialize(this);

        applicationController = this;
    }
    public static ApplicationController getInstance() {
        return applicationController;
    }
}
