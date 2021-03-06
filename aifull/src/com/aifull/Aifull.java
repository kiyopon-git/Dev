/* Aifull.java:
 * メインアクティビティ
 *
 * AifullService.java:
 * Bluetoothでの端末間のコネクション形成や情報の通信をするクラス
 *
 * DeviceFoundService.java:
 * 周囲の端末を探してコネクションを形成し、通信を行うサービス
 *
 */
package com.aifull;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class Aifull extends Activity {

	// デバッグ用
    private static final String TAG = "Aifull";
    private static final boolean D = true;

    // AifullServiceのハンドラから送られるメッセージの種類を判別する変数
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_VIBRATE_ON = 6;
    public static final int MESSAGE_VIBRATE_OFF = 7;

    // AifullServiceのハンドラから受け取るキーネーム
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    // Local Bluetooth adapter
    private BluetoothAdapter _bluetooth = null;

  //取得したServiceの保存
    private DeviceFoundService mDeviceFoundService;
    private boolean mIsBound;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aifull);

		//Bluetoothアダプタの取得
		_bluetooth = BluetoothAdapter.getDefaultAdapter();

		Log.d(TAG, "Check Bluetooth wearable");
		//Blurtoothが対応しているかどうかのチェック
		if(_bluetooth.equals(null)){
			Toast.makeText(getApplicationContext(), "Bluetoothが対応していません", Toast.LENGTH_SHORT).show();
			finish();
		}

	}

	@Override
	public void onStart(){
		Log.d(TAG, "doStart()");
		super.onStart();

		// BluetoothをONかどうか確認してOFFならONにするリクエストをユーザに提示する
		Log.d(TAG, "Check ON Bluetooth");
		if (!_bluetooth.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

		Log.d(TAG, "Check discoverable");
		// 端末を被発見状態にする関数
		doDiscoverable();
		//Discoverable でなければ終了する
		if(_bluetooth.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
			Toast.makeText(getApplicationContext(), "端末を被発見状態にできませんでした", Toast.LENGTH_SHORT).show();
			finish();
		}

		// DeviceFoundServiceサービスの開始
		doBindService();

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.aifull, menu););
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);

        Log.d("onActivityResule", "test");

	}


	//menuボタンが押された時
	public boolean onOptionsItemSelected(MenuItem mi){

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


	@Override
	protected void onDestroy(){
		super.onDestroy();

		doUnbindService();

	}


	private ServiceConnection mConnection = new ServiceConnection() {
	    public void onServiceConnected(ComponentName className, IBinder service) {

	        // サービスにはIBinder経由で#getService()してダイレクトにアクセス可能
	    	mDeviceFoundService = ((DeviceFoundService.LocalBinder)service).getService();

	        //必要であればmBoundServiceを使ってバインドしたサービスへの制御を行う
	    }

	    public void onServiceDisconnected(ComponentName className) {
	        // サービスとの切断(異常系処理)
	        // プロセスのクラッシュなど意図しないサービスの切断が発生した場合に呼ばれる。
	    	mDeviceFoundService = null;
	    }
	};

	void doBindService() {
	    //サービスとの接続を確立する。明示的にServiceを指定
	    //(特定のサービスを指定する必要がある。他のアプリケーションから知ることができない = ローカルサービス)
	    bindService(new Intent(this, DeviceFoundService.class), mConnection, Context.BIND_AUTO_CREATE);
	    mIsBound = true;
	}

	void doUnbindService() {
	    if (mIsBound) {
	        // コネクションの解除
	        unbindService(mConnection);
	        mIsBound = false;
	    }
	}

}

