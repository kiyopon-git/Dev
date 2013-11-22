package com.aifull;

import java.util.ArrayList;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;


public class DeviceListActivity extends Activity {
    // Debugging
	private static final String TAG = "Aifull";
    private static final boolean D = true;

    // Return Intent extra
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    public static final String DETECT_TYPE = "detect_type";
    public static final int PIRED = 1;
    public static final int NEW = 2;
    private int resultData;
    private ArrayList<String> PairedAdressList;
    private ArrayList<String> NewAdressList;
    
    // Member fields
    private BluetoothAdapter mBtAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
     // Setup the window
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.device_list);

        // Set result CANCELED incase the user backs out
        setResult(Activity.RESULT_CANCELED);
        
        Log.d(TAG, "onCreate DeviceListActivity");
        // Set result CANCELED incase the user backs out
        setResult(Activity.RESULT_CANCELED);

     // Get the local Bluetooth adapter
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        
        PairedAdressList = new ArrayList<String>();
        NewAdressList = new ArrayList<String>();
        
        Log.d(TAG, "onCreate Register DeviceListActivity");
        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);
        
     // to perform device discovery
        doDiscovery();

    }
    
    @Override protected void  onStart(){
    	super.onStart();
    	
    	Log.d(TAG, "onStart DeviceListActivity");
    	Intent intent = getIntent();
    	if(intent != null){
    		Bundle extras = getIntent().getExtras();
    		resultData = extras.getInt(DETECT_TYPE);
    		
    		if(resultData == PIRED){
    			getPairedDevicesArrayList();
    		}else if(resultData == NEW){
    			getNewDivicesArrayList();
    		}
    	}else{
    		Toast.makeText(getApplicationContext(), "検出できる端末がありませんでした。", Toast.LENGTH_SHORT).show();
    		finish();
    	}
    	
    }
    
    private void getPairedDevicesArrayList(){
    	Log.d(TAG, "getPairedDevicesArrayList() DeviceListActivity");
    	
    	Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
    	
    	if (pairedDevices.size() > 0) {
            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                PairedAdressList.add(device.getAddress());
            }
        }
    	
    	Intent intent = new Intent();
        intent.putStringArrayListExtra(EXTRA_DEVICE_ADDRESS, PairedAdressList);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
    
    private void getNewDivicesArrayList(){
    	Log.d(TAG, "getNewDivicesArrayList() DeviceListActivity");
    	
    	//12秒待ち
    	try {
			Thread.sleep(12000);
		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
    	
    	Intent intent = new Intent();
        intent.putStringArrayListExtra(EXTRA_DEVICE_ADDRESS, NewAdressList);
        setResult(Activity.RESULT_OK, intent);
        finish();
    	
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Make sure we're not doing discovery anymore
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }

        // Unregister broadcast listeners
        this.unregisterReceiver(mReceiver);
    }

    /**
     * Start device discover with the BluetoothAdapter
     */
    private void doDiscovery() {
        if (D) Log.d(TAG, "doDiscovery()");

        // If we're already discovering, stop it
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        mBtAdapter.startDiscovery();
    }

    // The BroadcastReceiver that listens for discovered devices and
    // changes the title when discovery is finished
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    NewAdressList.add(device.getAddress());
                }
            }
        }
    };

}
