package com.openarena.model.objects;

import android.database.Cursor;

import com.openarena.util.DBConst;

public class League {
	private int mID;
	private String mCaption;
	private String mLeague;
	private int mYear;
	private int mCurrentMatchday;
	private int mNumberOfMatchdays;
	private int mNumberOfTeams;
	private int mNumberOfGames;
	private long mLastUpdated;

	public League(
			int id,
			String caption,
			String league,
			int year,
			int currentMatchday,
			int numberOfMatchdays,
			int numberOfTeams,
			int numberOfGames,
			long lastUpdated) {
		this.mID = id;
		this.mCaption = caption;
		this.mLeague = league;
		this.mYear = year;
		this.mCurrentMatchday = currentMatchday;
		this.mNumberOfMatchdays = numberOfMatchdays;
		this.mNumberOfTeams = numberOfTeams;
		this.mNumberOfGames = numberOfGames;
		this.mLastUpdated = lastUpdated;
	}

	public static League parse(Cursor leagueCursor) {
		if (leagueCursor.moveToFirst()) {
			int col_id = leagueCursor.getColumnIndex(DBConst.ID);
			int col_caption = leagueCursor.getColumnIndex(DBConst.CAPTION);
			int col_league = leagueCursor.getColumnIndex(DBConst.LEAGUE);
			int col_year = leagueCursor.getColumnIndex(DBConst.YEAR);
			int col_currentMatchday = leagueCursor.getColumnIndex(DBConst.CURRENT_MATCHDAY);
			int col_numberOfMatchdays = leagueCursor.getColumnIndex(DBConst.NUMBER_OF_MATCHDAYS);
			int col_numberOfTeams = leagueCursor.getColumnIndex(DBConst.NUMBER_OF_TEAMS);
			int col_numberOfGames = leagueCursor.getColumnIndex(DBConst.NUMBER_OF_GAMES);
			int col_lastUpdated = leagueCursor.getColumnIndex(DBConst.LAST_UPDATED);
			return new League(
					leagueCursor.getInt(col_id),
					leagueCursor.getString(col_caption),
					leagueCursor.getString(col_league),
					leagueCursor.getInt(col_year),
					leagueCursor.getInt(col_currentMatchday),
					leagueCursor.getInt(col_numberOfMatchdays),
					leagueCursor.getInt(col_numberOfTeams),
					leagueCursor.getInt(col_numberOfGames),
					leagueCursor.getLong(col_lastUpdated));
		}
		return null;
	}

	public int getID() {
		return mID;
	}

	public String getCaption() {
		return mCaption;
	}

	public String getLeague() {
		return mLeague;
	}

	public int getYear() {
		return mYear;
	}

	public int getCurrentMatchday() {
		return mCurrentMatchday;
	}

	public int getNumberOfMatchdays() {
		return mNumberOfMatchdays;
	}

	public int getNumberOfTeams() {
		return mNumberOfTeams;
	}

	public int getNumberOfGames() {
		return mNumberOfGames;
	}

	public long getLastUpdated() {
		return mLastUpdated;
	}

}
