package com.example.mobilesafeteach.activity;

import com.example.mobilesafeteach.R;
import com.example.mobilesafeteach.utils.PrefUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LostAndFindActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		boolean configed=PrefUtils.getBoolean("configed", false, this);
		if(!configed){
			startActivity(new Intent(getApplicationContext(), SetUp1Activity.class));
			finish();//Ë¢Íê¾ÍÌø
		}else{
			setContentView(R.layout.activity_lostandfind);
		}		
	}
	
	
}
