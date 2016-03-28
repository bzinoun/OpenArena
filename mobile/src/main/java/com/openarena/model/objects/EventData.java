package com.openarena.model.objects;

public class EventData {

	private int mCode;
	private int mID;

	public EventData(int code) {
		mCode = code;
	}

	public int gecCode() {
		return mCode;
	}

	public int getSoccerSeasonId() {
		return mID;
	}

	public EventData setSoccerSeasonId(int id) {
		mID = id;
		return this;
	}
}
