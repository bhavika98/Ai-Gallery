package com.app.incroyable.ai_gallery.adapter;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.incroyable.ai_gallery.R;
import com.app.incroyable.ai_gallery.model.Filter;

import java.util.ArrayList;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {

    ArrayList<Filter> list;
    Activity activity;
    String Selection = "0";
    FilterCallback filterCallback;

    public FilterAdapter(Activity activity, ArrayList<Filter> list) {
        this.list = list;
        this.activity = activity;
        this.filterCallback = (FilterCallback) activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.adapter_filter, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.thumbnail.setImageResource(list.get(position).getThumb());
        holder.txtTitle.setText(list.get(position).getTitle());

        if (position == Integer.parseInt(Selection))
            holder.border.setVisibility(View.VISIBLE);
        else
            holder.border.setVisibility(View.INVISIBLE);

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Selection = String.valueOf(position);
                notifyDataSetChanged();
                filterCallback.FilterMethod(position);

            }
        });
    }

    public void setSelection(int selection)
    {
        Selection = String.valueOf(selection);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnail;
        RelativeLayout border;
        LinearLayout mainView;
        TextView txtTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            border = (RelativeLayout) itemView.findViewById(R.id.border);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            mainView = (LinearLayout) itemView.findViewById(R.id.mainView);
        }
    }

    public interface FilterCallback{
        void FilterMethod(int position);
    }
}