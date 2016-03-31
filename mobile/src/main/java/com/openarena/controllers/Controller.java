package com.openarena.controllers;

import android.content.Context;
import android.os.Handler;

import com.openarena.model.interfaces.OnResultListener;
import com.openarena.model.objects.Fixture;
import com.openarena.model.objects.Head2head;
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
	private Handler mHandler;

	public static synchronized void init() {
		if (mInstance == null) synchronized (Controller.class) {
			if (mInstance == null) {
				mInstance = new Controller();
				mInstance.mHandler = new Handler();
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
				String resultCurrent = null,
						resultLast = null;
				resultCurrent= Api.getLeagueByYear(context, year);
				if (resultCurrent != null) resultLast = Api.getLeagueByYear(context, year - 1);
				if (resultCurrent != null && resultLast != null) {
					try {
						JSONArray currentArray = new JSONArray(resultCurrent);
						JSONArray lastArray = new JSONArray(resultLast);
						final ArrayList<League> list = League.parseArray(currentArray);
						list.addAll(League.parseArray(lastArray));
						if (!list.isEmpty()) mHandler.post(new Runnable() {
								@Override
								public void run() {
									callback.onSuccess(list);
								}
							});
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
			final OnGetFixtures callback) {
		sExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String result = Api.getFixturesBySeasonId(context, soccerseasonId);
				if (result != null) {
					try {
						JSONArray array = new JSONObject(result).getJSONArray("fixtures");
						final ArrayList<Fixture> list = Fixture.parseArray(array);
						if (!list.isEmpty()) mHandler.post(new Runnable() {
								@Override
								public void run() {
									callback.onSuccess(list);
								}
							});
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
		sExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String result = Api.getFixtureDetailsById(context, fixtureId);
				if (result != null) {
					try {
						JSONObject object = new JSONObject(result).getJSONObject("head2head");
						final Head2head head2head = Head2head.parse(fixtureId, object);
						if (head2head != null) {
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

	public interface OnGetLeagues extends OnResultListener<ArrayList<League>> {}

	public interface OnGetFixtures extends OnResultListener<ArrayList<Fixture>> {}

	public interface OnGetFixtureDetails extends OnResultListener<Head2head> {}

}
