package com.isteam.sleepapp.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.isteam.sleepapp.R;

public class SleepMonitorActivity extends AppCompatActivity {

    private String imageSrc_VeryBad = "";
    private ImageView sleep_condition;
    private ImageButton start_stop_button;

    private boolean isStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_monitor);

        setContent();

        // Example Status
        sleep_condition.setImageResource(R.drawable.very_bad);


        // START / STOP Button Click Listener
        start_stop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
    }

    private void setContent() {
        sleep_condition = (ImageView) findViewById(R.id.condition_image);
        start_stop_button = (ImageButton) findViewById(R.id.start_stop_btn);
    }


    // Bluetooth Request Methods : For Start/Stop Button Click Event
    private void startButton() {

    }

    private void stopButton() {

    }
}
