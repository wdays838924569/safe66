package com.example.mobilesafeteach.activity;

import java.io.IOException;

import java.io.File;
import java.io.InputStream;
import java.io.StreamTokenizer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;



import com.example.mobilesafeteach.R;
import com.example.mobilesafeteach.utils.PrefUtils;
import com.example.mobilesafeteach.utils.StreamUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class SplashActivity extends Activity {
	/**
	 * ����ҳ��
	 * 
	 * - չʾlogo, ��˾Ʒ�� - ���汾���� - ��Ŀ��ʼ�� - У��Ϸ���(����Ƿ�������, ����Ƿ��¼)
	 * ��������:
	 * 1. �����ļ�
	 * 2. ��ȡ�汾��,��ʾ��TextView
	 * 3. ���ʷ�����,��ȡjson����
	 * 4. ����json, �ж��Ƿ��и���
	 * 5. �и���,������ʾ
	 * 6. �޸���,����ҳ��
	 * 7. �����쳣�����,Ҳ����ҳ��
	 * 8. ����ҳ��ʾ2���߼�
	 * 9. ���2.0�汾
	 * 10. ʹ��xutils����apk
	 * 11. �������ؽ���
	 * 12. ��װapk
	 * 13. ���ǩ����ͻ����
	 * 14. �޸�bug(���ص���,ȡ����װ,style��ʽ�޸�)
	 * 15. ����ҳ���䶯��
	 * @author wwt
	 * 
	 */
	private TextView tvName;
	private TextView tvProgres;
	private RelativeLayout rlRoot;
	
	private static final int CODE_UPDATE_DIALOG=1;
	private static final int CODE_ENTER_HOME=2;
	private static final int CODE_URL_ERROR=3;
	private static final int CODE_NETWORK_ERROR=4;
	private static final int CODE_JSON_ERROR=5;
	
	private String mVersionName;
	private int mVersionCode;
	private String mDes;
	private String mUrl;
	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CODE_UPDATE_DIALOG:
				showUpdateDialog();
				break;
			case CODE_ENTER_HOME:
				enterHome();
				break;
			case CODE_URL_ERROR:
				Toast.makeText(getApplicationContext(), "�������Ӵ���",Toast.LENGTH_LONG).show();
				enterHome();
				break;
			case CODE_NETWORK_ERROR:
				Toast.makeText(getApplicationContext(), "�����쳣",Toast.LENGTH_LONG).show();
				enterHome();
				break;
			case CODE_JSON_ERROR:
				Toast.makeText(getApplicationContext(), "���ݽ����쳣",Toast.LENGTH_LONG).show();
				enterHome();
				break;

			default:
				break;
			}
		};
	};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tvName=(TextView) findViewById(R.id.tv_name);
        tvName.setText("�汾��:"+getVersionName());
        
        tvProgres=(TextView) findViewById(R.id.tv_progress);
        rlRoot=(RelativeLayout) findViewById(R.id.rl_root);
        
        
        
        //SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        boolean autoUpdate=PrefUtils.getBoolean("auto_update", true,this);
        if(autoUpdate) {//��Ҫ���汾
			checkVersion();
		}else {
			mHandler.sendEmptyMessageDelayed(CODE_ENTER_HOME, 2000);//������ʱ�������Ϣ,������ҳ��
		}
        checkVersion();
        //����Ч��
        AlphaAnimation anim = new AlphaAnimation(0.2f, 1);
        anim.setDuration(2000);
        rlRoot.setAnimation(anim);
    }
    /*
     * ���汾
     */
    private void checkVersion() {
		new Thread(){
			

			@Override
			public void run() {	
				//���̷߳���������ʱ
				long StratTime=System.currentTimeMillis();
				//�õ���Ϣ
				Message msg=Message.obtain();
				try {
					HttpURLConnection conn=(HttpURLConnection)new URL("http://10.0.2.2:8080/updata66.json")
					.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(2000);
					conn.setReadTimeout(2000);
					
					conn.connect();
					
					int responseCode=conn.getResponseCode();
					
					if (responseCode==200) {
						InputStream in =conn.getInputStream();
						String result=StreamUtils.streamToString(in);
						System.out.println("result:"+result);
						
						JSONObject jo=new JSONObject(result);
						mVersionName = jo.getString("versionName");
						mVersionCode = jo.getInt("versionCode");
						mDes = jo.getString("des");
						mUrl = jo.getString("url");
						
						if(getVersionCode()<mVersionCode){
							System.out.println("�и���");
							//��������
							
							msg.what=CODE_UPDATE_DIALOG;
						}else{
							System.out.println("�޸���");
							msg.what=CODE_ENTER_HOME;
							
						}
					}
					
				} catch (MalformedURLException e) {
					//·��д��
					e.printStackTrace();
					msg.what=CODE_URL_ERROR;
				}catch (IOException e) {
					//����δ����
					e.printStackTrace();
					msg.what=CODE_NETWORK_ERROR;
				} catch (JSONException e) {
					//json���ݲ���
					e.printStackTrace();
					msg.what=CODE_JSON_ERROR;
				}finally{
					long EndTime=System.currentTimeMillis();
					long UseTime=StratTime-EndTime;
					
					try {
						if (UseTime<2000) {
							sleep(2000);//ǿ��˯����չʾ��˾logo
						}
						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					mHandler.sendMessage(msg);
				}
			}
		}.start();
	}
    /*
     * ��������
     */
	protected void showUpdateDialog() {
		AlertDialog.Builder builder=new AlertDialog.Builder(this);//������봫һ��activity����
		builder.setTitle("���°汾:"+mVersionName);
		builder.setMessage(mDes);
		//builder.setCancelable(false);��ò�Ҫʹ����������ؼ�����Ч�û�����ܲ�
		
		builder.setPositiveButton("��������", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 
				downloadApk();
			}
		});
		builder.setNegativeButton("�´���˵", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				enterHome();
			}
		});
		
		//�����û�ȡ�������򣬱���㷵�ؼ�
		builder.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				enterHome();
			}
		});
		
		builder.show();
	}
	/*
	 * ���ذ�װ��
	 */
	protected void downloadApk() {
		//�ж�SD���Ƿ����
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			String path = Environment.getExternalStorageDirectory()
					.getAbsolutePath()+"/mobilsafe66.apk";
			
			tvProgres.setVisibility(View.VISIBLE);
			
			//xUtils  �����߳�����
			HttpUtils utils=new HttpUtils();
										//arg3:��һ���ص�
			utils.download(mUrl, path, new RequestCallBack<File>() {
				//��ȡ���ؽ���
				@Override				//����		���ؽ���			�Ƿ������ϴ�
				public void onLoading(long total, long current, boolean isUploading) {
					// TODO Auto-generated method stub
					super.onLoading(total, current, isUploading);
					//���ؽ���
					int percent=(int)(100 *current/total);
					System.out.println("���ؽ���"+percent+"%");
					tvProgres.setText("���ؽ���"+percent+"%");
					
					
				}
				@Override
				public void onSuccess(ResponseInfo<File> responseInfo) {
					// ���سɹ�
					String p=responseInfo.result.getAbsolutePath();
					System.out.println("���سɹ�"+p);
					
					//���سɹ���ת��ϵͳ��װ����
					Intent intent=new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					intent.setDataAndType(Uri.fromFile(responseInfo.result), 
							"application/vnd.android.package-archive");
					startActivityForResult(intent, 0);
				}
				
				@Override
				public void onFailure(HttpException errot, String msg) {
					// ����ʧ��
					errot.printStackTrace();
					Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
				}
			});
		}else{
			Toast.makeText(getApplicationContext(), "û���ҵ�SDcard��", Toast.LENGTH_SHORT).show();
		}
		
	}
	private String getVersionName(){
    	PackageManager pm=getPackageManager();//��ȡ��������
    	
    	try {
			PackageInfo packageinfo=pm.getPackageInfo(getPackageName(), 0);//��ȡ��������ȡ����Ϣ
			String versionName=packageinfo.versionName;
			int versionCode=packageinfo.versionCode;
			System.out.println("#########"+versionName+"  "+versionCode);
			
			return versionName;
			
		} catch (NameNotFoundException e) {
			// ����δ�ҵ��쳣
			e.printStackTrace();
		}
		return "";
    	
    }
	
	private int getVersionCode(){
		PackageManager pm=getPackageManager();//��ȡ��������
		    	
		    	try {
					PackageInfo packageinfo=pm.getPackageInfo(getPackageName(), 0);//��ȡ��������ȡ����Ϣ
					int versionCode=packageinfo.versionCode;
					
					System.out.println("#########"+"  "+versionCode);
					
					return versionCode;
					
				} catch (NameNotFoundException e) {
					// ����δ�ҵ��쳣
					e.printStackTrace();
				}
				return -1;
	}
	//������ҳ��
	
	private void enterHome(){
		startActivity(new Intent(this, HomeActivity.class));
		finish();
	}
	//�û�ȡ����װ����ص��˷���
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		enterHome();
	}
   
}
