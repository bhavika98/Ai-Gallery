package com.app.incroyable.ai_gallery.util;

import android.app.Activity;
import android.util.Log;

import com.app.incroyable.ai_gallery.R;

public class ScreenBrightness {

    public static boolean brightness(int level, Activity activity) {

        try {
            android.provider.Settings.System.putInt(
                    activity.getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS, level);

            android.provider.Settings.System.putInt(activity.getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE,
                    android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);

            android.provider.Settings.System.putInt(
                    activity.getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS,
                    level);

            return true;
        } catch (Exception e) {
            Log.e("Screen Brightness", "error changing screen brightness");
            return false;
        }
    }

    public static int checkForFolderIcon(String name) {
        int icon = R.drawable.folder_gallery_icon;

        if (name.contains("Camera")) {
            icon = R.drawable.folder_camera_icon;
        } else if (name.contains("Download")) {
            icon = R.drawable.folder_download_icon;
        } else if (name.contains("Evernote")) {
            icon = R.drawable.folder_evernote_icon;
        } else if (name.contains("Facebook")) {
            icon = R.drawable.folder_facebook_icon;
        } else if (name.contains("Foursquare")) {
            icon = R.drawable.folder_foursquare_icon;
        } else if (name.contains("Instagram")) {
            icon = R.drawable.folder_instagram_icon;
        } else if (name.contains("Path")) {
            icon = R.drawable.folder_path_icon;
        } else if (name.contains("Pinterest")) {
            icon = R.drawable.folder_pinterest_icon;
        } else if (name.contains("Screenshots")) {
            icon = R.drawable.folder_screenshot_icon;
        } else if (name.contains("Twitter")) {
            icon = R.drawable.folder_twitter_icon;
        } else if (name.contains("Vine")) {
            icon = R.drawable.folder_vine_icon;
        } else if (name.contains("WhatsApp")) {
            icon = R.drawable.folder_whatsapp_icon;
        } else {
            icon = R.drawable.folder_gallery_icon;
        }

        return icon;
    }

    public static int checkForCoverIcon(String name) {
        int icon = R.drawable.folder_gallery_icon;

        if (name.contains("Camera")) {
            icon = R.drawable.cover_camera;
        } else if (name.contains("Download")) {
            icon = R.drawable.cover_download;
        } else if (name.contains("Evernote")) {
            icon = R.drawable.cover_evernote;
        } else if (name.contains("Facebook")) {
            icon = R.drawable.cover_facebook;
        } else if (name.contains("Foursquare")) {
            icon = R.drawable.cover_foursquare;
        } else if (name.contains("Instagram")) {
            icon = R.drawable.cover_instagram;
        } else if (name.contains("Path")) {
            icon = R.drawable.cover_path;
        } else if (name.contains("Pinterest")) {
            icon = R.drawable.cover_pinterest;
        } else if (name.contains("Screenshots")) {
            icon = R.drawable.cover_screenshot;
        } else if (name.contains("Twitter")) {
            icon = R.drawable.cover_twitter;
        } else if (name.contains("Vine")) {
            icon = R.drawable.cover_vine;
        } else if (name.contains("WhatsApp")) {
            icon = R.drawable.cover_whatsapp;
        } else {
            icon = R.drawable.cover_album;
        }

        return icon;
    }
}
