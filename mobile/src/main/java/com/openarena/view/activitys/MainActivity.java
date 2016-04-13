package com.openarena.view.activitys;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.openarena.R;
import com.openarena.model.interfaces.EventListener;
import com.openarena.model.objects.EventData;
import com.openarena.util.Const;
import com.openarena.util.L;
import com.openarena.view.fragments.FragmentFixtureInfo;
import com.openarena.view.fragments.FragmentFixtures;
import com.openarena.view.fragments.FragmentFixturesTeam;
import com.openarena.view.fragments.FragmentLeagues;
import com.openarena.view.fragments.FragmentPlayers;
import com.openarena.view.fragments.FragmentScores;
import com.openarena.view.dialogs.DialogTeam;

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
				if (mFragmentManager.findFragmentByTag(FragmentFixtures.TAG) == null) {
					Bundle data1 = new Bundle();
					data1.putParcelable("league", event.getLeague());
					mFragmentManager.beginTransaction()
							.replace(R.id.main_container, FragmentFixtures.getInstance(data1))
							.addToBackStack(FragmentFixtures.TAG)
							.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
							.commit();
				}
				break;

			case Const.EVENT_CODE_SELECT_FIXTURE:
				if (mFragmentManager.findFragmentByTag(FragmentFixtureInfo.TAG) == null) {
					Bundle data2 = new Bundle();
					data2.putParcelable("fixture", event.getFixture());
					mFragmentManager.beginTransaction()
							.replace(R.id.main_container, FragmentFixtureInfo.getInstance(data2))
							.addToBackStack(FragmentFixtureInfo.TAG)
							.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
							.commit();
				}
				break;

			case Const.EVENT_CODE_SHOW_SCORES_TABLE:
				if (mFragmentManager.findFragmentByTag(FragmentScores.TAG) == null) {
					Bundle data3 = new Bundle();
					data3.putParcelable("league", event.getLeague());
					mFragmentManager.beginTransaction()
							.replace(R.id.main_container, FragmentScores.getInstance(data3))
							.addToBackStack(FragmentScores.TAG)
							.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
							.commit();
				}
				break;

			case Const.EVENT_CODE_SELECT_SCORES:
				Bundle data4 = new Bundle();
				data4.putParcelable("scores", event.getScores());
				DialogTeam dialog = DialogTeam.getInstance(data4);
				dialog.show(mFragmentManager, DialogTeam.TAG);
				break;

			case Const.EVENT_CODE_SELECT_PLAYERS:
				if (mFragmentManager.findFragmentByTag(FragmentPlayers.TAG) == null) {
					Bundle data5 = new Bundle();
					data5.putParcelable("team", event.getTeam());
					mFragmentManager.beginTransaction()
							.replace(R.id.main_container, FragmentPlayers.getInstance(data5))
							.addToBackStack(FragmentPlayers.TAG)
							.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
							.commit();
				}
				break;

			case Const.EVENT_CODE_SELECT_FIXTURES:
				if (mFragmentManager.findFragmentByTag(FragmentFixturesTeam.TAG) == null) {
					Bundle data6 = new Bundle();
					data6.putParcelable("team", event.getTeam());
					mFragmentManager.beginTransaction()
							.replace(R.id.main_container, FragmentFixturesTeam.getInstance(data6))
							.addToBackStack(FragmentFixturesTeam.TAG)
							.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
							.commit();
				}
				break;

			default:
				L.e(MainActivity.class, "new event code->" + code);
		}
	}

	private void setupUI() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);
	}

	private void showContent() {
		if (mFragmentManager.findFragmentByTag(FragmentLeagues.TAG) == null) {
			mFragmentManager.beginTransaction()
					.replace(
							R.id.main_container,
							FragmentLeagues.getInstance(null),
							FragmentLeagues.TAG)
					.commit();
		}
	}

}
