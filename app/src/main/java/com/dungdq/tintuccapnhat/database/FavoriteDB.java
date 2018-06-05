package com.dungdq.tintuccapnhat.database;

import java.util.ArrayList;
import java.util.List;

import com.dungdq.tintuccapnhat.model.FavoriteList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoriteDB extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "FavoriteList";

	// Contacts table name
	private static final String TABLE_FAVORITES = "Favorites";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_TITLE = "title";
	private static final String KEY_SUMMARY = "summary";
	private static final String KEY_THUMBNAIL = "thumbnail";
	private static final String KEY_AUTHOR = "author";
	private static final String KEY_URL = "url";

	public FavoriteDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_FAVORITES_TABLE = "CREATE TABLE " + TABLE_FAVORITES + "("
				+ KEY_ID + " TEXT PRIMARY KEY," + KEY_TITLE + " TEXT,"
				+ KEY_SUMMARY + " TEXT," + KEY_THUMBNAIL + " TEXT,"
				+ KEY_AUTHOR + " TEXT," + KEY_URL + " TEXT" + ")";
		db.execSQL(CREATE_FAVORITES_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new News item
	public void addFavorite(FavoriteList favorite) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, favorite.getID());
		values.put(KEY_TITLE, favorite.getTitle());
		values.put(KEY_SUMMARY, favorite.getSummary());
		values.put(KEY_THUMBNAIL, favorite.getThumbnail());
		values.put(KEY_AUTHOR, favorite.getAuthor());
		values.put(KEY_URL, favorite.getUrl());

		// Inserting Row
		db.insert(TABLE_FAVORITES, null, values);
		db.close(); // Closing database connection
	}

	// Getting single item
	public FavoriteList getFavoriteItem(String id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_FAVORITES, new String[] { KEY_ID,
				KEY_TITLE, KEY_SUMMARY, KEY_THUMBNAIL, KEY_AUTHOR, KEY_URL },
				KEY_ID + "=?", new String[] { id }, null, null, null, null);
		FavoriteList favorite = null;

		if (cursor != null) {
			if (cursor.moveToFirst()) {
				favorite = new FavoriteList(cursor.getString(0),
						cursor.getString(1), cursor.getString(2),
						cursor.getString(3), cursor.getString(4),
						cursor.getString(5));
			}
		}

		// return favorite list
		if (favorite != null)
			return favorite;
		else
			return null;
	}

	// Getting All Favorites
	public List<FavoriteList> getAllFavorites() {
		List<FavoriteList> favoriteList = new ArrayList<FavoriteList>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_FAVORITES;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				FavoriteList favorite = new FavoriteList();
				favorite.setID(cursor.getString(0));
				favorite.setTitle(cursor.getString(1));
				favorite.setSummary(cursor.getString(2));
				favorite.setThumbnail(cursor.getString(3));
				favorite.setAuthor(cursor.getString(4));
				favorite.setUrl(cursor.getString(5));
				// Adding favorite item to list
				favoriteList.add(favorite);
			} while (cursor.moveToNext());
		}

		// return favorite list
		return favoriteList;
	}

	// Updating single favorite item
	public int updateFavorite(FavoriteList favorite) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, favorite.getTitle());
		values.put(KEY_SUMMARY, favorite.getSummary());
		values.put(KEY_THUMBNAIL, favorite.getThumbnail());
		values.put(KEY_AUTHOR, favorite.getAuthor());
		values.put(KEY_URL, favorite.getUrl());

		// updating row
		return db.update(TABLE_FAVORITES, values, KEY_ID + " = ?",
				new String[] { String.valueOf(favorite.getID()) });
	}

	// Deleting single favorite item
	public void deleteFavorite(String id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_FAVORITES, KEY_ID + " = ?", new String[] { id });
		db.close();
	}

	// Getting favorite Count
	public int getContactsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_FAVORITES;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();

		// return count
		return count;
	}
}
