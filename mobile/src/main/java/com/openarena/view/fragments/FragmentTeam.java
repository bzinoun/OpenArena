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
import com.openarena.model.RecyclerViewItemTouchListener;
import com.openarena.model.interfaces.OnItemClickListener;
import com.openarena.model.objects.Scores;
import com.openarena.util.UI;

public class FragmentTeam extends Fragment implements OnItemClickListener {

	public static final String TAG = "FragmentTeam";

	private RecyclerView mRecyclerView;
	private FrameLayout mProgressContent;
	private LinearLayout mErrorContent;
	private LinearLayout mEmptyContent;
	private Snackbar mSnackbar;
	private Scores mScores;
	private boolean mIsShow;

	public static FragmentTeam getInstance(@Nullable Bundle data) {
		FragmentTeam fragment = new FragmentTeam();
		fragment.setArguments(data == null ? new Bundle() : data);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		if (mScores == null) mScores = getArguments().getParcelable("scores");

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_fixture_details, container, false);
		setupUI(view);
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
		if (mSnackbar != null) {
			mSnackbar.dismiss();
			mSnackbar = null;
		}
		mIsShow = false;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_fixture_details, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
			case R.id.action_refresh:
				loadData();
				break;

			case R.id.action_score_table:
				Snackbar.make(getActivity().findViewById(R.id.main_container), "score table", Snackbar.LENGTH_SHORT).show();
				break;

			default:
				return super.onOptionsItemSelected(item);
		}
		return true;
	}

	@Override
	public void onItemClick(View view, int position) {

	}

	@Override
	public void onItemLongClick(View view, int position) {

	}

	private void setupUI(View view) {
		ActionBar toolbar = ((AppCompatActivity)getActivity()).getSupportActionBar();
		if (toolbar != null) {
			toolbar.setTitle(mScores.getTeam());
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
		UI.hide(mRecyclerView, mErrorContent, mEmptyContent, mProgressContent);
	}

	private void showContent() {

	}

	private void loadData() {
		UI.hide(mRecyclerView, mEmptyContent, mErrorContent);
		UI.show(mProgressContent);

	}

}
