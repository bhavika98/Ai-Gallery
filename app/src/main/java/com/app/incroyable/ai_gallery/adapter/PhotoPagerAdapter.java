package com.app.incroyable.ai_gallery.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.alexvasilkov.android.commons.utils.Views;
import com.alexvasilkov.gestures.commons.RecyclePagerAdapter;
import com.alexvasilkov.gestures.views.GestureImageView;
import com.app.incroyable.ai_gallery.R;
import com.app.incroyable.ai_gallery.model.ThumbImage;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;

public class PhotoPagerAdapter extends RecyclePagerAdapter<PhotoPagerAdapter.ViewHolder> {

    private final ViewPager viewPager;
    private ArrayList<ThumbImage> photos;
    Activity activity;
    VideoPlayCallback videoPlayCallback;

    private boolean activated;

    public PhotoPagerAdapter(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    public void setPhotos(ArrayList<ThumbImage> photos, Activity activity) {
        this.photos = photos;
        this.activity = activity;
        notifyDataSetChanged();
        this.videoPlayCallback = (VideoPlayCallback) activity;
    }

    public ThumbImage getPhoto(int pos) {
        return photos.get(pos);
    }

    public void setActivated(boolean activated) {
        if (this.activated != activated) {
            this.activated = activated;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return !activated || photos == null ? 0 : photos.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup container) {
        final ViewHolder holder = new ViewHolder(container);

        holder.image.getController().getSettings()
                .setMaxZoom(10f)
                .setDoubleTapZoom(3f);

        holder.image.getController().enableScrollInViewPager(viewPager);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        ThumbImage photo = photos.get(position);

        if (photo.getType() == 2)
            Glide.with(activity).load(photo.getPath()).into(holder.image);
        else {
            Bitmap bitmap = null;
            if (photo.getType() == 1)
                bitmap = ThumbnailUtils.createVideoThumbnail(photo.getPath(), MediaStore.Video.Thumbnails.MINI_KIND);
            else
                bitmap = Compressor.getDefault(activity).compressToBitmap(new File(photo.getPath()));
            holder.image.setImageBitmap(bitmap);
        }

        if (photo.getType() == 1)
            holder.imgVideo.setVisibility(View.VISIBLE);
        else
            holder.imgVideo.setVisibility(View.GONE);

        holder.imgVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoPlayCallback.VideoMethod(position);
            }
        });
    }

    @Override
    public void onRecycleViewHolder(@NonNull ViewHolder holder) {
        super.onRecycleViewHolder(holder);

        Glide.clear(holder.image);
        holder.image.setImageDrawable(null);
    }

    public static GestureImageView getImage(RecyclePagerAdapter.ViewHolder holder) {
        return ((ViewHolder) holder).image;
    }

    static class ViewHolder extends RecyclePagerAdapter.ViewHolder {
        final GestureImageView image;
        ImageView imgVideo;

        ViewHolder(ViewGroup parent) {
            super(Views.inflate(parent, R.layout.adapter_photo_pager));
            image = Views.find(itemView, R.id.photo_full_image);
            imgVideo = Views.find(itemView, R.id.imgVideo);
        }
    }

    public static interface VideoPlayCallback {
        void VideoMethod(int position);
    }
}
