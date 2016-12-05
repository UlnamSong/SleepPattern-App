package com.isteam.sleepapp.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.isteam.sleepapp.Module.TypefaceUtil;
import com.isteam.sleepapp.R;

import at.grabner.circleprogress.CircleProgressView;

public class ConditionActivity extends AppCompatActivity {

    private TextView tvTitle = null;
    private TextView tvTemp = null;
    private TextView tvTempVal = null;
    private TextView tvHumid = null;
    private TextView tvHumidVal = null;
    private TextView tvBright = null;
    private TextView tvBrightVal = null;
    private TextView tvSound = null;
    private TextView tvSoundVal = null;

    private ImageButton button1 = null;
    private ImageButton button2 = null;
    private ImageButton button3 = null;
    private ImageButton button4 = null;

    private CircleProgressView tempGraph = null;
    private CircleProgressView humidGraph = null;
    private CircleProgressView illumGraph = null;
    private CircleProgressView noiseGraph = null;

    // Sensor Data
    private float currentTemp = 33.0f;
    private float currentHumid = 15.7f;
    private float currentIllum = 60.5f;
    private float currentNoise = 15.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypefaceUtil.loadTypeface(ConditionActivity.this);
        setContentView(R.layout.activity_condition_snowball);

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
        setDataToGraph();

        // Bluetooth Button
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ConditionActivity.this, "Bluetooth Connect", Toast.LENGTH_SHORT).show();
            }
        });

        // Light Button
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ConditionActivity.this, LightActivity.class));
                overridePendingTransition(R.anim.fade, R.anim.hold);
                finish();
            }
        });

        // Sleep Monitor Button
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ConditionActivity.this, SleepMonitorActivity.class));
                overridePendingTransition(R.anim.fade, R.anim.hold);
                finish();
            }
        });
    }

    // Set Graph Value as Upper Value
    private void setDataToGraph() {
        tempGraph.setValue(currentTemp);
        humidGraph.setValue(currentHumid);
        illumGraph.setValue(currentIllum);
        noiseGraph.setValue(currentNoise);

        tvTempVal.setText(currentTemp + " ℃");
        tvHumidVal.setText(currentHumid + " %");
        tvBrightVal.setText(currentIllum + " lux");
        tvSoundVal.setText(currentNoise + " dB");
    }

    private void setContent() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setTypeface(TypefaceUtil.typeface_1);

        tvTemp = (TextView) findViewById(R.id.tv_temptitle);
        tvTemp.setTypeface(TypefaceUtil.typeface_2);

        tvTempVal = (TextView) findViewById(R.id.tv_tempVal);
        tvTempVal.setTypeface(TypefaceUtil.typeface_4);

        tvHumid = (TextView) findViewById(R.id.tv_humidtitle);
        tvHumid.setTypeface(TypefaceUtil.typeface_2);

        tvHumidVal = (TextView) findViewById(R.id.tv_humidVal);
        tvHumidVal.setTypeface(TypefaceUtil.typeface_4);

        tvBright = (TextView) findViewById(R.id.tv_brighttitle);
        tvBright.setTypeface(TypefaceUtil.typeface_2);

        tvBrightVal = (TextView) findViewById(R.id.tv_brightVal);
        tvBrightVal.setTypeface(TypefaceUtil.typeface_4);

        tvSound = (TextView) findViewById(R.id.tv_soundtitle);
        tvSound.setTypeface(TypefaceUtil.typeface_2);

        tvSoundVal = (TextView) findViewById(R.id.tv_soundVal);
        tvSoundVal.setTypeface(TypefaceUtil.typeface_4);

        button1 = (ImageButton) findViewById(R.id.imageButton);
        button2 = (ImageButton) findViewById(R.id.imageButton2);
        button3 = (ImageButton) findViewById(R.id.imageButton3);
        button4 = (ImageButton) findViewById(R.id.imageButton4);

        // 일단 최대값을 100으로 설정하고 진행함
        // 추후 그래프 활성화하기 때문에 구체적인 값을 나중에 작업 진행
        tempGraph = (CircleProgressView) findViewById(R.id.temp_graph);
        tempGraph.setMaxValue(100.0f);

        humidGraph = (CircleProgressView) findViewById(R.id.humid_graph);
        humidGraph.setMaxValue(100.0f);

        illumGraph = (CircleProgressView) findViewById(R.id.illum_graph);
        illumGraph.setMaxValue(100.0f);

        noiseGraph = (CircleProgressView) findViewById(R.id.noise_graph);
        noiseGraph.setMaxValue(100.0f);
    }
}
