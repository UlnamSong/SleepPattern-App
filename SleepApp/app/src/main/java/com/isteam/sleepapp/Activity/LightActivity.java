package com.isteam.sleepapp.Activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.isteam.sleepapp.Bluetooth.BluetoothLeService;
import com.isteam.sleepapp.Bluetooth.SampleGattAttributes;
import com.isteam.sleepapp.Module.TypefaceUtil;
import com.isteam.sleepapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import at.grabner.circleprogress.CircleProgressView;

public class LightActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_BLE_DEVICE = 2;

    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";

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

    //	App -> Mat.
    private static final String CMD_REQ_MAT_STT		= "e01000";
    private static final String CMD_REQ_MOVEMENT_STT= "e01100";		//	뒤척임.
    private static final String CMD_REQ_MAT_SET_ON 		= "e0200311";
    private static final String CMD_REQ_MAT_SET_OFF 	= "e0200310";
    private static final String CMD_REQ_MOVEMENT_RST = "e02100";    // 뒤척임 reset

    private static final String CMD_REQ_LIGHT_FULL = "e020010a";
    private static final String CMD_REQ_LIGHT_THIRD = "e200103";
    private static final String CMD_REQ_LIGHT_SEVEN = "e200107";
    private static final String CMD_REQ_LIGHT_NO= "e0200100";

    private static final String CMD_REQ_MOVMENT_SET_ON 	= "e0210211";	//	뒤척임 ON.
    private static final String CMD_REQ_MOVMENT_SET_OFF = "e0210210";	//	뒤척임 OFF.

    //	Mat -> App.
    private static final String CMD_ACK_MAT_STT_ON 		= "e0300911";
    private static final String CMD_ACK_MAT_STT_OFF 	= "e0300910";
    private static final String CMD_ACK_MOVMENT_STT_ON 	= "e0310611";	//	뒤척임 ON.
    private static final String CMD_ACK_MOVMENT_STT_OFF = "e0310610";	//	뒤척임 OFF.



    public static final String EVNT_TEMPER_DOWN 	= "EVNT_TEMPER_DOWN";
    public static final String EVNT_TEMPER_UP 	= "EVNT_TEMPER_UP";


    public static final int VIEWTP_ENVIRONMENT	= 1;
    public static final int VIEWTP_SUBTITLE	= 2;


    public static final int TEMPERCHKTP_SINGLE	= 1;
    public static final int TEMPERCHKTP_LIST	= 2;

    public static int crntTmprChkTp	= TEMPERCHKTP_SINGLE;


    private boolean mConnected = false;
    private String mDeviceName;
    private String mDeviceAddress;
    public static BluetoothLeService mBluetoothLeService;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGattCharacteristic bluetoothGattCharacteristic;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothGattCharacteristic mNotifyCharacteristic;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + " : connect");
            mBluetoothGatt = mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + " : disconnect");
            mBluetoothLeService = null;
        }
    };

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
        textView = (TextView) findViewById(R.id.tvMainTitle);

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
                        callSendMessage(CMD_REQ_LIGHT_NO);
                        lightImage.setImageResource(R.drawable.light0_snowball);
                        requestLightValToPCB(i);
                        break;
                    case 1:
                        // ISTeam
                        //lightImage.setImageResource(R.drawable.light_i1);

                        // Snowball
                        callSendMessage(CMD_REQ_LIGHT_THIRD);
                        lightImage.setImageResource(R.drawable.light1_snowball);
                        requestLightValToPCB(i);
                        break;
                    case 2:
                        // ISTeam
                        //lightImage.setImageResource(R.drawable.light_i2);

                        // Snowball
                        callSendMessage(CMD_REQ_LIGHT_SEVEN);
                        lightImage.setImageResource(R.drawable.light2_snowball);
                        requestLightValToPCB(i);
                        break;
                    case 3:
                        // ISTeam
                        //lightImage.setImageResource(R.drawable.light_i3);

                        // Snowball
                        callSendMessage(CMD_REQ_LIGHT_FULL);
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

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + " : registerReceiver");
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + " : connected");
                mConnected = true;
                //mBluetoothLeService.writeCharacteristic();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + " : disconnected");
                mConnected = false;

            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + " : discovered");
                // Show all the supported services and characteristics on the user interface.
                //displayGattServices(mBluetoothLeService.getSupportedGattServices());
                callSendMessage(CMD_REQ_MOVEMENT_RST);
                getGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + " : available");
                Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + " : " + intent.getStringExtra("data"));
            }
        }
    };

    private void callSendMessage(String message) {
        List<BluetoothGattService> gattServices = mBluetoothLeService.getSupportedGattServices();

        if (gattServices == null) {
            Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + " : Can't call Message");
            return;
        }

        String uuid;
        for (BluetoothGattService service : gattServices) {
            List<BluetoothGattCharacteristic> gattCharacteristics = service.getCharacteristics();
            for (BluetoothGattCharacteristic characteristic : gattCharacteristics) {
                uuid = characteristic.getUuid().toString();
                //Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + " : " + uuid);
                if (uuid.contains("0000ff01")) {
                    ArrayList<Integer>	t_arrInt	= new ArrayList<Integer>();
                    ArrayList<Integer> 	t_arrTmpr	= new ArrayList<Integer>();
                    t_arrInt.add( 0xe0 );

                    if( CMD_REQ_MAT_STT.equals(message)) {
                        t_arrInt.add( 0x10 );		//	cmd code.
                        t_arrInt.add( 0x00 );		//	length.

                    }
                    else if( CMD_REQ_MOVEMENT_STT.equals(message)) {
                        t_arrInt.add( 0x11 );		//	cmd code.
                        t_arrInt.add( 0x00 );		//	length.

                    }
                    else if (CMD_REQ_MAT_SET_ON.equals(message)) {
                        t_arrInt.add(0x20);        //	cmd code.
                        t_arrInt.add(0x03);        //	length.
                        t_arrInt.add(0x11);        //	sub cmd (mat on).
                        t_arrTmpr	= getTemperateTwoBytesWithInteger(270);
                        t_arrInt.add( t_arrTmpr.get(1) );
                        t_arrInt.add( t_arrTmpr.get(0) );
                    }
                    else if (CMD_REQ_MAT_SET_OFF.equals(message)) {
                        t_arrInt.add( 0x20 );
                        t_arrInt.add( 0x03 );
                        t_arrInt.add( 0x10 );
                        t_arrTmpr	= getTemperateTwoBytesWithInteger(270);
                        t_arrInt.add( t_arrTmpr.get(1) );
                        t_arrInt.add( t_arrTmpr.get(0) );
                    }
                    else if (CMD_REQ_MOVEMENT_RST.equals(message)) {
                        t_arrInt.add(0x21);        //	cmd code.
                        t_arrInt.add(0x00);
                        t_arrInt.add(0x10);
                    }
                    else if (CMD_REQ_LIGHT_FULL.equals(message)) {
                        t_arrInt.add(0x20);        //	cmd code.
                        t_arrInt.add(0x01);
                        t_arrInt.add(0x0a);
                    }
                    else if (CMD_REQ_LIGHT_NO.equals(message)) {
                        t_arrInt.add(0x20);        //	cmd code.
                        t_arrInt.add(0x01);
                        t_arrInt.add(0x00);
                    }
                    else if (CMD_REQ_LIGHT_SEVEN.equals(message)) {
                        t_arrInt.add(0x20);        //	cmd code.
                        t_arrInt.add(0x01);
                        t_arrInt.add(0x07);
                    }
                    else if (CMD_REQ_LIGHT_THIRD.equals(message)) {
                        t_arrInt.add(0x20);        //	cmd code.
                        t_arrInt.add(0x01);
                        t_arrInt.add(0x03);
                    }
                    int	t_chksum = getCheckSumOfList( t_arrInt  );

                    t_arrInt.add( t_chksum );
                    t_arrInt.add( 0xe1 );

                    byte[] strBytes	= new byte[t_arrInt.size()];
                    int	m;
                    for( m=0; m<t_arrInt.size(); m++ ){
                        strBytes[m]	= (byte)((int)t_arrInt.get(m));
                    }

                    characteristic.setValue(strBytes);
                    characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);

                    if (mBluetoothGatt != null) {
                        Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + " : " + message);
                        Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + " : writeCharacteristic");
                        Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + " : " + characteristic);
                        mBluetoothGatt.writeCharacteristic(characteristic);
                    }
                }
            }
        }

    }

    private ArrayList<Integer> getTemperateTwoBytesWithInteger ( int s_temper ){
        ArrayList<Integer> t_arrTmpr	= new ArrayList<Integer>();
        int 	t_firstInt	= (int)Math.floor(s_temper / 256);
        int 	t_secondInt	= (int)Math.floor(s_temper % 256);

        t_arrTmpr.add( t_firstInt );
        t_arrTmpr.add( t_secondInt );
        return	t_arrTmpr;
    }

    private int getCheckSumOfList( ArrayList<Integer> s_arrInt ){
        int		t_res	= 0;
        int		m = 0;
        for( m=0; m<s_arrInt.size(); m++ ){
            t_res	= t_res ^ s_arrInt.get(m);
        }
        return	t_res;
    }

    private void getGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null)
            return;
        String uuid = null;
        String unknownServiceString = getResources().getString(R.string.unknown_service);
        String unknownCharaString = getResources().getString(R.string.unknown_characteristic);

        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData = new ArrayList<ArrayList<HashMap<String, String>>>();
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {

            //HashMap
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(
                    LIST_NAME, SampleGattAttributes.lookup(uuid, unknownServiceString));
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            //Hash's ArrayList
            ArrayList<HashMap<String, String>> gattCharacteristicGroupData =
                    new ArrayList<HashMap<String, String>>();
            //List BluetoothGattCharacteristic
            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();
            //ArrayList BluetoothGattCharacteristic
            ArrayList<BluetoothGattCharacteristic> charas =
                    new ArrayList<BluetoothGattCharacteristic>();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic); //input to ArrayList
                HashMap<String, String> currentCharaData = new HashMap<String, String>(); //Make HashMap
                uuid = gattCharacteristic.getUuid().toString();
                if (uuid.contains("0000ff01")) {
                    bluetoothGattCharacteristic = gattCharacteristic;
                }
                currentCharaData.put(
                        LIST_NAME, SampleGattAttributes.lookup(uuid, unknownCharaString));
                currentCharaData.put(LIST_UUID, uuid);
                gattCharacteristicGroupData.add(currentCharaData);
            }
            mGattCharacteristics.add(charas);
            gattCharacteristicData.add(gattCharacteristicGroupData);
        }

        /*
        Log.d(TAG, "================================================");
        for (ArrayList<HashMap<String, String>> tmp : gattCharacteristicData) {
            for (HashMap<String, String> tmp1 : tmp) {

                Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + " : " + tmp1);
            }
            Log.d(TAG, "================================================");
        }
        */


    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

}
