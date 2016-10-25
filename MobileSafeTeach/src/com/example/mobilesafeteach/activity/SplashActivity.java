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
	 * 闪屏页面
	 * 
	 * - 展示logo, 公司品牌 - 检查版本更新 - 项目初始化 - 校验合法性(检查是否有网络, 检查是否登录)
	 * 开发流程:
	 * 1. 布局文件
	 * 2. 获取版本名,显示给TextView
	 * 3. 访问服务器,获取json数据
	 * 4. 解析json, 判断是否有更新
	 * 5. 有更新,弹窗提示
	 * 6. 无更新,跳主页面
	 * 7. 网络异常等情况,也跳主页面
	 * 8. 闪屏页显示2秒逻辑
	 * 9. 打包2.0版本
	 * 10. 使用xutils下载apk
	 * 11. 更新下载进度
	 * 12. 安装apk
	 * 13. 解决签名冲突问题
	 * 14. 修改bug(返回弹窗,取消安装,style样式修改)
	 * 15. 闪屏页渐变动画
	 * @author wwt
	 * 
	 */
	private TextView tvName;
	private TextView tvProgres;
	private RelativeLayout rlRoot;
	
	private static final int CODE_UPDATE_DIALOG=1;
	private static final int CODE_ENTER_DIALOG=2;
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
			case CODE_ENTER_DIALOG:
				enterHome();
				break;
			case CODE_URL_ERROR:
				Toast.makeText(getApplicationContext(), "网络连接错误",Toast.LENGTH_LONG).show();
				enterHome();
				break;
			case CODE_NETWORK_ERROR:
				Toast.makeText(getApplicationContext(), "网络异常",Toast.LENGTH_LONG).show();
				enterHome();
				break;
			case CODE_JSON_ERROR:
				Toast.makeText(getApplicationContext(), "数据解析异常",Toast.LENGTH_LONG).show();
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
        tvName.setText("版本号:"+getVersionName());
        
        tvProgres=(TextView) findViewById(R.id.tv_progress);
        rlRoot=(RelativeLayout) findViewById(R.id.rl_root);
        
        checkVersion();
        //渐变效果
        AlphaAnimation anim = new AlphaAnimation(0.2f, 1);
        anim.setDuration(2000);
        rlRoot.setAnimation(anim);
    }
    /*
     * 检查版本
     */
    private void checkVersion() {
		new Thread(){
			

			@Override
			public void run() {	
				//子线程访问网络用时
				long StratTime=System.currentTimeMillis();
				//拿到消息
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
							System.out.println("有更新");
							//升级弹窗
							
							msg.what=CODE_UPDATE_DIALOG;
						}else{
							System.out.println("无更新");
							msg.what=CODE_ENTER_DIALOG;
							
						}
					}
					
				} catch (MalformedURLException e) {
					//路径写错
					e.printStackTrace();
					msg.what=CODE_URL_ERROR;
				}catch (IOException e) {
					//网络未连接
					e.printStackTrace();
					msg.what=CODE_NETWORK_ERROR;
				} catch (JSONException e) {
					//json数据不对
					e.printStackTrace();
					msg.what=CODE_JSON_ERROR;
				}finally{
					long EndTime=System.currentTimeMillis();
					long UseTime=StratTime-EndTime;
					
					try {
						if (UseTime<2000) {
							sleep(2000);//强制睡两秒展示公司logo
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
     * 升级弹窗
     */
	protected void showUpdateDialog() {
		AlertDialog.Builder builder=new AlertDialog.Builder(this);//这里必须传一个activity对象
		builder.setTitle("有新版本:"+mVersionName);
		builder.setMessage(mDes);
		//builder.setCancelable(false);最好不要使用这个，返回键不起效用户体验很差
		
		builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 
				downloadApk();
			}
		});
		builder.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				enterHome();
			}
		});
		
		//监听用户取消弹出框，比如点返回键
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
	 * 下载安装包
	 */
	protected void downloadApk() {
		//判断SD卡是否挂载
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			String path = Environment.getExternalStorageDirectory()
					.getAbsolutePath()+"/mobilsafe66.apk";
			
			tvProgres.setVisibility(View.VISIBLE);
			
			//xUtils  在主线程运行
			HttpUtils utils=new HttpUtils();
										//arg3:是一个回调
			utils.download(mUrl, path, new RequestCallBack<File>() {
				//获取下载进度
				@Override				//总数		下载进度			是否正在上传
				public void onLoading(long total, long current, boolean isUploading) {
					// TODO Auto-generated method stub
					super.onLoading(total, current, isUploading);
					//下载进度
					int percent=(int)(100 *current/total);
					System.out.println("下载进度"+percent+"%");
					tvProgres.setText("下载进度"+percent+"%");
					
					
				}
				@Override
				public void onSuccess(ResponseInfo<File> responseInfo) {
					// 下载成功
					String p=responseInfo.result.getAbsolutePath();
					System.out.println("下载成功"+p);
					
					//下载成功跳转到系统安装界面
					Intent intent=new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					intent.setDataAndType(Uri.fromFile(responseInfo.result), 
							"application/vnd.android.package-archive");
					startActivityForResult(intent, 0);
				}
				
				@Override
				public void onFailure(HttpException errot, String msg) {
					// 下载失败
					errot.printStackTrace();
					Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
				}
			});
		}else{
			Toast.makeText(getApplicationContext(), "没有找到SDcard！", Toast.LENGTH_SHORT).show();
		}
		
	}
	private String getVersionName(){
    	PackageManager pm=getPackageManager();//获取包管理器
    	
    	try {
			PackageInfo packageinfo=pm.getPackageInfo(getPackageName(), 0);//获取包名，获取包信息
			String versionName=packageinfo.versionName;
			int versionCode=packageinfo.versionCode;
			System.out.println("#########"+versionName+"  "+versionCode);
			
			return versionName;
			
		} catch (NameNotFoundException e) {
			// 包名未找到异常
			e.printStackTrace();
		}
		return "";
    	
    }
	
	private int getVersionCode(){
		PackageManager pm=getPackageManager();//获取包管理器
		    	
		    	try {
					PackageInfo packageinfo=pm.getPackageInfo(getPackageName(), 0);//获取包名，获取包信息
					int versionCode=packageinfo.versionCode;
					
					System.out.println("#########"+"  "+versionCode);
					
					return versionCode;
					
				} catch (NameNotFoundException e) {
					// 包名未找到异常
					e.printStackTrace();
				}
				return -1;
	}
	//跳到主页面
	
	private void enterHome(){
		startActivity(new Intent(this, HomeActivity.class));
		finish();
	}
	//用户取消安装，会回调此方法
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		enterHome();
	}
   
}
