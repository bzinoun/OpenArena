package com.openarena.model.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class IntroPagerAdapter extends FragmentPagerAdapter{

	private final List<Fragment> fragments = new ArrayList<>();

	public IntroPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	public void addFragment(Fragment fragment) {
		fragments.add(fragment);
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		if (fragments != null) return fragments.size();
		else return 0;
	}

}
