package com.example.mobilesafeteach.activity;

import com.example.mobilesafeteach.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SetUp1Activity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_setup1);
		
	}
	//обр╩рЁ
	public void next(View v){
		startActivity(new Intent(this, SetUp2Activity.class));
		finish();
	}
}
