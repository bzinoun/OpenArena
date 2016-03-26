package com.openarena.view.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.openarena.R;
import com.openarena.controllers.Controller;
import com.openarena.model.RecyclerViewItemTouchListener;
import com.openarena.model.adapters.LeaguesAdapter;
import com.openarena.model.interfaces.OnItemClickListener;
import com.openarena.model.objects.League;
import com.openarena.util.L;
import com.openarena.util.UI;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
		implements Controller.OnGetLeagues, OnItemClickListener {

	private Toolbar mToolbar;
	private RecyclerView mRecyclerView;
	private FrameLayout mProgress;
	private LinearLayout mErrorContent;
	private LeaguesAdapter mAdapter;
	private Controller mController;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setupUI();
		mController = Controller.getInstance();
		mController.getListOfLeagues(this, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
			case R.id.action_refresh:
				UI.hide(mRecyclerView, mErrorContent);
				UI.show(mProgress);
				mController.getListOfLeagues(this, this);
				break;

			default:
				return super.onOptionsItemSelected(item);
		}
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mToolbar != null) mToolbar = null;
		if (mRecyclerView != null) mRecyclerView = null;
		if (mProgress != null) mProgress = null;
		if (mErrorContent != null) mErrorContent = null;
		if (mAdapter != null) mAdapter = null;
		if (mController != null) mController = null;
	}

	@Override
	public void onError(int code) {
		L.e(MainActivity.class, "Error loading list");
		UI.hide(mRecyclerView, mProgress);
		UI.show(mErrorContent);
		Snackbar.make(mErrorContent, R.string.snackbar_result_null_text, Snackbar.LENGTH_INDEFINITE)
				.setAction(R.string.snackbar_result_null_action, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mController.getListOfLeagues(MainActivity.this, MainActivity.this);
					}
				})
				.show();
	}

	@Override
	public void onSuccess(ArrayList<League> data) {
		UI.hide(mProgress, mErrorContent);
		UI.show(mRecyclerView);
		if (mAdapter == null) {
			mAdapter = new LeaguesAdapter(data);
			mRecyclerView.setAdapter(mAdapter);
		} else {
			mAdapter.changeData(data);
		}
	}

	@Override
	public void onItemClick(View view, int position) {
		if (mAdapter != null) {
			League item = mAdapter.getItem(position);
			Intent intent = new Intent(this, TimeLineActivity.class);
			intent.putExtra("id", item.getID());
			startActivity(intent);
		}
	}

	@Override
	public void onItemLongClick(View view, int position) {

	}

	private void setupUI() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);
		mProgress = (FrameLayout) findViewById(R.id.progress_bar);
		mErrorContent = (LinearLayout) findViewById(R.id.error_content);
		mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(
				MainActivity.this,
				LinearLayoutManager.VERTICAL,
				false));
		mRecyclerView.addOnItemTouchListener(
				new RecyclerViewItemTouchListener(this, mRecyclerView, this));
		UI.hide(mRecyclerView, mErrorContent);
		UI.show(mProgress);
	}
}
