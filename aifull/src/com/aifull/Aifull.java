package com.aifull;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class Aifull extends Activity {
	
	 //For Debugging
    private static final String TAG = "Aifull";
	
	private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();
	private AifullService _service;
	private ListView lv;
	
	private static final int REQUEST_CONNECT_DEVICE = 1;
	
	// Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
	
 // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    
	// Return Intent extra
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
	
	
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
		
		
		//make List view
		lv = (ListView)findViewById(R.id.listView1);
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
		menu.add(Menu.NONE, 0, 0, "端末を検出する(Paired)");
		menu.add(Menu.NONE, 1, 1, "端末を検出する(New)");
		return super.onCreateOptionsMenu(menu);
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		
        Log.d("onActivityResule", "test");
        switch (requestCode){
        	case REQUEST_CONNECT_DEVICE:
        	if(resultCode == Activity.RESULT_OK){
        		Toast.makeText(getApplicationContext(), "検出した結果を出力します", Toast.LENGTH_LONG).show();
        		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, 
        				data.getStringArrayListExtra(EXTRA_DEVICE_ADDRESS));
        		lv.setAdapter(adapter);
        		//MACアドレスを使って端末をBluetooth接続する
        		
        		
        	}
        	break;
        	
        default:
        	break;
        	
        }
	}
	
	
	//menuボタンが押された時
	public boolean onOptionsItemSelected(MenuItem mi){
		
		if(mi.getItemId() == 0){
			Log.d(TAG, "getItemId() = 0");
			Toast.makeText(getApplicationContext(), "検出します", Toast.LENGTH_SHORT).show();
			// Launch the DeviceListActivity to see devices and do scan
	        Intent serverIntent = new Intent(this, DeviceListActivity.class);
	        serverIntent.putExtra(DeviceListActivity.DETECT_TYPE, DeviceListActivity.PIRED);
	        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
			return true;
		}else if(mi.getItemId() == 1){
			Log.d(TAG, "getItemId() = 0");
			Toast.makeText(getApplicationContext(), "検出します", Toast.LENGTH_SHORT).show();
			// Launch the DeviceListActivity to see devices and do scan
	        Intent serverIntent = new Intent(this, DeviceListActivity.class);
	        serverIntent.putExtra(DeviceListActivity.DETECT_TYPE, DeviceListActivity.NEW);
	        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
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
	
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		
	}

}
