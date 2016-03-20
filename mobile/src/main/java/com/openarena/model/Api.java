package com.openarena.model;

import android.content.Context;

import com.openarena.R;
import com.openarena.controllers.Connection;

public class Api {

	public String getLeaguesList(Context context) {
		Connection connection = getConnection(context);
		return connection.request(getBaseUri() + "soccerseasons");
	}

	private Connection getConnection(Context context) {
		return new Connection.Builder()
				.putHeader("X-Auth-Token", context.getString(R.string.api_token))
				.putHeader("X-Response-Control", context.getString(R.string.api_response_control_full))
				.build();
	}

	private String getBaseUri() {
		return "http://api.football-data.org/v1/";
	}
}
