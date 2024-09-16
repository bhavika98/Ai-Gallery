package com.app.incroyable.ai_gallery.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.incroyable.ai_gallery.R;
import com.app.incroyable.ai_gallery.model.Move;

import java.util.ArrayList;

public class MoveAdapter extends BaseAdapter {

    ArrayList<Move> moveArrayList = new ArrayList<>();
    Activity activity;

    public MoveAdapter(Activity activity, ArrayList<Move> moveArrayList)
    {
        this.activity = activity;
        this.moveArrayList = moveArrayList;
    }

    @Override
    public int getCount() {
        return moveArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        ImageView imgFolder;
        TextView txtName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(activity).inflate(R.layout.adapter_move, parent, false);

        Holder holder = new Holder();
        holder.imgFolder = (ImageView) view.findViewById(R.id.imgFolder);
        holder.txtName = (TextView) view.findViewById(R.id.txtName);

        holder.imgFolder.setImageResource(moveArrayList.get(position).getImage());
        holder.txtName.setText(moveArrayList.get(position).getName());

        return view;
    }
}
