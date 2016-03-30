package com.openarena.model.objects;

public class EventData {

	private int mCode;
	private int mID;
	private Fixture mFixture;

	public EventData(int code) {
		mCode = code;
	}

	public int gecCode() {
		return mCode;
	}

	public int getID() {
		return mID;
	}

	public Fixture getFixture() {
		return mFixture;
	}

	public EventData setID(int id) {
		mID = id;
		return this;
	}

	public EventData setFixture(Fixture fixture) {
		mFixture = fixture;
		return this;
	}
}
