package com.openarena.model.objects;

public class EventData {

	private int mCode;
	private int mSoccerSeasonId;

	public EventData(int code) {
		mCode = code;
	}

	public int gecCode() {
		return mCode;
	}

	public int getSoccerSeasonId() {
		return mSoccerSeasonId;
	}

	public EventData setSoccerSeasonId(int soccerSeasonId) {
		mSoccerSeasonId = soccerSeasonId;
		return this;
	}
}
