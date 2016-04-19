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

	protected ImageView mImage;
	protected TextView mTitle, mSubtitle;
	protected int mImageId;
	protected String mTitleStr, mSubtitleStr;

	public static AbstractFragmentIntro getInstance(
			int imageId,
			@Nullable String title,
			@Nullable String subtitle) {
		AbstractFragmentIntro fragment = new AbstractFragmentIntro();
		Bundle data = new Bundle();
		data.putInt("imageId", imageId);
		data.putString("title", title);
		data.putString("subtitle", subtitle);
		fragment.setArguments(data);
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageId = getArguments().getInt("imageId");
		mTitleStr = getArguments().getString("title");
		mSubtitleStr = getArguments().getString("subtitle");
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
		mImage = (ImageView) view.findViewById(R.id.image);
		mTitle = (TextView) view.findViewById(R.id.title);
		mSubtitle = (TextView) view.findViewById(R.id.subtitle);
		if (mImageId > 0) {
			mImage.setImageResource(mImageId);
			UI.show(mImage);
		}
		if (mTitleStr != null) {
			mTitle.setText(mTitleStr);
			UI.show(mTitle);
		}
		if (mSubtitleStr != null) {
			mSubtitle.setText(mSubtitleStr);
			UI.show(mSubtitle);
		}

	}
}
