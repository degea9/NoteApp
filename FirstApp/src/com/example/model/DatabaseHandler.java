package com.example.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	private SQLiteDatabase db;

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	public static final String DATABASE_NAME = "firstapp";

	// Note table name
	public static final String TABLE_NOTE = "notes";
	// photo table name
	public static final String TABLE_PHOTO = "photo";

	// Note Table Columns names
	public static final String KEY_ID = "id";
	public static final String KEY_TITLE = "title";
	public static final String KEY_DETAIL = "detail";
	public static final String KEY_CREATED_AT = "created_at";
	public static final String KEY_COLOR = "color";
	public static final String KEY_ALARM = "alarm";

	// Photo Table Columns names
	public static final String KEY_PHOTO_PATH = "path";
	public static final String KEY_NOTE_ID = "note_id";

	public static DatabaseHandler sInstance;

	// sql command here
	private static final String CREATE_NOTE_TABLE = "CREATE TABLE "
			+ TABLE_NOTE + "(" + KEY_ID + " INTEGER PRIMARY KEY autoincrement,"
			+ KEY_TITLE + " TEXT," + KEY_DETAIL + " TEXT," + KEY_CREATED_AT
			+ " DATETIME," + KEY_COLOR + " INTEGER," + KEY_ALARM + " DATETIME"
			+ ")";

	private static final String CREATE_PHOTO_TABLE = "CREATE TABLE "
			+ TABLE_PHOTO + "(" + KEY_ID
			+ " INTEGER PRIMARY KEY autoincrement," + KEY_PHOTO_PATH + " TEXT,"
			+ KEY_NOTE_ID + " INTEGER" + ")";

	private DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		db = getWritableDatabase();
	}

	public static synchronized DatabaseHandler getInstance(Context context) {

		if (sInstance == null) {
			sInstance = new DatabaseHandler(context);
		}
		return sInstance;
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_NOTE_TABLE);
		db.execSQL(CREATE_PHOTO_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTO);
		// Create tables again
		onCreate(db);

	}

	public void close() {
		if (db != null && db.isOpen())
			db.close();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		close();
	}
}