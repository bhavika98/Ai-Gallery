package com.app.incroyable.ai_gallery.ui;

import static com.app.incroyable.ai_gallery.R.id.imgDate;
import static com.app.incroyable.ai_gallery.R.id.imgLM;
import static com.app.incroyable.ai_gallery.R.id.imgName;
import static com.app.incroyable.ai_gallery.R.id.imgSize;
import static com.app.incroyable.ai_gallery.R.id.layDate;
import static com.app.incroyable.ai_gallery.R.id.layLM;
import static com.app.incroyable.ai_gallery.R.id.layName;
import static com.app.incroyable.ai_gallery.R.id.laySize;
import static com.app.incroyable.ai_gallery.util.BlurEffect.fastBlur;
import static com.app.incroyable.ai_gallery.util.Compare.byDate;
import static com.app.incroyable.ai_gallery.util.Compare.byDateCover;
import static com.app.incroyable.ai_gallery.util.Compare.byManual;
import static com.app.incroyable.ai_gallery.util.Compare.byName;
import static com.app.incroyable.ai_gallery.util.Constants.REQUEST_CODE_COVER_IMAGE;
import static com.app.incroyable.ai_gallery.util.Constants.REQUEST_CODE_EDIT_IMAGE;
import static com.app.incroyable.ai_gallery.util.ImageOperations.cropCenter;
import static com.app.incroyable.ai_gallery.util.ImageOperations.sendBroadcastMedia;
import static com.app.incroyable.ai_gallery.util.ScreenBrightness.checkForCoverIcon;
import static com.app.incroyable.ai_gallery.util.sharedPref.albumStart;
import static com.app.incroyable.ai_gallery.util.sharedPref.autoPlay;
import static com.app.incroyable.ai_gallery.util.sharedPref.backButton;
import static com.app.incroyable.ai_gallery.util.sharedPref.brightSlideShow;
import static com.app.incroyable.ai_gallery.util.sharedPref.brightVideo;
import static com.app.incroyable.ai_gallery.util.sharedPref.cover;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Process;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.alexvasilkov.android.commons.utils.Views;
import com.alexvasilkov.events.Events;
import com.alexvasilkov.events.Events.Failure;
import com.alexvasilkov.events.Events.Result;
import com.alexvasilkov.gestures.animation.ViewPositionAnimator;
import com.alexvasilkov.gestures.commons.DepthPageTransformer;
import com.alexvasilkov.gestures.commons.RecyclePagerAdapter;
import com.alexvasilkov.gestures.transition.GestureTransitions;
import com.alexvasilkov.gestures.transition.ViewsTransitionAnimator;
import com.alexvasilkov.gestures.transition.tracker.SimpleTracker;
import com.app.incroyable.ai_gallery.R;
import com.app.incroyable.ai_gallery.adapter.FolderAdapter;
import com.app.incroyable.ai_gallery.adapter.LocationAdapter;
import com.app.incroyable.ai_gallery.adapter.MoveAdapter;
import com.app.incroyable.ai_gallery.adapter.PhotoListAdapter;
import com.app.incroyable.ai_gallery.adapter.PhotoPagerAdapter;
import com.app.incroyable.ai_gallery.model.AlbumSetting;
import com.app.incroyable.ai_gallery.model.Folder;
import com.app.incroyable.ai_gallery.model.ImageMetaData;
import com.app.incroyable.ai_gallery.model.Move;
import com.app.incroyable.ai_gallery.model.ThumbImage;
import com.app.incroyable.ai_gallery.util.Compare;
import com.app.incroyable.ai_gallery.util.Constants;
import com.app.incroyable.ai_gallery.util.DirectoryCleaner;
import com.app.incroyable.ai_gallery.util.ImageOperations;
import com.app.incroyable.ai_gallery.util.ScreenBrightness;
import com.app.incroyable.ai_gallery.util.sharedPref;
import com.app.mylib.drawer.SlidingFragmentActivity;
import com.app.mylib.drawer.SlidingMenu;
import com.app.mylib.pager.EndlessRecyclerAdapter;
import com.app.mylib.pager.FlickrApi;
import com.app.mylib.util.RecyclerItemClickListener;
import com.bumptech.glide.Glide;
import com.drew.imaging.ImageMetadataReader;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.GpsDirectory;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.googlecode.flickrjandroid.photos.Photo;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.FilenameFilter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import id.zelory.compressor.Compressor;

public class MainActivity extends SlidingFragmentActivity implements PhotoPagerAdapter.VideoPlayCallback, FolderAdapter.DeleteCallback, View.OnClickListener, FolderAdapter.HideCallback, ViewPositionAnimator.PositionUpdateListener, PhotoListAdapter.OnPhotoListener {

    Activity activity = MainActivity.this;

    SlidingMenu slidingMenu;
    RecyclerView recyclerViewFolder;
    LinearLayoutManager linearLayoutManagerFolder;
    FolderAdapter folderAdapter;
    ArrayList<Folder> folderArrayList = new ArrayList<>();
    ArrayList<ThumbImage> thumbImageArrayList = new ArrayList<>();
    ArrayList<ThumbImage> subImageArrayList = new ArrayList<>();
    ArrayList<ThumbImage> tempImageArrayList = new ArrayList<>();
    ArrayList<String> locationArrayList = new ArrayList<>();
    ArrayList<String> locationCount = new ArrayList<>();

    ImageView imgTop, imgFolder, imgFolder1;
    TextView txtFolderName, txtTotal, txtNoData;
    RelativeLayout relHideDone, topLeftToolbar, bottomLeftToolbar;
    File fileImagePath;
    String fileName, newName, extension;
    int currentPosition, currentType;
    EditText etxtRename;
    Animation slideUpAnimation;
    Animation slideDownAnimation;
    TextView txtTotalRight;

    int[] resourceImgLayout = {imgName, imgDate, imgSize, imgLM};
    ImageView[] imgLayoutArray;
    int[] resourceRelLayout = {layName, layDate, laySize, layLM};
    RelativeLayout[] relLayoutArray;
    String currentImageOrder = "10";

    RecyclerView recyclerViewLocation;
    LinearLayoutManager linearLayoutManagerLocation;
    LocationAdapter locationAdapter;

    Uri contentUri;

    String[] projectionVideo = {
            MediaStore.Video.Media.BUCKET_ID,
            MediaStore.Video.Media.DATA
    };

    String[] projectionImage = {
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.DATA,
    };

    View bottomSheetView;
    BottomSheetDialog bottomSheetDialog;
    BottomSheetBehavior bottomSheetBehavior;
    TextView txtTitleBottomSheet, txtCurrentStorage;
    RelativeLayout phoneDir, memoryDir;
    View viewPhone, viewMemory;
    ListView listViewFolder;
    CheckBox checkboxCopy;
    ArrayList<Move> moveArrayList = new ArrayList<Move>();

    String currentOrder = "";
    int angleName, angleDate;

    private static int PAGE_SIZE = 0;
    private static final int NO_POSITION = -1;

    private ViewHolder views;
    private ViewsTransitionAnimator<Integer> animator;
    private PhotoListAdapter gridAdapter;
    private PhotoPagerAdapter pagerAdapter;
    private ViewPager.OnPageChangeListener pagerListener;

    private int savedPagerPosition = NO_POSITION;
    private int savedGridPosition = NO_POSITION;
    private int savedGridPositionFromTop;
    private int savedPhotoCount;

    public int PERMISSION_CODE = 23;
    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbar;
    AppBarLayout.LayoutParams collapsingToolbarLayoutParams;
    Toolbar toolbar;
    LinearLayout albumTop, albumBottom, albumSettingLayout, albumSettingGrid;
    RelativeLayout imgListLayout;
    TextView txtFolderNameChange, txtMonthDetails, txtAlbumSettingStorage, txtAlbumSettingGrid, txtAlbumSettingSorting;
    AlertDialog alertDialogGrid;
    int numColumns = 3, currentFolderPosition = 0;
    public static int curBrightnessValue;
    LinearLayout changeImageCover;

    ImageView imgfilterImage, imgfilterVideo, imgfilterGif;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        views = new ViewHolder(this);

