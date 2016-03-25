package com.openarena.controllers;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.openarena.model.Api;
import com.openarena.model.PreferencesManager;
import com.openarena.model.interfaces.OnNetworkResponse;
import com.openarena.model.objects.Fixture;
import com.openarena.model.objects.League;
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
	private ThreadPoolExecutor sExecutor;
	private Handler sHandler;
	private Context sContext;

	public static synchronized void init(Context context) {
		if (mInstance == null) synchronized (Controller.class) {
			if (mInstance == null) {
				mInstance = new Controller();
				mInstance.sContext = context;
				mInstance.sHandler = new Handler();
				mInstance.sExecutor = new ThreadPoolExecutor(
						1,
						NUMBERS_OF_CORES,
						5000,
						TimeUnit.MILLISECONDS,
						new LinkedBlockingQueue<Runnable>());
			}
		}
	}

	public static synchronized Controller getInstance() {
		return mInstance;
	}

	protected Controller() {}

	public void getListOfLeagues(final Context context, final OnGetLeagues callback) {
		sExecutor.execute(new Runnable() {
			@Override
			public void run() {
				int year = Calendar.getInstance().get(Calendar.YEAR);
				String resultCurrent = Api.getLeagueByYear(context, year);
				String resultLast = Api.getLeagueByYear(context, year - 1);
				if (resultCurrent != null && resultLast != null) {
					/*Gson gson = new Gson();
					ArrayList<LeagueG> list1 = gson.fromJson(
							resultCurrent,
							new TypeToken<ArrayList<LeagueG>>() {}.getType());
					ArrayList<LeagueG> list2 = gson.fromJson(
							resultLast,
							new TypeToken<ArrayList<LeagueG>>() {}.getType());*/
					try {
						JSONArray currentArray = new JSONArray(resultCurrent);
						JSONArray lastArray = new JSONArray(resultLast);
						final ArrayList<League> list = parseLeagues(currentArray);
						list.addAll(parseLeagues(lastArray));
						if (!list.isEmpty()) {
							sHandler.post(new Runnable() {
								@Override
								public void run() {
									callback.onSuccess(list);
								}
							});
						}
						else sHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onError();
							}
						});

					} catch (JSONException e) {
						L.e(Controller.class, e.toString());
						e.printStackTrace();
						sHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onError();
							}
						});
					}
				}
				else sHandler.post(new Runnable() {
					@Override
					public void run() {
						callback.onError();
					}
				});
			}
		});
	}

	public void getListOfFixtures(final Context context, final int soccerseasonId, final OnGetFixtures callback) {
		sExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String result = Api.getFixturesBySeasonId(context, soccerseasonId);
				if (result != null) {
					try {
						JSONArray array = new JSONObject(result).getJSONArray("fixtures");
						final ArrayList<Fixture> list = parseFixtures(array);
						if (!list.isEmpty()) {
							sHandler.post(new Runnable() {
								@Override
								public void run() {
									callback.onSuccess(list);
								}
							});
						}
						else sHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onError();
							}
						});
					} catch (JSONException e) {
						L.e(Controller.class, e.toString());
						e.printStackTrace();
						sHandler.post(new Runnable() {
							@Override
							public void run() {
								callback.onError();
							}
						});
					}
				}
				else sHandler.post(new Runnable() {
					@Override
					public void run() {
						callback.onError();
					}
				});
			}
		});
	}

	public boolean isFirstEnter() {
		return PreferencesManager.getInstance(sContext).getBoolean(Const.PREF_FIRST_ENTER, true);
	}

	@NonNull
	private ArrayList<League> parseLeagues(JSONArray array) {
		ArrayList<League> list = new ArrayList<>();
		if (array != null) {
			int count = array.length();
			for (int i = 0; i < count; i++) {
				try {
					list.add(League.parse(array.getJSONObject(i)));
				} catch (JSONException e) {
					L.e(Controller.class, e.toString());
					e.printStackTrace();
				}
			}
		}
		return list;
	}

	@NonNull
	private ArrayList<Fixture> parseFixtures(JSONArray array) {
		ArrayList<Fixture> list = new ArrayList<>();
		if (array != null) {
			int count = array.length();
			for (int i = 0; i < count; i++) {
				try {
					list.add(Fixture.parse(array.getJSONObject(i)));
				} catch (JSONException e) {
					L.e(Controller.class, e.toString());
					e.printStackTrace();
				}
			}
		}
		return list;
	}

	public interface OnGetLeagues extends OnNetworkResponse<ArrayList<League>> {}

	public interface OnGetFixtures extends OnNetworkResponse<ArrayList<Fixture>> {}

}
