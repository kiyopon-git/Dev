package com.example.listviewtest;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
        // リソースに準備した画像ファイルからBitmapを作成しておく
        Bitmap image = null;
		try {
		    InputStream is = getResources().getAssets().open("test.jpg");
		    image = BitmapFactory.decodeStream(is);
		} catch (IOException e) {
		    // 例外処理 
		}
 
        // データの作成
        ArrayList<UserData> objects = new ArrayList<UserData>();
        UserData item1 = new UserData();
        item1.setImage(image);
        item1.setName("１つ目〜");
        item1.setDate("1分");
        item1.setIntroduction("aaa");
 
        UserData item2 = new UserData();
        item2.setImage(image);
        item2.setName("The second");
        item2.setDate("1時間");
        item2.setIntroduction("bbb");
 
        UserData item3 = new UserData();
        item3.setImage(image);
        item3.setName("Il terzo");
        item3.setDate("1日");
        item3.setIntroduction("ccc");
 
        objects.add(item1);
        objects.add(item2);
        objects.add(item3);
 
        CustomAdapter customAdapater = new CustomAdapter(this, 0, objects);
        
        ListView listView = (ListView)findViewById(R.id.list);
        listView.setAdapter(customAdapater);
        
        //リストをクリックしたときの処理
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	@Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                UserData item = (UserData) listView.getItemAtPosition(position);	//ユーザデータの取得
                Toast.makeText(getApplicationContext(), "name:" + item.getName() + "\ndate:"
                		+ item.getDate() + "\nintro:" + item.getIntroduction(), Toast.LENGTH_SHORT).show();
                
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
