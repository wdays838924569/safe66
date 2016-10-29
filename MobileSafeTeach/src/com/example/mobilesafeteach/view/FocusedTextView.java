package com.example.mobilesafeteach.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
//ǿ�ƻ�ȡ�����TextView
public class FocusedTextView extends TextView {

	
	//�������ļ��о������Ժ���ʽstyleʱ��ϵͳ�ײ����ʱ���ͻ��ߴֹ��췽��
	public FocusedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		System.out.println("FocusedTextView style");
	}
	//�������ļ��о�������ʱ��ϵͳ�ײ����ʱ���ͻ��ߴ˹��췽��
	public FocusedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		System.out.println("FocusedTextView AttributeSet");
	}
	//��������new����
	public FocusedTextView(Context context) {
		super(context);
		System.out.println("FocusedTextView context");
	}
	//��д���ཹ��ķ���
	@Override
	public boolean isFocused() {
		return true;//ǿ��ʹ����ȡ����
	}
}
