package com.openarena.controllers;

import android.content.Context;
import android.os.Handler;

import com.openarena.model.adapters.FixturesAdapter;
import com.openarena.model.comparators.ComparatorFixtures;
import com.openarena.model.comparators.ComparatorPlayers;
import com.openarena.model.comparators.ComparatorScores;
import com.openarena.model.interfaces.OnResultListener;
import com.openarena.model.objects.Fixture;
import com.openarena.model.objects.Head2head;
import com.openarena.model.objects.League;
import com.openarena.model.objects.Player;
import com.openarena.model.objects.Scores;
import com.openarena.model.objects.Team;
import com.openarena.model.receivers.NotificationBroadcastReceiver;
import com.openarena.util.Const;
import com.openarena.util.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Controller {

	private static final int NUMBERS_OF_CORES = Runtime.getRuntime().availableProcessors();
	private static Controller mInstance;
	private ThreadPoolExecutor mExecutor;
	private Handler mHandler;
	private NotificationBroadcastReceiver mReceiver;

	public static synchronized void init(Context context) {
		DBManager.init(context);
		if (mInstance == null) synchronized (Controller.class) {
			if (mInstance == null) {
				mInstance = new Controller();
				mInstance.mHandler = new Handler();
				mInstance.mExecutor = new ThreadPoolExecutor(
						1,
						NUMBERS_OF_CORES,
						5000,
						TimeUnit.MILLISECONDS,
						new LinkedBlockingQueue<Runnable>()
				);
				mInstance.mReceiver = new NotificationBroadcastReceiver();
			}
		}
	}

	public static synchronized Controller getInstance() {
		return mInstance;
	}

	protected Controller() {}

	public void getListOfLeagues(final Context context, final OnGetLeagues callback) {
		mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				final ArrayList<League> dbList = DBManager.getLeaguesList();
				if (dbList != null) mHandler.post(new Runnable() {
					@Override
					public void run() {
						callback.onSuccess(dbList);
					}
				});
				int year = Calendar.getInstance().get(Calendar.YEAR);
				String resultCurrent,
						resultLast = null;
				resultCurrent= Api.getLeagueByYear(context, year);
				if (resultCurrent != null) resultLast = Api.getLeagueByYear(context, year - 1);
				if (resultCurrent != null && resultLast != null) {
					try {
						JSONArray currentArray = new JSONArray(resultCurrent);
						JSONArray lastArray = new JSONArray(resultLast);
						final ArrayList<League> list = League.parseArray(currentArray);
						list.addAll(League.parseArray(lastArray));
						if (!list.isEmpty()) {
							DBManager.setLeaguesList(list);
							mHandler.post(new Runnable() {
								@Override
								public void run() {
									callback.onSuccess(list);
								}
							});
						}
						else mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onError(Const.ERROR_CODE_RESULT_EMPTY);
							}
						});

					} catch (JSONException e) {
						L.e(Controller.class, e.toString());
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onError(Const.ERROR_CODE_PARSE_ERROR);
							}
						});
					}
				}
				else mHandler.post(new Runnable() {
					@Override
					public void run() {
						callback.onError(Const.ERROR_CODE_RESULT_NULL);
					}
				});
			}
		});
	}

	public void getListOfFixtures(
			final Context context,
			final int soccerseasonId,
			final int matchday,
			final OnGetFixtures callback) {
		mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String result = Api.getFixturesByMatchday(context, soccerseasonId, matchday);
				if (result != null) {
					try {
						JSONArray array = new JSONObject(result).getJSONArray("fixtures");
						final ArrayList<Fixture> list = Fixture.parseArray(array);
						if (list != null && !list.isEmpty()) {
							ComparatorFixtures.sortByDate(list);
							DBManager.setFixturesList(list);
							final ArrayList<Fixture> dbList = DBManager.getFixturesListByMatchday(soccerseasonId, matchday);
							if (dbList != null) {
								ComparatorFixtures.sortByDate(dbList);
								mHandler.post(new Runnable() {
									@Override
									public void run() {
										callback.onSuccess(dbList);
									}
								});
							}
						}
						else mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onError(Const.ERROR_CODE_RESULT_EMPTY);
							}
						});
					} catch (JSONException e) {
						L.e(Controller.class, e.toString());
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onError(Const.ERROR_CODE_PARSE_ERROR);
							}
						});
					}
				}
				else {
					final ArrayList<Fixture> dbList = DBManager.getFixturesListByMatchday(soccerseasonId, matchday);
					if (dbList != null) {
						ComparatorFixtures.sortByDate(dbList);
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onSuccess(dbList);
							}
						});
					}
					else mHandler.post(new Runnable() {
						@Override
						public void run() {
							callback.onError(Const.ERROR_CODE_RESULT_NULL);
						}
					});
				}
			}
		});
	}

	public void getScores(
			final Context context,
			final int soccerSeasonId,
			final OnGetScores callback) {
		mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				final ArrayList<Scores> dbList = DBManager.getScoresList(soccerSeasonId);
				if (dbList != null) {
					ComparatorScores.sortByPoints(dbList);
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							callback.onSuccess(dbList);
						}
					});
				}
				String result = Api.getScores(context, soccerSeasonId);
				if (result != null) {
					try {
						JSONArray array = new JSONObject(result).getJSONArray("standing");
						final ArrayList<Scores> list = Scores.parseArray(soccerSeasonId, array);
						if (list != null && !list.isEmpty()) {
							ComparatorScores.sortByPoints(list);
							DBManager.setScoresList(list);
							mHandler.post(new Runnable() {
								@Override
								public void run() {
									callback.onSuccess(list);
								}
							});
						}
						else mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onError(Const.ERROR_CODE_RESULT_EMPTY);
							}
						});
					} catch (JSONException e) {
						L.e(Controller.class, e.toString());
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onError(Const.ERROR_CODE_PARSE_ERROR);
							}
						});
					}
				}
				else mHandler.post(new Runnable() {
					@Override
					public void run() {
						callback.onError(Const.ERROR_CODE_RESULT_NULL);
					}
				});
			}
		});
	}

	public void getFixtureDetails(
			final Context context,
			final int fixtureId,
			final OnGetFixtureDetails callback) {
		mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				final Head2head dbHead2head = DBManager.getHead2head(fixtureId);
				if (dbHead2head != null) mHandler.post(new Runnable() {
					@Override
					public void run() {
						callback.onSuccess(dbHead2head);
					}
				});
				String result = Api.getFixtureDetailsById(context, fixtureId);
				if (result != null) {
					try {
						JSONObject object = new JSONObject(result).getJSONObject("head2head");
						final Head2head head2head = Head2head.parse(fixtureId, object);
						if (head2head != null) {
							DBManager.setHead2head(dbHead2head);
							mHandler.post(new Runnable() {
								@Override
								public void run() {
									callback.onSuccess(head2head);
								}
							});
						}
						else mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onError(Const.ERROR_CODE_RESULT_NULL);
							}
						});
					} catch (JSONException e) {
						L.e(Controller.class, e.toString());
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onError(Const.ERROR_CODE_PARSE_ERROR);
							}
						});
					}
				}
				else mHandler.post(new Runnable() {
					@Override
					public void run() {
						callback.onError(Const.ERROR_CODE_RESULT_NULL);
					}
				});
			}
		});
	}

	public void getTeam(
			final Context context,
			final int teamId,
			final OnGetTeam callback) {
		mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				final Team dbTeam = DBManager.getTeam(teamId);
				if (dbTeam != null) mHandler.post(new Runnable() {
					@Override
					public void run() {
						callback.onSuccess(dbTeam);
					}
				});
				String result = Api.getTeam(context, teamId);
				if (result != null) {
					try {
						JSONObject object = new JSONObject(result);
						final Team team = Team.parse(object);
						if (team != null) {
							DBManager.setTeam(team);
							mHandler.post(new Runnable() {
								@Override
								public void run() {
									callback.onSuccess(team);
								}
							});
						}
						else mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onError(Const.ERROR_CODE_RESULT_NULL);
							}
						});
					} catch (JSONException e) {
						L.e(Controller.class, e.toString());
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onError(Const.ERROR_CODE_PARSE_ERROR);
							}
						});
					}
				}
				else mHandler.post(new Runnable() {
					@Override
					public void run() {
						callback.onError(Const.ERROR_CODE_RESULT_NULL);
					}
				});
			}
		});
	}

	public void getListOfPlayers(
			final Context context,
			final int teamId,
			final OnGetPlayers callback) {
		mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				final ArrayList<Player> dbList = DBManager.getPlayerList(teamId);
				if (dbList != null) {
					ComparatorPlayers.sortByJerseyNumber(dbList);
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							callback.onSuccess(dbList);
						}
					});
				}
				String result = Api.getTeamPlayers(context, teamId);
				if (result != null) {
					try {
						JSONArray array = new JSONObject(result).getJSONArray("players");
						final ArrayList<Player> list = Player.parseArray(teamId, array);
						if (list != null && !list.isEmpty()) {
							ComparatorPlayers.sortByJerseyNumber(list);
							DBManager.setPlayersList(list);
							mHandler.post(new Runnable() {
								@Override
								public void run() {
									callback.onSuccess(list);
								}
							});
						}
						else mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onError(Const.ERROR_CODE_RESULT_NULL);
							}
						});
					} catch (JSONException e) {
						L.e(Controller.class, e.toString());
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onError(Const.ERROR_CODE_PARSE_ERROR);
							}
						});
					}
				}
				else mHandler.post(new Runnable() {
					@Override
					public void run() {
						callback.onError(Const.ERROR_CODE_RESULT_NULL);
					}
				});
			}
		});
	}

	public void getListOfTeamFixtures(
			final Context context,
			final int teamId,
			final OnGetFixtures callback) {
		mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String result = Api.getTeamFixtures(context, teamId);
				if (result != null) {
					try {
						JSONArray array = new JSONObject(result).getJSONArray("fixtures");
						final ArrayList<Fixture> list = Fixture.parseArray(array);
						if (list != null && !list.isEmpty()) {
							DBManager.setFixturesList(list);
							mHandler.post(new Runnable() {
								@Override
								public void run() {
									callback.onSuccess(list);
								}
							});
						}
						else mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onError(Const.ERROR_CODE_RESULT_EMPTY);
							}
						});
					} catch (JSONException e) {
						L.e(Controller.class, e.toString());
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onError(Const.ERROR_CODE_PARSE_ERROR);
							}
						});
					}
				}
				else mHandler.post(new Runnable() {
					@Override
					public void run() {
						callback.onError(Const.ERROR_CODE_RESULT_NULL);
					}
				});
			}
		});
	}

	public void changeNotification(final Context context, final int position, final FixturesAdapter adapter) {
		mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				Fixture fixture = adapter.getItem(position);
				if (fixture != null) {
					fixture.setChange();
					DBManager.setFixture(fixture);
					mReceiver.setNotifiedFixture(context, fixture, fixture.isNotified());
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							adapter.notifyItemChanged(position);
						}
					});
				}
			}
		});
	}

	public interface OnGetLeagues extends OnResultListener<ArrayList<League>> {}

	public interface OnGetFixtures extends OnResultListener<ArrayList<Fixture>> {}

	public interface OnGetFixtureDetails extends OnResultListener<Head2head> {}

	public interface OnGetScores extends OnResultListener<ArrayList<Scores>> {}

	public interface OnGetTeam extends OnResultListener<Team> {}

	public interface OnGetPlayers extends OnResultListener<ArrayList<Player>> {}

}
