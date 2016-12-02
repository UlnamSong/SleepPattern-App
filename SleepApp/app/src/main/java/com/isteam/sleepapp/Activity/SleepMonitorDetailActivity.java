package com.isteam.sleepapp.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.isteam.sleepapp.R;

public class SleepMonitorDetailActivity extends AppCompatActivity {

    private String imageSrc_VeryBad = "";
    private ImageView sleep_condition;
    private ImageView sleep_condition_explain;

    // Sleep Condition Code (5 Level)
    private final int SLEEP_VERY_BAD = 1;
    private final int SLEEP_BAD = 2;
    private final int SLEEP_NORMAL = 3;
    private final int SLEEP_GOOD = 4;
    private final int SLEEP_VERY_NICE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_monitor_detail);

        setContent();

        setSleepConditionImage(SLEEP_VERY_NICE);
    }

    private void setContent() {
        sleep_condition = (ImageView) findViewById(R.id.condition_image);
        sleep_condition = (ImageView) findViewById(R.id.iv_sleep_detail);
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
