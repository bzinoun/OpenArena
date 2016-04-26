package com.openarena.util;

import android.util.Log;

public class L {

	public static void i(String message) {
		if (Const.IS_DEBUG_MODE) {
			Log.i(Const.APP_TAG, message);
		}
	}

	public static void i(Class c, String message) {
		if (Const.IS_DEBUG_MODE) {
			Log.i(Const.APP_TAG, "{" + c.getSimpleName() + "} : " + message);
		}
	}

	public static void e(String message) {
		if (Const.IS_DEBUG_MODE) {
			Log.e(Const.APP_TAG, message);
		}
	}

	public static void e(Class c, String message) {
		if (Const.IS_DEBUG_MODE) {
			Log.e(Const.APP_TAG, "{" + c.getSimpleName() + "} : " + message);
		}
	}

	public static void d(String message) {
		if (Const.IS_DEBUG_MODE) {
			Log.d(Const.APP_TAG, message);
		}
	}

	public static void d(Class c, String message) {
		if (Const.IS_DEBUG_MODE) {
			Log.d(Const.APP_TAG, "{" + c.getSimpleName() + "} : " + message);
		}
	}

}
