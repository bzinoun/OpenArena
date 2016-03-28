package com.openarena.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.openarena.model.SQLHelper;
import com.openarena.model.objects.Fixture;
import com.openarena.model.objects.League;
import com.openarena.util.DBConst;

public class DBManager {

	private static SQLHelper sSQLHelper;

	public void init(Context context) {
		sSQLHelper = SQLHelper.getInstance(context);
	}

	public static League getLeague(int id) {
		Cursor cursor = sSQLHelper.getAll(
				DBConst.TABLE_LEAGUES,
				new String[] {DBConst.ID},
				new String[] {String.valueOf(id)});
		League league = League.parse(cursor);
		if (cursor != null) cursor.close();
		return league;
	}

	public static Fixture getFixture(int id) {
		Cursor cursor = sSQLHelper.getAll(
				DBConst.TABLE_FIXTURES,
				new String[] {DBConst.ID},
				new String[] {String.valueOf(id)});
		Fixture fixture = Fixture.parse(cursor);
		if (cursor != null) cursor.close();
		return fixture;
	}

	public static void setLeague(League league) {
		if (league != null) {
			ContentValues data = new ContentValues();
			data.put(DBConst.ID, league.getID());
			data.put(DBConst.CAPTION, league.getCaption());
			data.put(DBConst.LEAGUE, league.getLeague());
			data.put(DBConst.YEAR, league.getYear());
			data.put(DBConst.CURRENT_MATCHDAY, league.getCurrentMatchday());
			data.put(DBConst.NUMBER_OF_MATCHDAYS, league.getNumberOfMatchdays());
			data.put(DBConst.NUMBER_OF_TEAMS, league.getNumberOfTeams());
			data.put(DBConst.NUMBER_OF_GAMES, league.getNumberOfGames());
			data.put(DBConst.LAST_UPDATED, league.getLastUpdated());

			Cursor cursor = sSQLHelper.getAll(
					DBConst.TABLE_LEAGUES,
					new String[] {DBConst.ID},
					new String[] {String.valueOf(league.getID())});
			if (cursor.moveToFirst()) {
				sSQLHelper.update(
						DBConst.TABLE_LEAGUES,
						data, new String[] {DBConst.ID},
						new String[] {String.valueOf(league.getID())});
			}
			else {
				sSQLHelper.insert(DBConst.TABLE_LEAGUES, data);
			}
			cursor.close();
		}
	}

	public static void setFixture(Fixture fixture) {
		if (fixture != null) {
			ContentValues data = new ContentValues();
			data.put(DBConst.ID, fixture.getID());
			data.put(DBConst.SOCCER_SEASON_ID, fixture.getSoccerSeasonID());
			data.put(DBConst.DATE, fixture.getDate());
			data.put(DBConst.STATUS, fixture.getStatus());
			data.put(DBConst.MATCHDAY, fixture.getMatchday());
			data.put(DBConst.HOME_TEAM_ID, fixture.getHomeTeamID());
			data.put(DBConst.HOME_TEAM_NAME, fixture.getHomeTeamName());
			data.put(DBConst.AWAY_TEAM_ID, fixture.getAwayTeamID());
			data.put(DBConst.AWAY_TEAM_NAME, fixture.getAwayTeamName());
			data.put(DBConst.GOALS_HOME_TEAM, fixture.getGoalsHomeTeam());
			data.put(DBConst.GOALS_AWAY_TEAM, fixture.getGoalsAwayTeam());

			Cursor cursor = sSQLHelper.getAll(
					DBConst.TABLE_FIXTURES,
					new String[] {DBConst.ID},
					new String[] {String.valueOf(fixture.getID())});
			if (cursor.moveToFirst()) {
				sSQLHelper.update(
						DBConst.TABLE_FIXTURES,
						data, new String[] {DBConst.ID},
						new String[] {String.valueOf(fixture.getID())});
			}
			else {
				sSQLHelper.insert(DBConst.TABLE_FIXTURES, data);
			}
			cursor.close();
		}
	}

}
