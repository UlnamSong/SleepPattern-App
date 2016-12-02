package com.isteam.sleepapp.Activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.isteam.sleepapp.Module.TypefaceUtil;
import com.isteam.sleepapp.R;

import at.grabner.circleprogress.CircleProgressView;

public class TrippingActivity extends AppCompatActivity {

    private TextView tvTitle = null;
    private TextView tvMainTitle = null;

    private TextView tvValue1 = null;
    private TextView tvValue2 = null;
    private TextView tvValue3 = null;
    private TextView tvValue4 = null;

    private TextView tvGraphTitle = null;
    private TextView tvGraphValue = null;

    private ImageButton button1 = null;
    private ImageButton button2 = null;
    private ImageButton button3 = null;
    private ImageButton button4 = null;

    private CircleProgressView graph = null;


    // Data Processing
    private int moving_count = 47;
    private int detect_count = 100;

    private int moving_percent = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypefaceUtil.loadTypeface(TrippingActivity.this);
        setContentView(R.layout.activity_tripping);

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


        setContent();

        // Data Processing
        moving_percent = (int)(((float)moving_count / (float)detect_count) * 100.0f);

        tvValue1.setText(moving_count + "");
        tvValue3.setText(detect_count + "");

        tvGraphValue.setText(moving_percent + "%");

        graph.setMaxValue(100);
        graph.setValue(moving_percent);
        // End Data Processing

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TrippingActivity.this, SleepMonitorActivity.class));
                overridePendingTransition(R.anim.fade, R.anim.hold);
                finish();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TrippingActivity.this, "서비스 준비중입니다.", Toast.LENGTH_SHORT).show();
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TrippingActivity.this, "서비스 준비중입니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setContent() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvMainTitle = (TextView) findViewById(R.id.tvMainTitle);

        graph = (CircleProgressView) findViewById(R.id.moving_graph);

        tvValue1 = (TextView) findViewById(R.id.textViewValue1);
        tvValue2 = (TextView) findViewById(R.id.textViewValue2);
        tvValue3 = (TextView) findViewById(R.id.textViewValue3);
        tvValue4 = (TextView) findViewById(R.id.textViewValue4);

        button1 = (ImageButton) findViewById(R.id.imageButton);
        button2 = (ImageButton) findViewById(R.id.imageButton2);
        button3 = (ImageButton) findViewById(R.id.imageButton3);
        button4 = (ImageButton) findViewById(R.id.imageButton4);

        tvGraphTitle = (TextView) findViewById(R.id.tvGraphTitle);
        tvGraphValue = (TextView) findViewById(R.id.tvGraphValue);

        tvTitle.setTypeface(TypefaceUtil.typeface_1);
        tvMainTitle.setTypeface(TypefaceUtil.typeface_2);

        tvValue1.setTypeface(TypefaceUtil.typeface_4);
        tvValue2.setTypeface(TypefaceUtil.typeface_2);
        tvValue3.setTypeface(TypefaceUtil.typeface_2);
        tvValue4.setTypeface(TypefaceUtil.typeface_2);

        tvGraphTitle.setTypeface(TypefaceUtil.typeface_2);
        tvGraphValue.setTypeface(TypefaceUtil.typeface_4);
    }
}
