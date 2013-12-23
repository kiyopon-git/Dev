package com.aifull;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AifullNDBAdapter {
	
	static final String DATABASE_NAME = "notes.db";
	static final int DATABASE_VERSION = 1;
	
	public static final String TABLE_NAME = "notes";
	public static final String COL_ID = "mac_id";
	public static final String COL_TIME = "time";
	public static final String COL_NOTE = "note";
	
	protected final Context context;
	protected DatabaseHelper dbHelper;
	protected SQLiteDatabase db;
	
	public AifullNDBAdapter(Context context){
		this.context = context;
		dbHelper = new DatabaseHelper(this.context);
	}
	
	//
	// SQLiteOpenHelper
	//
  
	private static class DatabaseHelper extends SQLiteOpenHelper {
		
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      db.execSQL(
        "CREATE TABLE " + TABLE_NAME + " ("
        + COL_ID + " TEXT PRIMARY KEY AUTOINCREMENT,"
        + COL_TIME + " TEXT NOT NULL,"
        + COL_NOTE + " TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    		onCreate(db);
    	}
	}
    
	//
	// Adapter Methods
	//
	
	public AifullNDBAdapter open() {
		db = dbHelper.getWritableDatabase();
		return this;
	}
	
	
	public void close(){
		dbHelper.close();
	}
	
	
	//
	// App Methods
	//
  
  
	public boolean deleteAllNotes(){
		return db.delete(TABLE_NAME, null, null) > 0;
	}
	
	
	public Cursor getAllNotes(){
		return db.query(TABLE_NAME, null, null, null, null, null, null);
	}
	
	
	public static long insert(Context context, String macid, String time, String note){
		SQLiteDatabase db = null;
		try {
			DatabaseHelper helper = new DatabaseHelper(context);
			db = helper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(COL_ID, macid);
			values.put(COL_TIME, time);
			values.put(COL_NOTE, note);;
			
			return db.insert(TABLE_NAME, null, values);
			
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}
	
}