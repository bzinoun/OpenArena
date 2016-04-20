package com.openarena.model;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.openarena.R;

public class IntroPageTransformer implements ViewPager.PageTransformer {

	public IntroPageTransformer() {

	}

	@Override
	public void transformPage(View page, float position) {
		int pagePosition = (int) page.getTag();
		int pageWidth = page.getWidth();
		float pageWidthTimesPosition = pageWidth * position;
		float absPosition = Math.abs(position);

		// Now it's time for the effects
		if (position <= -1.0f || position >= 1.0f) {

			// The page is not visible. This is a good place to stop
			// any potential work / animations you may have running.

		} else if (position == 0.0f) {

			// The page is selected. This is a good time to reset Views
			// after animations as you can't always count on the PageTransformer
			// callbacks to match up perfectly.

		} else {
			View title = page.findViewById(R.id.title);
			View subtitle = page.findViewById(R.id.subtitle);
			View image = page.findViewById(R.id.image);

			title.setAlpha(1.0f - absPosition);
			title.setTranslationX(-pageWidthTimesPosition);
			subtitle.setTranslationY(0.2f - absPosition);

			if (pagePosition == 0 && image != null) {
				image.setAlpha(1.0f - absPosition);
				image.setTranslationX(-pageWidthTimesPosition * 0.8f);
				//image.setTranslationX(-pageWidthTimesPosition * 0.1f);
				//image.setTranslationY(-pageWidthTimesPosition * 0.1f);
			}

			if (pagePosition == 1 && image != null) {
				image.setAlpha(1.0f - absPosition);
				image.setTranslationX(-pageWidthTimesPosition * 0.8f);
			}

			if (pagePosition == 2 && image != null) {
				image.setAlpha(1.0f - absPosition);
				image.setTranslationX(pageWidthTimesPosition * 1.2f);
			}

			if (position < 0) {
				// Create your out animation here
			} else {
				// Create your in animation here
			}
		}
	}

}
