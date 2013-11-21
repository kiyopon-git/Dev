package com.aifull;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class Aifull extends Activity {
	
	 //For Debugging
    private static final String TAG = "Aifull";
    private static final boolean D = true;
	
	private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();
	private AifullService _service;
	
	
	private static final int REQUEST_CONNECT_DEVICE = 1;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aifull);
		
		Log.d(TAG, "Check Bluetooth wearable");
		//Blurtoothが対応しているかどうかのチェック
		if(_bluetooth.equals(null)){
			Toast.makeText(getApplicationContext(), "Bluetoothが対応していません", Toast.LENGTH_SHORT).show();
			finish();
		}
		
		Log.d(TAG, "Bluetooth ON");
		//Bluetooth ON
 		_bluetooth.enable();
 		
 		Log.d(TAG, "set bluetoooth discoverable");
 		//Bluetooth Discoverable ON
 		doDiscoverable();
 		
 		Log.d(TAG, "Check discoverable");
		//Discoverable でなければ終了する
		if(_bluetooth.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
			Toast.makeText(getApplicationContext(), "端末を被発見状態にできませんでした", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
	}
	
	@Override
	public void onStart(){
		Log.d(TAG, "doStart()");
		super.onStart();
		if(_service == null)
			setupAifull();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.aifull, menu);
		
		//項目の追加
		menu.add(Menu.NONE, 0, 0, "端末を検出する");
		return super.onCreateOptionsMenu(menu);
		
	}
	
	//menuボタンが押された時
	public boolean onOptionsItemSelected(MenuItem mi){
		
		if(mi.getItemId() == 0){
			Log.d(TAG, "getItemId() = 0");
			Toast.makeText(getApplicationContext(), "検出します", Toast.LENGTH_SHORT).show();
			doDiscovery();
			return true;
		}
		return false;
	}
	
	private synchronized void doDiscoverable(){
		Log.d(TAG, "doDiscoverable()");
		//被検出状態じゃない時は検出するために許可をとる
		if(_bluetooth.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
			//0秒の間検出できるようにユーザに確認する
			Intent discoverableIntent = new
			Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
			startActivity(discoverableIntent);
		}
	}
	
	private void setupAifull(){
		 Log.d(TAG, "setupAifull()");
		 
	}
	
	private void doDiscovery() {
		Log.d(TAG, "doDiscover()");
		// Launch the DeviceListActivity to see devices and do scan
        Intent serverIntent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
	    /*
		Log.d(TAG, "show prev devices");
	    //接続履歴のあるデバイスを取得して表示
	    pairedDeviceAdapter = new ArrayAdapter(this, R.layout.activity_aifull);
	    //BluetoothAdapterから、接続履歴のあるデバイスの情報を取得
	    Set<BluetoothDevice> pairedDevices = _bluetooth.getBondedDevices();
	    if(pairedDevices.size() > 0){
	    	//接続履歴のあるデバイスが存在する
	    	for(BluetoothDevice device:pairedDevices){
	    		//接続履歴のあるデバイスの情報を順に取得してアダプタに詰める
	    		//getName()・・・デバイス名取得メソッド
	    		//getAddress()・・・デバイスのMACアドレス取得メソッド
	    		pairedDeviceAdapter.add(device.getName() + "\n" + device.getAddress());
	    	}
	    	ListView deviceList = (ListView)findViewById(R.id.listView1);
	    	deviceList.setAdapter(pairedDeviceAdapter);
	    }
	    
	    
	    Log.d(TAG, "start Discover");
	    //接続したことが無い端末を探す
	    if (!_bluetooth.isDiscovering()) {
	        _bluetooth.startDiscovery();
	        // 検索中がわかるようにToast表示
	        Toast.makeText(getApplicationContext(), "デバイスを検索中です", Toast.LENGTH_LONG).show();
	    }
	    
	    Log.d(TAG, "define reciver");
	    final BroadcastReceiver _receiver = new BroadcastReceiver() {
	        public void onReceive(Context context, Intent intent) {
	            String action = intent.getAction();
	            // When discovery finds a device
	            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	                // Get the BluetoothDevice object from the Intent
	                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	                // Add the name and address to an array adapter to show in a ListView
	                pairedDeviceAdapter.add(device.getName() + "\n" + device.getAddress());
	            }
	        }
	    };
	    
	    Log.d(TAG, "regist reciver");
	    // Register the BroadcastReceiver
	    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
	    registerReceiver(_receiver, filter);
	    */
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		
	}

}
