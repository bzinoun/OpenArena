package com.openarena.view.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.openarena.R;
import com.openarena.controllers.Controller;
import com.openarena.model.adapters.FixturesAdapter;
import com.openarena.model.objects.Fixture;
import com.openarena.util.UI;

import java.util.ArrayList;

public class TimeLineActivity extends AppCompatActivity implements Controller.OnGetFixtures {

	private RecyclerView mRecyclerView;
	private LinearLayout mEmptyContent;
	private TextView mDay;
	private TextView mCount;
	private FixturesAdapter mAdapter;
	private Controller mController;
	private int mSoccerSeasonId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		setupUI();
		mSoccerSeasonId = getIntent().getIntExtra("id", 0);
		mController = Controller.getInstance();
		mDay.setText(R.string.timeline_today);
		mController.getListOfFixtures(this, mSoccerSeasonId, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_timeline, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
			case R.id.action_refresh:
				mController.getListOfFixtures(this, mSoccerSeasonId, this);
				break;

			default:
				return super.onOptionsItemSelected(item);
		}
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mRecyclerView != null) mRecyclerView = null;
		if (mAdapter != null) mAdapter = null;
		if (mController != null) mController = null;
		if (mEmptyContent != null) mEmptyContent = null;
		//if (mToolbar != null) mToolbar = null;
		//if (mProgress != null) mProgress = null;
	}

	@Override
	public void onError() {
		UI.hide(mRecyclerView);
		UI.show(mEmptyContent);
	}

	@Override
	public void onSuccess(ArrayList<Fixture> data) {
		mCount.setText(String.valueOf(data.size()));
		UI.hide(mEmptyContent);
		UI.show(mRecyclerView);
		if (mAdapter == null) {
			mAdapter = new FixturesAdapter(data);
			mRecyclerView.setAdapter(mAdapter);
		}
		else mAdapter.changeData(data);
	}

	private void setupUI() {
		mRecyclerView = (RecyclerView) findViewById(R.id.timeline_recycler_view);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(
				TimeLineActivity.this,
				LinearLayoutManager.VERTICAL,
				false));
		mEmptyContent = (LinearLayout) findViewById(R.id.empty_content);
		mDay = (TextView) findViewById(R.id.day);
		mCount = (TextView) findViewById(R.id.count);
	}
}
