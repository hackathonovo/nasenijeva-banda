package com.example.hkozak.gss;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by HKozak on 5/20/2017.
 */

public class GSSApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }
}
