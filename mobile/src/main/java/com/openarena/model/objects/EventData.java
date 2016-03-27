package com.openarena.model.objects;

public class EventData {

	private int mCode;
	private int mPosition;

	public EventData(int code) {
		mCode = code;
	}

	public int gecCode() {
		return mCode;
	}

	public int getPosition() {
		return mPosition;
	}

	public EventData setPosition(int position) {
		mPosition = position;
		return this;
	}
}
