package com.openarena.view.activitys;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.openarena.R;
import com.openarena.model.abstractions.AbstractFragmentIntro;
import com.openarena.model.adapters.IntroPagerAdapter;
import com.openarena.model.listeners.OnIntroSwipeListener;
import com.openarena.util.L;

public class IntroActivity extends AppCompatActivity implements View.OnClickListener {

	private ViewPager mViewPager;
	private Button mSkipButton, mNextButton, mFinishButton;
	private ImageView[] mIndicators;
	private IntroPagerAdapter mAdapter;
	private ArgbEvaluator mEvaluator;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);
		if (mEvaluator == null) mEvaluator = new ArgbEvaluator();
		setupUI();
		setupAdapter();
		setupViewPager();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mViewPager != null) mViewPager = null;
		if (mSkipButton != null) mSkipButton = null;
		if (mFinishButton != null) mFinishButton = null;
		if (mNextButton != null) mNextButton = null;
		if (mAdapter != null) mAdapter = null;
		if (mEvaluator != null) mEvaluator = null;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.button_skip:
				onBackPressed();
				break;

			case R.id.button_next:
				if (mViewPager.getCurrentItem() < mViewPager.getChildCount())
				mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
				break;

			case R.id.button_finish:
				onBackPressed();
				break;

			default:
				L.e(IntroActivity.class, "default id");
		}
	}

	private void setupUI() {
		mViewPager = (ViewPager) findViewById(R.id.view_pager);
		mNextButton = (Button) findViewById(R.id.button_next);
		mFinishButton = (Button) findViewById(R.id.button_finish);
		Button skipButton = (Button) findViewById(R.id.button_skip);
		if (mNextButton != null) mNextButton.setOnClickListener(this);
		if (mFinishButton != null) mFinishButton.setOnClickListener(this);
		if (skipButton != null) skipButton.setOnClickListener(this);
	}

	private void setupAdapter() {
		if (mAdapter == null) {
			mAdapter = new IntroPagerAdapter(getSupportFragmentManager());
			mAdapter.addFragment(AbstractFragmentIntro.getInstance(
					R.mipmap.ic_launcher,
					"Title1",
					"Subtitle1"), R.color.intro_page1);
			mAdapter.addFragment(AbstractFragmentIntro.getInstance(
					R.mipmap.ic_launcher,
					"Title2",
					"Subtitle2"), R.color.intro_page2);
			mAdapter.addFragment(AbstractFragmentIntro.getInstance(
					R.mipmap.ic_launcher,
					"Title3",
					"Subtitle3"), R.color.intro_page3);
		}
		mViewPager.setAdapter(mAdapter);
	}

	private void setupViewPager() {
		final float scale = getResources().getDisplayMetrics().density;
		final int size = (int) (8 * scale + 0.5f);
		final int margin = (int) (2 * scale + 0.5f);
		LinearLayout layoutIndicators = (LinearLayout) findViewById(R.id.layout_indicators);
		int count = mAdapter.getCount();
		if (count > 0 && layoutIndicators != null) {
			mIndicators = new ImageView[count];
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
			params.setMargins(margin, 0, margin, 0);
			for (int i = 0; i < count; i++) {
				ImageView indicator = new ImageView(this);
				indicator.setLayoutParams(params);
				mIndicators[i] = indicator;
				layoutIndicators.addView(indicator);
			}
		}
		mViewPager.addOnPageChangeListener(new OnIntroSwipeListener(this, mViewPager, mAdapter, mIndicators) {
			@Override
			public void pageChanged(int position) {
				mNextButton.setVisibility(position == 2 ? View.GONE : View.VISIBLE);
				mFinishButton.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
			}
		});
	}



}
