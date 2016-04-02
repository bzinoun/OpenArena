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

	public static String getFixturesByMatchday(Context context, int id, int matchday) {
		return getFixtures(context, id, "matchday=" + String.valueOf(matchday));
	}

	public static String getFixtures(Context context, int id) {
		return getFixtures(context, id, null);
	}

	public static String getFixtures(Context context, int id, @Nullable String filter) {
		Connection connection = getConnection(context);
		StringBuilder request = new StringBuilder()
				.append(getBaseUri())
				.append("soccerseasons/")
				.append(String.valueOf(id))
				.append("/fixtures");
		if (filter != null) request.append("?")
				.append(filter);
		else request.append("?timeFrame=n7");
		return connection.request(request.toString());
	}

	private static Connection getConnection(Context context) {
		return new Connection.Creator()
				.putHeader("X-Auth-Token", context.getString(R.string.api_key))
				.putHeader("X-Response-Control", context.getString(R.string.api_response_control_mini))
				.create();
	}

	private static String getBaseUri() {
		return "http://api.football-data.org/v1/";
	}
}
