package com.langchao.mamanage.volley;

import android.app.Application;
import android.content.Context;

import org.xutils.x;


public class App extends Application {
	private static Context mContext;

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
		x.Ext.init(this);//Xutils初始化
	}

	public static Context getContext() {
		return mContext;
	}
}
