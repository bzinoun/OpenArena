package com.openarena.view.activitys;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.openarena.R;
import com.openarena.model.interfaces.EventListener;
import com.openarena.model.objects.EventData;
import com.openarena.util.Const;
import com.openarena.view.fragments.FragmentLeagues;
import com.openarena.view.fragments.FragmentTimeLine;

public class MainActivity extends AppCompatActivity implements EventListener {

	private Toolbar mToolbar;
	private FragmentManager mFragmentManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setupUI();

		mFragmentManager = getFragmentManager();
		showContent();
	}

	@Override
	public void onBackPressed() {
		if (mFragmentManager.getBackStackEntryCount() > 0) mFragmentManager.popBackStack();
		else super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mToolbar != null) mToolbar = null;
		if (mFragmentManager != null) mFragmentManager = null;
	}

	@Override
	public void onEvent(EventData event) {
		int code = event.gecCode();
		switch (code) {
			case Const.EVENT_CODE_SELECT_LEAGUE:
				if (mFragmentManager.findFragmentByTag(FragmentTimeLine.TAG) == null) {
					Bundle data = new Bundle();
					data.putInt("soccerSeasonId", event.getSoccerSeasonId());
					mFragmentManager.beginTransaction()
							.replace(R.id.main_container, FragmentTimeLine.getInstance(data))
							.addToBackStack(FragmentTimeLine.TAG)
							.commit();
				}
				break;
		}
	}

	private void setupUI() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);
	}

	private void showContent() {
			if (mFragmentManager.findFragmentByTag(FragmentLeagues.TAG) == null) {
				mFragmentManager.beginTransaction()
						.replace(R.id.main_container, FragmentLeagues.getInstance(), FragmentLeagues.TAG)
						.commit();
			}
	}

}
