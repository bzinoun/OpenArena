package com.openarena.model.objects;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.openarena.util.DBConst;
import com.openarena.util.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class League implements Parcelable {

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

	protected League(Parcel in) {
		mID = in.readInt();
		mCaption = in.readString();
		mLeague = in.readString();
		mYear = in.readInt();
		mCurrentMatchday = in.readInt();
		mNumberOfMatchdays = in.readInt();
		mNumberOfTeams = in.readInt();
		mNumberOfGames = in.readInt();
		mLastUpdated = in.readLong();
	}

	public static final Creator<League> CREATOR = new Creator<League>() {
		@Override
		public League createFromParcel(Parcel in) {
			return new League(in);
		}

		@Override
		public League[] newArray(int size) {
			return new League[size];
		}
	};

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
		league.mID = o.optInt("id");
		league.mCaption = o.optString("caption");
		league.mLeague = o.optString("league");
		league.mYear = Integer.valueOf(o.optString("year"));
		league.mCurrentMatchday = o.optInt("currentMatchday");
		league.mNumberOfMatchdays = o.optInt("numberOfMatchdays");
		league.mNumberOfTeams = o.optInt("numberOfTeams");
		league.mNumberOfTeams = o.optInt("numberOfGames");
		String date = o.optString("lastUpdated", null);
		if (date != null) {
			try {
				league.mLastUpdated = new SimpleDateFormat(
						"yyyy-MM-dd'T'hh:mm:ss'Z'",
						Locale.getDefault())
						.parse(date)
						.getTime();
			} catch (ParseException e) {
				L.e(League.class, e.toString());
			}
		}
		return league;
	}

	public static ArrayList<League> parseArray(JSONArray array) {
		if (array != null) {
			ArrayList<League> list = new ArrayList<>();
			int count = array.length();
			for (int i = 0; i < count; i++) {
				try {
					list.add(League.parse(array.getJSONObject(i)));
				} catch (JSONException e) {
					L.e(League.class, e.toString());
				}
			}
			return list;
		}
		else return null;
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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeInt(mID);
		dest.writeString(mCaption);
		dest.writeString(mLeague);
		dest.writeInt(mYear);
		dest.writeInt(mCurrentMatchday);
		dest.writeInt(mNumberOfMatchdays);
		dest.writeInt(mNumberOfTeams);
		dest.writeInt(mNumberOfGames);
		dest.writeLong(mLastUpdated);
	}
}
