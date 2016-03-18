package com.openarena.activitys;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.openarena.R;
import com.openarena.fragments.SplashscreenFragment;

public class MainActivity extends AppCompatActivity {

	private FragmentManager mFragmentManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setupUI();
		if (mFragmentManager == null) mFragmentManager = getFragmentManager();

		mFragmentManager.beginTransaction()
				.replace(R.id.main_container, SplashscreenFragment.getInstance(null))
				.commit();
	}

	private void setupUI() {

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mFragmentManager != null) mFragmentManager = null;
	}

}
