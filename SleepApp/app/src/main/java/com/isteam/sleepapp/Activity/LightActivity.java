package com.isteam.sleepapp.Activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.isteam.sleepapp.Module.TypefaceUtil;
import com.isteam.sleepapp.R;

import at.grabner.circleprogress.CircleProgressView;

public class LightActivity extends AppCompatActivity {

    private ImageButton button1 = null;
    private ImageButton button2 = null;
    private ImageButton button3 = null;
    private ImageButton button4 = null;

    // SeekBar
    private TextView textView6 = null;
    private TextView textView7 = null;
    private TextView textView8 = null;
    private TextView textView9 = null;

    private TextView tvTitle = null;
    private TextView textView = null;

    private TextView myLightValue = null;
    private TextView myLightLux = null;

    private CircleProgressView illumGraph = null;

    private SeekBar lightSeekBar = null;
    private ImageView lightImage = null;

    private float illumVal = 66.70f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypefaceUtil.loadTypeface(LightActivity.this);

        setContentView(R.layout.activity_light_snowball);

        // Lollipop 이상 버전에서의 상단바 아이콘 색상 문제
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // Do Nothing
        } else {
            // StatusBar Set
            Window window = getWindow();

            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // finally change the color
            window.setStatusBarColor(getResources().getColor(R.color.colorBackgroundSnowball));
        }

        button1 = (ImageButton) findViewById(R.id.imageButton);
        button2 = (ImageButton) findViewById(R.id.imageButton2);
        button3 = (ImageButton) findViewById(R.id.imageButton3);
        button4 = (ImageButton) findViewById(R.id.imageButton4);

        textView6 = (TextView) findViewById(R.id.textView6);
        textView7 = (TextView) findViewById(R.id.textView7);
        textView8 = (TextView) findViewById(R.id.textView8);
        textView9 = (TextView) findViewById(R.id.textView9);

        textView6.setTypeface(TypefaceUtil.typeface_2);
        textView7.setTypeface(TypefaceUtil.typeface_2);
        textView8.setTypeface(TypefaceUtil.typeface_2);
        textView9.setTypeface(TypefaceUtil.typeface_2);

        myLightLux = (TextView) findViewById(R.id.tvLightLux);
        myLightValue = (TextView) findViewById(R.id.tvLightValue);

        myLightValue.setTypeface(TypefaceUtil.typeface_4);
        myLightLux.setTypeface(TypefaceUtil.typeface_2);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        textView = (TextView) findViewById(R.id.textView);

        tvTitle.setTypeface(TypefaceUtil.typeface_1);
        textView.setTypeface(TypefaceUtil.typeface_2);

        lightImage = (ImageView) findViewById(R.id.ivLightStatus);
        lightSeekBar = (SeekBar) findViewById(R.id.light_seekBar);

        illumGraph = (CircleProgressView) findViewById(R.id.illum_graph);

        setLightProgress(1);


        // Bluetooth Button
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LightActivity.this, "Bluetooth Connect", Toast.LENGTH_SHORT).show();
            }
        });

        // Condition Button
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LightActivity.this, ConditionActivity.class));
                overridePendingTransition(R.anim.fade, R.anim.hold);
                finish();
            }
        });

        // Sleep Monitor Button
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LightActivity.this, SleepMonitorActivity.class));
                overridePendingTransition(R.anim.fade, R.anim.hold);
                finish();
            }
        });

        lightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //Toast.makeText(LightActivity.this, "i : " + i, Toast.LENGTH_SHORT).show();
                switch(i) {
                    case 0:
                        // ISTeam
                        //lightImage.setImageResource(R.drawable.light_i0);

                        // Snowball
                        lightImage.setImageResource(R.drawable.light0_snowball);
                        requestLightValToPCB(i);
                        break;
                    case 1:
                        // ISTeam
                        //lightImage.setImageResource(R.drawable.light_i1);

                        // Snowball
                        lightImage.setImageResource(R.drawable.light1_snowball);
                        requestLightValToPCB(i);
                        break;
                    case 2:
                        // ISTeam
                        //lightImage.setImageResource(R.drawable.light_i2);

                        // Snowball
                        lightImage.setImageResource(R.drawable.light2_snowball);
                        requestLightValToPCB(i);
                        break;
                    case 3:
                        // ISTeam
                        //lightImage.setImageResource(R.drawable.light_i3);

                        // Snowball
                        lightImage.setImageResource(R.drawable.light3_snowball);
                        requestLightValToPCB(i);
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Value Set
        illumGraph.setMaxValue(100.0f);
        illumGraph.setValue(illumVal);

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LightActivity.this, ConditionActivity.class));
                overridePendingTransition(R.anim.fade, R.anim.hold);
                finish();
            }
        });
    }

    // 이 메소드를 사용하여 PCB에 명령보낼 수 있음 - 혹시 몰라 만들어 놓음 다른 메소드 구현해서 사용 가능
    private void requestLightValToPCB(int value) {
        // 0, 1, 2, 3 : Light Level

    }

    private void setLightProgress(int i) {
        lightSeekBar.setProgress(i);
        switch(i) {
            case 0:
                lightImage.setImageResource(R.drawable.light0_snowball);
                break;
            case 1:
                lightImage.setImageResource(R.drawable.light1_snowball);
                break;
            case 2:
                lightImage.setImageResource(R.drawable.light2_snowball);
                break;
            case 3:
                lightImage.setImageResource(R.drawable.light3_snowball);
                break;
        }
    }
}
