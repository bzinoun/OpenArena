package com.openarena.model.objects;

import com.google.gson.annotations.SerializedName;

public class LeagueG {

	@SerializedName("id")
	private int mID;
	@SerializedName("caption")
	private String mCaption;
	@SerializedName("league")
	private String mLeague;
	@SerializedName("year")
	private String mYear;
	@SerializedName("currentMatchday")
	private int mCurrentMatchday;
	@SerializedName("numberOfMatchdays")
	private int mNumberOfMatchdays;
	@SerializedName("numberOfTeams")
	private int mNumberOfTeams;
	@SerializedName("numberOfGames")
	private int mNumberOfGames;
	@SerializedName("lastUpdated")
	private String mLastUpdated;

	protected LeagueG() {}

	public int getId() {
		return mID;
	}

	public void setId(int id) {
		this.mID = id;
	}

	public String getCaption() {
		return mCaption;
	}

	public void setCaption(String caption) {
		this.mCaption = caption;
	}

	public String getLeague() {
		return mLeague;
	}

	public void setLeague(String league) {
		this.mLeague = league;
	}

	public String getYear() {
		return mYear;
	}

	public void setYear(String year) {
		this.mYear = year;
	}

	public int getCurrentMatchday() {
		return mCurrentMatchday;
	}

	public void setCurrentMatchday(int currentMatchday) {
		this.mCurrentMatchday = currentMatchday;
	}

	public int getNumberOfMatchdays() {
		return mNumberOfMatchdays;
	}

	public void setNumberOfMatchdays(int numberOfMatchdays) {
		this.mNumberOfMatchdays = numberOfMatchdays;
	}

	public int getNumberOfTeams() {
		return mNumberOfTeams;
	}

	public void setNumberOfTeams(int numberOfTeams) {
		this.mNumberOfTeams = numberOfTeams;
	}

	public int getNumberOfGames() {
		return mNumberOfGames;
	}

	public void setNumberOfGames(int numberOfGames) {
		this.mNumberOfGames = numberOfGames;
	}

	public String getLastUpdated() {
		return mLastUpdated;
	}

	public void setLastUpdated(String lastUpdated) {
		this.mLastUpdated = lastUpdated;
	}
}
