package com.openarena.controllers;

import android.content.Context;
import android.support.annotation.Nullable;

import com.openarena.R;
import com.openarena.model.Connection;

public class Api {

	public static String getLeagueByYear(Context context, int year) {
		Connection connection = getConnection(context);
		return connection.request(getBaseUri() + "soccerseasons?season=" + String.valueOf(year));
	}

	public static String getFixtureDetailsById(Context context, int id) {
		Connection connection = getConnection(context);
		return connection.request(getBaseUri() + "fixtures/" + String.valueOf(id));
	}

	public static String getFixturesBySeasonId(Context context, int id) {
		return getFixturesBySeasonId(context, id, null);
	}

	/**
	 *
	 * @param timeFrame <b>//p|n[1-9]{1,2}//</b> of <b>null</b> for example "n1"
	 */
	public static String getFixturesBySeasonId(Context context, int id, @Nullable String timeFrame) {
		Connection connection = getConnection(context);
		StringBuilder request = new StringBuilder()
				.append(getBaseUri())
				.append("soccerseasons/")
				.append(String.valueOf(id))
				.append("/fixtures");
		if (timeFrame != null) request.append("?timeFrame=")
				.append(timeFrame);
		else request.append("?timeFrame=n7");
		return connection.request(request.toString());
	}

	private static Connection getConnection(Context context) {
		return new Connection.Builder()
				.putHeader("X-Auth-Token", context.getString(R.string.api_key))
				.putHeader("X-Response-Control", context.getString(R.string.api_response_control_mini))
				.build();
	}

	private static String getBaseUri() {
		return "http://api.football-data.org/v1/";
	}
}
