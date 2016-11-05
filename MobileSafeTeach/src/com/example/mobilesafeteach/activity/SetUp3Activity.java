package com.example.mobilesafeteach.activity;

import com.example.mobilesafeteach.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SetUp3Activity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_setup3);
	}
	//上一页
	public void previous(View v){
			startActivity(new Intent(this, SetUp2Activity.class));
			finish();
	}
		//下一页
	public void next(View v){
		startActivity(new Intent(this, SetUp4Activity.class));
		finish();
	}
}
