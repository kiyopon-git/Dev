package com.sqlite;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
 

public class MainActivity extends Activity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyOpenHelper helper = new MyOpenHelper(this);

        final SQLiteDatabase db = helper.getWritableDatabase();
        final EditText nameText = (EditText) findViewById(R.id.editName);
        final EditText ageText = (EditText) findViewById(R.id.editAge);
        Button entryButton = (Button) findViewById(R.id.insert);
        entryButton.setOnClickListener(new OnClickListener() {
        	
           @Override
            public void onClick(View v) {
                String name = nameText.getText().toString();
                String age = ageText.getText().toString();

                ContentValues insertValues = new ContentValues();
                insertValues.put("name", name);
                insertValues.put("age", age);
                long id = db.insert("person", name, insertValues);
            }
        });

        Button updateButton = (Button) findViewById(R.id.update);
        updateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameText.getText().toString();
                String age = ageText.getText().toString();
                if (name.equals("")) {
                    ContentValues updateValues = new ContentValues();
                    updateValues.put("age", age);
                    db.update("person", updateValues, "name=?",
                            new String[] { name });
                } else {
                    Toast.makeText(MainActivity.this, "名前を入力してください。",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        Button deleteButton = (Button) findViewById(R.id.delete);
        deleteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameText.equals("")) {
                    String name = nameText.getText().toString();
                    db.delete("person", "name=?", new String[] { name });
                } else {
					Toast.makeText(MainActivity.this, "名前を入力してください。",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button deleteAllButton = (Button) findViewById(R.id.deleteAll);
        deleteAllButton.setOnClickListener(new OnClickListener() {
        	
            @Override
            public void onClick(View v) {
                String name = nameText.getText().toString();
                String age = ageText.getText().toString();
                db.delete("person", null, null);
            }
        });

        Button detaBaseButton = (Button) findViewById(R.id.dataBase);
        detaBaseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dbIntent = new Intent(MainActivity.this,
                        ShowDataBase.class);
                startActivity(dbIntent);
            }
        });
    }

}