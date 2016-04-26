package com.openarena.model.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.openarena.model.abstractions.AbstractFragmentIntro;

import java.util.ArrayList;
import java.util.List;

public class IntroPagerAdapter extends FragmentPagerAdapter{

	private List<String> mTitles;
	private List<Integer> mColors;
	private List<AbstractFragmentIntro> mFragments;

	public IntroPagerAdapter(FragmentManager fm) {
		super(fm);
		mTitles = new ArrayList<>();
		mColors = new ArrayList<>();
		mFragments = new ArrayList<>();
	}

	public void addFragment(@Nullable String title, int color, AbstractFragmentIntro fragment) {
		mTitles.add(title);
		mColors.add(color);
		mFragments.add(fragment);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return mTitles.get(position);
	}

	@Override
	public AbstractFragmentIntro getItem(int position) {
		return mFragments.get(position);
	}

	@Override
	public int getCount() {
		if (mFragments != null) return mFragments.size();
		else return 0;
	}

	public int getColor(int position) {
		if (!mColors.isEmpty()) return mColors.get(position);
		else return 0;
	}

}
