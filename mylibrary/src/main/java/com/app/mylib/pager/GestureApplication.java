package com.app.mylib.pager;

import android.app.Application;

import com.alexvasilkov.events.Events;
import com.alexvasilkov.gestures.internal.GestureDebug;

public class GestureApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Events.register(FlickrApi.class);

        GestureDebug.setDebugFps(true);
        GestureDebug.setDebugAnimator(true);
    }

}
