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

	public int getID() {
		return mID;
	}

	public EventData setID(int id) {
		mID = id;
		return this;
	}
}
