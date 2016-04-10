package com.openarena.view.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.openarena.R;
import com.openarena.controllers.Controller;
import com.openarena.model.objects.Scores;
import com.openarena.model.objects.Team;
import com.openarena.util.UI;

public class FragmentTeam extends Fragment implements Controller.OnGetTeam {

	public static final String TAG = "FragmentTeam";

	private FrameLayout mProgressContent;
	private LinearLayout mErrorContent;
	private LinearLayout mEmptyContent;
	private ImageView mIcon;
	private TextView mName;
	private TextView mShortName;
	private TextView mSquadMarketValue;
	private Snackbar mSnackbar;
	private Controller mController;
	private Scores mScores;
	private Team mTeam;
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
		if (mController == null) mController = Controller.getInstance();
		if (mScores == null) mScores = getArguments().getParcelable("scores");
		if (mTeam == null && savedInstanceState != null) {
			mTeam = savedInstanceState.getParcelable("team");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_team, container, false);
		setupUI(view);
		showContent();
		mIsShow = true;
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (mErrorContent != null) mErrorContent = null;
		if (mEmptyContent != null) mEmptyContent = null;
		if (mProgressContent != null) mProgressContent = null;
		if (mIcon != null) mIcon = null;
		if (mName != null) mName = null;
		if (mShortName != null) mShortName = null;
		if (mSquadMarketValue != null) mSquadMarketValue = null;
		if (mSnackbar != null) {
			mSnackbar.dismiss();
			mSnackbar = null;
		}
		mIsShow = false;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mTeam != null) outState.putParcelable("team", mTeam);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mController != null) mController = null;
		if (mScores != null) mScores = null;
		if (mTeam != null) mTeam = null;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_team, menu);
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
	public void onError(int code) {
		if (mIsShow) {
			UI.hide(mEmptyContent, mProgressContent, mIcon, mName, mShortName, mSquadMarketValue);
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
	public void onSuccess(Team data) {
		if (mIsShow) {
			mTeam = data;
			UI.hide(mErrorContent, mEmptyContent, mProgressContent);
			UI.show(mIcon, mName, mShortName, mSquadMarketValue);
			ImageLoader.getInstance().displayImage(
					mTeam.getCrestURL(),
					mIcon,
					new DisplayImageOptions.Builder()
							.showImageOnFail(R.drawable.ic_player)
							.build()
			);
			mName.setText(mTeam.getName());
			mShortName.setText(mTeam.getShortName());
			mSquadMarketValue.setText(mTeam.getSquadMarketValue());
			if (mSnackbar != null) mSnackbar.dismiss();
		}
	}

	private void setupUI(View view) {
		ActionBar toolbar = ((AppCompatActivity)getActivity()).getSupportActionBar();
		if (toolbar != null) {
			toolbar.setTitle(mScores.getTeam());
		}
		mProgressContent = (FrameLayout) view.findViewById(R.id.content_progress);
		mEmptyContent = (LinearLayout) view.findViewById(R.id.content_empty);
		mErrorContent = (LinearLayout) view.findViewById(R.id.content_error);
		mIcon = (ImageView) view.findViewById(R.id.icon);
		mName = (TextView) view.findViewById(R.id.name);
		mShortName = (TextView) view.findViewById(R.id.short_name);
		mSquadMarketValue = (TextView) view.findViewById(R.id.squad_market_value);
		UI.hide(mErrorContent, mEmptyContent, mProgressContent, mIcon, mName, mShortName, mSquadMarketValue);
	}

	private void showContent() {
		if (mTeam == null) loadData();
		else {
			UI.hide(mErrorContent, mEmptyContent, mProgressContent);
			UI.show(mIcon, mName, mShortName, mSquadMarketValue);
			ImageLoader.getInstance().displayImage(mTeam.getCrestURL(), mIcon);
			mName.setText(mTeam.getName());
			mShortName.setText(mTeam.getShortName());
			mSquadMarketValue.setText(mTeam.getSquadMarketValue());
		}
	}

	private void loadData() {
		UI.hide(mErrorContent, mEmptyContent, mIcon, mName, mShortName, mSquadMarketValue);
		UI.show(mProgressContent);
		if (mController != null) mController.getTeam(getActivity(), mScores.getTeamId(), this);
	}

}
