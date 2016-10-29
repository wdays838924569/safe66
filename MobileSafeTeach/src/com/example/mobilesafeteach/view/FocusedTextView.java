package com.example.mobilesafeteach.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
//强制获取焦点的TextView
public class FocusedTextView extends TextView {

	
	//当布局文件中具有属性和样式style时候，系统底层解析时，就会走粗构造方法
	public FocusedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		System.out.println("FocusedTextView style");
	}
	//当布局文件中具有属性时，系统底层解析时，就会走此构造方法
	public FocusedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		System.out.println("FocusedTextView AttributeSet");
	}
	//从嗲吗中new对象
	public FocusedTextView(Context context) {
		super(context);
		System.out.println("FocusedTextView context");
	}
	//重写父类焦点的方法
	@Override
	public boolean isFocused() {
		return true;//强制使他获取焦点
	}
}
