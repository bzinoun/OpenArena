package com.openarena.model.abstractions;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.openarena.R;
import com.openarena.util.UI;

public class AbstractFragmentIntro extends Fragment {

	private static final String IS_CUSTOM_LAYOUT = "is_custom_layout";
	private static final String VIEW_ID = "view_id";
	private static final String TITLE = "title";
	private static final String SUBTITLE = "subtitle";

	protected ImageView mImage;
	protected TextView mTitle, mSubtitle;
	protected boolean mIsCustomLayout;
	protected int mViewId;
	protected String mTitleStr, mSubtitleStr;

	public static AbstractFragmentIntro getInstance(
			@Nullable String title,
			@Nullable String subtitle,
			int imageId) {
		return  getInstance(title, subtitle, imageId, false);
	}

	public static AbstractFragmentIntro getInstance(
			@Nullable String title,
			@Nullable String subtitle,
			int viewId,
			boolean isCustomLayout) {

		// FIXME: 20.04.2016
		isCustomLayout = false;
		
		AbstractFragmentIntro fragment = new AbstractFragmentIntro();
		Bundle data = new Bundle();
		data.putBoolean(IS_CUSTOM_LAYOUT, isCustomLayout);
		data.putInt(VIEW_ID, viewId);
		data.putString(TITLE, title);
		data.putString(SUBTITLE, subtitle);
		fragment.setArguments(data);
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mIsCustomLayout = getArguments().getBoolean(IS_CUSTOM_LAYOUT);
		mViewId = getArguments().getInt(VIEW_ID);
		mTitleStr = getArguments().getString(TITLE);
		mSubtitleStr = getArguments().getString(SUBTITLE);
	}

	@Override
	public View onCreateView(
			LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_intro, container, false);
		setupUI(view);
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (mImage != null) mImage = null;
		if (mTitle != null) mTitle = null;
		if (mSubtitle != null) mSubtitle = null;
	}

	private void setupUI(View view) {
		mTitle = (TextView) view.findViewById(R.id.title);
		mSubtitle = (TextView) view.findViewById(R.id.subtitle);
		mImage = (ImageView) view.findViewById(R.id.image);

		if (mTitleStr != null) {
			mTitle.setText(mTitleStr);
			UI.show(mTitle);
		}
		if (mSubtitleStr != null) {
			mSubtitle.setText(mSubtitleStr);
			UI.show(mSubtitle);
		}
		if (!mIsCustomLayout) {
			mImage.setImageResource(mViewId);
			UI.show(mImage);
		}
	}
}
