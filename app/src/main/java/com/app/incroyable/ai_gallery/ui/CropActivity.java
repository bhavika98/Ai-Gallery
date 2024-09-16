package com.app.incroyable.ai_gallery.ui;

import static com.app.incroyable.ai_gallery.ui.PhotoEditActivity.cropBitmap;
import static com.app.incroyable.ai_gallery.util.Constants.normalColor;
import static com.app.incroyable.ai_gallery.util.Constants.selectedColor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.incroyable.ai_gallery.R;
import com.app.incroyable.ai_gallery.model.Crop;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;

import me.angrybyte.numberpicker.listener.OnValueChangeListener;
import me.angrybyte.numberpicker.view.ActualNumberPicker;

public class CropActivity extends AppCompatActivity implements OnValueChangeListener, CropImageView.OnSetImageUriCompleteListener, CropImageView.OnCropImageCompleteListener {

    Activity activity = CropActivity.this;

    CropImageView cropImageView;
    String selection = "0";
    RecyclerView recyclerViewCrop;
    LinearLayoutManager linearLayoutManager;
    CropAdapter cropAdapter;
    ArrayList<Crop> cropArrayList = new ArrayList<>();
    Bitmap originalBitmap;

    LinearLayout flipImage, rotateImage;
    ActualNumberPicker angleImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        bindToolbar();
        bindControls();
        clickEvents();

        //imagePath = getIntent().getExtras().getString(Constants.imgSource);
        //originalBitmap = Compressor.getDefault(activity).compressToBitmap(new File(imagePath));
        originalBitmap = cropBitmap;
        loadData();
    }

    private void clickEvents() {

        flipImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                originalBitmap = flipImg(originalBitmap);
                cropImageView.setImageBitmap(originalBitmap);
            }
        });

        rotateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImageView.rotateImage(90);
            }
        });
    }

    private void bindControls() {
        recyclerViewCrop = (RecyclerView) findViewById(R.id.recyclerViewCrop);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCrop.setLayoutManager(linearLayoutManager);
        cropImageView = (CropImageView) findViewById(R.id.cropImageView);
        cropImageView.setFixedAspectRatio(false);
        cropImageView.setAutoZoomEnabled(false);
        cropImageView.setShowCropOverlay(true);
        cropImageView.setGuidelines(CropImageView.Guidelines.ON_TOUCH);
        cropImageView.setCropShape(CropImageView.CropShape.RECTANGLE);
        cropImageView.setOnSetImageUriCompleteListener(this);
        cropImageView.setOnCropImageCompleteListener(this);

        flipImage = (LinearLayout) findViewById(R.id.flipImage);
        rotateImage = (LinearLayout) findViewById(R.id.rotateImage);
        angleImage = (ActualNumberPicker) findViewById(R.id.angleImage);
        angleImage.setListener(this);
    }

    private void bindToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.back_btn);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView textView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        textView.setText(getResources().getString(R.string.CropActivity_Title));
    }

    @Override
    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
        if (result.getError() == null) {
            originalBitmap = cropImageView.getCropShape() == CropImageView.CropShape.OVAL
                    ? CropImage.toOvalBitmap(result.getBitmap())
                    : result.getBitmap();

            cropBitmap = originalBitmap;
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Image crop failed.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
        if (error == null) {
        } else {
            AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(activity);

            alertDialogbuilder.setMessage(getResources().getString(R.string.sdcard_error));
            alertDialogbuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            alertDialogbuilder.setCancelable(false);
            alertDialogbuilder.show();
        }
    }

    @Override
    public void onValueChanged(int oldValue, int newValue) {
        cropImageView.setRotatedDegrees(newValue);
    }

    public class CropAdapter extends RecyclerView.Adapter<CropAdapter.ViewHolder> {

        ArrayList<Crop> list;
        Context context;

        public CropAdapter(Context context, ArrayList<Crop> list) {
            this.list = list;
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.adapter_crop, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {


            if (selection.equals("")) {
                holder.cropImage.setColorFilter(ContextCompat.getColor(getApplicationContext(), normalColor));
                holder.cropText.setTextColor(ContextCompat.getColor(getApplicationContext(), normalColor));
            } else {
                if (position == Integer.parseInt(selection)) {
                    holder.cropImage.setColorFilter(ContextCompat.getColor(getApplicationContext(), selectedColor));
                    holder.cropText.setTextColor(ContextCompat.getColor(getApplicationContext(), selectedColor));
                } else {
                    holder.cropImage.setColorFilter(ContextCompat.getColor(getApplicationContext(), normalColor));
                    holder.cropText.setTextColor(ContextCompat.getColor(getApplicationContext(), normalColor));
                }
            }

            holder.cropImage.setImageResource(list.get(position).getImage());
            holder.cropText.setText(list.get(position).getText());

            holder.border.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    selection = String.valueOf(position);
                    notifyDataSetChanged();

                    if (position == 0) {
                        cropImageView.setFixedAspectRatio(false);
                    } else {
                        cropImageView.setFixedAspectRatio(true);
                        cropImageView.setAspectRatio(list.get(position).getLeft(), list.get(position).getRight());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            ImageView cropImage;
            TextView cropText;
            LinearLayout border;

            public ViewHolder(View itemView) {
                super(itemView);
                cropImage = (ImageView) itemView.findViewById(R.id.cropImage);
                cropText = (TextView) itemView.findViewById(R.id.cropText);
                border = (LinearLayout) itemView.findViewById(R.id.border);
            }
        }
    }

    private void loadData() {

        cropImageView.setImageBitmap(originalBitmap);
        cropArrayList.clear();
        cropArrayList = fetchCrop();
        cropAdapter = new CropAdapter(getApplicationContext(), cropArrayList);
        recyclerViewCrop.setAdapter(cropAdapter);
    }

    private Bitmap flipImg(Bitmap bitmap) {
        Matrix m = new Matrix();
        m.preScale(-1, 1);
        Bitmap src = bitmap;
        originalBitmap = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), m, false);
        return originalBitmap;
    }

    private Bitmap rotateImg(Bitmap bitmap, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        originalBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return originalBitmap;
    }

    public static ArrayList<Crop> fetchCrop() {
        ArrayList<Crop> cropArrayList = new ArrayList<>();
        cropArrayList.add(new Crop(R.drawable.crop_original_btn, "Original", 10, 10));
        cropArrayList.add(new Crop(R.drawable.crop_1_1_btn, "1:1", 1, 1));
        cropArrayList.add(new Crop(R.drawable.crop_5_4_btn, "5:4", 5, 4));
        cropArrayList.add(new Crop(R.drawable.crop_4_5_btn, "4:5", 4, 5));
        cropArrayList.add(new Crop(R.drawable.crop_4_3_btn, "4:3", 4, 3));
        cropArrayList.add(new Crop(R.drawable.crop_3_4_btn, "3:4", 3, 4));
        cropArrayList.add(new Crop(R.drawable.crop_3_2_btn, "3:2", 3, 2));
        cropArrayList.add(new Crop(R.drawable.crop_2_3_btn, "2:3", 2, 3));
        cropArrayList.add(new Crop(R.drawable.crop_16_9_btn, "16:9", 16, 9));
        cropArrayList.add(new Crop(R.drawable.crop_9_16_btn, "9:16", 9, 16));
        return cropArrayList;
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
            cropImageView.getCroppedImageAsync();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
