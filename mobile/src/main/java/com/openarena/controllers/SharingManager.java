package com.openarena.controllers;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.openarena.R;
import com.openarena.model.objects.Fixture;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SharingManager {

	public static String getShareFixture(Resources resources, Fixture fixture) {
		int goalsHomeTeam = fixture.getGoalsHomeTeam(), goalsAwayTeam = fixture.getGoalsAwayTeam();
		if (goalsHomeTeam >= 0 && goalsAwayTeam >= 0) {
			String date = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault())
					.format(new Date(fixture.getDate()));
			if (goalsHomeTeam > goalsAwayTeam) {
				return String.format(
						resources.getString(R.string.share_fixture_home_win), date,
						fixture.getHomeTeamName(),
						fixture.getAwayTeamName(),
						goalsHomeTeam, goalsAwayTeam
				);
			}
			else if (goalsHomeTeam < goalsAwayTeam){
				return String.format(
						resources.getString(R.string.share_fixture_away_win), date,
						fixture.getHomeTeamName(),
						fixture.getAwayTeamName(),
						goalsHomeTeam, goalsAwayTeam
				);
			}
			else {
				return String.format(
						resources.getString(R.string.share_fixture_draw), date,
						fixture.getHomeTeamName(),
						fixture.getAwayTeamName(),
						goalsHomeTeam, goalsAwayTeam
				);
			}
		}
		else {
			String date = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault())
					.format(new Date(fixture.getDate()));
			return String.format(resources.getString(R.string.share_fixture_timed), date,
					fixture.getHomeTeamName(), fixture.getAwayTeamName());

		}
	}

	public static String getShareFixturesList(
			Resources resources, @NonNull ArrayList<Fixture> list) {
		StringBuilder builder = new StringBuilder();
		int goalsHomeTeam, goalsAwayTeam, count;
		Fixture fixture;
		Calendar calendar = Calendar.getInstance(), calendarTmp = Calendar.getInstance();
		Date date = new Date(), dateTmp = new Date();
		if (!list.isEmpty()) {
			builder.append("###")
					.append(String.format(
							resources.getString(R.string.share_fixtures_matchday),
							list.get(0).getMatchday()))
					.append("\n");
			count = list.size();
			for (int i = 0; i < count; i++) {
				fixture = list.get(i);
				goalsHomeTeam = fixture.getGoalsHomeTeam();
				goalsAwayTeam = fixture.getGoalsAwayTeam();
				date.setTime(fixture.getDate());
				calendar.setTime(date);

				if (i > 0) {
					dateTmp.setTime(list.get(i - 1).getDate());
					calendarTmp.setTime(dateTmp);
				}
				if (i == 0
						|| (calendar.get(Calendar.DAY_OF_YEAR)
						!= calendarTmp.get(Calendar.DAY_OF_YEAR))) {
					dateTmp.setTime(System.currentTimeMillis());
					calendarTmp.setTime(dateTmp);
					builder.append("##");
					if (calendar.get(Calendar.YEAR)
							== calendarTmp.get(Calendar.YEAR)
							&&calendar.get(Calendar.DAY_OF_YEAR)
							== calendarTmp.get(Calendar.DAY_OF_YEAR)) {
						builder.append(
								resources.getString(R.string.fixtures_list_item_header_today));
					}
					else if (calendar.get(Calendar.YEAR)
							== calendarTmp.get(Calendar.YEAR)
							&& calendar.get(Calendar.DAY_OF_YEAR)
							== calendarTmp.get(Calendar.DAY_OF_YEAR) + 1) {
						builder.append(
								resources.getString(R.string.fixtures_list_item_header_tomorrow));
					}
					else {
						builder.append(
								new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
										.format(new Date(fixture.getDate())));
					}
					builder.append("\n");
				}

				if (goalsHomeTeam >= 0 && goalsAwayTeam >= 0) {
					builder.append(
							String.format(resources.getString(R.string.share_fixtures_finished),
							fixture.getHomeTeamName(), fixture.getAwayTeamName(),
							goalsHomeTeam, goalsAwayTeam));

				}
				else {
					String dateStr = new SimpleDateFormat("HH:mm", Locale.getDefault())
							.format(new Date(fixture.getDate()));
					builder.append(
							String.format(resources.getString(R.string.share_fixtures_timed),
							fixture.getHomeTeamName(), fixture.getAwayTeamName(), dateStr));
				}
				builder.append("\n");
			}
		}
		return builder.toString();
	}

}
