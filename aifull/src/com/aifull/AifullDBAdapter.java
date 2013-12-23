package com.aifull;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AifullDBAdapter {
	
	static final String DATABASE_NAME = "devices.db";
	static final int DATABASE_VERSION = 1;
	
	public static final String TABLE_NAME = "devices";
	public static final String COL_ID = "mac_id";
	public static final String COL_NAME = "name";
	public static final String COL_IMAGE = "image";
	public static final String COL_LASTUPDATE = "lastupdate";
	
	protected final Context context;
	protected DatabaseHelper dbHelper;
	protected SQLiteDatabase db;
	
	public AifullDBAdapter(Context context){
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
        + COL_NAME + " TEXT NOT NULL,"
        + COL_IMAGE + " BLOB,"
        + COL_LASTUPDATE + " TEXT NOT NULL);");
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
	
	public AifullDBAdapter open() {
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
	
	public boolean deleteNote(int id){
		return db.delete(TABLE_NAME, COL_ID + "=" + id, null) > 0;
	}
	
	public Cursor getAllNotes(){
		return db.query(TABLE_NAME, null, null, null, null, null, null);
	}
	
	public void saveNote(String note){
		Date dateNow = new Date ();
		ContentValues values = new ContentValues();
		values.put(COL_NAME, note);
		values.put(COL_LASTUPDATE, dateNow.toLocaleString());
		db.insertOrThrow(TABLE_NAME, null, values);
	}
	
	
	public static long insert(Context context, String macid, String name, String updata, byte[] blob){
		SQLiteDatabase db = null;
		try {
			DatabaseHelper helper = new DatabaseHelper(context);
			db = helper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(COL_ID, macid);
			values.put(COL_NAME, name);
			values.put(COL_LASTUPDATE, updata);
			values.put(COL_IMAGE, blob);
			
			return db.insert(TABLE_NAME, null, values);
			
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}
	
	
}