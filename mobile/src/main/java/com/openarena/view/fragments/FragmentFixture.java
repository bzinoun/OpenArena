package com.openarena.view.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.openarena.R;

public class FragmentFixture extends Fragment {

	public static final String TAG = "FixtureFragment";

	private int mFixtureId;

	public static FragmentFixture getInstance(@Nullable Bundle data) {
		FragmentFixture fragment = new FragmentFixture();
		fragment.setArguments(data == null ? new Bundle() : data);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_fixture, container, false);
		setupUI(view);
		mFixtureId = getArguments().getInt("fixtureId");

		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

	}

	private void setupUI(View view) {

	}

}
