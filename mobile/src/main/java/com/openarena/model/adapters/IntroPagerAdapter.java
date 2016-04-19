package com.openarena.model.adapters;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.openarena.model.abstractions.AbstractFragmentIntro;

import java.util.ArrayList;
import java.util.List;

public class IntroPagerAdapter extends FragmentPagerAdapter{

	private List<AbstractFragmentIntro> mFragments;
	private List<Integer> mColors;

	public IntroPagerAdapter(FragmentManager fm) {
		super(fm);
		mFragments = new ArrayList<>();
		mColors = new ArrayList<>();
	}

	public void addFragment(AbstractFragmentIntro fragment, int color) {
		mFragments.add(fragment);
		mColors.add(color);
	}

	@Override
	public AbstractFragmentIntro getItem(int position) {
		return mFragments.get(position);
	}

	public int getColor(int position) {
		if (!mColors.isEmpty()) return mColors.get(position);
		else return 0;
	}

	@Override
	public int getCount() {
		if (mFragments != null) return mFragments.size();
		else return 0;
	}

}
