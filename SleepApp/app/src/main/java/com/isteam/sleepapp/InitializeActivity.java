package com.isteam.sleepapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Ulnamsong on 2016. 11. 22..
 */

public class InitializeActivity {

    public static Typeface typeface = null;
    public static Typeface typeface_m = null;
    public static Typeface typeface_b = null;

    public static void loadTypeface(Activity activity, String TYPEFACE_NAME, String TYPEFACE_NAME_BOLD, String TYPEFACE_NAME_EXBOLD){
        if(typeface == null)
            typeface = Typeface.createFromAsset(activity.getAssets(), TYPEFACE_NAME);

        if(typeface_m == null)
            typeface_m = Typeface.createFromAsset(activity.getAssets(), TYPEFACE_NAME_BOLD);

        if(typeface_b == null)
            typeface_b = Typeface.createFromAsset(activity.getAssets(), TYPEFACE_NAME_EXBOLD);
    }

    public static void setContentView(Activity activity, int viewId) {
        View view = LayoutInflater.from(activity).inflate(viewId, null);
        ViewGroup group = (ViewGroup)view;
        int childCnt = group.getChildCount();
        for(int i=0; i<childCnt; i++){
            View v = group.getChildAt(i);
            if(v instanceof TextView){
                ((TextView)v).setTypeface(typeface);
            }
        }
        activity.setContentView(view);
    }
}
