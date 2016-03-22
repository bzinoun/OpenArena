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

	protected League(
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
			league.setID(leagueCursor.getInt(col_id));
			league.setCaption(leagueCursor.getString(col_caption));
			league.setLeague(leagueCursor.getString(col_league));
			league.setYear(leagueCursor.getInt(col_year));
			league.setCurrentMatchday(leagueCursor.getInt(col_currentMatchday));
			league.setNumberOfMatchdays(leagueCursor.getInt(col_numberOfMatchdays));
			league.setNumberOfTeams(leagueCursor.getInt(col_numberOfTeams));
			league.setNumberOfGames(leagueCursor.getInt(col_numberOfGames));
			league.setLastUpdated(leagueCursor.getLong(col_lastUpdated));
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

	protected void setID(int id) {
		this.mID = id;
	}

	protected void setCaption(String caption) {
		this.mCaption = caption;
	}

	protected void setLeague(String league) {
		this.mLeague = league;
	}

	protected void setYear(int year) {
		this.mYear = year;
	}

	protected void setCurrentMatchday(int currentMatchday) {
		this.mCurrentMatchday = currentMatchday;
	}

	protected void setNumberOfMatchdays(int numberOfMatchdays) {
		this.mNumberOfMatchdays = numberOfMatchdays;
	}

	protected void setNumberOfTeams(int numberOfTeams) {
		this.mNumberOfTeams = numberOfTeams;
	}

	protected void setNumberOfGames(int numberOfGames) {
		this.mNumberOfGames = numberOfGames;
	}

	protected void setLastUpdated(long lastUpdated) {
		this.mLastUpdated = lastUpdated;
	}

}
