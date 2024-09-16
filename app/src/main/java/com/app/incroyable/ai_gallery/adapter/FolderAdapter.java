package com.app.incroyable.ai_gallery.adapter;

import android.app.Activity;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.incroyable.ai_gallery.R;
import com.app.incroyable.ai_gallery.model.Folder;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.app.incroyable.ai_gallery.util.ScreenBrightness.checkForFolderIcon;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {

    LayoutInflater inflater;
    ArrayList<Folder> arrayList;
    Activity activity;
    String Selection = "0";
    boolean aBoolean = false;
    HideCallback hideCallback;
    DeleteCallback deleteCallback;

    public FolderAdapter(Activity activity, ArrayList<Folder> arrayList) {
        this.inflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.arrayList = arrayList;
        this.hideCallback = (HideCallback) activity;
        this.deleteCallback = (DeleteCallback) activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.adapter_folder, parent, false);
        return new ViewHolder(v);
    }

    public void showHiddenFolder(boolean show) {
        this.aBoolean = show;
    }

    public boolean getClickEventStatus()
    {
        return aBoolean;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.setIsRecyclable(false);
        if(aBoolean == true)
        {
            if(holder.imgHide.getVisibility() == View.GONE)
            {
//                Animation slideUpAnimation = AnimationUtils.loadAnimation(activity, R.anim.slide_up);
//                holder.imgHide.startAnimation(slideUpAnimation);
//                holder.imgDelete.startAnimation(slideUpAnimation);
            }
            holder.imgHide.setVisibility(View.VISIBLE);
            holder.imgDelete.setVisibility(View.VISIBLE);
            holder.imgStorage.setVisibility(View.INVISIBLE);
            holder.allLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            if(holder.imgHide.getVisibility() == View.VISIBLE)
            {
//                Animation slideDownAnimation = AnimationUtils.loadAnimation(activity, R.anim.slide_down);
//                holder.imgHide.startAnimation(slideDownAnimation);
//                holder.imgDelete.startAnimation(slideDownAnimation);
            }
            holder.imgHide.setVisibility(View.GONE);
            holder.imgDelete.setVisibility(View.GONE);

            if (arrayList.get(position).getStorage() == 1)
                holder.imgStorage.setVisibility(View.VISIBLE);
            else
                holder.imgStorage.setVisibility(View.INVISIBLE);

            if (arrayList.get(position).isHide() == false) {
                holder.allLayout.setVisibility(View.VISIBLE);
            } else {
                holder.allLayout.setVisibility(View.GONE);
            }
        }

        if(arrayList.get(position).isHide() == true)
        {
            holder.imgShadow.setBackground(activity.getResources().getDrawable(R.drawable.stroke_hide_folder));
            holder.txtFolderName.setTextColor(ContextCompat.getColor(activity, R.color.hiddenText));
            holder.txtTotal.setTextColor(ContextCompat.getColor(activity, R.color.hiddenText));
            holder.imgHide.setImageResource(R.drawable.drawer_hide);
        }
        else
        {
            holder.imgShadow.setBackground(activity.getResources().getDrawable(R.drawable.stroke_folder));
            holder.txtFolderName.setTextColor(ContextCompat.getColor(activity, R.color.white));
            holder.txtTotal.setTextColor(ContextCompat.getColor(activity, R.color.white));
            holder.imgHide.setImageResource(R.drawable.drawer_unhide);
        }

        if (position == Integer.parseInt(Selection))
            holder.imgStrip.setVisibility(View.VISIBLE);
        else
            holder.imgStrip.setVisibility(View.INVISIBLE);

        holder.txtFolderName.setText(arrayList.get(position).getName());
        Glide.with(activity).load(arrayList.get(position).getFrontImage()).into(holder.imgFolderImage);

        String txtVideo = "", txtImage = "";
        if (arrayList.get(position).getTotal_Video() != 0 && arrayList.get(position).getTotal_Photo() != 0) {
            if (arrayList.get(position).getTotal_Video() == 1)
                txtVideo = "video";
            else
                txtVideo = "videos";

            if (arrayList.get(position).getTotal_Photo() == 1)
                txtImage = "photo";
            else
                txtImage = "photos";

            holder.txtTotal.setText(arrayList.get(position).getTotal_Photo() + " " + txtImage + " - " + arrayList.get(position).getTotal_Video() + " " + txtVideo);
        } else {
            if (arrayList.get(position).getTotal_Photo() != 0) {
                if (arrayList.get(position).getTotal_Photo() == 1)
                    txtImage = "photo";
                else
                    txtImage = "photos";
                holder.txtTotal.setText(arrayList.get(position).getTotal_Photo() + " " + txtImage);
            } else {
                if (arrayList.get(position).getTotal_Video() == 1)
                    txtVideo = "video";
                else
                    txtVideo = "videos";
                holder.txtTotal.setText(arrayList.get(position).getTotal_Video() + " " + txtVideo);
            }
        }

        holder.imgIcon.setImageResource(checkForFolderIcon(arrayList.get(position).getName()));

        holder.imgHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideCallback.HideMethod(position);
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCallback.DeleteMethod(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtFolderName, txtTotal;
        ImageView imgStorage, imgFolderImage, imgHide, imgShadow, imgIcon, imgDelete;
        LinearLayout imgStrip, allLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            txtFolderName = (TextView) itemView.findViewById(R.id.txtFolderName);
            txtTotal = (TextView) itemView.findViewById(R.id.txtTotal);
            imgStorage = (ImageView) itemView.findViewById(R.id.imgStorage);
            imgFolderImage = (ImageView) itemView.findViewById(R.id.imgFolderImage);
            imgShadow = (ImageView) itemView.findViewById(R.id.imgShadow);
            imgHide = (ImageView) itemView.findViewById(R.id.imgHide);
            imgIcon = (ImageView) itemView.findViewById(R.id.imgIcon);
            imgDelete = (ImageView) itemView.findViewById(R.id.imgDelete);
            imgStrip = (LinearLayout) itemView.findViewById(R.id.imgStrip);
            allLayout = (LinearLayout) itemView.findViewById(R.id.allLayout);
        }
    }

    public String getCurrentData() {
        return arrayList.get(Integer.parseInt(Selection)).getPath();
    }

    public void setSelection(int position) {
        this.Selection = String.valueOf(position);
        notifyDataSetChanged();
    }

    public String getSelection(){
        return Selection;
    }

    public interface HideCallback{
        void HideMethod(int position);
    }

    public interface DeleteCallback{
        void DeleteMethod(int position);
    }
}