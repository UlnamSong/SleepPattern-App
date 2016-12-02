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
import android.widget.TextView;
import android.widget.Toast;

import com.isteam.sleepapp.Module.TypefaceUtil;
import com.isteam.sleepapp.R;

public class SleepMonitorDetailActivity extends AppCompatActivity {

    private ImageView sleep_condition;
    private ImageView sleep_condition_explain;

    // Sleep Condition Code (5 Level)
    private final int SLEEP_VERY_BAD = 1;
    private final int SLEEP_BAD = 2;
    private final int SLEEP_NORMAL = 3;
    private final int SLEEP_GOOD = 4;
    private final int SLEEP_VERY_NICE = 5;

    private ImageButton button1 = null;
    private ImageButton button2 = null;
    private ImageButton button3 = null;
    private ImageButton button4 = null;

    private TextView tvTitle = null;
    private TextView textView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypefaceUtil.loadTypeface(SleepMonitorDetailActivity.this);
        setContentView(R.layout.activity_sleep_monitor_detail_snowball);

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

        setSleepConditionImage(SLEEP_VERY_NICE);


        // Bluetooth Button
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SleepMonitorDetailActivity.this, "Bluetooth Connect", Toast.LENGTH_SHORT).show();
            }
        });

        // Light Button
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SleepMonitorDetailActivity.this, LightActivity.class));
                overridePendingTransition(R.anim.fade, R.anim.hold);
                finish();
            }
        });

        // Condition Button
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SleepMonitorDetailActivity.this, ConditionActivity.class));
                overridePendingTransition(R.anim.fade, R.anim.hold);
                finish();
            }
        });
    }

    private void setContent() {
        sleep_condition = (ImageView) findViewById(R.id.condition_image);
        sleep_condition_explain = (ImageView) findViewById(R.id.iv_sleep_detail);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        textView = (TextView) findViewById(R.id.textView);

        tvTitle.setTypeface(TypefaceUtil.typeface_1);
        textView.setTypeface(TypefaceUtil.typeface_1);

        button1 = (ImageButton) findViewById(R.id.imageButton);
        button2 = (ImageButton) findViewById(R.id.imageButton2);
        button3 = (ImageButton) findViewById(R.id.imageButton3);
        button4 = (ImageButton) findViewById(R.id.imageButton4);
    }

    // Set Images to status image & explanation image
    private void setSleepConditionImage(int status) {
        switch(status) {
            case SLEEP_VERY_BAD:
                sleep_condition.setImageResource(R.drawable.very_bad);
                sleep_condition_explain.setImageResource(R.drawable.very_bad_explain);
                break;
            case SLEEP_BAD:
                sleep_condition.setImageResource(R.drawable.bad);
                sleep_condition_explain.setImageResource(R.drawable.bad_explain);
                break;
            case SLEEP_NORMAL:
                sleep_condition.setImageResource(R.drawable.normal);
                sleep_condition_explain.setImageResource(R.drawable.normal_explain);
                break;
            case SLEEP_GOOD:
                sleep_condition.setImageResource(R.drawable.good);
                sleep_condition_explain.setImageResource(R.drawable.good_explain);
                break;
            case SLEEP_VERY_NICE:
                sleep_condition.setImageResource(R.drawable.very_nice);
                sleep_condition_explain.setImageResource(R.drawable.very_nice_explain);
                break;
        }
    }
}
