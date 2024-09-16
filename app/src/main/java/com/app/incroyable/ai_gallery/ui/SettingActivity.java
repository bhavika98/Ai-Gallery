package com.app.incroyable.ai_gallery.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.incroyable.ai_gallery.R;
import com.app.incroyable.ai_gallery.util.sharedPref;

import static com.app.incroyable.ai_gallery.util.sharedPref.albumStart;
import static com.app.incroyable.ai_gallery.util.sharedPref.autoPlay;
import static com.app.incroyable.ai_gallery.util.sharedPref.backButton;
import static com.app.incroyable.ai_gallery.util.sharedPref.brightSlideShow;
import static com.app.incroyable.ai_gallery.util.sharedPref.brightVideo;
import static com.app.incroyable.ai_gallery.util.sharedPref.cover;
import static com.app.incroyable.ai_gallery.util.sharedPref.loopPlay;

public class SettingActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    Activity activity = SettingActivity.this;
    CheckBox checkBackButton, checkAlbumStart, checkCover, checkAutoPlay, checkLoopPlay, checkBrightVideo, checkBrightSlideShow;
    TextView txtTimeVal, txtLoopVal, txtAnimVal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        bindToolbar();
        bindControls();
        initControls();
    }

    private void initControls() {

        checkBackButton.setChecked(Boolean.parseBoolean(sharedPref.getData(activity, backButton)));
        checkAlbumStart.setChecked(Boolean.parseBoolean(sharedPref.getData(activity, albumStart)));
        checkCover.setChecked(Boolean.parseBoolean(sharedPref.getData(activity, cover)));
        checkAutoPlay.setChecked(Boolean.parseBoolean(sharedPref.getData(activity, autoPlay)));
        checkLoopPlay.setChecked(Boolean.parseBoolean(sharedPref.getData(activity, loopPlay)));
        checkBrightSlideShow.setChecked(Boolean.parseBoolean(sharedPref.getData(activity, brightSlideShow)));
        checkBrightVideo.setChecked(Boolean.parseBoolean(sharedPref.getData(activity, brightVideo)));

        txtTimeVal.setText(sharedPref.getTimeAnim(activity));
        txtLoopVal.setText(sharedPref.getLoop(activity));
        txtAnimVal.setText(sharedPref.getAnimation(activity));
    }

    private void bindControls() {
        checkBackButton = (CheckBox) findViewById(R.id.checkBackButton);
        checkAlbumStart = (CheckBox) findViewById(R.id.checkAlbumStart);
        checkCover = (CheckBox) findViewById(R.id.checkCover);
        checkAutoPlay = (CheckBox) findViewById(R.id.checkAutoPlay);
        checkLoopPlay = (CheckBox) findViewById(R.id.checkLoopPlay);
        checkBrightVideo = (CheckBox) findViewById(R.id.checkBrightVideo);
        checkBrightSlideShow = (CheckBox) findViewById(R.id.checkBrightSlideShow);
        checkBackButton.setOnCheckedChangeListener(this);
        checkAlbumStart.setOnCheckedChangeListener(this);
        checkCover.setOnCheckedChangeListener(this);
        checkAutoPlay.setOnCheckedChangeListener(this);
        checkLoopPlay.setOnCheckedChangeListener(this);
        checkBrightVideo.setOnCheckedChangeListener(this);
        checkBrightSlideShow.setOnCheckedChangeListener(this);

        txtTimeVal = (TextView) findViewById(R.id.txtTimeVal);
        txtLoopVal = (TextView) findViewById(R.id.txtLoopVal);
        txtAnimVal = (TextView) findViewById(R.id.txtAnimVal);
    }

    private void bindToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.back_btn);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView textView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        textView.setText(getResources().getString(R.string.SettingActivity_Title));
    }

    public void setTime(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        final View view1 = layoutInflater.inflate(R.layout.layout_slideshow_time, null);
        float dpi = getResources().getDisplayMetrics().density;
        builder.setView(view1, (int) (19 * dpi), (int) (5 * dpi), (int) (14 * dpi), (int) (5 * dpi));
        builder.setTitle("Time between photos");
        builder.setCancelable(true);
        builder.setNegativeButton("Cancel", null);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        int[] ints = {R.id.one, R.id.two, R.id.three, R.id.five, R.id.seven};
        RadioButton[] radioButton = new RadioButton[5];
        for (int i = 0; i < radioButton.length; i++) {
            radioButton[i] = (RadioButton) view1.findViewById(ints[i]);
            if (radioButton[i].getText().toString().equals(sharedPref.getTimeAnim(activity)))
                radioButton[i].setChecked(true);
        }

        RadioGroup radioGroup = (RadioGroup) view1.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton radioButton = (RadioButton) view1.findViewById(checkedId);
                sharedPref.setTimeAnim(activity, radioButton.getText().toString());
                txtTimeVal.setText(sharedPref.getTimeAnim(activity));
                alertDialog.dismiss();
            }
        });
    }

    public void setLoop(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        final View view1 = layoutInflater.inflate(R.layout.layout_slideshow_loop, null);
        float dpi = getResources().getDisplayMetrics().density;
        builder.setView(view1, (int) (19 * dpi), (int) (5 * dpi), (int) (14 * dpi), (int) (5 * dpi));
        builder.setTitle("Loop");
        builder.setCancelable(true);
        builder.setNegativeButton("Cancel", null);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        int[] ints = {R.id.on, R.id.off};
        RadioButton[] radioButton = new RadioButton[2];
        for (int i = 0; i < radioButton.length; i++) {
            radioButton[i] = (RadioButton) view1.findViewById(ints[i]);
            if (radioButton[i].getText().toString().equals(sharedPref.getLoop(activity)))
                radioButton[i].setChecked(true);
        }

        RadioGroup radioGroup = (RadioGroup) view1.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton radioButton = (RadioButton) view1.findViewById(checkedId);
                sharedPref.setLoop(activity, radioButton.getText().toString());
                txtLoopVal.setText(sharedPref.getLoop(activity));
                alertDialog.dismiss();
            }
        });
    }

    @SuppressLint("RestrictedApi")
    public void setAnimation(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        final View view1 = layoutInflater.inflate(R.layout.layout_slideshow_animation, null);
        float dpi = getResources().getDisplayMetrics().density;
        builder.setView(view1, (int) (19 * dpi), (int) (5 * dpi), (int) (14 * dpi), (int) (5 * dpi));
        builder.setTitle("Animation");
        builder.setCancelable(true);
        builder.setNegativeButton("Cancel", null);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        int[] ints = {R.id.radio1, R.id.radio2, R.id.radio3, R.id.radio4, R.id.radio5
                , R.id.radio6, R.id.radio7, R.id.radio8, R.id.radio9, R.id.radio10
                , R.id.radio11, R.id.radio12, R.id.radio13, R.id.radio14, R.id.radio15};

        RadioButton[] radioButton = new RadioButton[15];
        for (int i = 0; i < radioButton.length; i++) {
            radioButton[i] = (RadioButton) view1.findViewById(ints[i]);
            if (radioButton[i].getText().toString().equals(sharedPref.getAnimation(activity)))
                radioButton[i].setChecked(true);
        }

        RadioGroup radioGroup = (RadioGroup) view1.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton radioButton = (RadioButton) view1.findViewById(checkedId);
                sharedPref.setAnimation(activity, radioButton.getText().toString());
                txtAnimVal.setText(sharedPref.getAnimation(activity));
                alertDialog.dismiss();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }

            default: {
                return false;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int viewId = buttonView.getId();

        if (viewId == R.id.checkBackButton) {
            sharedPref.setData(activity, backButton, isChecked + "");
        } else if (viewId == R.id.checkAlbumStart) {
            sharedPref.setData(activity, albumStart, isChecked + "");
        } else if (viewId == R.id.checkCover) {
            sharedPref.setData(activity, cover, isChecked + "");
        } else if (viewId == R.id.checkAutoPlay) {
            sharedPref.setData(activity, autoPlay, isChecked + "");
        } else if (viewId == R.id.checkLoopPlay) {
            sharedPref.setData(activity, loopPlay, isChecked + "");
        } else if (viewId == R.id.checkBrightSlideShow) {
            sharedPref.setData(activity, brightSlideShow, isChecked + "");
        } else if (viewId == R.id.checkBrightVideo) {
            sharedPref.setData(activity, brightVideo, isChecked + "");
        }
    }

}
