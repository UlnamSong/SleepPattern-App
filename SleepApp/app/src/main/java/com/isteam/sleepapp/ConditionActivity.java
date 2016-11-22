package com.isteam.sleepapp;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ConditionActivity extends AppCompatActivity {

    private static String TYPEFACE_NAME = "fonts/NanumSquareOTFRegular.otf";
    private static String TYPEFACE_NAME_BOLD = "fonts/NanumSquareOTFBold.otf";
    private static String TYPEFACE_NAME_EXBOLD = "fonts/NanumSquareOTFExtraBold.otf";

    private Typeface typeface = null;
    private Typeface typeface_b= null;
    private Typeface typeface_eb = null;

    private TextView tvTitle = null;
    private TextView tvTemp = null;
    private TextView tvTempVal = null;
    private TextView tvHumid = null;
    private TextView tvHumidVal = null;
    private TextView tvBright = null;
    private TextView tvBrightVal = null;
    private TextView tvSound = null;
    private TextView tvSoundVal = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitializeActivity.loadTypeface(ConditionActivity.this, TYPEFACE_NAME, TYPEFACE_NAME_BOLD, TYPEFACE_NAME_EXBOLD);
        InitializeActivity.setContentView(ConditionActivity.this, R.layout.activity_condition);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTemp = (TextView) findViewById(R.id.tv_temptitle);
        tvTemp.setTypeface(typeface);

        tvTempVal = (TextView) findViewById(R.id.tv_tempVal);
        tvTempVal.setTypeface(typeface_b);

        tvHumid = (TextView) findViewById(R.id.tv_humidtitle);
        tvHumid.setTypeface(typeface);

        tvHumidVal = (TextView) findViewById(R.id.tv_humidVal);
        tvHumidVal.setTypeface(typeface_b);

        tvBright = (TextView) findViewById(R.id.tv_brighttitle);
        tvBright.setTypeface(typeface);

        tvBrightVal = (TextView) findViewById(R.id.tv_brightVal);
        tvBrightVal.setTypeface(typeface_b);

        tvSound = (TextView) findViewById(R.id.tv_soundtitle);
        tvSound.setTypeface(typeface);

        tvSoundVal = (TextView) findViewById(R.id.tv_soundVal);
        tvSoundVal.setTypeface(typeface_b);
    }

    private void loadTypeface() {

        //TYPEFACE_NAME = getString(R.string.snowball_typeface_normal);
        //TYPEFACE_NAME_BOLD = getString(R.string.snowball_typeface_bold);
        //TYPEFACE_NAME_EXBOLD = getString(R.string.snowball_typeface_extrabold);

        if(typeface == null)
            typeface = Typeface.createFromAsset(getAssets(), TYPEFACE_NAME);

        if(typeface_b == null)
            typeface_b = Typeface.createFromAsset(getAssets(), TYPEFACE_NAME_BOLD);

        if(typeface_eb == null)
            typeface_eb = Typeface.createFromAsset(getAssets(), TYPEFACE_NAME_EXBOLD);
    }

    @Override
    public void setContentView(int viewId) {
        View view = LayoutInflater.from(this).inflate(viewId, null);
        ViewGroup group = (ViewGroup)view;
        int childCnt = group.getChildCount();
        for(int i=0; i<childCnt; i++){
            View v = group.getChildAt(i);
            if(v instanceof TextView){
                ((TextView)v).setTypeface(typeface);
            }
        }
        super.setContentView(view);
    }
}
