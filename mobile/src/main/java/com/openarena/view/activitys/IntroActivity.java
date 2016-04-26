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
import com.openarena.controllers.PreferencesManager;
import com.openarena.model.IntroPageTransformer;
import com.openarena.model.abstractions.AbstractFragmentIntro;
import com.openarena.model.adapters.IntroPagerAdapter;
import com.openarena.model.listeners.OnIntroSwipeListener;
import com.openarena.util.Const;
import com.openarena.util.L;

public class IntroActivity extends AppCompatActivity implements View.OnClickListener {

	private ViewPager mViewPager;
	private Button mSkipButton, mNextButton, mStartButton;
	private ImageView[] mIndicators;
	private IntroPagerAdapter mAdapter;
	private ArgbEvaluator mEvaluator;
	private IntroPageTransformer mTransformer;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);
		if (mEvaluator == null) mEvaluator = new ArgbEvaluator();
		if (mTransformer == null) mTransformer = new IntroPageTransformer();
		setupUI();
		setupAdapter();
		setupViewPager();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mViewPager != null) mViewPager = null;
		if (mSkipButton != null) mSkipButton = null;
		if (mStartButton != null) mStartButton = null;
		if (mNextButton != null) mNextButton = null;
		if (mAdapter != null) mAdapter = null;
		if (mEvaluator != null) mEvaluator = null;
		if (mTransformer != null) mTransformer = null;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.button_skip:
				PreferencesManager.from(this).setBoolean(Const.SUBMITTED, true).commit();
				onBackPressed();
				break;

			case R.id.button_next:
				int current_item = mViewPager.getCurrentItem();
				int count = mViewPager.getChildCount();
				if (current_item < count)
				mViewPager.setCurrentItem(current_item + 1, true);
				break;

			case R.id.button_finish:
				PreferencesManager.from(this).setBoolean(Const.SUBMITTED, true).commit();
				onBackPressed();
				break;

			default:
				L.e(IntroActivity.class, "default id");
		}
	}

	private void setupUI() {
		mViewPager = (ViewPager) findViewById(R.id.view_pager);
		mNextButton = (Button) findViewById(R.id.button_next);
		mStartButton = (Button) findViewById(R.id.button_finish);
		Button skipButton = (Button) findViewById(R.id.button_skip);
		if (mNextButton != null) mNextButton.setOnClickListener(this);
		if (mStartButton != null) mStartButton.setOnClickListener(this);
		if (skipButton != null) skipButton.setOnClickListener(this);
	}

	private void setupAdapter() {
		if (mAdapter == null) {
			mAdapter = new IntroPagerAdapter(getSupportFragmentManager());
			mAdapter.addFragment(AbstractFragmentIntro.getInstance(
					0,
					getString(R.string.intro_page1_title),
					getString(R.string.intro_page1_subtitle),
					R.drawable.im_intro_on_pulse), R.color.intro_page1);
			mAdapter.addFragment(AbstractFragmentIntro.getInstance(
					1,
					getString(R.string.intro_page2_title),
					getString(R.string.intro_page2_subtitle),
					R.drawable.im_intro_share_with_friends), R.color.intro_page2);
			mAdapter.addFragment(AbstractFragmentIntro.getInstance(
					2,
					getString(R.string.intro_page3_title),
					null,
					R.drawable.im_intro_get_started), R.color.intro_page3);
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
				if (i == 0) indicator.setBackgroundResource(R.drawable.indicator_selected);
				else indicator.setBackgroundResource(R.drawable.indicator_unselected);
				mIndicators[i] = indicator;
				layoutIndicators.addView(indicator);
			}
		}
		mViewPager.addOnPageChangeListener(new OnIntroSwipeListener(
				this,
				mViewPager,
				mAdapter,
				mIndicators) {
			@Override
			public void pageChanged(int position, int count) {
				mNextButton.setVisibility(position == count - 1 ? View.GONE : View.VISIBLE);
				mStartButton.setVisibility(position == count - 1 ? View.VISIBLE : View.GONE);
			}
		});
		mViewPager.setPageTransformer(false, mTransformer);
	}

}
