package com.openarena.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.openarena.R;
import com.openarena.controllers.Controller;
import com.openarena.controllers.DBManager;
import com.openarena.model.abstractions.AbstractFragment;
import com.openarena.model.listeners.RecyclerViewItemTouchListener;
import com.openarena.model.adapters.FixturesAdapter;
import com.openarena.model.interfaces.EventListener;
import com.openarena.model.interfaces.OnItemClickListener;
import com.openarena.model.objects.EventData;
import com.openarena.model.objects.Fixture;
import com.openarena.model.objects.Head2head;
import com.openarena.util.Const;
import com.openarena.util.UI;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class FragmentFixtureInfo extends AbstractFragment
		implements OnItemClickListener, Controller.OnGetFixtureDetails {

	public static final String TAG = "FragmentFixtureInfo";

	private RecyclerView mRecyclerView;
	private FrameLayout mProgressContent;
	private LinearLayout mErrorContent;
	private LinearLayout mEmptyContent;
	private TextView mHome;
	private TextView mAway;
	private TextView mResult;
	private TextView mHeader;
	private Snackbar mSnackbar;
	private EventListener mEventListener;
	private FixturesAdapter mAdapter;
	private Controller mController;
	private Fixture mFixture;
	private boolean mIsShow;

	public static FragmentFixtureInfo getInstance(@Nullable Bundle data) {
		FragmentFixtureInfo fragment = new FragmentFixtureInfo();
		fragment.setArguments(data == null ? new Bundle() : data);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		if (mFixture == null) mFixture = getArguments().getParcelable("fixture");
		if (savedInstanceState != null) {
			ArrayList<Fixture> list = savedInstanceState.getParcelableArrayList("list");
			if (list != null) mAdapter = new FixturesAdapter(getResources(), list);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_fixture_info, container, false);
		setupUI(view);
		if (mEventListener == null) mEventListener = (EventListener) getActivity();
		if (mController == null) mController = Controller.getInstance();
		showContent();
		mIsShow = true;
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (mRecyclerView != null) mRecyclerView = null;
		if (mErrorContent != null) mErrorContent = null;
		if (mEmptyContent != null) mEmptyContent = null;
		if (mProgressContent != null) mProgressContent = null;
		if (mHome != null) mHome = null;
		if (mAway != null) mAway = null;
		if (mResult != null) mResult = null;
		if (mHeader != null) mHeader = null;
		if (mSnackbar != null) {
			mSnackbar.dismiss();
			mSnackbar = null;
		}
		mIsShow = false;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mAdapter != null) {
			ArrayList<Fixture> list = mAdapter.getList();
			if (!list.isEmpty()) outState.putParcelableArrayList("list", list);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mEventListener != null) mEventListener = null;
		if (mAdapter != null) mAdapter = null;
		if (mController != null) mController = null;
		if (mFixture != null) mFixture = null;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_fixture_info, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
			// FIXME: 17.04.2016
			/*case R.id.action_refresh:
				loadData();
				break;*/

			case R.id.action_score_table:
				mEventListener.onEvent(
						new EventData(Const.EVENT_CODE_SHOW_SCORES_TABLE)
								.setLeague(DBManager.getLeague(mFixture.getSoccerSeasonID())));
				break;

			case R.id.action_settings:
				mEventListener.onEvent(new EventData(Const.EVENT_CODE_SHOW_SETTINGS));
				break;

			case R.id.action_about:
				mEventListener.onEvent(new EventData(Const.EVENT_CODE_SHOW_ABOUT));
				break;

			default:
				return super.onOptionsItemSelected(item);
		}
		return true;
	}

	@Override
	public void onItemClick(View view, int position) {
		setupHeader(mAdapter.getItem(position));
	}

	@Override
	public void onItemLongClick(View view, int position) {
		
	}

	@Override
	public void onError(int code) {
		if (mIsShow && (mAdapter == null || mAdapter.getList().isEmpty())) {
			UI.hide(mRecyclerView, mEmptyContent, mProgressContent, mHeader);
			UI.show(mErrorContent);
			mSnackbar = Snackbar.make(
					getActivity().findViewById(R.id.main_container),
					R.string.snackbar_result_null_text,
					Snackbar.LENGTH_INDEFINITE)
					.setAction(
							R.string.snackbar_result_null_action,
							new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									loadData();
								}
							}
					);
			mSnackbar.show();
		}
	}

	@Override
	public void onSuccess(Head2head data) {
		if (mIsShow) {
			if (mSnackbar != null) mSnackbar.dismiss();
			if (data.getFixtures() == null || data.getFixtures().isEmpty()) {
				UI.hide(mErrorContent, mProgressContent, mRecyclerView, mHeader);
				UI.show(mEmptyContent);
			}
			else {
				UI.hide(mErrorContent, mEmptyContent, mProgressContent);
				UI.show(mRecyclerView, mHeader);
				if (mAdapter == null) {
					mAdapter = new FixturesAdapter(getResources(), data.getFixtures());
					mRecyclerView.setAdapter(mAdapter);
				} else {
					mAdapter.changeData(data.getFixtures());
				}
			}
		}
	}

	private void setupUI(View view) {
		ActionBar toolbar = ((AppCompatActivity)getActivity()).getSupportActionBar();
		if (toolbar != null) {
			toolbar.setTitle(getString(R.string.fixture_details_title));
			if (mFixture.getStatus() == Fixture.TIMED) {
				toolbar.setSubtitle(
						new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
								.format(new Date(mFixture.getDate())));
			}
		}
		mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(
				getActivity(),
				LinearLayoutManager.VERTICAL,
				false));
		mRecyclerView.addOnItemTouchListener(new RecyclerViewItemTouchListener(
				getActivity(),
				mRecyclerView,
				this));
		mRecyclerView.setHasFixedSize(true);
		mHome = (TextView) view.findViewById(R.id.home);
		mAway = (TextView) view.findViewById(R.id.away);
		mResult = (TextView) view.findViewById(R.id.result);
		mHeader = (TextView) view.findViewById(R.id.header);
		mProgressContent = (FrameLayout) view.findViewById(R.id.content_progress);
		mEmptyContent = (LinearLayout) view.findViewById(R.id.content_empty);
		mErrorContent = (LinearLayout) view.findViewById(R.id.content_error);
		UI.hide(mRecyclerView, mErrorContent, mEmptyContent, mProgressContent, mHeader);
	}

	private void showContent() {
		setupHeader(mFixture);
		if (mAdapter == null) loadData();
		else {
			UI.hide(mEmptyContent, mErrorContent, mProgressContent);
			UI.show(mRecyclerView, mHeader);
			mRecyclerView.setAdapter(mAdapter);
		}
	}

	private void setupHeader(Fixture fixture) {
		mHome.setText(fixture.getHomeTeamName());
		mAway.setText(fixture.getAwayTeamName());
		if (fixture.getStatus() != Fixture.TIMED) {
			mResult.setText(String.format(
					getString(R.string.fixture_details_result),
					fixture.getGoalsHomeTeam(),
					fixture.getGoalsAwayTeam()));
		}
		else {
			mResult.setText(getString(R.string.fixture_details_empty_result));
		}
	}

	private void loadData() {
		UI.hide(mRecyclerView, mEmptyContent, mErrorContent, mHeader);
		UI.show(mProgressContent);
		mController.getFixtureDetails(getActivity(), mFixture.getID(), this);
	}

}
