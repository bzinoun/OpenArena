package com.openarena.util;

import android.util.Log;

public class L {
	public static final String TAG = "TAG";

	public static void i(String message) {
		Log.i(TAG, message);
	}

	public static void i(Object object, String message) {
		Log.i(TAG, "{" + object.getClass().getSimpleName() + "} : " + message);
	}

	public static void e(String message) {
		Log.e(TAG, message);
	}

	public static void e(Object object, String message) {
		Log.e(TAG, "{" + object.getClass().getSimpleName() + "} : " + message);
	}
}
