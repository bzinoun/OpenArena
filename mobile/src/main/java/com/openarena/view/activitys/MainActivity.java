package com.openarena.view.activitys;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.openarena.R;
import com.openarena.controllers.Controller;
import com.openarena.model.PreferencesManager;
import com.openarena.model.adapters.LeaguesAdapter;
import com.openarena.model.objects.League;
import com.openarena.util.Const;
import com.openarena.util.L;
import com.openarena.util.UI;
import com.openarena.view.fragments.SplashscreenFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

	private DrawerLayout mDrawer;
	private Toolbar mToolbar;
	private FragmentManager mFragmentManager;
	private RecyclerView mRecyclerView;
	private LeaguesAdapter mAdapter;
	private Controller mController;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mFragmentManager = getFragmentManager();
		mController = Controller.getInstance();
		PreferencesManager.getInstance(this).setBoolean(Const.PREF_FIRST_ENTER, true).commit();
		setupUI();
		mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

		mController.getListOfLeagues(this, new Controller.OnGetLeagues() {
			@Override
			public void onError() {
				L.e(MainActivity.class, "Error");
			}

			@Override
			public void onSuccess(ArrayList<League> data) {
				NavigationView navigation = (NavigationView) findViewById(R.id.navigation_view);
				if (navigation != null) {
					mRecyclerView = (RecyclerView) navigation.findViewById(R.id.navigation_list);
					mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
					mAdapter = new LeaguesAdapter(data);
					mRecyclerView.setAdapter(mAdapter);
				} else {
					L.e(MainActivity.class, "RuntimeException");
					throw new RuntimeException("NavigationView have not find");
				}

				UI.show(mToolbar);

				mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
			}
		});

		if (mController.isFirstEnter()) {
			PreferencesManager.getInstance(this)
					.setBoolean(Const.PREF_FIRST_ENTER, false)
					.commit();
			mFragmentManager.beginTransaction()
					.replace(R.id.main_container, SplashscreenFragment.getInstance(null))
					.commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		if (mDrawer.isDrawerOpen(GravityCompat.START)) {
			mDrawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (mDrawer != null) mDrawer = null;
				if (mToolbar != null) mToolbar = null;
				if (mFragmentManager != null) mFragmentManager = null;
				if (mRecyclerView != null) mRecyclerView = null;
				if (mAdapter != null) mAdapter = null;
				if (mController != null) mController = null;
				System.gc();
			}
		}).start();
	}

	private void setupUI() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);
		mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this,
				mDrawer,
				mToolbar,
				R.string.drawer_open,
				R.string.drawer_close);
		mDrawer.addDrawerListener(toggle);
		toggle.syncState();
		UI.hide(mToolbar);
	}

}
