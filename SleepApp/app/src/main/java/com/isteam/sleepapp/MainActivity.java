package com.isteam.sleepapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ImageButton button1 = null;
    private ImageButton button2 = null;
    private ImageButton button3 = null;
    private ImageButton button4 = null;
    private static final String TYPEFACE_NAME = "fonts/NanumSquareOTFRegular.otf";
    private Typeface typeface = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTypeface();
        setContentView(R.layout.activity_main);

        button1 = (ImageButton) findViewById(R.id.ib_connect);
        button2 = (ImageButton) findViewById(R.id.ib_light);
        button3 = (ImageButton) findViewById(R.id.ib_temp);
        button4 = (ImageButton) findViewById(R.id.ib_sleep);

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ConditionActivity.class));
                overridePendingTransition(R.anim.fade, R.anim.hold);
            }
        });
    }

    private void loadTypeface(){
        if(typeface==null)
            typeface = Typeface.createFromAsset(getAssets(), TYPEFACE_NAME);
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
