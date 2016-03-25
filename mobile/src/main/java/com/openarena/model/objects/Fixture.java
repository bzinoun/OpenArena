package com.openarena.model.objects;

import android.database.Cursor;

import com.openarena.util.DBConst;
import com.openarena.util.L;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Fixture {

	public static final int FINISHED = 0;
	public static final int TIMED = 1;

	private int mID;
	private int mSoccerSeasonID;
	private long mDate;
	private int mStatus;
	private int mMatchday;
	private int mHomeTeamID;
	private int mAwayTeamID;
	private String mHomeTeamName;
	private String mAwayTeamName;
	private int mGoalsHomeTeam = -1;
	private int mGoalsAwayTeam = -1;

	protected Fixture() {}

	public static Fixture parse(Cursor fixtureCursor) {
		Fixture fixture = null;
		if (fixtureCursor.moveToFirst()) {
			int col_id = fixtureCursor.getColumnIndex(DBConst.ID);
			int col_soccerSeasonId = fixtureCursor.getColumnIndex(DBConst.SOCCER_SEASON_ID);
			int col_date = fixtureCursor.getColumnIndex(DBConst.DATE);
			int col_status = fixtureCursor.getColumnIndex(DBConst.STATUS);
			int col_matchday = fixtureCursor.getColumnIndex(DBConst.MATCHDAY);
			int col_homeTeamId = fixtureCursor.getColumnIndex(DBConst.HOME_TEAM_ID);
			int col_homeTeamName = fixtureCursor.getColumnIndex(DBConst.HOME_TEAM_NAME);
			int col_awayTeamId = fixtureCursor.getColumnIndex(DBConst.AWAY_TEAM_ID);
			int col_awayTeamName = fixtureCursor.getColumnIndex(DBConst.AWAY_TEAM_NAME);
			int col_goalsHomeTeam = fixtureCursor.getColumnIndex(DBConst.GOALS_HOME_TEAM);
			int col_goalsAwayTeam = fixtureCursor.getColumnIndex(DBConst.GOALS_AWAY_TEAM);
			fixture = new Fixture();
			fixture.mID = fixtureCursor.getInt(col_id);
			fixture.mSoccerSeasonID = fixtureCursor.getInt(col_soccerSeasonId);
			fixture.mDate = fixtureCursor.getLong(col_date);
			fixture.mStatus = fixtureCursor.getInt(col_status);
			fixture.mMatchday = fixtureCursor.getInt(col_matchday);
			fixture.mHomeTeamID = fixtureCursor.getInt(col_homeTeamId);
			fixture.mHomeTeamName = fixtureCursor.getString(col_homeTeamName);
			fixture.mAwayTeamID = fixtureCursor.getInt(col_awayTeamId);
			fixture.mAwayTeamName = fixtureCursor.getString(col_awayTeamName);
			fixture.mGoalsHomeTeam = fixtureCursor.getInt(col_goalsHomeTeam);
			fixture.mGoalsAwayTeam = fixtureCursor.getInt(col_goalsAwayTeam);
		}
		fixtureCursor.close();
		return fixture;
	}

	public static Fixture parse(JSONObject o) {
		Fixture fixture = new Fixture();
		try {
			if (!o.isNull("id")) fixture.mID = o.getInt("id");
			if (!o.isNull("soccerseasonId")) fixture.mSoccerSeasonID = o.getInt("soccerseasonId");
			if (!o.isNull("date")) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'", Locale.getDefault());
				fixture.mDate = dateFormat.parse(o.getString("date")).getTime();
			}
			if (!o.isNull("status")) {
				String status = o.getString("status");
				L.e(status);
				if (status.equals("FINISHED")) fixture.mStatus = FINISHED;
				else if (status.equals("TIMED")) fixture.mStatus = TIMED;
			}
			if (!o.isNull("matchday")) fixture.mMatchday = o.getInt("matchday");
			if (!o.isNull("homeTeamId")) fixture.mHomeTeamID = o.getInt("homeTeamId");
			if (!o.isNull("homeTeamName")) fixture.mHomeTeamName = o.getString("homeTeamName");
			if (!o.isNull("awayTeamId")) fixture.mAwayTeamID = o.getInt("awayTeamId");
			if (!o.isNull("awayTeamName")) fixture.mAwayTeamName = o.getString("awayTeamName");
			if (!o.isNull("result")) {
				JSONObject result = o.getJSONObject("result");
				if (!result.isNull("goalsHomeTeam")) fixture.mGoalsHomeTeam = result.getInt("goalsHomeTeam");
				if (!result.isNull("goalsAwayTeam")) fixture.mGoalsAwayTeam = result.getInt("goalsAwayTeam");
			}
			return fixture;

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

	public int getSoccerSeasonID() {
		return mSoccerSeasonID;
	}

	public long getDate() {
		return mDate;
	}

	public int getStatus() {
		return mStatus;
	}

	public int getMatchday() {
		return mMatchday;
	}

	public int getHomeTeamID() {
		return mHomeTeamID;
	}

	public int getAwayTeamID() {
		return mAwayTeamID;
	}

	public String getHomeTeamName() {
		return mHomeTeamName;
	}

	public String getAwayTeamName() {
		return mAwayTeamName;
	}

	public Integer getGoalsHomeTeam() {
		return mGoalsHomeTeam;
	}

	public Integer getGoalsAwayTeam() {
		return mGoalsAwayTeam;
	}
}
