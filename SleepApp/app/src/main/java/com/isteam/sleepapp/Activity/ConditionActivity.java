package com.isteam.sleepapp.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.isteam.sleepapp.R;

import at.grabner.circleprogress.CircleProgressView;

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
        InitializeActivity.loadTypeface(ConditionActivity.this, TYPEFACE_NAME, TYPEFACE_NAME_BOLD, TYPEFACE_NAME_EXBOLD);
        InitializeActivity.setContentView(ConditionActivity.this, R.layout.activity_condition);

        setContent();
        setDataToGraph();

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ConditionActivity.this, LightActivity.class));
                overridePendingTransition(R.anim.fade, R.anim.hold);
                finish();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ConditionActivity.this, ConditionActivity.class));
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

        tvTempVal.setText(currentTemp + " ˚C");
        tvHumidVal.setText(currentHumid + " %");
        tvBrightVal.setText(currentIllum + " lux");
        tvSoundVal.setText(currentNoise + " dB");
    }

    private void setContent() {
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

    private void loadTypeface() {

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
