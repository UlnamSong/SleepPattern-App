package com.isteam.sleepapp.Activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.isteam.sleepapp.Bluetooth.BluetoothLeService;
import com.isteam.sleepapp.Module.TypefaceUtil;
import com.isteam.sleepapp.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_BLE_DEVICE = 2;

    private BluetoothAdapter mBluetoothAdapter;

    private ImageButton button1 = null;
    private ImageButton button2 = null;
    private ImageButton button3 = null;
    private ImageButton button4 = null;

    private String mDeviceName;
    private String mDeviceAddress;
    private BluetoothLeService mBluetoothLeService;
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypefaceUtil.loadTypeface(MainActivity.this);
        setContentView(R.layout.activity_main);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:

                break;

            case REQUEST_BLE_DEVICE:
                if (resultCode == DeviceScanDialogActivity.RESPONSE_BLE_DEVICE) {
                    Log.d(TAG, Thread.currentThread().getStackTrace()[1].getMethodName() + " : OK Bluetooth get device");
                    Intent result = data;
                    BluetoothDevice device = (BluetoothDevice)data.getParcelableExtra("device");
                    mDeviceName = device.getName();
                    mDeviceAddress = device.getAddress();
                    Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
                    bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
                    Log.d(TAG, Thread.currentThread().getStackTrace()[1].getMethodName() + " : Start Service Bind");
                } else {

                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_connect:
                startActivityForResult(new Intent(this, DeviceScanDialogActivity.class), REQUEST_BLE_DEVICE);
                break;

            case R.id.ib_light:
                startActivity(new Intent(MainActivity.this, LightActivity.class));
                overridePendingTransition(R.anim.fade, R.anim.hold);
                finish();
                break;

            case R.id.ib_sleep:
                startActivity(new Intent(MainActivity.this, ConditionActivity.class));
                overridePendingTransition(R.anim.fade, R.anim.hold);
                finish();
                break;

            case R.id.ib_temp:
                startActivity(new Intent(MainActivity.this, SleepMonitorActivity.class));
                overridePendingTransition(R.anim.fade, R.anim.hold);
                finish();
                break;
        }
    }


}
