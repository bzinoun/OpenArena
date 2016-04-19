package com.openarena.view.activitys;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.openarena.R;
import com.openarena.model.abstractions.AbstractFragmentIntro;
import com.openarena.model.adapters.IntroPagerAdapter;

public class IntroActivity extends AppCompatActivity {

	private ViewPager mViewPager;
	private Button mSkipButton, mNextButton, mFinishButton;
	private IntroPagerAdapter mAdapter;
	private ImageView[] mIndicators;
	private ArgbEvaluator mEvaluator;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);
		if (mEvaluator == null) mEvaluator = new ArgbEvaluator();
		setupAdapter();
		setupUI();
		mViewPager.setAdapter(mAdapter);
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

	private void setupAdapter() {
		if (mAdapter == null) {
			mAdapter = new IntroPagerAdapter(getSupportFragmentManager());
			mAdapter.addFragment(AbstractFragmentIntro.getInstance(R.mipmap.ic_launcher, "Title 1", "Subtitle1"));
			mAdapter.addFragment(AbstractFragmentIntro.getInstance(R.mipmap.ic_launcher, "Title 2", "Subtitle2"));
			mAdapter.addFragment(AbstractFragmentIntro.getInstance(R.mipmap.ic_launcher, "Title 3", "Subtitle3"));
		}

	}

	private void setupUI() {
		mViewPager = (ViewPager) findViewById(R.id.view_pager);
		mSkipButton = (Button) findViewById(R.id.button_skip);
		mFinishButton = (Button) findViewById(R.id.button_finish);
		mNextButton = (Button) findViewById(R.id.button_next);
		mNextButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
			}
		});

		mIndicators = new ImageView[3];
		mIndicators[0] = (ImageView) findViewById(R.id.indicator_0);
		mIndicators[1] = (ImageView) findViewById(R.id.indicator_1);
		mIndicators[2] = (ImageView) findViewById(R.id.indicator_2);
	}

	private void setupViewPager() {
		final int color1 = ContextCompat.getColor(this, R.color.intro_page1);
		final int color2 = ContextCompat.getColor(this, R.color.intro_page2);
		final int color3 = ContextCompat.getColor(this, R.color.intro_page3);
		final int[] colorList = new int[]{color1, color2, color3};

		mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(
					int position,
					float positionOffset,
					int positionOffsetPixels) {
				int colorUpdate = (Integer) mEvaluator.evaluate(
						positionOffset,
						colorList[position],
						colorList[position == 2 ? position : position + 1]
				);
				mViewPager.setBackgroundColor(colorUpdate);
			}

			@Override
			public void onPageSelected(int position) {
				updateIndicators(position);
				switch (position) {
					case 0:
						mViewPager.setBackgroundColor(color1);
						break;
					case 1:
						mViewPager.setBackgroundColor(color2);
						break;
					case 2:
						mViewPager.setBackgroundColor(color3);
						break;
				}
				mNextButton.setVisibility(position == 2 ? View.GONE : View.VISIBLE);
				mFinishButton.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
			}

			@Override
			public void onPageScrollStateChanged(int state) {}
		});
	}

	private void updateIndicators(int position) {
		for (int i = 0; i < mIndicators.length; i++) {
			mIndicators[i].setBackgroundResource(
					i == position ? R.drawable.indicator_selected : R.drawable.indicator_unselected
			);
		}
	}

}
