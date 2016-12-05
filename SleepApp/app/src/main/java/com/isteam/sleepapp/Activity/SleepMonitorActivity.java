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

public class SleepMonitorActivity extends AppCompatActivity {

    private String imageSrc_VeryBad = "";
    private ImageView sleep_condition;
    private ImageButton start_stop_button;
    private ImageButton result_button;

    private ImageButton button1 = null;
    private ImageButton button2 = null;
    private ImageButton button3 = null;
    private ImageButton button4 = null;

    private TextView tvTitle = null;
    private TextView textView = null;

    private boolean isStarted = false;
    private int touchCount = 0;

    // Received Value by Bluetooth
    private int moving_value = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypefaceUtil.loadTypeface(SleepMonitorActivity.this);

        setContentView(R.layout.activity_sleep_monitor_snowball);

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

        // Example Status
        sleep_condition.setImageResource(R.drawable.very_bad);


        // START / STOP Button Click Listener
        start_stop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                touchCount++;

                if(isStarted) {
                    isStarted = false;
                    start_stop_button.setImageResource(R.drawable.start_button);

                    // Request Bluetooth Command
                    stopButton();

                    Toast.makeText(SleepMonitorActivity.this, "STOP!", Toast.LENGTH_SHORT).show();

                } else {
                    isStarted = true;
                    start_stop_button.setImageResource(R.drawable.stop_button);

                    // Request Bluetooth Command
                    startButton();

                    Toast.makeText(SleepMonitorActivity.this, "START!", Toast.LENGTH_SHORT).show();

                }

            }
        });

        // Result Button
        result_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(touchCount < 2) {
                    if(isStarted) {
                        Toast.makeText(SleepMonitorActivity.this, "STOP detection first!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SleepMonitorActivity.this, "No Data! Detection First!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Intent intent = new Intent(SleepMonitorActivity.this, SleepMonitorDetailActivity.class);
                    intent.putExtra("moving_value", moving_value);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade, R.anim.hold);
                    finish();
                }
            }
        });


        // Bluetooth Button
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SleepMonitorActivity.this, "Bluetooth Connect", Toast.LENGTH_SHORT).show();
            }
        });

        // Light Button
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SleepMonitorActivity.this, LightActivity.class));
                overridePendingTransition(R.anim.fade, R.anim.hold);
                finish();
            }
        });

        // Condition Button
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SleepMonitorActivity.this, ConditionActivity.class));
                overridePendingTransition(R.anim.fade, R.anim.hold);
                finish();
            }
        });

        // Sleep Monitor Button
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SleepMonitorActivity.this, SleepMonitorActivity.class));
                overridePendingTransition(R.anim.fade, R.anim.hold);
                finish();
            }
        });
    }

    private void setContent() {
        sleep_condition = (ImageView) findViewById(R.id.condition_image);
        start_stop_button = (ImageButton) findViewById(R.id.start_stop_btn);
        result_button = (ImageButton) findViewById(R.id.result_btn);


        tvTitle = (TextView) findViewById(R.id.tv_title);
        textView = (TextView) findViewById(R.id.tvMainTitle);

        tvTitle.setTypeface(TypefaceUtil.typeface_1);
        textView.setTypeface(TypefaceUtil.typeface_1);

        button1 = (ImageButton) findViewById(R.id.imageButton);
        button2 = (ImageButton) findViewById(R.id.imageButton2);
        button3 = (ImageButton) findViewById(R.id.imageButton3);
        button4 = (ImageButton) findViewById(R.id.imageButton4);
    }


    // Bluetooth Request Methods : For Start/Stop Button Click Event
    private void startButton() {

    }

    private void stopButton() {

    }
}
