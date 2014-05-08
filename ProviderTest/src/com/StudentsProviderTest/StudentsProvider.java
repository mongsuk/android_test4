package com.StudentsProviderTest;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class StudentsProvider extends ContentProvider{
	StudentsDBHelper		mOpenHelper 	= null;
	
	public static final  String 	AUTHORITY = "com.Provider.Students";
	public static final  Uri 		CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/students");
	
	public static final int STUDENTS 		= 1;
	public static final int STUDENT_ID		= 2;
	
	public static final  UriMatcher sUriMatcher;
	
	static 
	{
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(AUTHORITY, "students" 	, STUDENTS);
		sUriMatcher.addURI(AUTHORITY, "students/#" 	, STUDENT_ID);
    }

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		
        int count = 0;
        
        switch (sUriMatcher.match(uri)) 
        {
        	case STUDENTS:
        	{
        		count = db.delete(StudentsDBHelper.TABLE_NAME, where, whereArgs);
        		
        		break;
        	}

        	case STUDENT_ID:
        	{
        		String itemIndex = uri.getPathSegments().get(1);                
            
        		count = db.delete( StudentsDBHelper.TABLE_NAME, 
        							StudentsDBHelper.KEY_ID + "=" + 
        								itemIndex +
        								(!TextUtils.isEmpty(where) ? 
        									" AND (" + where + ')' : ""), 
        							whereArgs);
        		break;
        	}

        	default:
        	{
        		throw new IllegalArgumentException("Unknown URI " + uri);
        	}
        }
        
		if(count != 0)
		{
			Log.i("superdroid","provider notify delete : " + uri);
			getContext().getContentResolver().notifyChange(uri, null);
		}

        return count;	
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		switch (sUriMatcher.match(uri)) 
		{
        	case STUDENTS:
        	{
        		return "vnd.students.cursor.dir/students";
        	}

        	case STUDENT_ID:
        	{
        		return "vnd.students.cursor.item/students";
        	}
        	default:
        	{
        		throw new IllegalArgumentException("Unknown URI " + uri);
        	}
        		
        }
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		
        if (sUriMatcher.match(uri) != STUDENTS) 
        {
            throw new IllegalArgumentException("Unknown URI : " + uri);
        }

        long rowId = db.insert(StudentsDBHelper.TABLE_NAME, null, values);
        
        if (rowId > 0) 
        {
        	return ContentUris.withAppendedId(CONTENT_URI,rowId);
        }
        
        throw new SQLException("Failed to insert row into : " + uri);
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		Log.i("superdroid","StudentsProvider : onCreate()");
		
		mOpenHelper	= new StudentsDBHelper(getContext());
		
		if(mOpenHelper != null)
		{
			return true;
		}
		
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor c = null; 
		
		switch (sUriMatcher.match(uri)) 
		{		
        	case STUDENTS:
        	{
        		c = db.query( StudentsDBHelper.TABLE_NAME,
        					  projection, 
   					 		  selection, 
   					 		  selectionArgs, 
   					 		  null, 
   					 		  null, 
   					 		  sortOrder);
        		break;
        	}
        	case STUDENT_ID:
        	{
        		String itemIndex = uri.getPathSegments().get(1);
        		
        		c = db.query( StudentsDBHelper.TABLE_NAME,
        					  projection, 
        					  StudentsDBHelper.KEY_ID + "=" + itemIndex +  
      				   				(TextUtils.isEmpty(selection) ? 
      				   				"" : " AND (" + selection + ')'), 
					 		  selectionArgs, 
					 		  null, 
					 		  null, 
					 		  sortOrder);
        		break;
        	}
        	default:
        	{
        		throw new IllegalArgumentException("Unknown URI " + uri);
        	}
        }
        return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		
		int count = 0;
		
        switch (sUriMatcher.match(uri)) 
        {
	        case STUDENTS:
	        {
	            count = db.update(StudentsDBHelper.TABLE_NAME, 
	            					values, 
	            					where, 
	            					whereArgs);
	            break;
	        }

	        case STUDENT_ID:
	        {
	            String itemIndex = uri.getPathSegments().get(1);
	            
	            count = db.update(StudentsDBHelper.TABLE_NAME, 
	            				  values, 
	            				  StudentsDBHelper.KEY_ID + "=" + itemIndex +  
	            				   		(TextUtils.isEmpty(where) ? 
	            				   		"" : " AND (" + where + ')'), 
	            				  whereArgs);
	            break;
	        }
	
	        default:
	        {
	            throw new IllegalArgumentException("Unknown URI " + uri);
	        }
        }
        
        return count;
	}

}
