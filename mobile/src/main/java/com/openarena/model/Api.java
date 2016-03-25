package com.openarena.model;

import android.content.Context;

import com.openarena.R;
import com.openarena.controllers.Connection;

public class Api {

	public static String getLeagueByYear(Context context, int year) {
		Connection connection = getConnection(context);
		return connection.request(getBaseUri() + "soccerseasons?season=" + String.valueOf(year));
	}

	public static String getFixturesBySeasonId(Context context, int id) {
		return getFixturesBySeasonId(context, id, null);
	}

	/**
	 *
	 * @param timeFrame <b><i>//p|n[1-9]{1,2}//</i></b> of <b>null</b> for example "n1"
	 */
	public static String getFixturesBySeasonId(Context context, int id, String timeFrame) {
		Connection connection = getConnection(context);
		StringBuilder request = new StringBuilder()
				.append(getBaseUri())
				.append("soccerseasons/")
				.append(String.valueOf(id))
				.append("/fixtures");
		if (timeFrame != null) request.append("?timeFrame=")
				.append(timeFrame);
		else request.append("?timeFrame=n1");
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
