/*
①チャットサービス作成(小難しい端末間接続とかが作りこんである)
②他の端末を探す
③ひとつひとつの端末にイカの処理の繰返しを行う
	①ペアリングの作成
	②接続の開始
	③接続できた場合はバイブレーション(ハンドラでイベントを監視)
	④接続の解除
	⑤ペアリングの削除
*/
package com.aifull;

import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;


public class DeviceFoundService extends Service{

	// Debugging
    private static final String TAG = "Aifull";
    private static final boolean D = true;
    
    // Local Bluetooth adapter
    private BluetoothAdapter _bluetooth = null;
    private AifullService _chatservice = null;
    private Vibrator _vibrator= null;
    private BluetoothDevice founddevice;
    private Timer _timer;
    
    private final IBinder mBinder = new LocalBinder();
 
    public class LocalBinder extends Binder {
    	DeviceFoundService getService() {
            return DeviceFoundService.this;
        }
    }
 
    @Override
    public void onCreate() {
        Log.d(TAG,"onCreate");
        
        _vibrator = (Vibrator)this.getSystemService(VIBRATOR_SERVICE);
        
        _bluetooth=BluetoothAdapter.getDefaultAdapter();//getDefaultAdapter() の結果が null の場合は、そのデバイスが Bluetooth をサポートしていないことを示す
        if(_bluetooth.isEnabled()){
        	if (_chatservice==null) {
        		_chatservice = new AifullService(this, handler);
        	}
            if (_chatservice!=null) {
                if (_chatservice.getState()==AifullService.STATE_NONE) {
                    //Bluetoothの接続待ち(サーバ)
                	_chatservice.start();
                }
            }
        }else{
        	Log.d(TAG, "Bluetoothが有効になっていません");
        }
        
        _timer = new Timer();
        TimerTask timerTask = new MyTimerTask(this);
        _timer.scheduleAtFixedRate(timerTask, 0, 15000);
    }
 
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
 
    @Override
    public void onDestroy() {
        Log.d(TAG,"onDestroy");
    }
 
    // Methods for client
    public void userFunction() {
        Log.d(TAG,"userFunction");
    }
    
    public void connectPairdDevice(){
    	
        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);
        // If we're already discovering, stop it
        if (_bluetooth.isDiscovering()) {
            _bluetooth.cancelDiscovery();
        }
        Log.d(TAG,"startDiscovery");
        // Request discover from BluetoothAdapter
        _bluetooth.startDiscovery();
    	
    }

    
    private void conect(String bluetoothadder,BluetoothAdapter btAdapter){
        Log.d(TAG, "-------------接続要求-------------");
       //Bluetoothの接続要求(クライアント)
       Log.d(TAG, "bluetoothadder:" + bluetoothadder+"---");
        if(bluetoothadder!=null){
            BluetoothDevice devicebt = btAdapter.getRemoteDevice(bluetoothadder);
            _chatservice.connect(devicebt, true);
        }
    }

	//チャットサーバから情報を取得するハンドラ
	private final Handler handler=new Handler() {
	
	    //ハンドルメッセージ
	    @Override
	    public void handleMessage(Message msg) {
	        switch (msg.what) {
	            //状態変更
	            case Aifull.MESSAGE_STATE_CHANGE:
	                switch (msg.arg1) {
	                case AifullService.STATE_CONNECTED:
	                    Log.d(TAG, "-------------接続完了-------------");
	                    //メッセージの送信
	                    String message="S";
	                    Vibrate(Aifull.MESSAGE_VIBRATE_ON);
	                    Log.d(TAG, "送信データ:"+message);
	                    _chatservice.write(message.getBytes());
	                    Log.d(TAG, "SendMessage");
	                    break;
	
	                case AifullService.STATE_CONNECTING:
	                    Log.d(TAG, "接続中");break;
	                case AifullService.STATE_LISTEN:
	                case AifullService.STATE_NONE:
	                    Log.d(TAG, "未接続");break;
	                }
	                break;
	            //メッセージ受信
	            case Aifull.MESSAGE_READ:
	                byte[] readBuf=(byte[])msg.obj;
	                String message = new String(readBuf,0,msg.arg1);
	                Log.d("tagbt","受信データ:"+ message);
	                if(message.equals("S")){
	                    String endM="--end--";
	                    Vibrate(Aifull.MESSAGE_VIBRATE_ON);
	                    Log.d("tagbt", "送信データ:"+endM);
	                    _chatservice.write(endM.getBytes());
	                }
	                if (message.equals("--end--")) {
	                    _chatservice.stop();
	                    Log.d("tagbt", "----Bluetooth切断-----");
	                }
	                break;
	        }
	    }
	};
	
	
	// The BroadcastReceiver that listens for discovered devices and
	// changes the title when discovery is finished
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        String ACTION_PAIRING_REQUEST = "android.bluetooth.device.action.PAIRING_REQUEST";
	        
	        // When discovery finds a device
	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	        	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	            // Get the BluetoothDevice object from the Intent
	        	founddevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	            // If it's already paired, skip it, because it's been listed already
	            if (founddevice.getBondState() != BluetoothDevice.BOND_BONDED) {
	            	if(_chatservice.getState() == AifullService.STATE_LISTEN){
		            	_chatservice.connect(founddevice, false);
		            	unpairDevice(founddevice);
	            	}
	            }else {
	            	if(_chatservice.getState() == AifullService.STATE_LISTEN){
	            		_chatservice.connect(founddevice, false);
	            		unpairDevice(founddevice);
	            	}
	            }
	        }

	    }
	};
	
	public class MyTimerTask extends TimerTask {

        private Handler handler;
        private Context context;

        public MyTimerTask(Context context) {
                handler = new Handler();
            this.context = context;
        }
        @Override
        public void run() {
                handler.post(new Runnable() {
                        @Override
                        public void run() {
                                Log.d(TAG, "-------------");
                                if(_chatservice.getState() == AifullService.STATE_LISTEN){
                                        connectPairdDevice();
                                }
                        }
                });
        }
	}
	
	private void unpairDevice(BluetoothDevice device) {
		try {
			Method m = device.getClass().getMethod("removeBond", (Class[]) null);
			m.invoke(device, (Object[]) null);
		 } catch (Exception e) {
			 Log.e(TAG, e.getMessage());
		 }
	}
	
    public void Vibrate(int state){

    	 Log.d(TAG, "vib");
    	switch(state){
    	case Aifull.MESSAGE_VIBRATE_ON:
    		_vibrator.vibrate(1000);
    		break;
    	case Aifull.MESSAGE_VIBRATE_OFF:
    		_vibrator.cancel();
    		break;
    	}
    }
    
}