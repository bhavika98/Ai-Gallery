package com.app.incroyable.ai_gallery.ui;

import static com.app.incroyable.ai_gallery.util.Constants.REQUEST_CODE_CROP_IMAGE;
import static com.app.incroyable.ai_gallery.util.Constants.goneLayout;
import static com.app.incroyable.ai_gallery.util.Constants.visibleLayout;
import static com.app.incroyable.ai_gallery.util.DataBinder.applyFilter;
import static com.app.incroyable.ai_gallery.util.DataBinder.fetchFilters;
import static com.app.incroyable.ai_gallery.util.ImageOperations.sendBroadcastMedia;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.app.incroyable.ai_gallery.R;
import com.app.incroyable.ai_gallery.adapter.FilterAdapter;
import com.app.incroyable.ai_gallery.fragment.Fragment_Empty;
import com.app.incroyable.ai_gallery.model.Filter;
import com.app.incroyable.ai_gallery.util.Constants;
import com.app.mylib.util.CustomViewPager;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import id.zelory.compressor.Compressor;
import jp.co.cyberagent.android.gpuimage.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.GPUImageGaussianBlurFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHueFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

public class PhotoEditActivity extends AppCompatActivity implements FilterAdapter.FilterCallback, SeekBar.OnSeekBarChangeListener {

    Activity activity = PhotoEditActivity.this;

    int currentLayout = 0;
    LinearLayout captureLayout;
    ImageView imageView;
    String imgPath;

    // Filter
    LinearLayout filterLayout;
    RecyclerView recyclerViewFilter;
    LinearLayoutManager linearLayoutManagerFilter;
    ArrayList<Filter> filterArrayList = new ArrayList<>();
    FilterAdapter filterAdapter;
    GPUImageView gpuImageView;
    Bitmap originalBitmap;
    public static Bitmap cropBitmap = null;
    int oldPosition = 0, newPosition = 1;
    Bitmap oldBitmap, newBitmap;

    // Adjust
    LinearLayout brightnessLayout, contrastLayout, vignetteLayout, saturationLayout, sharpLayout;
    TextView txtBrightness, txtContrast, txtVignette, txtSaturation, txtSharp;
    SeekBar brightnessSeekbar, contrastSeekbar, vignetteSeekbar, saturationSeekbar, sharpSeekbar;
    float start, end, value;
    int oldSeekbar, newSeekbar;

