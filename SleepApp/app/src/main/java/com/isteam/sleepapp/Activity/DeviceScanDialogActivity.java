package com.isteam.sleepapp.Activity;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.isteam.sleepapp.Bluetooth.BluetoothLeService;
import com.isteam.sleepapp.Bluetooth.LeDeviceListAdapter;
import com.isteam.sleepapp.R;

import java.util.List;

public class DeviceScanDialogActivity extends AppCompatActivity {

    private static final String TAG = "DeviceScanDialog";
    public static final int RESPONSE_BLE_DEVICE = 101;

    private LeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;
    private ScanCallback scanCallback;
    private BluetoothAdapter.LeScanCallback mLeScanCallback;

    private boolean mScanning;
    private Handler mHandler;

    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    private ListView deviceListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_scan_dialog);
        mHandler = new Handler();

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        initCallback();
        initValue();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            startScan(true);
        } else {
            startScan(true);
        }
    }

    private void initValue() {
        mScanning = false;
        initView();
    }

    private void initView() {
        deviceListView = (ListView)findViewById(R.id.bluetooth_device_list);
        deviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getApplicationContext(), mLeDeviceListAdapter.getDevice(i).toString(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, Thread.currentThread().getStackTrace()[1].getMethodName() + " : " + mLeDeviceListAdapter.getDevice(i).toString());
                Intent result = new Intent();
                result.putExtra("device", mLeDeviceListAdapter.getDevice(i));
                setResult(RESPONSE_BLE_DEVICE, result);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

        // Initializes list view adapter.
        mLeDeviceListAdapter = new LeDeviceListAdapter(getApplicationContext());
        deviceListView.setAdapter(mLeDeviceListAdapter);
        startScan(true);
        Log.d(TAG, Thread.currentThread().getStackTrace()[1].getMethodName() + " : Start Scan");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        startScan(false);
        mLeDeviceListAdapter.clear();
    }

    private void startScan(final boolean enable) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
            if (enable && mBluetoothLeScanner != null) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mScanning = false;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            mBluetoothLeScanner.stopScan(scanCallback);
                        }
                        Log.d(TAG, Thread.currentThread().getStackTrace()[1].getMethodName() + " : Stop Scan");
                    }
                }, SCAN_PERIOD);
                Log.d(TAG, Thread.currentThread().getStackTrace()[1].getMethodName() + " : run Scan");
                mScanning = true;
                mBluetoothLeScanner.startScan(scanCallback);

            } else {
                mScanning = false;
                mBluetoothLeScanner.stopScan(scanCallback);
            }
        } else {
            if (enable) {
                // Stops scanning after a pre-defined scan period.
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mScanning = false;
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    }
                }, SCAN_PERIOD);

                mScanning = true;
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            } else {
                mScanning = false;
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            }
        }
    }

    private void initCallback() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            scanCallback = new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, final ScanResult result) {
                    super.onScanResult(callbackType, result);
                    Log.d(TAG, Thread.currentThread().getStackTrace()[1].getMethodName() + " : onScanResult");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                mLeDeviceListAdapter.addDevice(result.getDevice());
                            }
                            mLeDeviceListAdapter.notifyDataSetChanged();
                            Log.d(TAG, Thread.currentThread().getStackTrace()[1].getMethodName() + " : addDevice");
                        }
                    });
                }

                @Override
                public void onBatchScanResults(List<ScanResult> results) {
                    super.onBatchScanResults(results);
                    Log.d(TAG, Thread.currentThread().getStackTrace()[1].getMethodName() + " : onBatchScanResults");
                }

                @Override
                public void onScanFailed(int errorCode) {
                    super.onScanFailed(errorCode);
                    Log.d(TAG, Thread.currentThread().getStackTrace()[1].getMethodName() + " : onScanFailed");
                }
            };
        } else {
            mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    Log.d(TAG, Thread.currentThread().getStackTrace()[1].getMethodName() + " : onLeScan");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mLeDeviceListAdapter.addDevice(device);
                            mLeDeviceListAdapter.notifyDataSetChanged();
                            Log.d(TAG, Thread.currentThread().getStackTrace()[1].getMethodName() + " : addDevice");
                        }
                    });
                }
            };
        }
    }

}
