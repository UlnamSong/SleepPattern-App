package com.isteam.sleepapp.Module;

import android.content.Context;
import android.graphics.Typeface;

import com.isteam.sleepapp.R;

/**
 * Created by Ulnamsong on 2016. 12. 2..
 */

public class TypefaceUtil {
    public static Typeface typeface_1 = null;
    public static Typeface typeface_2 = null;
    public static Typeface typeface_3 = null;
    public static Typeface typeface_4 = null;
    public static Typeface typeface_5 = null;

    public static void loadTypeface(Context mContext) {
        if(typeface_1 == null) {
            typeface_1 = Typeface.createFromAsset(mContext.getAssets(), "fonts/yoon310.ttf");
        }

        if(typeface_2 == null) {
            typeface_2 = Typeface.createFromAsset(mContext.getAssets(), "fonts/yoon320.ttf");
        }

        if(typeface_3 == null) {
            typeface_3 = Typeface.createFromAsset(mContext.getAssets(), "fonts/yoon330.ttf");
        }

        if(typeface_4 == null) {
            typeface_4 = Typeface.createFromAsset(mContext.getAssets(), "fonts/yoon340.ttf");
        }

        if(typeface_5 == null) {
            typeface_5 = Typeface.createFromAsset(mContext.getAssets(), "fonts/yoon360.ttf");
        }
    }
}

