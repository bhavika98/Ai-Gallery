package com.app.incroyable.ai_gallery.util;

import android.app.Activity;
import android.graphics.BitmapFactory;

import com.app.incroyable.ai_gallery.R;
import com.app.incroyable.ai_gallery.model.Filter;
import com.app.mylib.filters.IF1977Filter;
import com.app.mylib.filters.IFBrannanFilter;
import com.app.mylib.filters.IFEarlybirdFilter;
import com.app.mylib.filters.IFInkwellFilter;
import com.app.mylib.filters.IFSierraFilter;
import com.app.mylib.filters.IFToasterFilter;
import com.app.mylib.filters.IFXprollFilter;

import java.util.ArrayList;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLookupFilter;

public class DataBinder {

    public static ArrayList<Filter> fetchFilters()
    {
        ArrayList<Filter> filterArrayList = new ArrayList<>();

        filterArrayList.add(new Filter(R.drawable.filter_1, "Original"));
        filterArrayList.add(new Filter(R.drawable.filter_2, "Tropic"));
        filterArrayList.add(new Filter(R.drawable.filter_3, "Valencia"));
        filterArrayList.add(new Filter(R.drawable.filter_4, "Nashville"));
        filterArrayList.add(new Filter(R.drawable.filter_5, "B&W"));
        filterArrayList.add(new Filter(R.drawable.filter_6, "Lomo"));
        filterArrayList.add(new Filter(R.drawable.filter_7, "Autumn"));
        filterArrayList.add(new Filter(R.drawable.filter_8, "Fresh"));
        filterArrayList.add(new Filter(R.drawable.filter_9, "Elegance"));
        filterArrayList.add(new Filter(R.drawable.filter_10, "Mellow"));
        filterArrayList.add(new Filter(R.drawable.filter_11, "Time"));
        filterArrayList.add(new Filter(R.drawable.filter_12, "Earlybird"));
        filterArrayList.add(new Filter(R.drawable.filter_13, "Dark"));
        filterArrayList.add(new Filter(R.drawable.filter_14, "Retro"));
        filterArrayList.add(new Filter(R.drawable.filter_15, "Twilight"));
        filterArrayList.add(new Filter(R.drawable.filter_16, "Inkwell"));
        filterArrayList.add(new Filter(R.drawable.filter_17, "Rise"));
        filterArrayList.add(new Filter(R.drawable.filter_18, "Myth"));
        filterArrayList.add(new Filter(R.drawable.filter_19, "Soft"));
        filterArrayList.add(new Filter(R.drawable.filter_20, "Sweet"));
        filterArrayList.add(new Filter(R.drawable.filter_21, "Forest"));

        return filterArrayList;
    }

    public static GPUImageFilter applyFilter(int position, Activity activity)
    {
        GPUImageFilter gpuImageFilter = null;
        GPUImageLookupFilter gpuImageLookupFilter = new GPUImageLookupFilter();;

        switch (position) {
            case 0:
                gpuImageFilter = new GPUImageFilter();
                break;

            case 1:
                gpuImageFilter = new IF1977Filter(activity);
                break;

            case 2:
                gpuImageFilter = new IFBrannanFilter(activity);
                break;

            case 3:
                gpuImageFilter = new IFEarlybirdFilter(activity);
                break;

            case 4:
                gpuImageFilter = new IFInkwellFilter(activity);
                break;

            case 5:
                gpuImageFilter = new IFSierraFilter(activity);
                break;

            case 6:
                gpuImageFilter = new IFToasterFilter(activity);
                break;

            case 7:
                gpuImageFilter = new IFXprollFilter(activity);
                break;

            case 8:
                gpuImageLookupFilter.setBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pf2));
                gpuImageFilter = gpuImageLookupFilter;
                break;

            case 9:
                gpuImageLookupFilter.setBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pf3));
                gpuImageFilter = gpuImageLookupFilter;
                break;

            case 10:
                gpuImageLookupFilter.setBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pf6));
                gpuImageFilter = gpuImageLookupFilter;
                break;

            case 11:
                gpuImageLookupFilter.setBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pf8));
                gpuImageFilter = gpuImageLookupFilter;
                break;

            case 12:
                gpuImageLookupFilter.setBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pf10));
                gpuImageFilter = gpuImageLookupFilter;
                break;

            case 13:
                gpuImageLookupFilter.setBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pf11));
                gpuImageFilter = gpuImageLookupFilter;
                break;

            case 14:
                gpuImageLookupFilter.setBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pf12));
                gpuImageFilter = gpuImageLookupFilter;
                break;

            case 15:
                gpuImageLookupFilter.setBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pf14));
                gpuImageFilter = gpuImageLookupFilter;
                break;

            case 16:
                gpuImageLookupFilter.setBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pf17));
                gpuImageFilter = gpuImageLookupFilter;
                break;

            case 17:
                gpuImageLookupFilter.setBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pf18));
                gpuImageFilter = gpuImageLookupFilter;
                break;

            case 18:
                gpuImageLookupFilter.setBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pf24));
                gpuImageFilter = gpuImageLookupFilter;
                break;

            case 19:
                gpuImageLookupFilter.setBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pf28));
                gpuImageFilter = gpuImageLookupFilter;
                break;

            case 20:
                gpuImageLookupFilter.setBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pf32));
                gpuImageFilter = gpuImageLookupFilter;
                break;
        }
        return gpuImageFilter;
    }
}
