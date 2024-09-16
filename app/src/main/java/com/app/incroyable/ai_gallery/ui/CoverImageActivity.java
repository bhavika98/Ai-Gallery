package com.app.incroyable.ai_gallery.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.app.incroyable.ai_gallery.R;
import com.app.incroyable.ai_gallery.adapter.CoverImageAdapter;
import com.app.incroyable.ai_gallery.model.ThumbImage;
import com.app.incroyable.ai_gallery.util.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CoverImageActivity extends AppCompatActivity {

    Activity activity = CoverImageActivity.this;
    GridView gridView;
    CoverImageAdapter coverImageAdapter;
    ArrayList<ThumbImage> thumbImageArrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cover_image);

        bindToolbar();
        bindControls();

        try {
            String stringExtra = getIntent().getStringExtra(Constants.imgSource);
            Gson gson = new Gson();
            Type type = new TypeToken<List<ThumbImage>>() {
            }.getType();
            thumbImageArrayList = gson.fromJson(stringExtra, type);
        }catch (Exception e){
            e.printStackTrace();
        }

        coverImageAdapter = new CoverImageAdapter(activity, thumbImageArrayList);
        gridView.setAdapter(coverImageAdapter);
    }

    private void bindControls() {
        gridView = (GridView) findViewById(R.id.gridView);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent output = new Intent();
                output.putExtra(Constants.imgPath, thumbImageArrayList.get(position).getPath());
                output.putExtra(Constants.imgType, String.valueOf(thumbImageArrayList.get(position).getType()));
                setResult(RESULT_OK, output);
                finish();
            }
        });
    }

    private void bindToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.back_btn);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView textView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        textView.setText(getResources().getString(R.string.CoverImageActivity_Title));
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
}
