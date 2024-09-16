package com.app.incroyable.ai_gallery.util;

import android.app.Activity;

import java.io.File;

import static com.app.incroyable.ai_gallery.util.ImageOperations.sendBroadcastMedia;

public class DirectoryCleaner {

    private final File mFile;

    public DirectoryCleaner(File file) {
        mFile = file;
    }

    public void clean(Activity activity) {
        if (null == mFile || !mFile.exists() || !mFile.isDirectory()) return;
        for (File file : mFile.listFiles()) {
            delete(file);
            sendBroadcastMedia(activity, file);
        }
    }

    private void delete(File file) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                delete(child);
            }
        }
        file.delete();
    }
}
