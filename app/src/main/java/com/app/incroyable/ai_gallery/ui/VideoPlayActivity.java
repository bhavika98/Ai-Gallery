package com.app.incroyable.ai_gallery.ui;

import static com.app.incroyable.ai_gallery.util.sharedPref.brightVideo;
import static com.app.incroyable.ai_gallery.util.sharedPref.loopPlay;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;
import com.app.incroyable.ai_gallery.R;
import com.app.incroyable.ai_gallery.util.Constants;
import com.app.incroyable.ai_gallery.util.ScreenBrightness;
import com.app.incroyable.ai_gallery.util.sharedPref;

public class VideoPlayActivity extends AppCompatActivity implements EasyVideoCallback {

    Activity activity = VideoPlayActivity.this;
    EasyVideoPlayer videoView;
    String videoPath = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        videoPath = getIntent().getExtras().getString(Constants.imgSource);
        bindToolbar();
        bindControls();
        if (Boolean.valueOf(sharedPref.getData(activity, brightVideo)) == true)
            ScreenBrightness.brightness(255, activity);
    }

    private void bindToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.back_btn);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView textView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        String title = videoPath.substring(videoPath.lastIndexOf("/") + 1, videoPath.length());
        textView.setText(title);
    }

    private void bindControls() {
        videoView = (EasyVideoPlayer) findViewById(R.id.videoView);
        videoView.setCallback(this);
        videoView.setSource(Uri.parse(videoPath));
        videoView.setLoop(Boolean.parseBoolean(sharedPref.getData(activity, loopPlay)));
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.pause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onStarted(EasyVideoPlayer player) {

    }

    @Override
    public void onPaused(EasyVideoPlayer player) {

    }

    @Override
    public void onPreparing(EasyVideoPlayer player) {

    }

    @Override
    public void onPrepared(EasyVideoPlayer player) {

    }

    @Override
    public void onBuffering(int percent) {

    }

    @Override
    public void onError(EasyVideoPlayer player, Exception e) {

    }

    @Override
    public void onCompletion(EasyVideoPlayer player) {

    }

    @Override
    public void onRetry(EasyVideoPlayer player, Uri source) {

    }

    @Override
    public void onSubmit(EasyVideoPlayer player, Uri source) {

    }

    @Override
    public void onClickVideoFrame(EasyVideoPlayer player) {

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
}
