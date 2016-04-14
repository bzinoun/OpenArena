package com.openarena.view.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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

import com.openarena.R;
import com.openarena.controllers.Controller;
import com.openarena.model.RecyclerViewItemTouchListener;
import com.openarena.model.adapters.ScoresAdapter;
import com.openarena.model.comparators.ComparatorScores;
import com.openarena.model.interfaces.EventListener;
import com.openarena.model.interfaces.OnItemClickListener;
import com.openarena.model.objects.EventData;
import com.openarena.model.objects.League;
import com.openarena.model.objects.Scores;
import com.openarena.util.Const;
import com.openarena.util.UI;

import java.util.ArrayList;

public class FragmentScores extends Fragment
		implements Controller.OnGetScores, OnItemClickListener {

	public static final String TAG = "FragmentScores";

	private RecyclerView mRecyclerView;
	private FrameLayout mProgressContent;
	private LinearLayout mErrorContent;
	private LinearLayout mEmptyContent;
	private ScoresAdapter mAdapter;
	private Controller mController;
	private EventListener mEventListener;
	private League mLeague;
	private Snackbar mSnackbar;
	private AlertDialog mDialog;
	private boolean mIsShow;

	public static FragmentScores getInstance(@Nullable Bundle data) {
		FragmentScores fragment = new FragmentScores();
		fragment.setArguments(data == null ? new Bundle() : data);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		if (mLeague == null) mLeague = getArguments().getParcelable("league");
		if (savedInstanceState != null) {
			ArrayList<Scores> list = savedInstanceState.getParcelableArrayList("list");
			if (list != null) mAdapter = new ScoresAdapter(getResources(), list);
		}
	}

	@Override
	public View onCreateView(
			LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_scores, container, false);
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
		if (mEmptyContent != null) mEmptyContent = null;
		if (mErrorContent != null) mErrorContent = null;
		if (mProgressContent != null) mProgressContent = null;
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
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
			ArrayList<Scores> list = mAdapter.getList();
			if (!list.isEmpty()) outState.putParcelableArrayList("list", list);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mEventListener != null) mEventListener = null;
		if (mController != null) mController = null;
		if (mLeague != null) mLeague = null;
		if (mAdapter != null) mAdapter = null;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_scores, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
			case R.id.action_refresh:
				loadData();
				break;

			case R.id.action_sort:
				showSortDialog();
				break;

			default:
				return super.onOptionsItemSelected(item);
		}
		return true;
	}

	@Override
	public void onError(int code) {
		if (mIsShow && (mAdapter == null || mAdapter.getList().isEmpty())) {
			if (code == Const.ERROR_CODE_RESULT_EMPTY) {
				UI.hide(mRecyclerView, mErrorContent, mProgressContent);
				UI.show(mEmptyContent);
			} else {
				UI.hide(mRecyclerView, mEmptyContent, mProgressContent);
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
	}

	@Override
	public void onSuccess(ArrayList<Scores> data) {
		if (mIsShow) {
			UI.hide(mErrorContent, mEmptyContent, mProgressContent);
			UI.show(mRecyclerView);
			if (mSnackbar != null) mSnackbar.dismiss();
			if (mAdapter == null) {
				mAdapter = new ScoresAdapter(getResources(), data);
				mRecyclerView.setAdapter(mAdapter);
			} else {
				mAdapter.changeData(data);
			}
		}
	}

	@Override
	public void onItemClick(View view, int position) {
		mEventListener.onEvent(new EventData(Const.EVENT_CODE_SELECT_SCORES)
				.setScores(mAdapter.getItem(position)));
	}

	@Override
	public void onItemLongClick(View view, int position) {

	}

	private void setupUI(View view) {
		ActionBar toolbar = ((AppCompatActivity)getActivity()).getSupportActionBar();
		if (toolbar != null) {
			toolbar.setTitle(mLeague.getCaption());
			toolbar.setSubtitle(null);
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
		mProgressContent = (FrameLayout) view.findViewById(R.id.content_progress);
		mEmptyContent = (LinearLayout) view.findViewById(R.id.content_empty);
		mErrorContent = (LinearLayout) view.findViewById(R.id.content_error);
		UI.hide(mEmptyContent, mErrorContent, mProgressContent, mRecyclerView);
	}

	private void showContent() {
		if (mAdapter == null) loadData();
		else {
			UI.hide(mEmptyContent, mErrorContent, mProgressContent);
			UI.show(mRecyclerView);
			mRecyclerView.setAdapter(mAdapter);
		}
	}

	private void loadData() {
		UI.hide(mRecyclerView, mEmptyContent, mErrorContent);
		UI.show(mProgressContent);
		if (mAdapter != null) mAdapter = null;
		mController.getScores(getActivity(), mLeague.getID(), this);
	}

	private void showSortDialog() {
		if (mAdapter != null) {
			View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_scores_sort, null, false);
			view.findViewById(R.id.played_games).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					ComparatorScores.sortByPlayedGames(mAdapter.getList());
					mAdapter.notifyDataSetChanged();
					mDialog.dismiss();
				}
			});
			view.findViewById(R.id.goals).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					ComparatorScores.sortByGoals(mAdapter.getList());
					mAdapter.notifyDataSetChanged();
					mDialog.dismiss();
				}
			});
			mDialog = new AlertDialog.Builder(getActivity(), R.style.AppTheme_Dialog)
					.setTitle(getString(R.string.scores_sort_dialog_title))
					.setView(view)
					.create();
			mDialog.show();
		}
	}

}
