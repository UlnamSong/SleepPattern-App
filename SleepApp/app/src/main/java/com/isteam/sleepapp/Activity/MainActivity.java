package com.isteam.sleepapp.Activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.icu.text.LocaleDisplayNames;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;

import com.isteam.sleepapp.Bluetooth.BluetoothLeService;
import com.isteam.sleepapp.Bluetooth.SampleGattAttributes;
import com.isteam.sleepapp.Module.TypefaceUtil;
import com.isteam.sleepapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_BLE_DEVICE = 2;

    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";

    private ImageButton button1 = null;
    private ImageButton button2 = null;
    private ImageButton button3 = null;
    private ImageButton button4 = null;


    //	App -> Mat.
    private static final String CMD_REQ_MAT_STT		= "e01000";
    private static final String CMD_REQ_MOVEMENT_STT= "e01100";		//	뒤척임.
    private static final String CMD_REQ_MAT_SET_ON 		= "e0200311";
    private static final String CMD_REQ_MAT_SET_OFF 	= "e0200310";
    private static final String CMD_REQ_MOVEMENT_RST = "e02100";    // 뒤척임 reset

    private static final String CMD_REQ_LIGHT_FULL = "e02000";
    private static final String CMD_REQ_LIGHT_NO= "e02001";

    private static final String CMD_REQ_ROOM_CONDITION = "e01000";

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
    private BluetoothLeService mBluetoothLeService;
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
        TypefaceUtil.loadTypeface(MainActivity.this);
        setContentView(R.layout.activity_main_snowball);

        checkBluetoothUsable();
        initButton();

    }

    private void initButton() {
        button1 = (ImageButton) findViewById(R.id.ib_connect);
        button2 = (ImageButton) findViewById(R.id.ib_light);
        button3 = (ImageButton) findViewById(R.id.ib_temp);
        button4 = (ImageButton) findViewById(R.id.ib_sleep);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
    }

    /**
     * Check User's device can use Bluetooth
     */
    private void checkBluetoothUsable() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(getApplicationContext(), "지원하지 않는 디바이스입니다.", Toast.LENGTH_SHORT).show();
        }

        final BluetoothManager bluetoothManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        if (mBluetoothAdapter != null || !mBluetoothAdapter.isEnabled()) {
            startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQUEST_ENABLE_BT);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_connect:
                startActivityForResult(new Intent(this, DeviceScanDialogActivity.class), REQUEST_BLE_DEVICE);
                break;

            case R.id.ib_light:
                /*
                startActivity(new Intent(MainActivity.this, LightActivity.class));
                overridePendingTransition(R.anim.fade, R.anim.hold);
                finish();
                */
                callSendMessage(CMD_REQ_LIGHT_FULL);
                break;

            case R.id.ib_sleep:
                /*
                startActivity(new Intent(MainActivity.this, SleepActivity.class));
                overridePendingTransition(R.anim.fade, R.anim.hold);
                finish();
                */
                //mBluetoothLeService.readCharacteristic(bluetoothGattCharacteristic);
//                callSendMessage(CMD_REQ_LIGHT_FULL);

                callSendMessage(CMD_REQ_ROOM_CONDITION);
//
                // 자기꺼 받아오는 코드
//                if (bluetoothGattCharacteristic != null) {
//                    Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + " : " + bluetoothGattCharacteristic);
//                    mBluetoothLeService.readCharacteristic(bluetoothGattCharacteristic);
//                }

                break;

            case R.id.ib_temp:
                /*
                startActivity(new Intent(MainActivity.this, SleepMonitorActivity.class));
                overridePendingTransition(R.anim.fade, R.anim.hold);
                finish();
                */
                callSendMessage(CMD_REQ_LIGHT_NO);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:

                break;

            case REQUEST_BLE_DEVICE:
                if (resultCode == DeviceScanDialogActivity.RESPONSE_BLE_DEVICE) {
                    Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + " : OK Bluetooth get device");
                    Intent result = data;
                    BluetoothDevice device = data.getParcelableExtra("device");
                    mDeviceName = device.getName();
                    mDeviceAddress = device.getAddress();
                    Log.d(TAG, result.toString());
                    Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
                    bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
                    Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + " : Start Service Bind");
                } else {

                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + " : registerReceiver");
    }

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
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
                //callSendMessage(CMD_REQ_MOVEMENT_RST);
                getGattServices(mBluetoothLeService.getSupportedGattServices());

            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                //byte[] TotalByteMessage = intent.getByteArrayExtra("data");
                String string = intent.getStringExtra("data");
                byte[] strBytes	= intent.getByteArrayExtra(BluetoothLeService.EXTRA_BYTESDATA);
                //byte[] strBytes	= intent.getByteArrayExtra("data");
                //String byteToString = new String(strBytes,0,strBytes.length);


                Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + " : available");
                Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + " : " + intent.getStringExtra("data"));
                Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + " : " + strBytes);
                //Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + " : " + byteToString);
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
                    } else if (CMD_REQ_ROOM_CONDITION.equals(message)) {
                        t_arrInt.add(0x10);        //   cmd code.
                        t_arrInt.add(0x00);        //   length
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

    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        String unknownServiceString = getResources().getString(R.string.unknown_service);
        String unknownCharaString = getResources().getString(R.string.unknown_characteristic);
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData
                = new ArrayList<ArrayList<HashMap<String, String>>>();
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(
                    LIST_NAME, SampleGattAttributes.lookup(uuid, unknownServiceString));
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData =
                    new ArrayList<HashMap<String, String>>();
            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas =
                    new ArrayList<BluetoothGattCharacteristic>();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<String, String>();
                uuid = gattCharacteristic.getUuid().toString();
                currentCharaData.put(
                        LIST_NAME, SampleGattAttributes.lookup(uuid, unknownCharaString));
                currentCharaData.put(LIST_UUID, uuid);
                gattCharacteristicGroupData.add(currentCharaData);
            }
            mGattCharacteristics.add(charas);
            gattCharacteristicData.add(gattCharacteristicGroupData);
        }

        SimpleExpandableListAdapter gattServiceAdapter = new SimpleExpandableListAdapter(
                this,
                gattServiceData,
                android.R.layout.simple_expandable_list_item_2,
                new String[] {LIST_NAME, LIST_UUID},
                new int[] { android.R.id.text1, android.R.id.text2 },
                gattCharacteristicData,
                android.R.layout.simple_expandable_list_item_2,
                new String[] {LIST_NAME, LIST_UUID},
                new int[] { android.R.id.text1, android.R.id.text2 }
        );
        //mGattServicesList.setAdapter(gattServiceAdapter);
    }


    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBluetoothLeService != null) {
            mBluetoothLeService.close();
            unbindService(mServiceConnection);
        }
        if (mGattUpdateReceiver != null)
            unregisterReceiver(mGattUpdateReceiver);
    }
}
