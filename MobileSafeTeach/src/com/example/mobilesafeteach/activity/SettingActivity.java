package com.example.mobilesafeteach.activity;

import com.example.mobilesafeteach.R;

import com.example.mobilesafeteach.view.SettingItemView;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
/*
 * ���ý���
 */
import android.view.View.OnClickListener;
	
public class SettingActivity extends Activity {
	/*
	 * �����ر��Զ����� 2016-10-29 16:04:54
	 */
	private SettingItemView sivUpdate;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		sp=getSharedPreferences("config", MODE_PRIVATE);
		
		sivUpdate=(SettingItemView) findViewById(R.id.siv_update);
		sivUpdate.setTitle("�Զ���������");
		
		boolean autoUpdate=sp.getBoolean("auto_update", true);
		if(autoUpdate){
			sivUpdate.setDesc("�Զ������ѿ���");
			sivUpdate.setChecked(true);
		}else{
			sivUpdate.setDesc("�Զ������ѹر�");
			sivUpdate.setChecked(false);
		}
		
		
		
		sivUpdate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(sivUpdate.isChecked()){
					sivUpdate.setChecked(false);
					sivUpdate.setDesc("�Զ������ѹر�");			//�ύ
					sp.edit().putBoolean("auto_update", false).commit();
				}else{
					sivUpdate.setChecked(true);
					sivUpdate.setDesc("�Զ������ѿ���");
					sp.edit().putBoolean("auto_update", true).commit();
				}
				
			}
		});
	}
}
