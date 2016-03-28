package com.openarena.view.fragments;

import android.app.Fragment;
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
import com.openarena.R;
import com.openarena.controllers.Controller;
import com.openarena.model.RecyclerViewItemTouchListener;
import com.openarena.model.adapters.LeaguesAdapter;
import com.openarena.model.interfaces.EventListener;
import com.openarena.model.interfaces.OnItemClickListener;
import com.openarena.model.objects.EventData;
import com.openarena.model.objects.League;
import com.openarena.util.Const;
import com.openarena.util.UI;
import java.util.ArrayList;

public class FragmentLeagues extends Fragment
		implements Controller.OnGetLeagues, OnItemClickListener {

	public static final String TAG = "FragmentLeagues";

	private RecyclerView mRecyclerView;
	private FrameLayout mProgressContent;
	private LinearLayout mErrorContent;
	private LinearLayout mEmptyContent;
	private LeaguesAdapter mAdapter;
	private Controller mController;
	private EventListener mEventListener;

	public static FragmentLeagues getInstance() {
		Bundle args = new Bundle();
		FragmentLeagues fragment = new FragmentLeagues();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		if (savedInstanceState != null) {
			ArrayList<League> list = savedInstanceState.getParcelableArrayList("list");
			if (list != null && !list.isEmpty()) mAdapter = new LeaguesAdapter(list);
		}
	}

	@Nullable
	@Override
	public View onCreateView(
			LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_leagues, container, false);
		setupUI(view);
		mEventListener = (EventListener) getActivity();
		mController = Controller.getInstance();
		showContent();
		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_leagues, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
			case R.id.action_refresh:
				loadData();
				break;

			default:
				return super.onOptionsItemSelected(item);
		}
		return true;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mAdapter != null) {
			ArrayList<League> list = mAdapter.getList();
			if (!list.isEmpty()) outState.putParcelableArrayList("list", list);
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (mEventListener != null) mEventListener = null;
		if (mController != null) mController = null;
		if (mRecyclerView != null) mRecyclerView = null;
		if (mAdapter != null) mAdapter = null;
		if (mEmptyContent != null) mEmptyContent = null;
		if (mErrorContent != null) mErrorContent = null;
		if (mProgressContent != null) mProgressContent = null;
	}

	@Override
	public void onError(int code) {
		mAdapter = null;
		if (code == Const.ERROR_CODE_RESULT_EMPTY) {
			UI.hide(mRecyclerView, mErrorContent, mProgressContent);
			UI.show(mEmptyContent);
		}
		else {
			UI.hide(mRecyclerView, mEmptyContent, mProgressContent);
			UI.show(mErrorContent);
			Snackbar.make(mErrorContent, R.string.snackbar_result_null_text, Snackbar.LENGTH_INDEFINITE)
					.setAction(R.string.snackbar_result_null_action, new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							loadData();
						}
					})
					.show();
		}
	}

	@Override
	public void onSuccess(ArrayList<League> data) {
		UI.hide(mErrorContent, mEmptyContent, mProgressContent);
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
		mEventListener.onEvent(new EventData(Const.EVENT_CODE_SELECT_LEAGUE)
				.setSoccerSeasonId(mAdapter.getItem(position).getID()));
	}

	@Override
	public void onItemLongClick(View view, int position) {

	}

	private void setupUI(View view) {
		ActionBar toolbar = ((AppCompatActivity)getActivity()).getSupportActionBar();
		if (toolbar != null) {
			toolbar.setTitle(getString(R.string.leagues_title));
			toolbar.setSubtitle(R.string.leagues_subtitle);
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
		mProgressContent = (FrameLayout) view.findViewById(R.id.progress_content);
		mEmptyContent = (LinearLayout) view.findViewById(R.id.empty_content);
		mErrorContent = (LinearLayout) view.findViewById(R.id.error_content);
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
		mController.getListOfLeagues(getActivity(), this);
	}

}
