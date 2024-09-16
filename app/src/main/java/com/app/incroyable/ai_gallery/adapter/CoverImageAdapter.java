package com.app.incroyable.ai_gallery.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.app.incroyable.ai_gallery.R;
import com.app.incroyable.ai_gallery.model.ThumbImage;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CoverImageAdapter extends BaseAdapter {

    LayoutInflater inflater;
    ArrayList<ThumbImage> arrayList;
    Activity activity;

    public CoverImageAdapter(Activity activity, ArrayList<ThumbImage> arrayList) {
        this.activity = activity;
        this.arrayList = arrayList;
        inflater = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {
        ImageView imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(R.layout.adapter_cover_image, parent, false);

        Holder holder = new Holder();
        holder.imageView = (ImageView) view.findViewById(R.id.imageView);

        Glide.with(activity).load(arrayList.get(position).getPath()).placeholder(R.drawable.placeholder).into(holder.imageView);

        return view;
    }
}
