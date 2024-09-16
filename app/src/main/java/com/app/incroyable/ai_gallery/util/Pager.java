package com.app.incroyable.ai_gallery.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.app.incroyable.ai_gallery.R;

public class Pager extends RelativeLayout {

    public Pager(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_pager, this, true);
    }
}
