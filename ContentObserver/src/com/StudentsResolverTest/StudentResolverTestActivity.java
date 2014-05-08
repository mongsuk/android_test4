package com.StudentsResolverTest;


import android.text.SpannableStringBuilder;
import android.app.Activity;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.StudentsResolverTest.R;

public class StudentResolverTestActivity extends Activity{
	EditText mEditText = null;
	
	public static final Uri CONTENT_URI = Uri.parse("content://com.Provider.Students/students");

	ContentObserver mContentObserver = null;
	
	public void onCreate(Bundle saveInstanceState) {
		super.onCreate(saveInstanceState);
		setContentView(R.layout.main);
		
		mEditText = (EditText)findViewById(R.id.edit_text);
		
		mContentObserver = new ContentObserver(new Handler() ) { 
			//Override  하여 정의 한다.!
			public void onChange(boolean selfChange) {
				super.onChange(selfChange);
				Log.e("superdroid","onChange()"+selfChange);	
			}
		};
	}
	
	public void onPause(){
		
		getContentResolver().unregisterContentObserver(mContentObserver);
		super.onPause();
	}
	
	public void onResume() {
		super.onResume();
		getContentResolver().registerContentObserver(CONTENT_URI, true, mContentObserver);
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.insert: 
			{
				mContentObserver.dispatchChange(true);
				
				ContentValues cv = new ContentValues();
				cv.put("number", "200106054");
				cv.put("name", "Felinus");
				cv.put("department", "Engineer");
				cv.put("grade", 3);
				
				Uri reUri = getContentResolver().insert(CONTENT_URI, cv);
				mEditText.setText("insert return \n" + reUri);
				break;
			}
			
			case R.id.delete:
			{
				int delCnt = getContentResolver().delete(CONTENT_URI, null, null);
				mEditText.setText("delete return \n" +delCnt);
				break;
			}
			
			case R.id.update:
			{
    			ContentValues cv = new ContentValues();
    			cv.put("name", "Jeong");
    			
    			int updateCnt = getContentResolver().update(CONTENT_URI,
    														cv, 
    														"number=200106054",
    														null);
    			
    			mEditText.setText("update return \n" + updateCnt);
    			break;
    		}
			
			case R.id.query:
			{
				Toast.makeText(this, "query", Toast.LENGTH_LONG).show();
				Cursor c = getContentResolver().query(CONTENT_URI, 
						new String[] {"_id","number","name","department","grade"}, 
						null, 
						null, 
						"_id ASC");
				SpannableStringBuilder text = new SpannableStringBuilder();
    			
    			if(c != null)
    			{
    				Toast.makeText(this, "query1", Toast.LENGTH_LONG).show();
    				
    				while(c.moveToNext())
    				{
    					int 	id 			= c.getInt(0);
    					String 	number		= c.getString(1);
    					String 	name 		= c.getString(2);
    					String 	department	= c.getString(3);
    					int 	grade 		= c.getInt(4);
    					
    					text.append("\n" +
    							    "ID : " 			+ id + "\n" + 
    							   	"Number : " 		+ number + "\n" +
    								"Name : " 		+ name + "\n" +
    								"Department : " 	+ department + "\n" +
    								"grade : " 		+ grade + "\n" +
    								"------------------------------------------");	
    				}
    				
    				mEditText.setText(text);
    				
    				c.close();
				
    			}
    			break;
			}
		}
	}
}