    Animation slideUpAnimation;
    Animation slideDownAnimation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_edit);

        bindToolbar();
        bindControls();
        bindPager();
        initControls();
    }

    private void bindToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.back_btn);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView textView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        textView.setText(getResources().getString(R.string.PhotoEditActivity_Title));
    }

    private void initControls() {

        imgPath = getIntent().getExtras().getString(Constants.imgSource);
        originalBitmap = Compressor.getDefault(activity).compressToBitmap(new File(imgPath));
        gpuImageView.setImage(originalBitmap);
        imageView.setImageBitmap(originalBitmap);
        oldBitmap = originalBitmap;
        newBitmap = originalBitmap;

        filterArrayList.clear();
        filterArrayList = fetchFilters();
        filterAdapter = new FilterAdapter(activity, filterArrayList);
        recyclerViewFilter.setAdapter(filterAdapter);

        slideUpAnimation = AnimationUtils.loadAnimation(activity, R.anim.slide_up);
        slideDownAnimation = AnimationUtils.loadAnimation(activity, R.anim.slide_down);
    }

    private void bindControls() {
        captureLayout = (LinearLayout) findViewById(R.id.captureLayout);
        imageView = (ImageView) findViewById(R.id.imageView);
        gpuImageView = (GPUImageView) findViewById(R.id.gpuImageView);

        // Filter
        recyclerViewFilter = (RecyclerView) findViewById(R.id.recyclerViewFilter);
        linearLayoutManagerFilter = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewFilter.setLayoutManager(linearLayoutManagerFilter);

        // Adjust
        filterLayout = (LinearLayout) findViewById(R.id.filterLayout);
        brightnessLayout = (LinearLayout) findViewById(R.id.brightnessLayout);
        contrastLayout = (LinearLayout) findViewById(R.id.contrastLayout);
        vignetteLayout = (LinearLayout) findViewById(R.id.vignetteLayout);
        saturationLayout = (LinearLayout) findViewById(R.id.saturationLayout);
        sharpLayout = (LinearLayout) findViewById(R.id.sharpLayout);
        txtBrightness = (TextView) findViewById(R.id.txtBrightness);
        txtContrast = (TextView) findViewById(R.id.txtContrast);
        txtVignette = (TextView) findViewById(R.id.txtVignette);
        txtSaturation = (TextView) findViewById(R.id.txtSaturation);
        txtSharp = (TextView) findViewById(R.id.txtSharp);
        brightnessSeekbar = (SeekBar) findViewById(R.id.brightnessSeekbar);
        contrastSeekbar = (SeekBar) findViewById(R.id.contrastSeekbar);
        vignetteSeekbar = (SeekBar) findViewById(R.id.vignetteSeekbar);
        saturationSeekbar = (SeekBar) findViewById(R.id.saturationSeekbar);
        sharpSeekbar = (SeekBar) findViewById(R.id.sharpSeekbar);
        brightnessSeekbar.setOnSeekBarChangeListener(this);
        contrastSeekbar.setOnSeekBarChangeListener(this);
        vignetteSeekbar.setOnSeekBarChangeListener(this);
        saturationSeekbar.setOnSeekBarChangeListener(this);
        sharpSeekbar.setOnSeekBarChangeListener(this);
    }

    private void bindPager() {
        ViewGroup tabFrameLayout = (ViewGroup) findViewById(R.id.tabFrameLayout);
        tabFrameLayout.addView(LayoutInflater.from(this).inflate(R.layout.layout_tab_top, tabFrameLayout, false));

        final CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.viewPager);
        SmartTabLayout tabLayout = (SmartTabLayout) findViewById(R.id.tabLayout);
        viewPager.setPagingEnabled(false);

        final LayoutInflater inflater = LayoutInflater.from(tabLayout.getContext());
        tabLayout.setCustomTabView(new SmartTabLayout.TabProvider() {
            @Override
            public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
                View tab = inflater.inflate(R.layout.layout_tab_bottom, container, false);
                ImageView icon = (ImageView) tab.findViewById(R.id.imageView);
                TextView text = (TextView) tab.findViewById(R.id.textView);
                switch (position) {
                    case 0:
                        icon.setImageDrawable(getResources().getDrawable(R.drawable.effect_btn));
                        text.setText("Filter");
                        break;
                    case 1:
                        icon.setImageDrawable(getResources().getDrawable(R.drawable.crop_btn));
                        text.setText("Crop");
                        break;
                    case 2:
                        icon.setImageDrawable(getResources().getDrawable(R.drawable.brightness_btn));
                        text.setText("Brightness");
                        break;
                    case 3:
                        icon.setImageDrawable(getResources().getDrawable(R.drawable.contrast_btn));
                        text.setText("Contrast");
                        break;
                    case 4:
                        icon.setImageDrawable(getResources().getDrawable(R.drawable.blur_btn));
                        text.setText("Blur");
                        break;
                    case 5:
                        icon.setImageDrawable(getResources().getDrawable(R.drawable.saturation_btn));
                        text.setText("Saturation");
                        break;
                    case 6:
                        icon.setImageDrawable(getResources().getDrawable(R.drawable.hue_btn));
                        text.setText("Hue");
                        break;
                    default:
                        throw new IllegalStateException("Invalid position: " + position);
                }
                return tab;
            }
        });

        FragmentPagerItems pages = new FragmentPagerItems(activity);
        pages.add(FragmentPagerItem.of("Fragment_Empty", Fragment_Empty.class));
        pages.add(FragmentPagerItem.of("Fragment_Empty", Fragment_Empty.class));
        pages.add(FragmentPagerItem.of("Fragment_Empty", Fragment_Empty.class));
        pages.add(FragmentPagerItem.of("Fragment_Empty", Fragment_Empty.class));
        pages.add(FragmentPagerItem.of("Fragment_Empty", Fragment_Empty.class));
        pages.add(FragmentPagerItem.of("Fragment_Empty", Fragment_Empty.class));
        pages.add(FragmentPagerItem.of("Fragment_Empty", Fragment_Empty.class));

        FragmentPagerItemAdapter fragmentPagerItemAdapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), pages);
        viewPager.setAdapter(fragmentPagerItemAdapter);
        tabLayout.setViewPager(viewPager);

        tabLayout.setOnTabClickListener(new SmartTabLayout.OnTabClickListener() {
            @Override
            public void onTabClicked(int position) {
                switch (position) {
                    case 0:
                        currentLayout = 1;
                        filterLayout.startAnimation(slideUpAnimation);
                        changeVisibility(visibleLayout, goneLayout, goneLayout, goneLayout, goneLayout, goneLayout);
                        break;

                    case 1:
                        Intent intent = new Intent(activity, CropActivity.class);
                        cropBitmap = gpuImageView.getGPUImage().getBitmapWithFilterApplied();
                        startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
                        break;

                    case 2:
                        currentLayout = 2;
                        brightnessLayout.startAnimation(slideUpAnimation);
                        oldSeekbar = brightnessSeekbar.getProgress();
                        newSeekbar = oldSeekbar;
                        changeVisibility(goneLayout, visibleLayout, goneLayout, goneLayout, goneLayout, goneLayout);
                        break;

                    case 3:
                        currentLayout = 3;
                        contrastLayout.startAnimation(slideUpAnimation);
                        oldSeekbar = contrastSeekbar.getProgress();
                        newSeekbar = oldSeekbar;
                        changeVisibility(goneLayout, goneLayout, visibleLayout, goneLayout, goneLayout, goneLayout);
                        break;

                    case 4:
                        currentLayout = 4;
                        vignetteLayout.startAnimation(slideUpAnimation);
                        oldSeekbar = vignetteSeekbar.getProgress();
                        newSeekbar = oldSeekbar;
                        changeVisibility(goneLayout, goneLayout, goneLayout, visibleLayout, goneLayout, goneLayout);
                        break;

                    case 5:
                        currentLayout = 5;
                        saturationLayout.startAnimation(slideUpAnimation);
                        oldSeekbar = saturationSeekbar.getProgress();
                        newSeekbar = oldSeekbar;
                        changeVisibility(goneLayout, goneLayout, goneLayout, goneLayout, visibleLayout, goneLayout);
                        break;

                    case 6:
                        currentLayout = 6;
                        sharpLayout.startAnimation(slideUpAnimation);
                        oldSeekbar = sharpSeekbar.getProgress();
                        newSeekbar = oldSeekbar;
                        changeVisibility(goneLayout, goneLayout, goneLayout, goneLayout, goneLayout, visibleLayout);
                        break;
                }
            }
        });
    }

    public void hideLayout(int pos) {
        switch (pos) {
            case 1:
                filterAdapter.setSelection(oldPosition);
                filterLayout.startAnimation(slideDownAnimation);
                filterLayout.setVisibility(View.GONE);
                break;

            case 2:
                brightnessLayout.startAnimation(slideDownAnimation);
                brightnessLayout.setVisibility(View.GONE);
                brightnessSeekbar.setProgress(oldSeekbar);
                break;

            case 3:
                contrastLayout.startAnimation(slideDownAnimation);
                contrastLayout.setVisibility(View.GONE);
                contrastSeekbar.setProgress(oldSeekbar);
                break;

            case 4:
                vignetteLayout.startAnimation(slideDownAnimation);
                vignetteLayout.setVisibility(View.GONE);
                vignetteSeekbar.setProgress(oldSeekbar);
                break;

            case 5:
                saturationLayout.startAnimation(slideDownAnimation);
                saturationLayout.setVisibility(View.GONE);
                saturationSeekbar.setProgress(oldSeekbar);
                break;

            case 6:
                sharpLayout.startAnimation(slideDownAnimation);
                sharpLayout.setVisibility(View.GONE);
                sharpSeekbar.setProgress(oldSeekbar);
                break;
        }
        if (currentLayout != 0) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    gpuImageView.setImage(oldBitmap);
                    imageView.setImageBitmap(oldBitmap);
                }
            }, 100);
        }
        currentLayout = 0;
    }

    public void closeLayout(View view) {
        hideLayout(currentLayout);
    }

    public void saveLayout(View view) {
        oldSeekbar = newSeekbar;
        oldPosition = newPosition;
        oldBitmap = newBitmap;
        hideLayout(currentLayout);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_CROP_IMAGE:
                if (cropBitmap != null) {
                    gpuImageView.setImage(cropBitmap);
                    imageView.setImageBitmap(cropBitmap);
                    oldBitmap = cropBitmap;
                    newBitmap = cropBitmap;
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void FilterMethod(int position) {
        gpuImageView.setFilter(applyFilter(position, activity));
        newPosition = position;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                newBitmap = gpuImageView.getGPUImage().getBitmapWithFilterApplied();
                imageView.setImageBitmap(newBitmap);
            }
        }, 100);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (itemId == R.id.menu_done) {
            captureLayout.setDrawingCacheEnabled(true);
            Bitmap bitmap = captureLayout.getDrawingCache();

            String img = imgPath.substring(0, imgPath.lastIndexOf("/") + 1) + new SimpleDateFormat("yyyyMMdd_HH_mm_ss").format(new Date()) + ".png";
            File file = new File(img);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            sendBroadcastMedia(activity, file);

            Intent output = new Intent();
            output.putExtra(Constants.imgPath, img);
            setResult(RESULT_OK, output);
            finish();

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        if (currentLayout != 0) {
            hideLayout(currentLayout);
        } else
            super.onBackPressed();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int seekBarId = seekBar.getId();

        if (seekBarId == R.id.brightnessSeekbar) {
            start = -0.5f;
            end = 0.5f;
            value = (end - start) * progress / 100.0f + start;
            List<GPUImageFilter> filters = new LinkedList<GPUImageFilter>();
            filters.add(applyFilter(oldPosition, activity));
            filters.add(new GPUImageBrightnessFilter(value));
            gpuImageView.setFilter(new GPUImageFilterGroup(filters));
            txtBrightness.setText(String.valueOf(progress));

        } else if (seekBarId == R.id.contrastSeekbar) {
            start = 0.4f;
            end = 2.0f;
            value = (end - start) * progress / 100.0f + start;
            List<GPUImageFilter> filters1 = new LinkedList<GPUImageFilter>();
            filters1.add(applyFilter(oldPosition, activity));
            filters1.add(new GPUImageContrastFilter(value));
            gpuImageView.setFilter(new GPUImageFilterGroup(filters1));
            txtContrast.setText(String.valueOf(progress));

        } else if (seekBarId == R.id.vignetteSeekbar) {
            start = 0.0f;
            end = 1.0f;
            value = (end - start) * progress / 100.0f + start;
            List<GPUImageFilter> filters2 = new LinkedList<GPUImageFilter>();
            filters2.add(applyFilter(oldPosition, activity));
            filters2.add(new GPUImageGaussianBlurFilter(value));
            gpuImageView.setFilter(new GPUImageFilterGroup(filters2));
            txtVignette.setText(String.valueOf(progress));

        } else if (seekBarId == R.id.saturationSeekbar) {
            start = 0.0f;
            end = 2.0f;
            value = (end - start) * progress / 100.0f + start;
            List<GPUImageFilter> filters3 = new LinkedList<GPUImageFilter>();
            filters3.add(applyFilter(oldPosition, activity));
            filters3.add(new GPUImageSaturationFilter(value));
            gpuImageView.setFilter(new GPUImageFilterGroup(filters3));
            txtSaturation.setText(String.valueOf(progress));

        } else if (seekBarId == R.id.sharpSeekbar) {
            start = 0.0f;
            end = 360.0f;
            value = (end - start) * progress / 100.0f + start;
            List<GPUImageFilter> filters4 = new LinkedList<GPUImageFilter>();
            filters4.add(applyFilter(oldPosition, activity));
            filters4.add(new GPUImageHueFilter(value));
            gpuImageView.setFilter(new GPUImageFilterGroup(filters4));
            txtSharp.setText(String.valueOf(progress));
        }

        newSeekbar = progress;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                newBitmap = gpuImageView.getGPUImage().getBitmapWithFilterApplied();
                imageView.setImageBitmap(newBitmap);
            }
        }, 100);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void changeVisibility(int filter, int brightness, int contrast, int vignette, int saturation, int sharp) {
        filterLayout.setVisibility(filter);
        brightnessLayout.setVisibility(brightness);
        contrastLayout.setVisibility(contrast);
        vignetteLayout.setVisibility(vignette);
        saturationLayout.setVisibility(saturation);
        sharpLayout.setVisibility(sharp);
    }
}
