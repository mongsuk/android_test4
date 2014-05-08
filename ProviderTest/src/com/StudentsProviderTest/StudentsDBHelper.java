package com.StudentsProviderTest;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class StudentsDBHelper extends SQLiteOpenHelper
{
	public static int	 DB_VERSION		= 2;
	
	public static String DB_FILE_NAME 	= "students.db";
	public static String TABLE_NAME 	= "students";
	public static String KEY_ID 		= "_id";
	public static String KEY_NUMBER 	= "number";
	public static String KEY_NAME 		= "name";
	public static String KEY_DEPARTMENT = "department";
	public static String KEY_GRADE 		= "grade";
	
	public static String DATABASE_CREATE = "create table IF NOT EXISTS " + 
  			TABLE_NAME 	+ " (" + 
  			KEY_ID 			+ " integer primary key autoincrement, " +
  			KEY_NUMBER 		+ " text, " +
  			KEY_NAME 		+ " text, " +
  			KEY_DEPARTMENT 	+ " text, " +
  			KEY_GRADE 		+ " integer);";	 

	public StudentsDBHelper(Context context) {
		super(context, DB_FILE_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}
    
}