        curBrightnessValue = android.provider.Settings.System.getInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, -1);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {
            sharedPref.setData(activity, cover, "true");
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }

        initDrawer();
        bindControls();
        initbottomSheet();
        bindToolbar();

        checkPermissions();

        if (savedPagerPosition != NO_POSITION) {
            onPositionUpdate(1f, false);
        }
    }

    private void executeCode() {
        initFolderMenu();
        clickEvent();
        changeColor(0);
    }

    public List<String> getStoragePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return Arrays.asList(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO
            );
        } else {
            return Arrays.asList(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            );
        }
    }

    private void checkPermissions() {
        checkStoragePermission(this, new Runnable() {
            @Override
            public void run() {
                executeCode();
            }
        });
    }

    private void checkStoragePermission(Context context, Runnable onPermissionGranted) {
        List<String> permissionsToRequest = new ArrayList<>();
        for (String permission : getStoragePermissions()) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (!permissionsToRequest.isEmpty()) {
            askPermission(context, Arrays.asList(permissionsToRequest.toArray(new String[0])), onPermissionGranted);
        } else {
            onPermissionGranted.run();
        }
    }

    public void askPermission(
            Context context,
            List<String> mainPermissions,
            Runnable onPermissionGranted
    ) {
        Dexter.withContext(context)
                .withPermissions(mainPermissions)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(@NonNull MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            onPermissionGranted.run();
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showPermissionRationale(context, null, true);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        showPermissionRationale(context, permissionToken, false);
                    }

                })
                .onSameThread()
                .check();
    }

    private void showPermissionRationale(Context context, PermissionToken token, boolean isPermanentlyDenied) {
        // Implement the method to show rationale
    }

    private void initbottomSheet() {
        bottomSheetView = getLayoutInflater().inflate(R.layout.fragment_move, null);
        bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());

        BottomSheetBehavior.BottomSheetCallback bottomSheetCallback =
                new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        switch (newState) {
                            case BottomSheetBehavior.STATE_COLLAPSED:
                                break;
                            case BottomSheetBehavior.STATE_DRAGGING:
                                break;
                            case BottomSheetBehavior.STATE_EXPANDED:
                                break;
                            case BottomSheetBehavior.STATE_HIDDEN:
                                bottomSheetDialog.dismiss();
                                break;
                            case BottomSheetBehavior.STATE_SETTLING:
                                break;
                            default:
                        }
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                    }
                };

        bottomSheetBehavior.setBottomSheetCallback(bottomSheetCallback);

        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
            }
        });

        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        txtTitleBottomSheet = (TextView) bottomSheetDialog.findViewById(R.id.txtTitleBottomSheet);
        txtCurrentStorage = (TextView) bottomSheetDialog.findViewById(R.id.txtCurrentStorage);
        txtCurrentStorage = (TextView) bottomSheetDialog.findViewById(R.id.txtCurrentStorage);
        phoneDir = (RelativeLayout) bottomSheetDialog.findViewById(R.id.phoneDir);
        memoryDir = (RelativeLayout) bottomSheetDialog.findViewById(R.id.memoryDir);
        viewPhone = (View) bottomSheetDialog.findViewById(R.id.viewPhone);
        viewMemory = (View) bottomSheetDialog.findViewById(R.id.viewMemory);
        listViewFolder = (ListView) bottomSheetDialog.findViewById(R.id.listViewFolder);
        checkboxCopy = (CheckBox) bottomSheetDialog.findViewById(R.id.checkboxCopy);
    }

    private void bindControls() {
        imgTop = (ImageView) findViewById(R.id.imgTop);
        imgFolder = (ImageView) findViewById(R.id.imgFolder);
        imgFolder1 = (ImageView) findViewById(R.id.imgFolder1);
        txtFolderName = (TextView) findViewById(R.id.txtFolderName);
        txtTotal = (TextView) findViewById(R.id.txtTotal);
        txtNoData = (TextView) findViewById(R.id.txtNoData);

        relHideDone = (RelativeLayout) findViewById(R.id.relHideDone);
        topLeftToolbar = (RelativeLayout) findViewById(R.id.topLeftToolbar);
        bottomLeftToolbar = (RelativeLayout) findViewById(R.id.bottomLeftToolbar);
        txtTotalRight = (TextView) findViewById(R.id.txtTotalRight);

        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayoutParams = (AppBarLayout.LayoutParams) collapsingToolbar.getLayoutParams();
        albumTop = (LinearLayout) findViewById(R.id.albumTop);
        albumBottom = (LinearLayout) findViewById(R.id.albumBottom);
        albumSettingLayout = (LinearLayout) findViewById(R.id.albumSettingLayout);
        imgListLayout = (RelativeLayout) findViewById(R.id.imgListLayout);
        albumSettingGrid = (LinearLayout) findViewById(R.id.albumSettingGrid);
        txtFolderNameChange = (TextView) findViewById(R.id.txtFolderNameChange);
        txtMonthDetails = (TextView) findViewById(R.id.txtMonthDetails);
        txtAlbumSettingStorage = (TextView) findViewById(R.id.txtAlbumSettingStorage);
        txtAlbumSettingGrid = (TextView) findViewById(R.id.txtAlbumSettingGrid);
        txtAlbumSettingSorting = (TextView) findViewById(R.id.txtAlbumSettingSorting);

        changeImageCover = (LinearLayout) findViewById(R.id.changeImageCover);

        imgfilterImage = (ImageView) findViewById(R.id.imgfilterImage);
        imgfilterVideo = (ImageView) findViewById(R.id.imgfilterVideo);
        imgfilterGif = (ImageView) findViewById(R.id.imgfilterGif);
    }

    private void bindToolbar() {

        toolbar = (Toolbar) findViewById(R.id.advanced_toolbar_back);
        toolbar.setTitle("Gallery");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.menu_btn);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView leftSideToolbar = (TextView) findViewById(R.id.leftSideToolbar);

        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            leftSideToolbar.setText("  " + model);
        } else {
            leftSideToolbar.setText("  " + manufacturer + " " + model);
        }
    }

    private class getLocData extends AsyncTask<String, Void, String> {

        int position;
        ProgressDialog progressDialog;

        public getLocData(int position) {
            this.position = position;
        }

        @Override
        protected String doInBackground(String... urls) {

            subImageArrayList.clear();
            for (ThumbImage thumbImage : tempImageArrayList) {
                if (thumbImage.getLoc().equals(locationArrayList.get(position))) {
                    subImageArrayList.add(thumbImage);
                }
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            slidingMenu.toggle();
            locationAdapter.setSelection(position);
            gridAdapter.notifyDataSetChanged();
            pagerAdapter.notifyDataSetChanged();
            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    private class getImgData extends AsyncTask<String, Void, String> {

        int position, pos1;
        ProgressDialog progressDialog;

        public getImgData(int position, int pos1) {
            this.position = position;
            this.pos1 = pos1;
        }

        @Override
        protected String doInBackground(String... urls) {

            subImageArrayList.clear();
            tempImageArrayList.clear();
            locationArrayList.clear();

            for (ThumbImage thumbImage : thumbImageArrayList) {
                if (thumbImage.getPosition() == position) {
                    subImageArrayList.add(thumbImage);
                    if (!thumbImage.getLoc().equals(""))
                        locationArrayList.add(thumbImage.getLoc());
                }
            }

            Compare.sortImages(Integer.parseInt(currentImageOrder), subImageArrayList);
            tempImageArrayList.addAll(subImageArrayList);

            locationCount.clear();
            locationCount = locationArrayList;
            locationArrayList = new ArrayList<String>(new LinkedHashSet<String>(locationArrayList));

            PAGE_SIZE = subImageArrayList.size();
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            recyclerViewLocation.setAdapter(locationAdapter);

            if (subImageArrayList.size() == 0)
                txtNoData.setVisibility(View.VISIBLE);
            else
                txtNoData.setVisibility(View.GONE);

            locationAdapter = new LocationAdapter(activity, locationArrayList, locationCount);
            recyclerViewLocation.setAdapter(locationAdapter);
            getTotal(pos1);
            ArrayList<ThumbImage> arrayList = new ArrayList<>();
            arrayList.addAll(subImageArrayList);
            subImageArrayList.clear();
            tempImageArrayList.clear();
            Compare.sortImages(Integer.parseInt(currentImageOrder), arrayList);
            subImageArrayList.addAll(arrayList);
            tempImageArrayList.addAll(subImageArrayList);
            folderAdapter.setSelection(pos1);
            gridAdapter.notifyDataSetChanged();
            pagerAdapter.notifyDataSetChanged();

            progressDialog.dismiss();
            slidingMenu.toggle();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    private void clickEvent() {

        recyclerViewLocation.addOnItemTouchListener(new RecyclerItemClickListener(activity, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                new getLocData(position).execute();
            }
        }));

        recyclerViewFolder.addOnItemTouchListener(new RecyclerItemClickListener(activity, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                if (folderAdapter.getClickEventStatus() == false) {
                    //addGridImages(folderArrayList.get(position).getPosition());
                    new getImgData(folderArrayList.get(position).getPosition(), position).execute();
                }
                changeColor(0);
            }
        }));

        changeImageCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Gson gson = new Gson();
                    String json = gson.toJson(subImageArrayList);
                    Intent intent = new Intent(MainActivity.this, CoverImageActivity.class);
                    intent.putExtra(Constants.imgSource, json);
                    startActivityForResult(intent, REQUEST_CODE_COVER_IMAGE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        views.imgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                LayoutInflater layoutInflater = activity.getLayoutInflater();
                final View view1 = layoutInflater.inflate(R.layout.layout_details, null);
                float dpi = getResources().getDisplayMetrics().density;
                builder.setView(view1, (int) (19 * dpi), (int) (5 * dpi), (int) (14 * dpi), (int) (5 * dpi));
                builder.setCancelable(true);
                builder.setNegativeButton("Cancel", null);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                TextView textInfo = (TextView) view1.findViewById(R.id.textInfo);

                Collections.sort(fetchImageMetaData(), new Comparator<ImageMetaData>() {
                    @Override
                    public int compare(ImageMetaData o1, ImageMetaData o2) {
                        return Integer.valueOf(o1.getId()).compareTo(o2.getId());
                    }
                });

                String imgInfo = "";
                imgInfo += "Title: " + fileName + "\n\n";

                if (currentType != 1) {
                    for (int i = 0; i < fetchImageMetaData().size(); i++) {
                        if (!fetchImageMetaData().get(i).getDesc().equals("")) {
                            if (fetchImageMetaData().get(i).getId() == 4) {
                                String[] str = fetchImageMetaData().get(i).getDesc().split(" bytes");
                                String data = ImageOperations.readableFileSize(Long.parseLong(str[0]));
                                imgInfo += fetchImageMetaData().get(i).getName() + ": " + data + "\n\n";
                            } else if (fetchImageMetaData().get(i).getId() == 0) {


                                String date = fetchImageMetaData().get(i).getDesc();
                                SimpleDateFormat spf = new SimpleDateFormat("EEE MMM dd hh:mm:ss zz yyyy");
                                Date newDate = null;
                                try {
                                    newDate = spf.parse(date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                spf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
                                date = spf.format(newDate);
                                imgInfo += fetchImageMetaData().get(i).getName() + ": " + date + "\n\n";

                            } else {
                                imgInfo += fetchImageMetaData().get(i).getName() + ": " + fetchImageMetaData().get(i).getDesc() + "\n\n";
                            }
                        }
                    }
                } else {

                    String[] projectionVideo = {
                            MediaStore.Video.Media.DURATION,
                            MediaStore.Video.Media.DATE_TAKEN,
                            MediaStore.Video.Media.RESOLUTION,
                            MediaStore.Video.Media.SIZE
                    };

                    Cursor cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projectionVideo,
                            MediaStore.MediaColumns.DATA + "=?", new String[]{String.valueOf(fileImagePath)}, null);

                    if (cursor != null) {
                        if (cursor.moveToLast()) {
                            do {
                                if (Thread.interrupted()) {
                                    return;
                                }

                                // Width-Height
                                String[] str = cursor.getString(cursor.getColumnIndex(projectionVideo[2])).split("x");

                                imgInfo += "Width: " + str[0] + "\n\n";
                                imgInfo += "Height: " + str[1] + "\n\n";

                                // Duration
                                int duration = Integer.parseInt(cursor.getString(cursor.getColumnIndex(projectionVideo[0])));
                                String Duration = String.format("%d:%d",
                                        TimeUnit.MILLISECONDS.toMinutes(duration),
                                        TimeUnit.MILLISECONDS.toSeconds(duration) -
                                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));

                                imgInfo += "Duration: " + Duration + "\n\n";

                                // File size
                                String val = cursor.getString(cursor.getColumnIndex(projectionVideo[3]));
                                String data = ImageOperations.readableFileSize(Long.parseLong(val));

                                imgInfo += "File size: " + data + "\n\n";

                                // Taken date
                                Long TimeinMilliSeccond = cursor.getLong(cursor.getColumnIndex(projectionVideo[1]));
                                String dateString = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a").format(new Date(TimeinMilliSeccond));

                                imgInfo += "Taken date: " + dateString + "\n\n";

                            } while (cursor.moveToPrevious());
                        }
                        cursor.close();
                    }
                }

                imgInfo += "Path: " + fileImagePath;
                textInfo.setText(imgInfo);
            }
        });

        txtFolderNameChange.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String path = txtAlbumSettingStorage.getText().toString().substring(0, txtAlbumSettingStorage.getText().toString().lastIndexOf("/"));
                    ImageOperations.renameFolder(activity, path, txtFolderName.getText().toString(), v.getText().toString());

                    // sharedPref Columns
                    changePath(path + "/" + txtFolderName.getText().toString(), path + "/" + v.getText().toString());

                    txtAlbumSettingStorage.setText(path + "/" + v.getText().toString());
                    txtFolderName.setText(v.getText().toString());
                    toolbar.setTitle(v.getText().toString());

                    Folder folder = folderArrayList.get(currentFolderPosition);
                    String frontImage = folder.getFrontImage().substring(folder.getFrontImage().lastIndexOf("/") + 1, folder.getFrontImage().length());

                    folderArrayList.set(currentFolderPosition, new Folder(v.getText().toString(), path + frontImage, path + "/" + v.getText().toString(),
                            folder.getFrontImage(), folder.getStorage(), folder.getTotal_Photo(), folder.getTotal_Video(), folder.getPosition(),
                            folder.isHide(), folder.getFrontThumbType()));

                    folderAdapter.notifyDataSetChanged();

                    return false;
                }
                return false;
            }
        });

        albumSettingGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                LayoutInflater layoutInflater = MainActivity.this.getLayoutInflater();
                View view1 = layoutInflater.inflate(R.layout.layout_grid_images, null);
                builder.setView(view1);
                builder.setCancelable(true);
                alertDialogGrid = builder.create();
                alertDialogGrid.show();
            }
        });

        relHideDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //bottomLeftToolbar.startAnimation(slideDownAnimation);
                bottomLeftToolbar.setVisibility(View.GONE);
                topLeftToolbar.setVisibility(View.VISIBLE);
                folderAdapter.showHiddenFolder(false);
                folderAdapter.notifyDataSetChanged();

                ArrayList<AlbumSetting> stringArrayList = new ArrayList<>();
                if (sharedPref.getAlbumData(activity) != null)
                    stringArrayList = sharedPref.getAlbumData(activity);

                for (Folder folder : folderArrayList) {
                    if (folder.isHide() == true) {
                        if (stringArrayList.size() == 0) {
                            stringArrayList.add(new AlbumSetting(folder.getFrontThumbType(), folder.getFrontImage(), folder.getPath(), 3, "10", true));
                        } else {
                            int count = 0, pos = 0;
                            for (int i = 0; i < stringArrayList.size(); i++) {
                                pos = i;
                                if (folder.getPath().equals(stringArrayList.get(i).getPath())) {
                                    count = 1;
                                    break;
                                } else
                                    count = 0;
                            }
                            if (count == 1)
                                stringArrayList.set(pos, new AlbumSetting(stringArrayList.get(pos).getImageType(), stringArrayList.get(pos).getCoverImage(), stringArrayList.get(pos).getPath(), stringArrayList.get(pos).getColumn(), stringArrayList.get(pos).getOrder(), true));
                            else
                                stringArrayList.add(new AlbumSetting(folder.getFrontThumbType(), folder.getFrontImage(), folder.getPath(), 3, "10", true));
                        }
                    } else if (folder.isHide() == false) {
                        for (int i = 0; i < stringArrayList.size(); i++) {
                            if (folder.getPath().equals(stringArrayList.get(i).getPath())) {
                                stringArrayList.set(i, new AlbumSetting(folder.getFrontThumbType(), folder.getFrontImage(), stringArrayList.get(i).getPath(), stringArrayList.get(i).getColumn(), stringArrayList.get(i).getOrder(), false));
                                break;
                            }
                        }
                    }
                }
                sharedPref.setAlbumData(activity, stringArrayList);

                for (int k = 0; k < folderArrayList.size(); k++) {
                    if (folderArrayList.get(k).isHide() == false) {
//                        getTotal(k);
//                        addGridImages(folderArrayList.get(k).getPosition());
//                        slidingMenu.toggle();
//                        folderAdapter.setSelection(k);
                        new getImgData(folderArrayList.get(k).getPosition(), k).execute();
                        break;
                    }
                }
            }
        });

        views.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentType == 0) {
                    Intent intent = new Intent(MainActivity.this, PhotoEditActivity.class);
                    intent.putExtra(Constants.imgSource, fileImagePath.toString());
                    startActivityForResult(intent, REQUEST_CODE_EDIT_IMAGE);
                } else if (currentType == 1) {
                    Intent intent = new Intent(MainActivity.this, VideoPlayActivity.class);
                    intent.putExtra(Constants.imgSource, fileImagePath.toString());
                    startActivity(intent);
                } else if (currentType == 2)
                    Toast.makeText(activity, "GIF can't be edited", Toast.LENGTH_SHORT).show();
            }
        });

        views.imgMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                bottomSheetDialog.show();

                // Internal - Environment.getExternalStorageDirectory().getAbsolutePath()
                // External - System.getenv("SECONDARY_STORAGE")

                String isSDPresent = System.getenv("SECONDARY_STORAGE") + "";
                if (!isSDPresent.equals("null"))
                    memoryDir.setVisibility(View.VISIBLE);
                else
                    memoryDir.setVisibility(View.INVISIBLE);

                storageOne();
                checkboxCopy.setChecked(false);

                phoneDir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        storageOne();
                    }
                });

                memoryDir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        storageTwo();
                    }
                });

                listViewFolder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        if (position == 0) {
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            bottomSheetDialog.dismiss();

                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            LayoutInflater layoutInflater = activity.getLayoutInflater();
                            View view1 = layoutInflater.inflate(R.layout.layout_rename, null);
                            float dpi = getResources().getDisplayMetrics().density;
                            builder.setView(view1, (int) (19 * dpi), (int) (5 * dpi), (int) (14 * dpi), (int) (5 * dpi));
                            builder.setTitle("Enter album name");
                            builder.setCancelable(false);
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String currentPath = fileImagePath.toString().substring(0, fileImagePath.toString().lastIndexOf("/"));
                                    File Directory = null;

                                    if (moveArrayList.get(position).getStorage() == 0)
                                        Directory = new File(new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath()).append(File.separator).append(etxtRename.getText().toString()).toString());
                                    else if (moveArrayList.get(position).getStorage() == 1)
                                        Directory = new File(new StringBuilder(System.getenv("SECONDARY_STORAGE")).append(File.separator).append(etxtRename.getText().toString()).toString());

                                    if (!Directory.exists())
                                        Directory.mkdir();
                                    String movePath = Directory.getAbsolutePath();

                                    if (checkboxCopy.isChecked())
                                        ImageOperations.copyFile(currentPath + "/", fileName, movePath + "/");
                                    else
                                        ImageOperations.moveFile(currentPath + "/", fileName, movePath + "/");

                                    File file = new File("file://" + Environment.getExternalStorageDirectory());
                                    sendBroadcastMedia(activity, file);

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            restartActivity();
                                        }
                                    }, 500);
                                }
                            });
                            builder.setNegativeButton("Cancel", null);

                            final AlertDialog alertDialog = builder.create();
                            alertDialog.show();

                            etxtRename = (EditText) view1.findViewById(R.id.etxtRename);
                            etxtRename.setHint("album name");

                            etxtRename.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                    if (s.length() >= 1) {
                                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                                    } else {
                                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                                    }
                                }
                            });
                        } else {
                            AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(activity);

                            alertDialogbuilder.setMessage("Are you sure want to move selected item?");
                            alertDialogbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                    bottomSheetDialog.dismiss();

                                    String movePath = moveArrayList.get(position).getPath();
                                    String currentPath = fileImagePath.toString().substring(0, fileImagePath.toString().lastIndexOf("/"));
                                    if (checkboxCopy.isChecked()) {
                                        ImageOperations.copyFile(currentPath + "/", fileName, movePath + "/");
                                        restartActivity();
                                    } else {
                                        ImageOperations.moveFile(currentPath + "/", fileName, movePath + "/");
                                        restartActivity();
                                    }

                                    File file = new File("file://" + Environment.getExternalStorageDirectory());
                                    sendBroadcastMedia(activity, file);

                                }
                            });
                            alertDialogbuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            alertDialogbuilder.setCancelable(false);
                            alertDialogbuilder.show();
                        }
                    }
                });
            }
        });

        views.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(fileImagePath));
                startActivity(intent);
            }
        });

        views.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(activity);

                alertDialogbuilder.setMessage("Delete selected item?");
                alertDialogbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ImageOperations.deleteFile(activity.getContentResolver(), fileImagePath);
                        DeleteRecord(currentPosition);
                    }
                });
                alertDialogbuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alertDialogbuilder.setCancelable(false);
                alertDialogbuilder.show();
            }
        });
    }

    private ArrayList<ImageMetaData> fetchImageMetaData() {

        int Id = 0;
        String Title = "";
        ArrayList<ImageMetaData> metaDataArrayList = new ArrayList<ImageMetaData>();
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(fileImagePath);
            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {

                    String[] str = tag.toString().split("-");
                    if (str[0].equals("File Modified Date") || str[0].equals("Image Width") || str[0].equals("Image Height") || str[0].equals("Orientation") || str[0].equals("File Size") || str[0].equals("Make") || str[0].equals("Model") || str[0].equals("Flash") || str[0].equals("Focal Length") || str[0].equals("White Balance") || str[0].equals("ISO Speed Ratings")) {
                        if (str[0].equals("File Modified Date")) {
                            Id = 0;
                            Title = "Taken date";
                        } else if (str[0].equals("Image Width")) {
                            Id = 1;
                            Title = "Width";
                        } else if (str[0].equals("Image Height")) {
                            Id = 2;
                            Title = "Height";
                        } else if (str[0].equals("Orientation")) {
                            Id = 3;
                            Title = "Orientation";
                        } else if (str[0].equals("File Size")) {
                            Id = 4;
                            Title = "File Size";
                        } else if (str[0].equals("Make")) {
                            Id = 5;
                            Title = "Maker";
                        } else if (str[0].equals("Model")) {
                            Id = 6;
                            Title = "Model";
                        } else if (str[0].equals("Flash")) {
                            Id = 7;
                            Title = "Flash";
                        } else if (str[0].equals("Focal Length")) {
                            Id = 8;
                            Title = "Focal Length";
                        } else if (str[0].equals("White Balance")) {
                            Id = 9;
                            Title = "White Balance";
                        } else if (str[0].equals("ISO Speed Ratings")) {
                            Id = 10;
                            Title = "ISO";
                        }

                        metaDataArrayList.add(new ImageMetaData(Id, Title, str[1]));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return metaDataArrayList;
    }

    public void relOrder(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater layoutInflater = MainActivity.this.getLayoutInflater();
        View view1 = layoutInflater.inflate(R.layout.layout_order_album, null);
        builder.setView(view1);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sharedPref.setAlbumOrder(activity, currentOrder);
                setSortedData();
            }
        });
        builder.setNegativeButton("Cancel", null);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        final ImageView imgByname = (ImageView) alertDialog.findViewById(R.id.imgByname);
        final ImageView imgBydate = (ImageView) alertDialog.findViewById(R.id.imgBydate);
        final RelativeLayout layManual = (RelativeLayout) alertDialog.findViewById(R.id.layManual);
        final RelativeLayout layName = (RelativeLayout) alertDialog.findViewById(R.id.layName);
        final RelativeLayout layDate = (RelativeLayout) alertDialog.findViewById(R.id.layDate);

        if (sharedPref.getAlbumOrder(activity).equals("1")) {
            imgBydate.setVisibility(View.INVISIBLE);
            imgByname.setVisibility(View.INVISIBLE);
            layManual.setBackgroundColor(getResources().getColor(R.color.Dialog_Selection));
        } else if (sharedPref.getAlbumOrder(activity).equals("2")) {

            if (sharedPref.getUpDown(activity).equals("1")) {
                angleName = 1;
                imgByname.setRotation(0);
            } else {
                angleName = 0;
                imgByname.setRotation(180);
            }
            imgBydate.setVisibility(View.INVISIBLE);
            imgByname.setVisibility(View.VISIBLE);
            layName.setBackgroundColor(getResources().getColor(R.color.Dialog_Selection));
        } else if (sharedPref.getAlbumOrder(activity).equals("3")) {
            if (sharedPref.getUpDown(activity).equals("1")) {
                angleDate = 1;
                imgBydate.setRotation(0);
            } else {
                angleDate = 0;
                imgBydate.setRotation(180);
            }
            imgBydate.setVisibility(View.VISIBLE);
            imgByname.setVisibility(View.INVISIBLE);
            layDate.setBackgroundColor(getResources().getColor(R.color.Dialog_Selection));
        } else {
            imgBydate.setVisibility(View.INVISIBLE);
            imgByname.setVisibility(View.INVISIBLE);
            layManual.setBackgroundColor(getResources().getColor(R.color.Dialog_Selection));
        }

        layManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentOrder = "1";
                imgBydate.setVisibility(View.INVISIBLE);
                imgByname.setVisibility(View.INVISIBLE);
                layManual.setBackgroundColor(getResources().getColor(R.color.Dialog_Selection));
                layName.setBackgroundColor(Color.TRANSPARENT);
                layDate.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        layName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentOrder = "2";

                if (layName.getBackground() != null) {
                    ColorDrawable buttonColor = (ColorDrawable) layName.getBackground();
                    int colorId = buttonColor.getColor();
                    if (colorId == getResources().getColor(R.color.Dialog_Selection)) {
                        imgByname.animate().rotation(imgByname.getRotation() + 180).start();
                        angleName = (int) ((imgByname.getRotation() / 180) % 2);
                    }
                } else
                    angleName = 1;

                imgBydate.setVisibility(View.INVISIBLE);
                imgByname.setVisibility(View.VISIBLE);
                layName.setBackgroundColor(getResources().getColor(R.color.Dialog_Selection));
                layManual.setBackgroundColor(Color.TRANSPARENT);
                layDate.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        layDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentOrder = "3";

                if (layDate.getBackground() != null) {
                    ColorDrawable buttonColor = (ColorDrawable) layDate.getBackground();
                    int colorId = buttonColor.getColor();
                    if (colorId == getResources().getColor(R.color.Dialog_Selection)) {
                        imgBydate.animate().rotation(imgBydate.getRotation() + 180).start();
                        angleDate = (int) ((imgBydate.getRotation() / 180) % 2);
                    }
                } else
                    angleDate = 1;

                imgBydate.setVisibility(View.VISIBLE);
                imgByname.setVisibility(View.INVISIBLE);
                layDate.setBackgroundColor(getResources().getColor(R.color.Dialog_Selection));
                layName.setBackgroundColor(Color.TRANSPARENT);
                layManual.setBackgroundColor(Color.TRANSPARENT);
            }
        });
    }

    private class getAllData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... urls) {
            fetchGalleryData();
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (folderArrayList.size() != 0) {

                folderAdapter = new FolderAdapter(activity, folderArrayList);
                orderFolderList();
                recyclerViewFolder.setAdapter(folderAdapter);

                subImageArrayList.clear();
                tempImageArrayList.clear();
                locationArrayList.clear();

                int showPos = 0;
                for (int i = 0; i < folderArrayList.size(); i++) {
                    if (folderArrayList.get(i).isHide() == false) {
                        showPos = i;
                        break;
                    }
                }

                for (ThumbImage thumbImage : thumbImageArrayList) {
                    if (thumbImage.getPosition() == folderArrayList.get(showPos).getPosition()) {
                        subImageArrayList.add(thumbImage);
                        if (!thumbImage.getLoc().equals(""))
                            locationArrayList.add(thumbImage.getLoc());
                    }
                }
                locationCount.clear();
                locationCount = locationArrayList;
                locationArrayList = new ArrayList<String>(new LinkedHashSet<String>(locationArrayList));
                tempImageArrayList.addAll(subImageArrayList);

                getTotal(showPos);
                ArrayList<ThumbImage> arrayList = new ArrayList<>();
                arrayList.addAll(subImageArrayList);
                subImageArrayList.clear();
                tempImageArrayList.clear();
                Compare.sortImages(Integer.parseInt(currentImageOrder), arrayList);
                subImageArrayList.addAll(arrayList);
                tempImageArrayList.addAll(subImageArrayList);
                folderAdapter.setSelection(showPos);

                if (Boolean.valueOf(sharedPref.getData(activity, albumStart)) == true)
                    if (slidingMenu.isMenuShowing() == false)
                        slidingMenu.toggle();

                locationAdapter = new LocationAdapter(activity, locationArrayList, locationCount);
                recyclerViewLocation.setAdapter(locationAdapter);

                initGrid();
                initPager();
                initAnimator();
            }
            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    public String getLoc(String path) {
        String getOutput = "";
        Metadata metadata = null;
        try {
            File file = new File(path);
            metadata = ImageMetadataReader.readMetadata(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (metadata != null) {
            Collection<GpsDirectory> gpsDirectories = metadata.getDirectoriesOfType(GpsDirectory.class);
            for (GpsDirectory gpsDirectory : gpsDirectories) {
                GeoLocation geoLocation = gpsDirectory.getGeoLocation();
                if (geoLocation != null && !geoLocation.isZero()) {

                    Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(geoLocation.getLatitude(), geoLocation.getLongitude(), 1);
                        if (addresses != null) {
                            String cityName = addresses.get(0).getAddressLine(0);
                            if (!cityName.equals(""))
                                getOutput = cityName;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
        return getOutput;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_COVER_IMAGE:
                String coverImg = data.getExtras().getString(Constants.imgPath);
                String imgType = data.getExtras().getString(Constants.imgType);
                setCoverImage(coverImg, Integer.parseInt(imgType));
                break;

            case REQUEST_CODE_EDIT_IMAGE:

                if (!animator.isLeaving()) {
                    animator.exit(true);
                }

                String editedImg = data.getExtras().getString(Constants.imgPath);

                Folder folder = folderArrayList.get(currentFolderPosition);
                folderArrayList.set(currentFolderPosition, new Folder(
                        folder.getName(),
                        folder.getDate(),
                        folder.getPath(),
                        folder.getFrontImage(),
                        folder.getStorage(),
                        folder.getTotal_Photo() + 1,
                        folder.getTotal_Video(),
                        folder.getPosition(),
                        folder.isHide(),
                        folder.getFrontThumbType()
                ));

                int video = folder.getTotal_Video();
                int img = folder.getTotal_Photo() + 1;
                String txtVideo = "", txtImage = "";
                if (video != 0 && img != 0) {
                    if (video == 1)
                        txtVideo = "video";
                    else
                        txtVideo = "videos";

                    if (img == 1)
                        txtImage = "photo";
                    else
                        txtImage = "photos";

                    txtTotal.setText(img + " " + txtImage + " - " + video + " " + txtVideo);
                    txtTotalRight.setText("(" + String.valueOf(img + video) + ")");
                } else {
                    if (img != 0) {
                        if (img == 1)
                            txtImage = "photo";
                        else
                            txtImage = "photos";
                        txtTotal.setText(img + " " + txtImage);
                        txtTotalRight.setText("(" + String.valueOf(img) + ")");
                    } else {
                        if (video == 1)
                            txtVideo = "video";
                        else
                            txtVideo = "videos";
                        txtTotal.setText(video + " " + txtVideo);
                        txtTotalRight.setText("(" + String.valueOf(video) + ")");
                    }
                }

                File file1 = new File(editedImg);
                Date lastModDate = new Date(file1.lastModified());

                String imgName = editedImg.substring(editedImg.lastIndexOf("/") + 1, editedImg.length());

                long length = file1.length();
                length = length / 1024;

                thumbImageArrayList.add(new ThumbImage(
                        "",
                        editedImg,
                        String.valueOf(lastModDate),
                        imgName,
                        "0",
                        length,
                        0,
                        folder.getPosition(),
                        thumbImageArrayList.size()
                ));

                subImageArrayList.add(thumbImageArrayList.get(thumbImageArrayList.size() - 1));
                tempImageArrayList.add(thumbImageArrayList.get(thumbImageArrayList.size() - 1));

                ArrayList<ThumbImage> arrayList = new ArrayList<>();
                arrayList.addAll(subImageArrayList);

                subImageArrayList.clear();
                tempImageArrayList.clear();

                Compare.sortImages(Integer.parseInt(currentImageOrder), arrayList);
                subImageArrayList.addAll(arrayList);
                tempImageArrayList.addAll(subImageArrayList);

                folderAdapter.notifyDataSetChanged();
                gridAdapter.notifyDataSetChanged();
                pagerAdapter.notifyDataSetChanged();

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setCoverImage(String coverImg, int imgType) {
        ArrayList<AlbumSetting> stringArrayList = new ArrayList<>();
        if (sharedPref.getAlbumData(activity) != null)
            stringArrayList = sharedPref.getAlbumData(activity);

        String path = txtAlbumSettingStorage.getText().toString();

        int count = 0, pos = 0;
        if (stringArrayList.size() == 0)
            stringArrayList.add(new AlbumSetting(imgType, coverImg, path, 3, "10", false));
        else {
            for (int i = 0; i < stringArrayList.size(); i++) {

                pos = i;
                if (stringArrayList.get(i).getPath().equals(path)) {
                    count = 1;
                    break;
                } else
                    count = 0;
            }
            if (count == 1)
                stringArrayList.set(pos, new AlbumSetting(imgType, coverImg, path, stringArrayList.get(pos).getColumn(), stringArrayList.get(pos).getOrder(), stringArrayList.get(pos).isHide()));
            else
                stringArrayList.add(new AlbumSetting(imgType, coverImg, path, 3, "10", false));
        }
        sharedPref.setAlbumData(activity, stringArrayList);

        Bitmap bmThumbnail;

        if (imgType == 1)
            bmThumbnail = ThumbnailUtils.createVideoThumbnail(coverImg, MediaStore.Video.Thumbnails.MINI_KIND);
        else {
            bmThumbnail = Compressor.getDefault(activity).compressToBitmap(new File(coverImg));
        }

        bmThumbnail = cropCenter(bmThumbnail);
        bmThumbnail = fastBlur(bmThumbnail);
        imgTop.setImageBitmap(bmThumbnail);

        Folder folder = folderArrayList.get(currentFolderPosition);
        folderArrayList.set(currentFolderPosition, new Folder(
                folder.getName(),
                folder.getDate(),
                folder.getPath(),
                coverImg,
                folder.getStorage(),
                folder.getTotal_Photo(),
                folder.getTotal_Video(),
                folder.getPosition(),
                folder.isHide(),
                imgType
        ));
        folderAdapter.notifyDataSetChanged();
    }

    public void AlbumSettingSorting(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater layoutInflater = MainActivity.this.getLayoutInflater();
        View view1 = layoutInflater.inflate(R.layout.layout_order_album_setting, null);
        builder.setView(view1);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < imgLayoutArray.length; i++) {
                    if (imgLayoutArray[i].getVisibility() == View.VISIBLE) {
                        String order = i + "" + (int) ((imgLayoutArray[i].getRotation() / 180) % 2);
                        setOrderImage(order);
                        currentImageOrder = order;
                        break;
                    }
                }
                Compare.sortImages(Integer.parseInt(currentImageOrder), subImageArrayList);
                gridAdapter.notifyDataSetChanged();
                pagerAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Cancel", null);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        imgLayoutArray = new ImageView[4];
        relLayoutArray = new RelativeLayout[4];

        for (int i = 0; i < imgLayoutArray.length; i++) {
            imgLayoutArray[i] = (ImageView) alertDialog.findViewById(resourceImgLayout[i]);
            relLayoutArray[i] = (RelativeLayout) alertDialog.findViewById(resourceRelLayout[i]);
            relLayoutArray[i].setOnClickListener(this);
        }

        String number = String.valueOf(currentImageOrder);
        int[] no = new int[number.length()];
        for (int i = 0; i < number.length(); i++) {
            no[i] = Character.digit(number.charAt(i), 10);
        }

        changeVisibility(no[0]);
        if (no[1] == 1)
            imgLayoutArray[no[0]].animate().rotation(imgLayoutArray[no[0]].getRotation() + 180).start();
    }

    private void setOrderImage(String order) {

        ArrayList<AlbumSetting> stringArrayList = new ArrayList<>();
        if (sharedPref.getAlbumData(activity) != null)
            stringArrayList = sharedPref.getAlbumData(activity);

        String path = txtAlbumSettingStorage.getText().toString();
        String frontImg = folderArrayList.get(currentFolderPosition).getFrontImage();
        int imgType = folderArrayList.get(currentFolderPosition).getFrontThumbType();

        int count = 0, pos = 0;
        if (stringArrayList.size() == 0)
            stringArrayList.add(new AlbumSetting(imgType, frontImg, path, 3, order, false));
        else {
            for (int i = 0; i < stringArrayList.size(); i++) {

                pos = i;
                if (stringArrayList.get(i).getPath().equals(path)) {
                    count = 1;
                    break;
                } else
                    count = 0;
            }
            if (count == 1)
                stringArrayList.set(pos, new AlbumSetting(stringArrayList.get(pos).getImageType(), stringArrayList.get(pos).getCoverImage(), stringArrayList.get(pos).getPath(), stringArrayList.get(pos).getColumn(), order, stringArrayList.get(pos).isHide()));
            else
                stringArrayList.add(new AlbumSetting(imgType, frontImg, path, 3, order, false));
        }
        sharedPref.setAlbumData(activity, stringArrayList);
        setSortingText(order);
    }

    private void setSortingText(String order) {
        if (order.equals("00"))
            txtAlbumSettingSorting.setText("By name asc");
        else if (order.equals("01"))
            txtAlbumSettingSorting.setText("By name desc");
        else if (order.equals("10"))
            txtAlbumSettingSorting.setText("By date asc");
        else if (order.equals("11"))
            txtAlbumSettingSorting.setText("By date desc");
        else if (order.equals("20"))
            txtAlbumSettingSorting.setText("By size asc");
        else if (order.equals("21"))
            txtAlbumSettingSorting.setText("By size desc");
        else if (order.equals("30"))
            txtAlbumSettingSorting.setText("Last modified asc");
        else if (order.equals("31"))
            txtAlbumSettingSorting.setText("Last modified desc");
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        if (viewId == R.id.layName) {
            changeVisibility(0);
        } else if (viewId == R.id.layDate) {
            changeVisibility(1);
        } else if (viewId == R.id.laySize) {
            changeVisibility(2);
        } else if (viewId == R.id.layLM) {
            changeVisibility(3);
        }
    }

    private void changeVisibility(int pos) {
        for (int i = 0; i < relLayoutArray.length; i++) {
            if (i == pos) {
                if (relLayoutArray[i].getBackground() != null) {
                    ColorDrawable buttonColor = (ColorDrawable) relLayoutArray[i].getBackground();
                    int colorId = buttonColor.getColor();
                    if (colorId == getResources().getColor(R.color.Dialog_Selection)) {
                        imgLayoutArray[i].animate().rotation(imgLayoutArray[i].getRotation() + 180).start();
                    }
                }
                relLayoutArray[i].setBackgroundColor(getResources().getColor(R.color.Dialog_Selection));
                imgLayoutArray[i].setVisibility(View.VISIBLE);
            } else {
                relLayoutArray[i].setBackgroundColor(Color.TRANSPARENT);
                imgLayoutArray[i].setVisibility(View.GONE);
            }
        }
    }

    public void relEdit(View view) {
        //bottomLeftToolbar.startAnimation(slideUpAnimation);
        bottomLeftToolbar.setVisibility(View.VISIBLE);
        topLeftToolbar.setVisibility(View.GONE);
        folderAdapter.showHiddenFolder(true);
        folderAdapter.notifyDataSetChanged();
    }

    private void setSortedData() {
        String current = folderAdapter.getCurrentData();
        if (currentOrder.equals("1") || currentOrder.equals("no")) {
            folderArrayList = byManual(folderArrayList);
        } else if (currentOrder.equals("2")) {
            sharedPref.setUpDown(activity, String.valueOf(angleName));
            folderArrayList = byName(angleName, folderArrayList);
        } else if (currentOrder.equals("3")) {
            sharedPref.setUpDown(activity, String.valueOf(angleDate));
            folderArrayList = byDate(angleDate, folderArrayList);
        }
        for (int i = 0; i < folderArrayList.size(); i++) {
            if (folderArrayList.get(i).getPath().equals(current))
                folderAdapter.setSelection(i);
        }
        folderAdapter.notifyDataSetChanged();
    }

    private void restartActivity() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    private void storageOne() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            txtCurrentStorage.setText("  " + model);
        } else {
            txtCurrentStorage.setText("  " + manufacturer + " " + model);
        }

        viewPhone.setVisibility(View.VISIBLE);
        viewMemory.setVisibility(View.INVISIBLE);

        moveArrayList.clear();
        moveArrayList.add(new Move("", "New Album", R.drawable.folder_icon, 0));
        for (Folder folder : folderArrayList) {
            if (folder.getStorage() == 0) {
                String folderName = fileImagePath.toString().substring(0, fileImagePath.toString().lastIndexOf("/"));
                if (!folder.getPath().equals(folderName))
                    moveArrayList.add(new Move(folder.getPath(), folder.getName(), R.drawable.folder_icon, 0));
            }
        }
        listViewFolder.setAdapter(new MoveAdapter(activity, moveArrayList));
    }

    private void storageTwo() {

        String txt = System.getenv("SECONDARY_STORAGE") + "";
        txtCurrentStorage.setText(txt);

        viewPhone.setVisibility(View.INVISIBLE);
        viewMemory.setVisibility(View.VISIBLE);

        moveArrayList.clear();
        moveArrayList.add(new Move("", "New Album", R.drawable.folder_icon, 1));
        for (Folder folder : folderArrayList) {
            if (folder.getStorage() == 1) {
                String folderName = fileImagePath.toString().substring(0, fileImagePath.toString().lastIndexOf("/"));
                if (!folder.getPath().equals(folderName))
                    moveArrayList.add(new Move(folder.getPath(), folder.getName(), R.drawable.folder_icon, 1));
            }
        }
        listViewFolder.setAdapter(new MoveAdapter(activity, moveArrayList));
    }

    private void initFolderMenu() {
        recyclerViewFolder = (RecyclerView) findViewById(R.id.recyclerViewFolder);
        linearLayoutManagerFolder = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerViewFolder.setLayoutManager(linearLayoutManagerFolder);

        recyclerViewLocation = (RecyclerView) findViewById(R.id.recyclerViewLocation);
        linearLayoutManagerLocation = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerViewLocation.setLayoutManager(linearLayoutManagerLocation);

        new getAllData().execute();
    }

    private void orderFolderList() {
        String currentOrder = sharedPref.getAlbumOrder(activity);
        if (currentOrder.equals("1") || currentOrder.equals("no")) {
            folderArrayList = byManual(folderArrayList);
        } else if (currentOrder.equals("2")) {
            int value = Integer.parseInt(sharedPref.getUpDown(activity));
            folderArrayList = byName(value, folderArrayList);
        } else if (currentOrder.equals("3")) {
            int value = Integer.parseInt(sharedPref.getUpDown(activity));
            folderArrayList = byDate(value, folderArrayList);
        }
    }

    private void initDrawer() {
        slidingMenu = getSlidingMenu();
        slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        slidingMenu.setShadowDrawable(R.drawable.shadowleft);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        setBehindContentView(R.layout.layout_left);
        getSlidingMenu().setMode(SlidingMenu.LEFT_RIGHT);
        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        getSlidingMenu().setSecondaryMenu(R.layout.layout_right);
        getSlidingMenu().setSecondaryShadowDrawable(R.drawable.shadowright);
    }

    @Override
    public void onBackPressed() {
        if (!animator.isLeaving()) {
            animator.exit(true);
        } else {
            if (toolbar.getVisibility() == View.GONE) {
                toolbar.setVisibility(View.VISIBLE);
                albumTop.setVisibility(View.VISIBLE);
                albumBottom.setVisibility(View.GONE);
                imgListLayout.setVisibility(View.VISIBLE);
                albumSettingLayout.setVisibility(View.GONE);
                slidingMenu.setSlidingEnabled(true);
                collapsingToolbarLayoutParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
                collapsingToolbar.setLayoutParams(collapsingToolbarLayoutParams);
            } else if (Boolean.valueOf(sharedPref.getData(activity, backButton)) == true) {
                if (slidingMenu.isMenuShowing())
                    super.onBackPressed();
                else
                    slidingMenu.toggle();
            } else if (Boolean.valueOf(sharedPref.getData(activity, backButton)) == false) {
                if (slidingMenu.isMenuShowing())
                    slidingMenu.toggle();
                else
                    super.onBackPressed();
            } else
                super.onBackPressed();
        }
    }

    public void changeColor(int pos) {
        switch (pos) {
            case 0:
                imgfilterImage.setColorFilter(ContextCompat.getColor(activity, R.color.white));
                imgfilterVideo.setColorFilter(ContextCompat.getColor(activity, R.color.white));
                imgfilterGif.setColorFilter(ContextCompat.getColor(activity, R.color.white));
                break;

            case 1:
                imgfilterImage.setColorFilter(ContextCompat.getColor(activity, R.color.Header_Color));
                imgfilterVideo.setColorFilter(ContextCompat.getColor(activity, R.color.white));
                imgfilterGif.setColorFilter(ContextCompat.getColor(activity, R.color.white));
                break;

            case 2:
                imgfilterImage.setColorFilter(ContextCompat.getColor(activity, R.color.white));
                imgfilterVideo.setColorFilter(ContextCompat.getColor(activity, R.color.Header_Color));
                imgfilterGif.setColorFilter(ContextCompat.getColor(activity, R.color.white));
                break;

            case 3:
                imgfilterImage.setColorFilter(ContextCompat.getColor(activity, R.color.white));
                imgfilterVideo.setColorFilter(ContextCompat.getColor(activity, R.color.white));
                imgfilterGif.setColorFilter(ContextCompat.getColor(activity, R.color.Header_Color));
                break;
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            onBackPressed();
            return true;
        } else {
            return super.onKeyUp(keyCode, event);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (!slidingMenu.isMenuShowing()) {
                slidingMenu.toggle();
            }
            return true;
        } else if (item.getItemId() == R.id.menu_filter_by) {
            getSlidingMenu().showSecondaryMenu(true);
            return true;
        } else if (item.getItemId() == R.id.menu_album_setting) {
            toolbar.setVisibility(View.GONE);
            albumTop.setVisibility(View.GONE);
            albumBottom.setVisibility(View.VISIBLE);
            imgListLayout.setVisibility(View.GONE);
            albumSettingLayout.setVisibility(View.VISIBLE);
            appBarLayout.setExpanded(true);
            slidingMenu.setSlidingEnabled(false);
            collapsingToolbarLayoutParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP);
            collapsingToolbar.setLayoutParams(collapsingToolbarLayoutParams);
            return true;
        } else if (item.getItemId() == R.id.menu_slideshow) {
            int count = 0;
            for (ThumbImage thumbImage : subImageArrayList) {
                if (thumbImage.getType() == 0) {
                    count++;
                    break;
                }
            }

            if (count != 0) {
                Gson gson = new Gson();
                String json = gson.toJson(subImageArrayList);
                Intent intent = new Intent(activity, SlideShowActivity.class);
                intent.putExtra(Constants.imgSource, json);
                intent.putExtra(Constants.imgPosition, 0);
                startActivity(intent);
            } else {
                Toast.makeText(activity, "There is no image in this album", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (item.getItemId() == R.id.menu_setting) {
            startActivity(new Intent(activity, SettingActivity.class));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private boolean onOptionsItemSelectedFullMode(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.menu_rename) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            LayoutInflater layoutInflater = MainActivity.this.getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.layout_rename, null);
            float dpi = getResources().getDisplayMetrics().density;
            builder.setView(view, (int) (19 * dpi), (int) (5 * dpi), (int) (14 * dpi), (int) (5 * dpi));
            builder.setTitle("Enter new filename");
            builder.setCancelable(false);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    newName = etxtRename.getText().toString() + "." + extension;
                    int same = 0;
                    for (ThumbImage thumbImage : subImageArrayList) {
                        if (thumbImage.getName().equals(newName)) {
                            if (!newName.equals(fileName)) {
                                same++;
                            }
                        }
                    }
                    if (same == 0) {
                        renameFile(currentPosition);
                    } else {
                        Toast.makeText(activity, "Failed to rename the file or folder as it already exists", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setNegativeButton("Cancel", null);

            final AlertDialog alertDialog = builder.create();
            alertDialog.show();

            etxtRename = (EditText) view.findViewById(R.id.etxtRename);
            String[] result = fileName.split("\\.");
            extension = result[1];
            etxtRename.setText(result[0]);
            etxtRename.setSelection(result[0].length());

            etxtRename.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() >= 1) {
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                    } else {
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    }
                }
            });

            return true;
        } else if (itemId == R.id.menu_setas) {
            Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
            intent.setDataAndType(contentUri, "image/*");
            intent.putExtra("mimeType", "image/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(intent, "Set as"));
            return true;
        } else if (itemId == R.id.menu_slideshow) {
            if (subImageArrayList.get(currentPosition).getType() == 0) {
                Gson gson = new Gson();
                String json = gson.toJson(subImageArrayList);
                Intent intent1 = new Intent(activity, SlideShowActivity.class);
                intent1.putExtra(Constants.imgSource, json);
                intent1.putExtra(Constants.imgPosition, currentPosition);
                startActivity(intent1);
            } else if (subImageArrayList.get(currentPosition).getType() == 1) {
                Intent intent2 = new Intent(MainActivity.this, VideoPlayActivity.class);
                intent2.putExtra(Constants.imgSource, subImageArrayList.get(currentPosition).getPath());
                startActivity(intent2);
            } else {
                Toast.makeText(activity, "GIFs don't have any slideshow", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else {
            return false;
        }
    }

    private void onCreateOptionsMenuFullMode(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_image, menu);
    }

    private void renameFile(int position) {
        int Id = subImageArrayList.get(position).getPosition();
        String Path = subImageArrayList.get(position).getPath();
        String halfpath = subImageArrayList.get(position).getPath().substring(0, subImageArrayList.get(position).getPath().lastIndexOf("/") + 1);
        ImageOperations.renameFile(halfpath, fileName, newName);

        ThumbImage thumbImage = thumbImageArrayList.get(position);
        subImageArrayList.set(position, new ThumbImage(thumbImage.getLoc(), halfpath + newName, thumbImage.getDate(), newName, thumbImage.getDuration(), thumbImage.getSize(), thumbImage.getType(), thumbImage.getPosition(), thumbImage.getId()));
        tempImageArrayList.set(position, new ThumbImage(thumbImage.getLoc(), halfpath + newName, thumbImage.getDate(), newName, thumbImage.getDuration(), thumbImage.getSize(), thumbImage.getType(), thumbImage.getPosition(), thumbImage.getId()));

        for (int i = 0; i < thumbImageArrayList.size(); i++) {
            ThumbImage thumbImage1 = thumbImageArrayList.get(i);
            if (thumbImage1.getPosition() == Id && thumbImage1.getPath().equals(Path))
                thumbImageArrayList.set(i, new ThumbImage(thumbImage.getLoc(), halfpath + newName, thumbImage1.getDate(), newName, thumbImage1.getDuration(), thumbImage1.getSize(), thumbImage1.getType(), thumbImage1.getPosition(), thumbImage1.getId()));
        }

        for (int i = 0; i < folderArrayList.size(); i++) {
            Folder folder = folderArrayList.get(i);
            if (folder.getPosition() == Id && folder.getFrontImage().equals(Path)) {
                folderArrayList.set(i, new Folder(folder.getName(), folder.getDate(),
                        folder.getPath(), halfpath + newName, folder.getStorage(),
                        folder.getTotal_Photo(), folder.getTotal_Video(), folderArrayList.get(i).getPosition(), folderArrayList.get(i).isHide(), folderArrayList.get(i).getFrontThumbType()));
            }
        }
        fileName = newName;
        gridAdapter.notifyDataSetChanged();
        pagerAdapter.notifyDataSetChanged();
        folderAdapter.notifyDataSetChanged();
    }

    private void initGrid() {

        views.gridLayoutManager = new GridLayoutManager(activity, numColumns);
        views.grid.setLayoutManager(views.gridLayoutManager);
        views.grid.setItemAnimator(new DefaultItemAnimator());

        gridAdapter = new PhotoListAdapter(this);
        gridAdapter.setLoadingOffset(PAGE_SIZE / 2);
        gridAdapter.setCallbacks(new EndlessRecyclerAdapter.LoaderCallbacks() {
            @Override
            public boolean canLoadNextItems() {
                return gridAdapter.canLoadNext();
            }

            @Override
            public void loadNextItems() {
                int count = Math.max(savedPhotoCount, gridAdapter.getCount() + PAGE_SIZE);
                Events.create(FlickrApi.LOAD_IMAGES_EVENT).param(count).post();

            }
        });
        views.grid.setAdapter(gridAdapter);

        /*views.grid.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Toast.makeText(activity, dx + " :: " + dy, Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    private void initPager() {
        pagerAdapter = new PhotoPagerAdapter(views.pager);

        pagerListener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                onPhotoInPagerSelected(position);
            }
        };

        views.pager.setAdapter(pagerAdapter);
        views.pager.addOnPageChangeListener(pagerListener);
        views.pager.setPageTransformer(true, new DepthPageTransformer());

        views.pagerToolbar.setNavigationIcon(R.mipmap.back_btn);
        views.pagerToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View view) {
                onBackPressed();
            }
        });

        onCreateOptionsMenuFullMode(views.pagerToolbar.getMenu());

        views.pagerToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return onOptionsItemSelectedFullMode(item);
            }
        });
    }

    private void initAnimator() {
        final SimpleTracker gridTracker = new SimpleTracker() {
            @Override
            public View getViewAt(int pos) {
                RecyclerView.ViewHolder holder = views.grid.findViewHolderForLayoutPosition(pos);
                return holder == null ? null : PhotoListAdapter.getImage(holder);
            }
        };

        final SimpleTracker pagerTracker = new SimpleTracker() {
            @Override
            public View getViewAt(int pos) {
                RecyclePagerAdapter.ViewHolder holder = pagerAdapter.getViewHolder(pos);
                return holder == null ? null : PhotoPagerAdapter.getImage(holder);
            }
        };

        animator = GestureTransitions.from(views.grid, gridTracker).into(views.pager, pagerTracker);
        animator.addPositionUpdateListener(this);

        slideUpAnimation = AnimationUtils.loadAnimation(activity, R.anim.slide_up);
        slideDownAnimation = AnimationUtils.loadAnimation(activity, R.anim.slide_down);
    }

    private void onPhotoInPagerSelected(int position) {
        setCurrentPic(position);
    }

    private void DeleteRecord(int position) {
        int Id = subImageArrayList.get(position).getPosition();
        int type = subImageArrayList.get(position).getType();
        String Path = subImageArrayList.get(position).getPath();
        if (subImageArrayList.size() > position) {
            subImageArrayList.remove(position);
            tempImageArrayList.remove(position);
            gridAdapter.notifyDataSetChanged();
            pagerAdapter.notifyDataSetChanged();
        }

        for (int i = 0; i < thumbImageArrayList.size(); i++) {
            if (thumbImageArrayList.get(i).getPosition() == Id && thumbImageArrayList.get(i).getPath().equals(Path))
                thumbImageArrayList.remove(i);
        }
        for (int i = 0; i < folderArrayList.size(); i++) {
            if (folderArrayList.get(i).getPosition() == Id) {
                int img = folderArrayList.get(i).getTotal_Photo();
                int video = folderArrayList.get(i).getTotal_Video();

                if (type == 0)
                    img = img - 1;
                else
                    video = video - 1;

                String frontImage = folderArrayList.get(i).getFrontImage();
                if (position == 0) {
                    if (subImageArrayList.size() > 0) {
                        frontImage = subImageArrayList.get(0).getPath();
                        Glide.with(activity).load(frontImage).into(imgTop);
                    } else {
                        imgTop.setColorFilter(ContextCompat.getColor(activity, R.color.Header_Color));
                    }
                }

                folderArrayList.set(i, new Folder(folderArrayList.get(i).getName(), folderArrayList.get(i).getDate(),
                        folderArrayList.get(i).getPath(), frontImage, folderArrayList.get(i).getStorage(),
                        img, video, folderArrayList.get(i).getPosition(), folderArrayList.get(i).isHide(), folderArrayList.get(i).getFrontThumbType()));

                if (subImageArrayList.size() == 0) {
                    if (type == 0) {
                        txtTotal.setText(img + " " + "photo");
                        txtTotalRight.setText("(" + String.valueOf(img) + ")");
                    } else {
                        txtTotal.setText(video + " " + "video");
                        txtTotalRight.setText("(" + String.valueOf(video) + ")");
                    }
                    txtNoData.setVisibility(View.VISIBLE);
                    folderArrayList.remove(i);
                    onBackPressed();
                } else {
                    String txtVideo = "", txtImage = "";
                    if (video != 0 && img != 0) {
                        if (video == 1)
                            txtVideo = "video";
                        else
                            txtVideo = "videos";

                        if (img == 1)
                            txtImage = "photo";
                        else
                            txtImage = "photos";

                        txtTotal.setText(img + " " + txtImage + " - " + video + " " + txtVideo);
                        txtTotalRight.setText("(" + String.valueOf(img + video) + ")");
                    } else {
                        if (img != 0) {
                            if (img == 1)
                                txtImage = "photo";
                            else
                                txtImage = "photos";
                            txtTotal.setText(img + " " + txtImage);
                            txtTotalRight.setText("(" + String.valueOf(img) + ")");
                        } else {
                            if (video == 1)
                                txtVideo = "video";
                            else
                                txtVideo = "videos";
                            txtTotal.setText(video + " " + txtVideo);
                            txtTotalRight.setText("(" + String.valueOf(video) + ")");
                        }
                    }
                }
            }
        }
        folderAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPhotoClick(int position) {

        if (subImageArrayList.get(position).getType() == 1) {
            if (Boolean.valueOf(sharedPref.getData(activity, autoPlay)) == true) {
                Intent intent = new Intent(MainActivity.this, VideoPlayActivity.class);
                intent.putExtra(Constants.imgSource, subImageArrayList.get(position).getPath());
                startActivity(intent);
            } else {
                pagerAdapter.setActivated(true);
                animator.enter(position, true);
                setCurrentPic(position);
            }
        } else {
            pagerAdapter.setActivated(true);
            animator.enter(position, false);
            setCurrentPic(position);
        }
    }

    private void setCurrentPic(int position) {
        ThumbImage photo = pagerAdapter.getPhoto(position);
        if (photo == null) {
            views.pagerTitle.setText(null);
        } else {

            String date = photo.getDate();
            try {
                String newD = new SimpleDateFormat("MMM yyyy")
                        .format(new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy")
                                .parse(photo.getDate()));
                date = newD;
            } catch (ParseException e) {
                e.printStackTrace();
            }

            views.pagerTitle.setText(date);
            contentUri = Uri.parse(photo.getPath());
            fileImagePath = new File(photo.getPath());
            fileName = photo.getName();
            currentPosition = position;
            currentType = photo.getType();

            int type = photo.getType();
            if (type == 1)
                views.editImage.setImageResource(R.drawable.play_btn);
            else
                views.editImage.setImageResource(R.drawable.image_edit_btn);
        }
    }

    @Override
    public void onPositionUpdate(float position, boolean isLeaving) {
        views.pagerBackground.setVisibility(position == 0f ? View.INVISIBLE : View.VISIBLE);
        views.pagerBackground.getBackground().setAlpha((int) (255 * position));

        views.pagerToolbar.setVisibility(position == 0f ? View.INVISIBLE : View.VISIBLE);
        views.pagerToolbar.setAlpha(position);

        views.pagerTitle.setVisibility(position == 1f ? View.VISIBLE : View.INVISIBLE);
        views.viewLine.setVisibility(position == 1f ? View.VISIBLE : View.INVISIBLE);
        views.bottomLayout.setVisibility(position == 1f ? View.VISIBLE : View.INVISIBLE);

        if (isLeaving && position == 0f) {
            pagerAdapter.setActivated(false);
        }
    }

    @Result(FlickrApi.LOAD_IMAGES_EVENT)
    private void onPhotosLoaded(List<Photo> photos, boolean hasMore) {
        PAGE_SIZE = subImageArrayList.size();

        gridAdapter.setPhotos(subImageArrayList, true, activity);
        pagerAdapter.setPhotos(subImageArrayList, activity);
        gridAdapter.onNextItemsLoaded();

        // Ensure listener called for 0 position
        pagerListener.onPageSelected(views.pager.getCurrentItem());

        // Restoring saved state
        if (savedPagerPosition != NO_POSITION && savedPagerPosition < photos.size()) {
            pagerAdapter.setActivated(true);
            animator.enter(savedPagerPosition, false);
        }

        if (savedGridPosition != NO_POSITION && savedGridPosition < photos.size()) {
            ((GridLayoutManager) views.grid.getLayoutManager())
                    .scrollToPositionWithOffset(savedGridPosition, savedGridPositionFromTop);
        }
    }

    @Failure(FlickrApi.LOAD_IMAGES_EVENT)
    private void onPhotosLoadFail() {
        gridAdapter.onNextItemsError();

        // Skipping state restoration
        if (savedPagerPosition != NO_POSITION) {
            // We can't show image right now, so we should return back to list
            onPositionUpdate(0f, true);
        }
    }

    @Override
    public void HideMethod(int position) {
        Folder folder = folderArrayList.get(position);
        boolean isHide = folder.isHide();
        if (isHide)
            isHide = false;
        else
            isHide = true;

        folderArrayList.set(position, new Folder(
                folder.getName(),
                folder.getDate(),
                folder.getPath(),
                folder.getFrontImage(),
                folder.getStorage(),
                folder.getTotal_Photo(),
                folder.getTotal_Video(),
                folder.getPosition(),
                isHide,
                folder.getFrontThumbType()
        ));
        folderAdapter.notifyDataSetChanged();
    }

    @Override
    public void DeleteMethod(final int position) {

        AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(activity);

        alertDialogbuilder.setMessage("Are you sure want to delete folder?");
        alertDialogbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteFolder(position);
            }
        });
        alertDialogbuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialogbuilder.setCancelable(false);
        alertDialogbuilder.show();
    }

    public void deleteFolder(int position) {
        for (int i = 0; i < thumbImageArrayList.size(); i++) {
            if (thumbImageArrayList.get(i).getPosition() == folderArrayList.get(position).getPosition()) {
                thumbImageArrayList.remove(i);
            }
        }

        ArrayList<AlbumSetting> stringArrayList = new ArrayList<>();
        if (sharedPref.getAlbumData(activity) != null)
            stringArrayList = sharedPref.getAlbumData(activity);

        if (stringArrayList.size() != 0) {
            int count = 0, pos = 0;
            for (int i = 0; i < stringArrayList.size(); i++) {
                if (stringArrayList.get(i).getPath().equals(folderArrayList.get(position).getPath())) {
                    count = 1;
                    pos = i;
                    break;
                } else
                    count = 0;
            }
            if (count == 1) {
                stringArrayList.remove(pos);
            }
            sharedPref.setAlbumData(activity, stringArrayList);
        }

        File dir = new File(folderArrayList.get(position).getPath());
        new DirectoryCleaner(dir).clean(activity);
        dir.delete();

        folderArrayList.remove(position);
        folderAdapter.notifyDataSetChanged();

        if (Integer.parseInt(folderAdapter.getSelection()) == position) {
            for (int k = 0; k < folderArrayList.size(); k++) {
                if (folderArrayList.get(k).isHide() == false) {
//                    getTotal(k);
//                    addGridImages(folderArrayList.get(k).getPosition());
//                    folderAdapter.setSelection(k);
                    new getImgData(folderArrayList.get(position).getPosition(), position).execute();
                    break;
                }
            }
        }
    }

    @Override
    public void VideoMethod(int position) {
        if (subImageArrayList.get(position).getType() == 1) {
            Intent intent = new Intent(MainActivity.this, VideoPlayActivity.class);
            intent.putExtra(Constants.imgSource, subImageArrayList.get(position).getPath());
            startActivity(intent);
        }
    }

    private class ViewHolder {
        final RecyclerView grid;

        GridLayoutManager gridLayoutManager;

        final ViewPager pager;
        final Toolbar pagerToolbar;
        final TextView pagerTitle;
        final View pagerBackground;
        View viewLine;
        LinearLayout bottomLayout;
        LinearLayout imgEdit, imgDelete, imgInfo, imgShare, imgMove;
        ImageView editImage;
        RelativeLayout mainRel;

        ViewHolder(Activity activity) {
            grid = Views.find(activity, R.id.advanced_grid);

            pager = Views.find(activity, R.id.advanced_pager);
            pagerToolbar = Views.find(activity, R.id.advanced_full_toolbar);
            pagerTitle = Views.find(activity, R.id.advanced_full_title);
            pagerBackground = Views.find(activity, R.id.advanced_full_background);
            viewLine = Views.find(activity, R.id.viewLine);
            bottomLayout = Views.find(activity, R.id.bottomLayout);
            editImage = Views.find(activity, R.id.editImage);
            imgEdit = Views.find(activity, R.id.imgEdit);
            imgDelete = Views.find(activity, R.id.imgDelete);
            imgInfo = Views.find(activity, R.id.imgInfo);
            imgShare = Views.find(activity, R.id.imgShare);
            imgMove = Views.find(activity, R.id.imgMove);
            mainRel = Views.find(activity, R.id.mainRel);

            int id = getResources().getIdentifier("config_showNavigationBar", "bool", "android");
            boolean val = id > 0 && getResources().getBoolean(id);
            if (val == true) {

                int height = 0;
                Resources resources = getResources();
                int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    height = resources.getDimensionPixelSize(resourceId);
                }

                if (bottomLayout != null) {
                    int pixels = (int) (50 * getResources().getDisplayMetrics().density + 0.5f);
                    RelativeLayout.LayoutParams buttonLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, pixels);
                    buttonLayoutParams.setMargins(0, 0, 0, height);
                    buttonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    bottomLayout.setLayoutParams(buttonLayoutParams);

                    CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT);
                    params.setBehavior(new AppBarLayout.ScrollingViewBehavior());
                    params.setMargins(0, 0, 0, height);
                    mainRel.setLayoutParams(params);
                }
            }
        }

    }

    private void fetchGalleryData() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

        Cursor cursorVideo = getApplicationContext().getContentResolver()
                .query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projectionVideo, null, null, MediaStore.Video.Media.DATE_ADDED);

        Cursor cursorImage = getApplicationContext().getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projectionImage, null, null, MediaStore.Images.Media.DATE_ADDED);

        if (cursorVideo == null && cursorImage == null) {
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
        } else {
            folderArrayList.clear();
            thumbImageArrayList.clear();

            ArrayList<String> tempFolder = new ArrayList<>();

            HashSet<Long> albumSet = new HashSet<>();
            File file;

            if (cursorVideo.moveToLast()) {
                do {
                    if (Thread.interrupted()) {
                        return;
                    }

                    long Id = cursorVideo.getLong(cursorVideo.getColumnIndex(projectionVideo[0]));
                    String Data = cursorVideo.getString(cursorVideo.getColumnIndex(projectionVideo[1]));

                    if (!albumSet.contains(Id)) {
                        file = new File(Data);
                        if (file.exists() && !file.getName().startsWith(".")) {
                            Cursor tempCursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projectionVideo,
                                    MediaStore.Video.Media.BUCKET_ID + " =?", new String[]{String.valueOf(Id)}, MediaStore.Video.Media.DATE_ADDED);

                            File folderPath = new File(Data.substring(0, Data.toString().lastIndexOf("/")));
                            tempFolder.add(String.valueOf(folderPath));

                            albumSet.add(Id);
                            tempCursor.close();
                        }
                    }

                } while (cursorVideo.moveToPrevious());
            }
            cursorVideo.close();

            albumSet = new HashSet<>();

            if (cursorImage.moveToLast()) {
                do {
                    if (Thread.interrupted()) {
                        return;
                    }

                    long Id = cursorImage.getLong(cursorImage.getColumnIndex(projectionImage[0]));
                    String Data = cursorImage.getString(cursorImage.getColumnIndex(projectionImage[1]));

                    if (!albumSet.contains(Id)) {
                        file = new File(Data);
                        if (file.exists() && !file.getName().startsWith(".")) {
                            Cursor tempCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projectionImage,
                                    MediaStore.Images.Media.BUCKET_ID + " =?", new String[]{String.valueOf(Id)}, MediaStore.Images.Media.DATE_ADDED);

                            File folderPath = new File(Data.substring(0, Data.toString().lastIndexOf("/")));
                            tempFolder.add(String.valueOf(folderPath));

                            albumSet.add(Id);
                            tempCursor.close();
                        }
                    }

                } while (cursorImage.moveToPrevious());
            }
            cursorImage.close();

            tempFolder = new ArrayList<String>(new LinkedHashSet<String>(tempFolder));

            for (int j = 0; j < tempFolder.size(); j++) {

                int storage = 0;
                if (tempFolder.get(j).contains("" + Environment.getExternalStorageDirectory().getAbsolutePath())) {
                    storage = 0;
                } else if (tempFolder.get(j).contains("" + System.getenv("SECONDARY_STORAGE"))) {
                    storage = 1;
                }

                File folder = new File(tempFolder.get(j) + "/");
                folder.mkdirs();

                File[] allImageFiles = folder.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return (!name.startsWith(".") && (name.endsWith(".gif") || name.endsWith(".GIF")
                                || name.endsWith(".arw") || name.endsWith(".ARW")
                                || name.endsWith(".cr2") || name.endsWith(".CR2")
                                || name.endsWith(".crw") || name.endsWith(".CRW")
                                || name.endsWith(".dcr") || name.endsWith(".DCR")
                                || name.endsWith(".dng") || name.endsWith(".DNG")
                                || name.endsWith(".jpeg") || name.endsWith(".JPEG")
                                || name.endsWith(".jpg") || name.endsWith(".JPG")
                                || name.endsWith(".mrw") || name.endsWith(".MRW")
                                || name.endsWith(".nef") || name.endsWith(".NEF")
                                || name.endsWith(".orf") || name.endsWith(".ORF")
                                || name.endsWith(".pef") || name.endsWith(".PEF")
                                || name.endsWith(".png") || name.endsWith(".PNG")
                                || name.endsWith(".raf") || name.endsWith(".RAF")
                                || name.endsWith(".sr2") || name.endsWith(".SR2")));
                    }
                });

                File[] allVideoFiles = folder.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return (!name.startsWith(".") && (name.endsWith(".mp4") || name.endsWith(".MP4")
                                || name.endsWith(".wav") || name.endsWith(".WAV")
                                || name.endsWith(".3gp") || name.endsWith(".3GP")
                                || name.endsWith(".mpg") || name.endsWith(".MPG")
                                || name.endsWith(".mov") || name.endsWith(".MOV")
                                || name.endsWith(".mts") || name.endsWith(".MTS")
                                || name.endsWith(".wmv") || name.endsWith(".WAV")
                                || name.endsWith(".mkv") || name.endsWith(".MKV")));
                    }
                });

                File[] allFiles = folder.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return (!name.startsWith(".") && (name.endsWith(".gif") || name.endsWith(".GIF")
                                || name.endsWith(".arw") || name.endsWith(".ARW")
                                || name.endsWith(".cr2") || name.endsWith(".CR2")
                                || name.endsWith(".crw") || name.endsWith(".CRW")
                                || name.endsWith(".dcr") || name.endsWith(".DCR")
                                || name.endsWith(".dng") || name.endsWith(".DNG")
                                || name.endsWith(".jpeg") || name.endsWith(".JPEG")
                                || name.endsWith(".jpg") || name.endsWith(".JPG")
                                || name.endsWith(".mrw") || name.endsWith(".MRW")
                                || name.endsWith(".nef") || name.endsWith(".NEF")
                                || name.endsWith(".orf") || name.endsWith(".ORF")
                                || name.endsWith(".pef") || name.endsWith(".PEF")
                                || name.endsWith(".png") || name.endsWith(".PNG")
                                || name.endsWith(".raf") || name.endsWith(".RAF")
                                || name.endsWith(".sr2") || name.endsWith(".SR2")
                                || name.endsWith(".mp4") || name.endsWith(".MP4")
                                || name.endsWith(".wav") || name.endsWith(".WAV")
                                || name.endsWith(".3gp") || name.endsWith(".3GP")
                                || name.endsWith(".mpg") || name.endsWith(".MPG")
                                || name.endsWith(".mov") || name.endsWith(".MOV")
                                || name.endsWith(".mts") || name.endsWith(".MTS")
                                || name.endsWith(".wmv") || name.endsWith(".WAV")
                                || name.endsWith(".mkv") || name.endsWith(".MKV")));
                    }
                });

                for (int i = 0; i < allFiles.length; i++) {

                    String name = allFiles[i].toString().substring(allFiles[i].toString().lastIndexOf(".") + 1);

                    int type = 0;

                    if (!name.startsWith(".") && (name.endsWith("arw") || name.endsWith("ARW")
                            || name.endsWith("cr2") || name.endsWith("CR2")
                            || name.endsWith("crw") || name.endsWith("CRW")
                            || name.endsWith("dcr") || name.endsWith("DCR")
                            || name.endsWith("dng") || name.endsWith("DNG")
                            || name.endsWith("jpeg") || name.endsWith("JPEG")
                            || name.endsWith("jpg") || name.endsWith("JPG")
                            || name.endsWith("mrw") || name.endsWith("MRW")
                            || name.endsWith("nef") || name.endsWith("NEF")
                            || name.endsWith("orf") || name.endsWith("ORF")
                            || name.endsWith("pef") || name.endsWith("PEF")
                            || name.endsWith("png") || name.endsWith("PNG")
                            || name.endsWith("raf") || name.endsWith("RAF")
                            || name.endsWith("sr2") || name.endsWith("SR2"))) {
                        type = 0;
                    } else if (!name.startsWith(".") && (name.endsWith("gif") || name.endsWith("GIF"))) {
                        type = 2;
                    } else {
                        type = 1;
                    }

                    String Path = String.valueOf(allFiles[i]);
                    String imgName = Path.substring(Path.lastIndexOf("/") + 1, Path.length());

                    File file1 = new File(Path);
                    Date lastModDate = new Date(file1.lastModified());

                    long length = file1.length();
                    length = length / 1024;
                    String Duration = "0";
                    if (type == 1 && !file1.getName().startsWith(".")) {

                        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                        retriever.setDataSource(activity, Uri.fromFile(file1));
                        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                        long duration = Long.parseLong(time);
                        Duration = String.format("%d:%d",
                                TimeUnit.MILLISECONDS.toMinutes(duration),
                                TimeUnit.MILLISECONDS.toSeconds(duration) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
                    }

                    //String location = getLoc(String.valueOf(allFiles[i]));
                    thumbImageArrayList.add(new ThumbImage("", String.valueOf(allFiles[i]), String.valueOf(lastModDate), imgName, Duration, length, type, j, i));
                }

                ArrayList<AlbumSetting> stringArrayList = new ArrayList<>();
                if (sharedPref.getAlbumData(activity) != null)
                    stringArrayList = sharedPref.getAlbumData(activity);

                String frontImage = "", folderDate = "";
                int frontThumbType = 0;

                if (allImageFiles.length != 0 && allVideoFiles.length != 0) {
                    Date lastModified1 = new Date(allImageFiles[0].lastModified());
                    Date lastModified2 = new Date(allVideoFiles[0].lastModified());

                    if (lastModified1.compareTo(lastModified2) > 0) {
                        frontImage = String.valueOf(allImageFiles[0]);
                        frontThumbType = 0;
                        folderDate = String.valueOf(lastModified1);
                    } else if (lastModified1.compareTo(lastModified2) < 0) {
                        frontImage = String.valueOf(allVideoFiles[0]);
                        frontThumbType = 1;
                        folderDate = String.valueOf(lastModified2);
                    }

                } else {
                    if (allImageFiles.length != 0) {
                        frontImage = String.valueOf(allImageFiles[0]);
                        frontThumbType = 0;
                        folderDate = String.valueOf(new Date(allImageFiles[0].lastModified()));
                    } else if (allVideoFiles.length != 0) {
                        frontImage = String.valueOf(allVideoFiles[0]);
                        frontThumbType = 1;
                        folderDate = String.valueOf(new Date(allVideoFiles[0].lastModified()));
                    }
                }

                int imgType = 0;
                String sharedCoverImage = "";
                boolean isHide = false;
                for (AlbumSetting s : stringArrayList) {
                    if (s.getPath().equals(tempFolder.get(j))) {
                        isHide = s.isHide();
                        sharedCoverImage = s.getCoverImage();
                        imgType = s.getImageType();
                        break;
                    }
                }

                if (sharedCoverImage.equals("")) {
                    sharedCoverImage = frontImage;
                } else {
                    File file1 = new File(sharedCoverImage);
                    if (!file1.exists())
                        sharedCoverImage = frontImage;
                    else
                        frontThumbType = imgType;
                }

                String folderName = tempFolder.get(j).substring(tempFolder.get(j).lastIndexOf("/") + 1, tempFolder.get(j).length());
                folderArrayList.add(new Folder(folderName, folderDate, tempFolder.get(j), sharedCoverImage, storage, allImageFiles.length, allVideoFiles.length, j, isHide, frontThumbType));
            }

            int totalImage = 0, totalVideo = 0;
            for (int i = 0; i < folderArrayList.size(); i++) {
                totalImage = totalImage + folderArrayList.get(i).getTotal_Photo();
                totalVideo = totalVideo + folderArrayList.get(i).getTotal_Video();
            }
            //folderArrayList.add(new Folder("All", folderArrayList.get(0).getDate(), "", folderArrayList.get(0).getFrontImage(), 0, totalImage, totalVideo, folderArrayList.size() - 1));
        }
    }

    private void getTotal(int position) {
        currentFolderPosition = position;
        int img = folderArrayList.get(position).getTotal_Photo();
        int video = folderArrayList.get(position).getTotal_Video();
        String txtVideo = "", txtImage = "";
        if (video != 0 && img != 0) {
            if (video == 1)
                txtVideo = "video";
            else
                txtVideo = "videos";

            if (img == 1)
                txtImage = "photo";
            else
                txtImage = "photos";

            txtTotal.setText(img + " " + txtImage + " - " + video + " " + txtVideo);
            txtTotalRight.setText("(" + String.valueOf(img + video) + ")");
        } else {
            if (img != 0) {
                if (img == 1)
                    txtImage = "photo";
                else
                    txtImage = "photos";
                txtTotal.setText(img + " " + txtImage);
                txtTotalRight.setText("(" + String.valueOf(img) + ")");
            } else {
                if (video == 1)
                    txtVideo = "video";
                else
                    txtVideo = "videos";
                txtTotal.setText(video + " " + txtVideo);
                txtTotalRight.setText("(" + String.valueOf(video) + ")");
            }
        }

        imgFolder.setImageResource(checkForCoverIcon(folderArrayList.get(position).getName()));
        txtFolderName.setText(folderArrayList.get(position).getName());
        toolbar.setTitle(folderArrayList.get(position).getName());
        txtFolderNameChange.setText(folderArrayList.get(position).getName());
        String str = folderArrayList.get(position).getFrontImage();
        txtAlbumSettingStorage.setText(str.substring(0, str.lastIndexOf("/")));

        Bitmap bmThumbnail = null;

        if (folderArrayList.get(position).getFrontThumbType() == 1) {
            File file = new File(folderArrayList.get(position).getFrontImage());
            if (file.exists())
                bmThumbnail = ThumbnailUtils.createVideoThumbnail(folderArrayList.get(position).getFrontImage(), MediaStore.Video.Thumbnails.MINI_KIND);
        } else {
            File file = new File(folderArrayList.get(position).getFrontImage());
            if (file.exists())
                bmThumbnail = Compressor.getDefault(activity).compressToBitmap(file);
        }

        if (bmThumbnail != null) {
            bmThumbnail = cropCenter(bmThumbnail);
            bmThumbnail = fastBlur(bmThumbnail);
            imgTop.setImageBitmap(bmThumbnail);
        }

        ArrayList<String> stringArrayList = new ArrayList<>();
        for (ThumbImage thumbImage : subImageArrayList) {
//            TimeZone tz = TimeZone.getTimeZone("UTC");
//            SimpleDateFormat df = new SimpleDateFormat("MMM yyyy", Locale.US);
//            df.setTimeZone(tz);
//            String time = df.format(new Date(thumbImage.getDate()));
//            stringArrayList.add(time);

            try {
                String newD = new SimpleDateFormat("MMM yyyy")
                        .format(new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy").parse(thumbImage.getDate()));
                stringArrayList.add(newD);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        stringArrayList = byDateCover(stringArrayList);

        if (stringArrayList.get(0).equals(stringArrayList.get(stringArrayList.size() - 1)))
            txtMonthDetails.setText(stringArrayList.get(0));
        else
            txtMonthDetails.setText(stringArrayList.get(0) + " - " + stringArrayList.get(stringArrayList.size() - 1));

        // -------- Start:- Set No. of Columns --------
        ArrayList<AlbumSetting> list = new ArrayList<>();
        if (sharedPref.getAlbumData(activity) != null)
            list = sharedPref.getAlbumData(activity);

        String path = txtAlbumSettingStorage.getText().toString();
        int count = 0, pos = 0;
        if (list.size() == 0) {
            txtAlbumSettingGrid.setText("3 Columns");
            views.grid.setLayoutManager(new GridLayoutManager(activity, 3));
            setSortingText("10");
            currentImageOrder = "10";
        } else {
            for (int i = 0; i < list.size(); i++) {
                pos = i;
                if (list.get(i).getPath().equals(path)) {
                    count = 1;
                    break;
                } else
                    count = 0;
            }
            if (count == 1) {
                numColumns = list.get(pos).getColumn();
                txtAlbumSettingGrid.setText(numColumns + " Columns");
                views.grid.setLayoutManager(new GridLayoutManager(activity, numColumns));
                setSortingText(list.get(pos).getOrder());
                currentImageOrder = list.get(pos).getOrder();

            } else {
                txtAlbumSettingGrid.setText("3 Columns");
                views.grid.setLayoutManager(new GridLayoutManager(activity, 3));
                setSortingText("10");
                currentImageOrder = "10";
            }
            // -------- End:- Set No. of Columns --------
        }
    }

    public void filterImage(View view) {
        filterList(0, "This album has no picture");
        changeColor(1);
    }

    public void filterVideo(View view) {
        filterList(1, "This album has no video");
        changeColor(2);
    }

    public void filterGif(View view) {
        filterList(2, "This album has no GIFs");
        changeColor(3);
    }

    private void filterList(int type, String msg) {
        ArrayList<ThumbImage> filteredList = new ArrayList<>();
        for (ThumbImage thumbImage : tempImageArrayList) {
            if (thumbImage.getType() == type) {
                filteredList.add(thumbImage);
            }
        }
        subImageArrayList.clear();
        subImageArrayList.addAll(filteredList);

        if (subImageArrayList.size() == 0) {
            txtNoData.setVisibility(View.VISIBLE);
            txtNoData.setText(msg);
        } else {
            txtNoData.setVisibility(View.GONE);
        }

        slidingMenu.toggle();
        PAGE_SIZE = subImageArrayList.size();
        gridAdapter.notifyDataSetChanged();
        pagerAdapter.notifyDataSetChanged();
    }

    public void twoColumn(View view) {
        setGrid(2);
        views.grid.setLayoutManager(new GridLayoutManager(activity, 2));
        alertDialogGrid.dismiss();
    }

    public void threeColumn(View view) {
        setGrid(3);
        views.grid.setLayoutManager(new GridLayoutManager(activity, 3));
        alertDialogGrid.dismiss();
    }

    public void fourColumn(View view) {
        setGrid(4);
        views.grid.setLayoutManager(new GridLayoutManager(activity, 4));
        alertDialogGrid.dismiss();
    }

    public void fiveColumn(View view) {
        setGrid(5);
        views.grid.setLayoutManager(new GridLayoutManager(activity, 5));
        alertDialogGrid.dismiss();
    }

    private void setGrid(int no) {
        ArrayList<AlbumSetting> stringArrayList = new ArrayList<>();
        if (sharedPref.getAlbumData(activity) != null)
            stringArrayList = sharedPref.getAlbumData(activity);

        String path = txtAlbumSettingStorage.getText().toString();
        String frontImg = folderArrayList.get(currentFolderPosition).getFrontImage();
        int imgType = folderArrayList.get(currentFolderPosition).getFrontThumbType();

        int count = 0, pos = 0;
        if (stringArrayList.size() == 0)
            stringArrayList.add(new AlbumSetting(imgType, frontImg, path, 3, "10", false));
        else {
            for (int i = 0; i < stringArrayList.size(); i++) {

                pos = i;
                if (stringArrayList.get(i).getPath().equals(path)) {
                    count = 1;
                    break;
                } else
                    count = 0;
            }
            if (count == 1)
                stringArrayList.set(pos, new AlbumSetting(imgType, frontImg, path, no, stringArrayList.get(pos).getOrder(), stringArrayList.get(pos).isHide()));
            else
                stringArrayList.add(new AlbumSetting(imgType, frontImg, path, 3, "10", false));
        }
        sharedPref.setAlbumData(activity, stringArrayList);
        txtAlbumSettingGrid.setText(no + " Columns");
    }

    private void changePath(String oldName, String newName) {

        ArrayList<AlbumSetting> list = new ArrayList<>();
        if (sharedPref.getAlbumData(activity) != null)
            list = sharedPref.getAlbumData(activity);

        if (list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getPath().equals(oldName)) {
                    list.set(i, new AlbumSetting(list.get(i).getImageType(), list.get(i).getCoverImage(), newName, list.get(i).getColumn(), list.get(i).getOrder(), list.get(i).isHide()));
                    break;
                }
            }
        }
        sharedPref.setAlbumData(activity, list);
    }

    public void AlbumSettingHide(View view) {
        Folder folder = folderArrayList.get(currentFolderPosition);
        folderArrayList.set(currentFolderPosition, new Folder(
                folder.getName(),
                folder.getDate(),
                folder.getPath(),
                folder.getFrontImage(),
                folder.getStorage(),
                folder.getTotal_Photo(),
                folder.getTotal_Video(),
                folder.getPosition(),
                true,
                folder.getFrontThumbType()
        ));
        folderAdapter.notifyDataSetChanged();

        ArrayList<AlbumSetting> stringArrayList = new ArrayList<>();
        if (sharedPref.getAlbumData(activity) != null)
            stringArrayList = sharedPref.getAlbumData(activity);

        if (stringArrayList.size() == 0) {
            stringArrayList.add(new AlbumSetting(folder.getFrontThumbType(), folder.getFrontImage(), folder.getPath(), 3, "10", true));
        } else {
            int count = 0, pos = 0;
            for (int i = 0; i < stringArrayList.size(); i++) {
                pos = i;
                if (folder.getPath().equals(stringArrayList.get(i).getPath())) {
                    count = 1;
                    break;
                } else
                    count = 0;
            }
            if (count == 1)
                stringArrayList.set(pos, new AlbumSetting(stringArrayList.get(pos).getImageType(), stringArrayList.get(pos).getCoverImage(), stringArrayList.get(pos).getPath(), stringArrayList.get(pos).getColumn(), stringArrayList.get(pos).getOrder(), true));
            else
                stringArrayList.add(new AlbumSetting(folder.getFrontThumbType(), folder.getFrontImage(), folder.getPath(), 3, "10", true));
        }
        sharedPref.setAlbumData(activity, stringArrayList);

        toolbar.setVisibility(View.VISIBLE);
        albumTop.setVisibility(View.VISIBLE);
        albumBottom.setVisibility(View.GONE);
        imgListLayout.setVisibility(View.VISIBLE);
        albumSettingLayout.setVisibility(View.GONE);
        slidingMenu.setSlidingEnabled(true);
        collapsingToolbarLayoutParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        collapsingToolbar.setLayoutParams(collapsingToolbarLayoutParams);

        for (int k = 0; k < folderArrayList.size(); k++) {
            if (folderArrayList.get(k).isHide() == false) {
//                getTotal(k);
//                addGridImages(folderArrayList.get(k).getPosition());
//                slidingMenu.toggle();
//                folderAdapter.setSelection(k);
                new getImgData(folderArrayList.get(k).getPosition(), k).execute();
                break;
            }
        }
    }

    @Override
    protected void onResume() {

        if (Boolean.valueOf(sharedPref.getData(activity, brightSlideShow)) == true || Boolean.valueOf(sharedPref.getData(activity, brightVideo)) == true)
            ScreenBrightness.brightness(curBrightnessValue, activity);
//        if (folderArrayList.size() != 0) {
//            ArrayList<AlbumSetting> stringArrayList = new ArrayList<>();
//            if (sharedPref.getAlbumData(activity) != null)
//                stringArrayList = sharedPref.getAlbumData(activity);
//
//            if (stringArrayList.size() != 0) {
//                int available = 0;
//                for (int i = 0; i < stringArrayList.size(); i++) {
//                    available = 0;
//                    for (int j = 0; j < folderArrayList.size(); j++) {
//                        if (folderArrayList.get(j).getPath().equals(stringArrayList.get(i).getPath())) {
//                            available = 1;
//                            break;
//                        }
//                    }
//                    if (available == 0)
//                        stringArrayList.remove(i);
//                }
//                sharedPref.setAlbumData(activity, stringArrayList);
//
//                for (AlbumSetting albumSetting : stringArrayList)
//                    Log.e("PAth", albumSetting.getPath() + " :: " + stringArrayList.size());
//            }
//        }
        super.onResume();
    }
}
