package com.openarena.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.openarena.util.DBConst;

public class SQLHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "openarena.ApplicationDB";
	private static final int DB_VERSION = 1;

	private static volatile SQLHelper sInstance;

	public static synchronized SQLHelper getInstance(Context context) {
		if (sInstance == null) synchronized (SQLHelper.class) {
			if (sInstance == null) sInstance = new SQLHelper(context);
		}
		return sInstance;
	}

	protected SQLHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String params1 = DBConst.ID + " INTEGER NOT NULL," +
				DBConst.CAPTION + " TEXT," +
				DBConst.LEAGUE + " TEXT," +
				DBConst.YEAR + " INTEGER," +
				DBConst.CURRENT_MATCHDAY + " INTEGER," +
				DBConst.NUMBER_OF_MATCHDAYS + " INTEGER," +
				DBConst.NUMBER_OF_TEAMS + " INTEGER," +
				DBConst.NUMBER_OF_GAMES + " INTEGER," +
				DBConst.LAST_UPDATED + " INTEGER";

		String params2 = DBConst.ID + " INTEGER NOT NULL," +
				DBConst.SOCCER_SEASON_ID + " INTEGER," +
				DBConst.DATE + " INTEGER," +
				DBConst.STATUS + " INTEGER," +
				DBConst.MATCHDAY + " INTEGER," +
				DBConst.HOME_TEAM_ID + " INTEGER NOT NULL," +
				DBConst.HOME_TEAM_NAME + " TEXT," +
				DBConst.AWAY_TEAM_ID + " INTEGER NOT NULL," +
				DBConst.AWAY_TEAM_NAME + " TEXT," +
				DBConst.GOALS_HOME_TEAM + " INTEGER," +
				DBConst.GOALS_AWAY_TEAM + " INTEGER";

		String params3 = DBConst.FIXTURE_ID + " INTEGER NOT NULL," +
				DBConst.COUNT + " INTEGER," +
				DBConst.TIME_FRAME_START + " INTEGER," +
				DBConst.TIME_FRAME_END + " INTEGER," +
				DBConst.HOME_TEAM_WINS + " INTEGER," +
				DBConst.AWAY_TEAM_WINS + " INTEGER," +
				DBConst.DRAWS + " INTEGER";

		String params4 = DBConst.SOCCER_SEASON_ID + " INTEGER NOT NULL," +
				DBConst.RANK + " INTEGER," +
				DBConst.TEAM + " TEXT," +
				DBConst.TEAM_ID + " INTEGER," +
				DBConst.PLAYED_GAMES + " INTEGER," +
				DBConst.CREST_URI + " TEXT," +
				DBConst.POINTS + " INTEGER," +
				DBConst.GOALS + " INTEGER," +
				DBConst.GOAL_AGAINST + " INTEGER," +
				DBConst.GOAL_DIFFERENCE + " INTEGER";

		createTable(db, DBConst.TABLE_LEAGUES, params1);
		createTable(db, DBConst.TABLE_FIXTURES, params2);
		createTable(db, DBConst.TABLE_HEAD2HEAD, params3);
		createTable(db, DBConst.TABLE_SCORES, params4);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		dropTables(db);
		onCreate(db);
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		dropTables(db);
		onCreate(db);
	}

	public void dropTables(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + DBConst.TABLE_LEAGUES);
		db.execSQL("DROP TABLE IF EXISTS " + DBConst.TABLE_FIXTURES);
		db.execSQL("DROP TABLE IF EXISTS " + DBConst.TABLE_HEAD2HEAD);
		db.execSQL("DROP TABLE IF EXISTS " + DBConst.TABLE_SCORES);
	}

	private void createTable(SQLiteDatabase db, String tableName, String params) {
		StringBuilder query = new StringBuilder()
				.append("CREATE TABLE ").append(tableName)
				.append("(").append(params).append(");");
		db.execSQL(query.toString());
	}

	public void insert(String tableName, ContentValues values) {
		getWritableDatabase().insert(tableName, null, values);
	}

	public void update(String tableName, ContentValues values, String[] where, String[] arguments) {
		StringBuilder sb = new StringBuilder();
		int len = where.length;
		sb.append(where[0]).append("=?");
		for (int i = 1; i < len; i++)
			sb.append(" AND ").append(where[i]).append("=?");
		getWritableDatabase().update(tableName, values, sb.toString(), arguments);
	}

	/**
	 *
	 * @param orderBy your column + <b>ASC</b> or <b>DESC</b>
	 */
	public Cursor get(String tableName, String[] columns, String[] where,
					  String[] arguments, String groupBy, String having, String orderBy) {
		StringBuilder sb = null;
		if (where != null) {
			sb = new StringBuilder();
			int len = where.length;
			sb.append(where[0]).append("=?");
			for (int i = 1; i < len; i++)
				sb.append(" AND ").append(where[i]).append("=?");
		}
		String where_str = null;
		if (sb != null) where_str = sb.toString();
		//order [ASC|DESC]
		return getReadableDatabase()
				.query(tableName, columns, where_str, arguments, groupBy, having, orderBy);
	}

	public Cursor getAll(String tableName) {
		return get(tableName, null, null, null, null, null, null);
	}

	public Cursor getAll(String tableName, String[] where, String[] arguments) {
		return get(tableName, null, where, arguments, null, null, null);
	}

	/**
	 * Get all columns and sort
	 * @param orderBy column to order
	 * @param beginningAtLarge if <b>true</b> from large to small
	 * @return Cursor
	 */
	public Cursor getAll(
			String tableName,
			String[] where,
			String[] arguments,
			String orderBy,
			boolean beginningAtLarge) {
		orderBy += beginningAtLarge ? " DESC" : " ASC";
		return get(tableName, null, where, arguments, null, null, orderBy);
	}

	public void deleteAll(String tableName, String[] where, String[] argument) {
		StringBuilder sb = null;
		if (where != null) {
			sb = new StringBuilder();
			int len = where.length;
			sb.append(where[0]).append("=?");
			for (int i = 1; i < len; i++)
				sb.append(" AND ").append(where[i]).append("=?");
		}
		String where_str = null;
		if (sb != null) where_str = sb.toString();
		getWritableDatabase().delete(tableName, where_str, argument);
	}

	public void deleteAll(String tableName) {
		getWritableDatabase().delete(tableName, null, null);
	}

}
