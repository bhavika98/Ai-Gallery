package com.app.incroyable.ai_gallery.adapter;

import android.app.Activity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.incroyable.ai_gallery.R;

import java.util.ArrayList;
import java.util.Collections;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    ArrayList<String> temp = new ArrayList<>();
    ArrayList<String> list;
    Activity activity;
    String Selection = "";
    int normalColor = R.color.white;
    int selectedColor = R.color.Header_Color;

    public LocationAdapter(Activity activity, ArrayList<String> list, ArrayList<String> temp) {
        this.list = list;
        this.activity = activity;
        this.temp = temp;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.adapter_location, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if (Selection.equals("")) {
            holder.txtLocation.setTextColor(ContextCompat.getColor(activity, normalColor));
            holder.txtTotal.setTextColor(ContextCompat.getColor(activity, normalColor));
        } else {
            if (position == Integer.parseInt(Selection)) {
                holder.txtLocation.setTextColor(ContextCompat.getColor(activity, selectedColor));
                holder.txtTotal.setTextColor(ContextCompat.getColor(activity, selectedColor));
            } else {
                holder.txtLocation.setTextColor(ContextCompat.getColor(activity, normalColor));
                holder.txtTotal.setTextColor(ContextCompat.getColor(activity, normalColor));
            }
        }

        int occurrences = Collections.frequency(temp, list.get(position));

        holder.txtLocation.setText(list.get(position).toString());
        holder.txtTotal.setText("(" + occurrences + ")");
    }

    public void setSelection(int selection) {
        Selection = String.valueOf(selection);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtLocation, txtTotal;

        public ViewHolder(View itemView) {
            super(itemView);
            txtLocation = (TextView) itemView.findViewById(R.id.txtLocation);
            txtTotal = (TextView) itemView.findViewById(R.id.txtTotal);
        }
    }
}