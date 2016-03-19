package com.openarena.controllers;

import android.os.Handler;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Controller {

	private static final int NUMBERS_OF_CORES = Runtime.getRuntime().availableProcessors();
	private static ThreadPoolExecutor sExecutor;
	private static Handler sHandler;

	public static void init() {
		sExecutor = new ThreadPoolExecutor(
				1,
				NUMBERS_OF_CORES,
				5000,
				TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>());
		sHandler = new Handler();
	}

	public void clean() {
		if (sExecutor != null) sExecutor.shutdown();
		if (sHandler != null) sHandler = null;
	}

}
