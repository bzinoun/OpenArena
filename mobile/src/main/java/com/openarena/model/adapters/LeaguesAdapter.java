package com.openarena.model.adapters;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.openarena.R;
import com.openarena.model.AbstractRecyclerAdapter;
import com.openarena.model.interfaces.OnItemTouchAdapter;
import com.openarena.model.objects.League;

import java.util.ArrayList;

public class LeaguesAdapter extends AbstractRecyclerAdapter<League, LeaguesAdapter.LeagueViewHolder> implements OnItemTouchAdapter {

	private Resources mResources;

	public LeaguesAdapter(Resources resources, ArrayList<League> list) {
		super(list);
		mResources = resources;
	}

	@Override
	public LeagueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_league, parent, false);
		return new LeagueViewHolder(view);
	}

	@Override
	public void onBindViewHolder(LeagueViewHolder holder, int position) {
		League item = mList.get(position);
		holder.mName.setText(item.getCaption());
		holder.mMatchday.setText(String.format(mResources.getString(R.string.leagues_list_item_matchday), item.getCurrentMatchday(), item.getNumberOfMatchdays()));
	}

	public static class LeagueViewHolder extends RecyclerView.ViewHolder {

		private TextView mName;
		private TextView mMatchday;

		public LeagueViewHolder(View itemView) {
			super(itemView);
			mName = (TextView) itemView.findViewById(R.id.name);
			mMatchday = (TextView) itemView.findViewById(R.id.matchday);
		}
	}

}
