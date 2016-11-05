package com.example.mobilesafeteach.activity;

import com.example.mobilesafeteach.R;
import com.example.mobilesafeteach.utils.PrefUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SetUp4Activity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_setup4);
	}
	//上一页
	public void previous(View v){
			startActivity(new Intent(this, SetUp3Activity.class));
			finish();
	}
		//下一页
	public void next(View v){
		PrefUtils.putBoolean("configed", true, this);
		startActivity(new Intent(this, LostAndFindActivity.class));
		//System.out.println("4444444444444444444444444444");
		finish();
	}
}
