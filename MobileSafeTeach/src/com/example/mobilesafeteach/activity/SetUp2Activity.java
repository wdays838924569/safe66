package com.example.mobilesafeteach.activity;

import com.example.mobilesafeteach.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SetUp2Activity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_setup2);
	}
	//��һҳ
	public void previous(View v){
			startActivity(new Intent(this, SetUp1Activity.class));
			finish();
	}
		//��һҳ
	public void next(View v){
		startActivity(new Intent(this, SetUp3Activity.class));
		finish();
	}
}
