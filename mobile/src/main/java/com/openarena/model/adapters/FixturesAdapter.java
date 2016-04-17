package com.openarena.model.adapters;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.openarena.R;
import com.openarena.model.AbstractRecyclerAdapter;
import com.openarena.model.objects.Fixture;
import com.openarena.util.UI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FixturesAdapter
		extends AbstractRecyclerAdapter<Fixture, FixturesAdapter.FixturesViewHolder> {

	private Resources mResources;
	private Date mDate;
	private Date mDateTmp;
	private Calendar mCalendar;
	private Calendar mCalendarTmp;

	public FixturesAdapter(Resources resources, ArrayList<Fixture> list) {
		super(list);
		mResources = resources;
		mCalendar = Calendar.getInstance();
		mCalendarTmp = Calendar.getInstance();
		mDate = new Date();
		mDateTmp = new Date();
	}

	@Override
	public FixturesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater
				.from(parent.getContext())
				.inflate(R.layout.item_fixture, parent, false);
		return new FixturesViewHolder(view);
	}

	@Override
	public void onBindViewHolder(FixturesViewHolder holder, int position) {
		Fixture item = mList.get(position);
		holder.mHomeTeamName.setText(item.getHomeTeamName());
		holder.mAwayTeamName.setText(item.getAwayTeamName());
		if (item.getStatus() != Fixture.TIMED) {
			UI.hide(holder.mDate);
			UI.show(holder.mResult);
			holder.mResult.setText(String.format(mResources.getString(
					R.string.fixtures_list_item_result),
					item.getGoalsHomeTeam(),
					item.getGoalsAwayTeam()));
			holder.mResult.setBackgroundResource(R.drawable.fixtures_list_item_result_background);
		}
		else {
			UI.hide(holder.mResult);
			UI.show(holder.mDate);
			DateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
			holder.mDate.setText(format.format(new Date(item.getDate())));
		}

		mDate.setTime(item.getDate());
		mCalendar.setTime(mDate);
		if (position > 0) {
			mDateTmp.setTime(mList.get(position - 1).getDate());
			mCalendarTmp.setTime(mDateTmp);
		}
		if (position == 0
				|| (mCalendar.get(Calendar.DAY_OF_YEAR)
				!= mCalendarTmp.get(Calendar.DAY_OF_YEAR))) {
			mDateTmp.setTime(System.currentTimeMillis());
			mCalendarTmp.setTime(mDateTmp);
			if (mCalendar.get(Calendar.YEAR)
					== mCalendarTmp.get(Calendar.YEAR)
					&&mCalendar.get(Calendar.DAY_OF_YEAR)
					== mCalendarTmp.get(Calendar.DAY_OF_YEAR)) {
				holder.mHeader.setText(
						mResources.getString(R.string.fixtures_list_item_header_today));
			}
			else if (mCalendar.get(Calendar.YEAR)
					== mCalendarTmp.get(Calendar.YEAR)
					&& mCalendar.get(Calendar.DAY_OF_YEAR)
					== mCalendarTmp.get(Calendar.DAY_OF_YEAR) + 1) {
				holder.mHeader.setText(
						mResources.getString(R.string.fixtures_list_item_header_tomorrow));
			}
			else {
				holder.mHeader.setText(
						new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
								.format(new Date(item.getDate())));
			}
			UI.show(holder.mHeader);
		}
		else UI.hide(holder.mHeader);
	}

	public static class FixturesViewHolder extends RecyclerView.ViewHolder {

		private TextView mHomeTeamName,
				mAwayTeamName,
				mDate,
				mResult,
				mHeader;

		public FixturesViewHolder(View itemView) {
			super(itemView);
			mHomeTeamName = (TextView) itemView.findViewById(R.id.home_team_name);
			mAwayTeamName = (TextView) itemView.findViewById(R.id.away_team_name);
			mDate = (TextView) itemView.findViewById(R.id.date);
			mResult = (TextView) itemView.findViewById(R.id.result);
			mHeader = (TextView) itemView.findViewById(R.id.header);
		}
	}
}
