package com.isteam.sleepapp.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.isteam.sleepapp.R;

import at.grabner.circleprogress.CircleProgressView;

public class LightActivity extends AppCompatActivity {

    private ImageButton button1 = null;
    private ImageButton button2 = null;
    private ImageButton button3 = null;
    private ImageButton button4 = null;

    private CircleProgressView illumGraph = null;

    private SeekBar lightSeekBar = null;
    private ImageView lightImage = null;

    private float illumVal = 66.70f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);

        button1 = (ImageButton) findViewById(R.id.imageButton);
        button2 = (ImageButton) findViewById(R.id.imageButton2);
        button3 = (ImageButton) findViewById(R.id.imageButton3);
        button4 = (ImageButton) findViewById(R.id.imageButton4);

        lightImage = (ImageView) findViewById(R.id.ivLightStatus);
        lightSeekBar = (SeekBar) findViewById(R.id.light_seekBar);

        illumGraph = (CircleProgressView) findViewById(R.id.illum_graph);

        setLightProgress(1);

        lightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //Toast.makeText(LightActivity.this, "i : " + i, Toast.LENGTH_SHORT).show();
                switch(i) {
                    case 0:
                        lightImage.setImageResource(R.drawable.light_i0);
                        requestLightValToPCB(i);
                        break;
                    case 1:
                        lightImage.setImageResource(R.drawable.light_i1);
                        requestLightValToPCB(i);
                        break;
                    case 2:
                        lightImage.setImageResource(R.drawable.light_i2);
                        requestLightValToPCB(i);
                        break;
                    case 3:
                        lightImage.setImageResource(R.drawable.light_i3);
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
                lightImage.setImageResource(R.drawable.light_i0);
                break;
            case 1:
                lightImage.setImageResource(R.drawable.light_i1);
                break;
            case 2:
                lightImage.setImageResource(R.drawable.light_i2);
                break;
            case 3:
                lightImage.setImageResource(R.drawable.light_i3);
                break;
        }
    }
}
