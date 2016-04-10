package com.openarena.view.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class DialogTeam extends DialogFragment implements Controller.OnGetTeam {

	public static final String TAG = "DialogTeam";

	private FrameLayout mProgressContent;
	private LinearLayout mErrorContent;
	private LinearLayout mEmptyContent;
	private ImageView mIcon;
	private TextView mName;
	private TextView mShortName;
	private TextView mSquadMarketValue;
	private Button mFixtures;
	private Button mPlayers;
	private Controller mController;
	private Scores mScores;
	private Team mTeam;
	private boolean mIsShow;

	public static DialogTeam getInstance(@Nullable Bundle data) {
		DialogTeam fragment = new DialogTeam();
		fragment.setArguments(data == null ? new Bundle() : data);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogTeam.STYLE_NO_TITLE, R.style.AppTheme_AlertDialog);
		setHasOptionsMenu(true);
		if (mController == null) mController = Controller.getInstance();
		if (mScores == null) mScores = getArguments().getParcelable("scores");
		if (mTeam == null && savedInstanceState != null) {
			mTeam = savedInstanceState.getParcelable("team");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_team, container, false);
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
		if (mFixtures != null) mFixtures = null;
		if (mPlayers != null) mPlayers = null;
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
	public void onError(int code) {
		if (mIsShow) {
			UI.hide(mEmptyContent, mProgressContent, mIcon, mName,
					mShortName, mSquadMarketValue, mFixtures, mPlayers);
			UI.show(mErrorContent);
		}
	}

	@Override
	public void onSuccess(Team data) {
		if (mIsShow) {
			mTeam = data;
			UI.hide(mErrorContent, mEmptyContent, mProgressContent);
			UI.show(mIcon, mName, mShortName, mSquadMarketValue, mFixtures, mPlayers);
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
		}
	}

	private void setupUI(View view) {
		mProgressContent = (FrameLayout) view.findViewById(R.id.content_progress);
		mEmptyContent = (LinearLayout) view.findViewById(R.id.content_empty);
		mErrorContent = (LinearLayout) view.findViewById(R.id.content_error);
		mIcon = (ImageView) view.findViewById(R.id.icon);
		mName = (TextView) view.findViewById(R.id.name);
		mShortName = (TextView) view.findViewById(R.id.short_name);
		mSquadMarketValue = (TextView) view.findViewById(R.id.squad_market_value);
		mFixtures = (Button) view.findViewById(R.id.button_fixtures);
		mPlayers = (Button) view.findViewById(R.id.button_players);
		UI.hide(mErrorContent, mEmptyContent, mProgressContent,
				mIcon, mName, mShortName, mSquadMarketValue, mFixtures, mPlayers);
	}

	private void showContent() {
		if (mTeam == null) loadData();
		else {
			UI.hide(mErrorContent, mEmptyContent, mProgressContent);
			UI.show(mIcon, mName, mShortName, mSquadMarketValue, mFixtures, mPlayers);
			ImageLoader.getInstance().displayImage(mTeam.getCrestURL(), mIcon);
			mName.setText(mTeam.getName());
			mShortName.setText(mTeam.getShortName());
			mSquadMarketValue.setText(mTeam.getSquadMarketValue());
		}
	}

	private void loadData() {
		UI.hide(mErrorContent, mEmptyContent, mIcon, mName,
				mShortName, mSquadMarketValue, mFixtures, mPlayers);
		UI.show(mProgressContent);
		if (mController != null) mController.getTeam(getActivity(), mScores.getTeamId(), this);
	}

}
