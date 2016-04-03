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
import java.util.Date;
import java.util.Locale;

public class FixturesAdapter extends AbstractRecyclerAdapter<Fixture, FixturesAdapter.FixturesViewHolder> {

	private Resources mResources;

	public FixturesAdapter(Resources resources, ArrayList<Fixture> list) {
		super(list);
		mResources = resources;
	}

	@Override
	public FixturesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fixture, parent, false);
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
		}
		else {
			UI.hide(holder.mResult);
			UI.show(holder.mDate);
			DateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
			holder.mDate.setText(format.format(new Date(item.getDate())));
		}
	}

	public static class FixturesViewHolder extends RecyclerView.ViewHolder {

		private TextView mHomeTeamName,
				mAwayTeamName,
				mDate,
				mResult;

		public FixturesViewHolder(View itemView) {
			super(itemView);
			mHomeTeamName = (TextView) itemView.findViewById(R.id.home_team_name);
			mAwayTeamName = (TextView) itemView.findViewById(R.id.away_team_name);
			mDate = (TextView) itemView.findViewById(R.id.date);
			mResult = (TextView) itemView.findViewById(R.id.result);
		}
	}
}
