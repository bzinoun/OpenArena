package com.openarena.model.objects;

import android.database.Cursor;

import com.openarena.util.DBConst;
import com.openarena.util.L;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

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

	protected League() {}

	public static League parse(Cursor leagueCursor) {
		League league = null;
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
			league = new League();
			league.mID = leagueCursor.getInt(col_id);
			league.mCaption = leagueCursor.getString(col_caption);
			league.mLeague = leagueCursor.getString(col_league);
			league.mYear = leagueCursor.getInt(col_year);
			league.mCurrentMatchday = leagueCursor.getInt(col_currentMatchday);
			league.mNumberOfMatchdays = leagueCursor.getInt(col_numberOfMatchdays);
			league.mNumberOfTeams = leagueCursor.getInt(col_numberOfTeams);
			league.mNumberOfGames = leagueCursor.getInt(col_numberOfGames);
			league.mLastUpdated = leagueCursor.getLong(col_lastUpdated);
		}
		leagueCursor.close();
		return league;
	}

	public static League parse(JSONObject o) {
		League league = new League();
		try {
			if (!o.isNull("id")) league.mID = o.getInt("id");
			if (!o.isNull("caption")) league.mCaption = o.getString("caption");
			if (!o.isNull("league")) league.mLeague = o.getString("league");
			if (!o.isNull("year")) league.mYear = Integer.valueOf(o.getString("year"));
			if (!o.isNull("currentMatchday")) league.mCurrentMatchday = o.getInt("currentMatchday");
			if (!o.isNull("numberOfMatchdays")) league.mNumberOfMatchdays = o.getInt("numberOfMatchdays");
			if (!o.isNull("numberOfTeams")) league.mNumberOfTeams = o.getInt("numberOfTeams");
			if (!o.isNull("numberOfGames")) league.mNumberOfTeams = o.getInt("numberOfGames");
			if (!o.isNull("lastUpdated")) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'", Locale.getDefault());
				league.mLastUpdated = dateFormat.parse(o.getString("lastUpdated")).getTime();
			}
			return league;

		} catch (JSONException e) {
			L.e(League.class, e.toString());
			e.printStackTrace();
		} catch (ParseException e) {
			L.e(League.class, e.toString());
			e.printStackTrace();
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
