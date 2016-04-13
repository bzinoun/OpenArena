package com.openarena.model.objects;

public class EventData {

	private int mCode;
	private League mLeague;
	private Fixture mFixture;
	private Scores mScores;
	private Team mTeam;
	private Player mPlayer;

	public EventData(int code) {
		mCode = code;
	}

	public int gecCode() {
		return mCode;
	}

	public League getLeague() {
		return mLeague;
	}

	public Fixture getFixture() {
		return mFixture;
	}

	public Scores getScores() {
		return mScores;
	}

	public Team getTeam() {
		return mTeam;
	}

	public Player getPlayer() {
		return mPlayer;
	}

	public EventData setLeague(League league) {
		mLeague = league;
		return this;
	}

	public EventData setFixture(Fixture fixture) {
		mFixture = fixture;
		return this;
	}

	public EventData setScores(Scores scores) {
		mScores = scores;
		return this;
	}

	public EventData setTeam(Team team) {
		mTeam = team;
		return this;
	}

	public EventData setPlayer(Player player) {
		mPlayer = player;
		return this;
	}

}
