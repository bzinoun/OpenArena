package com.openarena.model.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.openarena.util.L;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Head2head implements Parcelable {

	private int mCount;
	private long mTimeFrameStart;
	private long mTimeFrameEnd;
	private int mHomeTeamWins;
	private int mAwayTeamWins;
	private int mDraws;
	private ArrayList<Fixture> mFixtures;

	protected Head2head() {}

	protected Head2head(Parcel in) {
		mCount = in.readInt();
		mTimeFrameStart = in.readLong();
		mTimeFrameEnd = in.readLong();
		mHomeTeamWins = in.readInt();
		mAwayTeamWins = in.readInt();
		mDraws = in.readInt();
		mFixtures = in.createTypedArrayList(Fixture.CREATOR);
	}

	public static final Creator<Head2head> CREATOR = new Creator<Head2head>() {
		@Override
		public Head2head createFromParcel(Parcel in) {
			return new Head2head(in);
		}

		@Override
		public Head2head[] newArray(int size) {
			return new Head2head[size];
		}
	};

	public static Head2head parse(JSONObject o) {
		if (o != null) {
			Head2head head2head = new Head2head();
			head2head.mCount = o.optInt("count");
			String timeFrameStart = o.optString("timeFrameStart", null);
			if (timeFrameStart != null) {
				try {
					head2head.mTimeFrameStart = new SimpleDateFormat(
							"yyyy-MM-dd",
							Locale.getDefault())
							.parse(timeFrameStart)
							.getTime();
				} catch (ParseException e) {
					L.e(Head2head.class, e.toString());
				}
			}
			String timeFrameEnd = o.optString("timeFrameEnd", null);
			if (timeFrameEnd != null) {
				try {
					head2head.mTimeFrameEnd = new SimpleDateFormat(
							"yyyy-MM-dd",
							Locale.getDefault())
							.parse(timeFrameEnd)
							.getTime();
				} catch (ParseException e) {
					L.e(Head2head.class, e.toString());
				}
			}
			head2head.mHomeTeamWins = o.optInt("homeTeamWins");
			head2head.mAwayTeamWins = o.optInt("awayTeamWins");
			head2head.mDraws = o.optInt("draws");
			head2head.mFixtures = Fixture.parseArray(o.optJSONArray("fixtures"));
			return head2head;
		}
		else return null;
	}

	public int getCount() {
		return mCount;
	}

	public long getTimeFrameStart() {
		return mTimeFrameStart;
	}

	public long getTimeFrameEnd() {
		return mTimeFrameEnd;
	}

	public int getHomeTeamWins() {
		return mHomeTeamWins;
	}

	public int getAwayTeamWins() {
		return mAwayTeamWins;
	}

	public int getDraws() {
		return mDraws;
	}

	public ArrayList<Fixture> getFixtures() {
		return mFixtures;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeInt(mCount);
		dest.writeLong(mTimeFrameStart);
		dest.writeLong(mTimeFrameEnd);
		dest.writeInt(mHomeTeamWins);
		dest.writeInt(mAwayTeamWins);
		dest.writeInt(mDraws);
		dest.writeTypedList(mFixtures);
	}
}
