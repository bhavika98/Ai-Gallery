package com.app.incroyable.ai_gallery.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.app.incroyable.ai_gallery.model.AlbumSetting;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class sharedPref {

    public static final String MyPref = "MyPref";
    public static final String albumOrder = "albumOrder";
    public static final String upDown = "upDown";
    public static final String hideFolder = "hideFolder";
    public static final String noColumn = "noColumn";
    public static final String albumData = "albumData";

    // Settings
    public static final String backButton = "backButton";
    public static final String albumStart = "albumStart";
    public static final String cover = "cover";
    public static final String brightVideo = "brightVideo";
    public static final String autoPlay = "autoPlay";
    public static final String loopPlay = "loopPlay";
    public static final String brightSlideShow = "brightSlideShow";
    public static final String timeAnim = "timeAnim";
    public static final String loop = "loop";
    public static final String animation = "animation";

    public static String getTimeAnim(Activity activity) {
        SharedPreferences sharedpreferences = activity.getSharedPreferences(MyPref, Activity.MODE_PRIVATE);
        String value = sharedpreferences.getString(timeAnim, "3s");
        return value;
    }

    public static void setTimeAnim(Activity activity, String value) {
        SharedPreferences sharedpreferences = activity.getSharedPreferences(MyPref, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(timeAnim, value);
        editor.commit();
    }

    public static String getLoop(Activity activity) {
        SharedPreferences sharedpreferences = activity.getSharedPreferences(MyPref, Activity.MODE_PRIVATE);
        String value = sharedpreferences.getString(loop, "On");
        return value;
    }

    public static void setLoop(Activity activity, String value) {
        SharedPreferences sharedpreferences = activity.getSharedPreferences(MyPref, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(loop, value);
        editor.commit();
    }

    public static String getAnimation(Activity activity) {
        SharedPreferences sharedpreferences = activity.getSharedPreferences(MyPref, Activity.MODE_PRIVATE);
        String value = sharedpreferences.getString(animation, "Accordion");
        return value;
    }

    public static void setAnimation(Activity activity, String value) {
        SharedPreferences sharedpreferences = activity.getSharedPreferences(MyPref, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(animation, value);
        editor.commit();
    }

    public static String getAlbumOrder(Activity activity) {
        SharedPreferences sharedpreferences = activity.getSharedPreferences(MyPref, Activity.MODE_PRIVATE);
        String value = sharedpreferences.getString(albumOrder, "no");
        return value;
    }

    public static void setAlbumOrder(Activity activity, String value) {
        SharedPreferences sharedpreferences = activity.getSharedPreferences(MyPref, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(albumOrder, value);
        editor.commit();
    }

    public static String getUpDown(Activity activity) {
        SharedPreferences sharedpreferences = activity.getSharedPreferences(MyPref, Activity.MODE_PRIVATE);
        String value = sharedpreferences.getString(upDown, "no");
        return value;
    }

    public static void setUpDown(Activity activity, String value) {
        SharedPreferences sharedpreferences = activity.getSharedPreferences(MyPref, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(upDown, value);
        editor.commit();
    }

    public static void setHideFolder(Activity activity, ArrayList<String> stringList) {
        SharedPreferences sharedpreferences = activity.getSharedPreferences(MyPref, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        Set<String> value = new HashSet<String>();
        value.addAll(stringList);
        editor.putStringSet(hideFolder, value);
        editor.commit();
    }

    public static ArrayList<String> getHideFolder(Activity activity) {
        SharedPreferences sharedpreferences = activity.getSharedPreferences(MyPref, Activity.MODE_PRIVATE);
        ArrayList<String> list = null;
        Set<String> value = sharedpreferences.getStringSet(hideFolder, null);
        if (value != null)
            list = new ArrayList<String>(value);
        return list;
    }

    public static void setNoColumn(Activity activity, ArrayList<String> stringList) {
        SharedPreferences sharedpreferences = activity.getSharedPreferences(MyPref, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        Set<String> value = new HashSet<String>();
        value.addAll(stringList);
        editor.putStringSet(noColumn, value);
        editor.commit();
    }

    public static ArrayList<String> getNoColumn(Activity activity) {
        SharedPreferences sharedpreferences = activity.getSharedPreferences(MyPref, Activity.MODE_PRIVATE);
        ArrayList<String> list = null;
        Set<String> value = sharedpreferences.getStringSet(noColumn, null);
        if (value != null)
            list = new ArrayList<String>(value);
        return list;
    }

    public static void setAlbumData(Activity activity, ArrayList<AlbumSetting> albumSettingArrayList) {
        SharedPreferences sharedpreferences = activity.getSharedPreferences(MyPref, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        try {
            Gson gson = new Gson();
            String json = gson.toJson(albumSettingArrayList);
            editor.putString(albumData, json);
        } catch (Exception e) {

        }
        editor.commit();
    }

    public static ArrayList<AlbumSetting> getAlbumData(Activity activity) {
        ArrayList<AlbumSetting> albumSettingArrayList = new ArrayList<>();
        SharedPreferences sharedpreferences = activity.getSharedPreferences(MyPref, Activity.MODE_PRIVATE);
        try {
            Gson gson = new Gson();
            String json = sharedpreferences.getString(albumData, "");
            if (json.isEmpty()) {
                albumSettingArrayList = new ArrayList<>();
            } else {
                Type type = new TypeToken<List<AlbumSetting>>() {
                }.getType();
                albumSettingArrayList = gson.fromJson(json, type);
            }
        } catch (Exception e) {

        }
        return albumSettingArrayList;
    }

    public static String getData(Activity activity, String Var) {
        SharedPreferences sharedpreferences = activity.getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        String ans = sharedpreferences.getString(Var, "false");
        return ans;
    }

    public static void setData(Activity activity, String Var, String value) {
        SharedPreferences sharedpreferences = activity.getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Var, value);
        editor.commit();
    }
}
