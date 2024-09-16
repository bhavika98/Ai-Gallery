package com.app.incroyable.ai_gallery.adapter;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alexvasilkov.android.commons.utils.Views;
import com.app.incroyable.ai_gallery.R;
import com.app.incroyable.ai_gallery.model.ThumbImage;
import com.app.mylib.pager.DefaultEndlessRecyclerAdapter;
import com.app.mylib.pager.RecyclerAdapterHelper;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PhotoListAdapter extends DefaultEndlessRecyclerAdapter<PhotoListAdapter.ViewHolder> implements View.OnClickListener {

    private ArrayList<ThumbImage> photos;
    private boolean hasMore = true;
    private Activity activity;

    private final OnPhotoListener listener;

    public PhotoListAdapter(OnPhotoListener listener) {
        super();
        this.listener = listener;
    }

    public void setPhotos(ArrayList<ThumbImage> photos, boolean hasMore, Activity activity) {
        ArrayList<ThumbImage> old = this.photos;
        this.photos = photos;
        this.hasMore = hasMore;
        this.activity = activity;

        RecyclerAdapterHelper.notifyChanges(this, old, photos);
    }

    @Override
    public int getCount() {
        return photos == null ? 0 : photos.size();
    }

    public boolean canLoadNext() {
        return hasMore;
    }

    @Override
    protected ViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(parent);
        holder.image.setOnClickListener(this);
        return holder;
    }

    @Override
    protected void onBindHolder(ViewHolder holder, int position) {

        holder.setIsRecyclable(false);
        ThumbImage photo = photos.get(position);
        holder.image.setTag(R.id.tag_item, photo);

        if(photo.getType() == 1)
        {
            holder.layVideo.setVisibility(View.VISIBLE);
            holder.txtDuration.setText(photo.getDuration());
        }
        else
        {
            holder.layVideo.setVisibility(View.GONE);
        }

        if(photo.getType() == 2)
        {
            holder.layGif.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.layGif.setVisibility(View.GONE);
        }
        Glide.with(activity).load(photo.getPath()).placeholder(R.drawable.placeholder).into(holder.image);
    }

    @Override
    protected void onBindLoadingView(TextView loadingText) {
        loadingText.setText(R.string.loading_images);
    }

    @Override
    protected void onBindErrorView(TextView errorText) {
        errorText.setText(R.string.reload_images);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder instanceof ViewHolder) {
            Glide.clear(((ViewHolder) holder).image);
        }
    }

    @Override
    public void onClick(@NonNull View view) {
        ThumbImage photo = (ThumbImage) view.getTag(R.id.tag_item);
        int pos = photos.indexOf(photo);
        listener.onPhotoClick(pos);
    }

    public static ImageView getImage(RecyclerView.ViewHolder holder) {
        if (holder instanceof ViewHolder) {
            return ((ViewHolder) holder).image;
        } else {
            return null;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView image;
        TextView txtDuration;
        LinearLayout layVideo, layGif;

        ViewHolder(ViewGroup parent) {
            super(Views.inflate(parent, R.layout.adapter_photo_list));
            image = (ImageView) itemView.findViewById(R.id.images);
            txtDuration = (TextView) itemView.findViewById(R.id.txtDuration);
            layVideo = (LinearLayout) itemView.findViewById(R.id.layVideo);
            layGif = (LinearLayout) itemView.findViewById(R.id.layGif);
        }
    }

    public interface OnPhotoListener {
        void onPhotoClick(int position);
    }

}
