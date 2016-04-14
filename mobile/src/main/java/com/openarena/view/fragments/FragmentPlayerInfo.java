package com.openarena.view.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.openarena.R;
import com.openarena.model.objects.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FragmentPlayerInfo extends Fragment {

	public static final String TAG = "FragmentPlayerInfo";

	private TextView mName;
	private TextView mJersey;
	private TextView mPosition;
	private TextView mNationality;
	private TextView mMarketValue;
	private TextView mDateOfBirth;
	private TextView mContractUntil;
	private Player mPlayer;

	public static FragmentPlayerInfo getInstance(@Nullable Bundle data) {
		FragmentPlayerInfo fragment = new FragmentPlayerInfo();
		fragment.setArguments(data == null ? new Bundle() : data);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (mPlayer == null) mPlayer = getArguments().getParcelable("player");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_player_info, container, false);
		setupUI(view);
		showContent();
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (mName != null) mName = null;
		if (mJersey != null) mJersey = null;
		if (mPosition != null) mPosition = null;
		if (mNationality != null) mNationality = null;
		if (mMarketValue != null) mMarketValue = null;
		if (mDateOfBirth != null) mDateOfBirth = null;
		if (mContractUntil != null) mContractUntil = null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mPlayer != null) mPlayer = null;
	}

	private void setupUI(View view) {
		ActionBar toolbar = ((AppCompatActivity)getActivity()).getSupportActionBar();
		if (toolbar != null) {
			toolbar.setTitle(mPlayer.getName());
			toolbar.setSubtitle(null);
		}
		mName = (TextView) view.findViewById(R.id.name);
		mJersey = (TextView) view.findViewById(R.id.jersey);
		mPosition = (TextView) view.findViewById(R.id.position);
		mNationality = (TextView) view.findViewById(R.id.nationality);
		mMarketValue = (TextView) view.findViewById(R.id.market_value);
		mDateOfBirth = (TextView) view.findViewById(R.id.date_of_birth);
		mContractUntil = (TextView) view.findViewById(R.id.contract_until);
	}

	private void showContent() {
		mName.setText(mPlayer.getName());
		mJersey.setText(String.valueOf(mPlayer.getJerseyNumber()));
		mPosition.setText(String.valueOf(mPlayer.getPosition()));
		mNationality.setText(mPlayer.getNationality());
		mMarketValue.setText(String.valueOf(mPlayer.getMarketValue()));
		mDateOfBirth.setText(new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date(mPlayer.getDateOfBirth())));
		mContractUntil.setText(new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date(mPlayer.getContractUntil())));
	}

}