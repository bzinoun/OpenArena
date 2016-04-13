package com.openarena.view.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openarena.R;
import com.openarena.controllers.Controller;
import com.openarena.model.objects.Player;

public class FragmentPlayerInfo extends Fragment {

	public static final String TAG = "FixtureFragment";

	private Controller mController;
	private Player mPlayer;
	private boolean mIsShow;

	public static FragmentPlayerInfo getInstance(@Nullable Bundle data) {
		FragmentPlayerInfo fragment = new FragmentPlayerInfo();
		fragment.setArguments(data == null ? new Bundle() : data);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		if (mPlayer == null) mPlayer = getArguments().getParcelable("player");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_fixture_info, container, false);
		setupUI(view);
		if (mController == null) mController = Controller.getInstance();
		showContent();
		mIsShow = true;
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mIsShow = false;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mController != null) mController = null;
	}

	private void setupUI(View view) {
		ActionBar toolbar = ((AppCompatActivity)getActivity()).getSupportActionBar();
		if (toolbar != null) {
			toolbar.setTitle(getString(R.string.fixture_details_title));
			toolbar.setSubtitle(null);
		}
	}

	private void showContent() {

	}

}
