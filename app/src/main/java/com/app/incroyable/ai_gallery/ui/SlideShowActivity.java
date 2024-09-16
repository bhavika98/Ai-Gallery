package com.app.incroyable.ai_gallery.ui;

import static com.app.incroyable.ai_gallery.util.sharedPref.brightSlideShow;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.incroyable.ai_gallery.R;
import com.app.incroyable.ai_gallery.model.ThumbImage;
import com.app.incroyable.ai_gallery.util.Constants;
import com.app.incroyable.ai_gallery.util.ScreenBrightness;
import com.app.incroyable.ai_gallery.util.sharedPref;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SlideShowActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    Activity activity = SlideShowActivity.this;
    SliderLayout slider;
    ArrayList<ThumbImage> thumbImageArrayList = new ArrayList<>();
    ArrayList<ThumbImage> temp = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show);

        if (Boolean.valueOf(sharedPref.getData(activity, brightSlideShow)) == true)
            ScreenBrightness.brightness(255, activity);

        slider = (SliderLayout) findViewById(R.id.slider);

        String stringExtra = getIntent().getStringExtra(Constants.imgSource);
        Gson gson = new Gson();
        Type type = new TypeToken<List<ThumbImage>>() {
        }.getType();
        thumbImageArrayList = gson.fromJson(stringExtra, type);

        for (ThumbImage thumbImage : thumbImageArrayList) {
            if (thumbImage.getType() == 0) {
                temp.add(thumbImage);
                DefaultSliderView textSliderView = new DefaultSliderView(SlideShowActivity.this);
                textSliderView
                        .image(new File(thumbImage.getPath()))
                        .setScaleType(BaseSliderView.ScaleType.CenterInside)
                        .setOnSliderClickListener(this);

                textSliderView.bundle(new Bundle());
                textSliderView.getBundle().putString("extra", thumbImage.getName());
                slider.addSlider(textSliderView);
            }
        }

        String str = sharedPref.getTimeAnim(activity);
        int time = Integer.parseInt(str.substring(0, str.length() - 1)) * 1000;
        slider.setPresetTransformer(sharedPref.getAnimation(activity));
        slider.setCustomAnimation(new DescriptionAnimation());
        slider.setDuration(time);
        slider.addOnPageChangeListener(this);

        if (sharedPref.getLoop(activity).equals("On"))
            slider.startAutoCycle();
        else
            slider.stopAutoCycle();

        int position = getIntent().getExtras().getInt(Constants.imgPosition);
        for (int j = 0; j < temp.size(); j++) {
            if (thumbImageArrayList.get(position).getPath().toString().equals(temp.get(j).getPath().toString())) {
                position = j;
                break;
            }
        }
        slider.setCurrentPosition(position, true);
    }

    @Override
    public void onSliderClick(BaseSliderView baseSliderView) {

    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    protected void onStop() {
        slider.stopAutoCycle();
        super.onStop();
    }
}
