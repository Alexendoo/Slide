/*
 * Copyright (c) 2016. ccrama
 *
 * Slide is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.ccrama.redditslide.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import me.ccrama.redditslide.Models.StoredSubreddit;

public class DatabaseUtil {
    private static SQLiteDatabase mDatabase;

    public static int getUserIdByName(String name) {
        String[] columns = {
                UserEntry._ID
        };
        String[] selectionArgs = {
                name
        };

        Cursor cursor = mDatabase.query(
                true,
                UserEntry.TABLE_NAME,
                columns,
                UserEntry.COLUMN_NAME + " = ?",
                selectionArgs,
                null,
                null,
                null,
                null
        );
        cursor.moveToFirst();

        return cursor.getInt(0);
    }

    public static SQLiteDatabase init(Context context) {
        if (mDatabase == null) {
            final dbHelper helper = new dbHelper(context);
            mDatabase = helper.getWritableDatabase();
        }
        return mDatabase;
    }

    public static SQLiteDatabase getDatabase() {
        return mDatabase;
    }

    public static long storeSubreddit(StoredSubreddit subreddit) {
        ContentValues values = new ContentValues();
        values.put(SubredditEntry.COLUMN_USER_ID, subreddit.getUserId());
        values.put(SubredditEntry.COLUMN_NAME, subreddit.getName());
        values.put(SubredditEntry.COLUMN_SUBSCRIBED, subreddit.isUserSubscriber());
        values.put(SubredditEntry.COLUMN_CASUAL, subreddit.isCasual());
        values.put(SubredditEntry.COLUMN_HIDDEN, subreddit.isHidden());
        values.put(SubredditEntry.COLUMN_NSFW, subreddit.isNsfw());
        values.put(SubredditEntry.COLUMN_MODERATOR, subreddit.isUserModerator());
        // TODO: themes, colours, previews

        return mDatabase.insert(
                SubredditEntry.TABLE_NAME,
                null,
                values
        );
    }

    private static abstract class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_NAME = "name";
    }

    private static abstract class SortingEntry implements BaseColumns {
        public static final String TABLE_NAME = "sorting";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_MULTI = "multi";
        public static final String COLUMN_SORT_NUMBER = "sort";
        public static final String COLUMN_DETAIL_ID = "detail_id";
    }

    private static abstract class SubredditEntry implements BaseColumns {
        public static final String TABLE_NAME = "subreddits";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SUBSCRIBED = "subscribed";
        public static final String COLUMN_CASUAL = "casual";
        public static final String COLUMN_HIDDEN = "hidden";
        public static final String COLUMN_NSFW = "nsfw";
        public static final String COLUMN_MODERATOR = "moderator";
        public static final String COLUMN_THEME_PRIMARY = "theme_primary";
        public static final String COLUMN_THEME_ACCENT = "theme_accent";
        public static final String COLUMN_PREVIEW_BIG = "preview_big";
        public static final String COLUMN_PREVIEW_SELF = "preview_self";
        public static final String COLUMN_MOBILE_COLOR = "mobile_color";
    }

    private static abstract class MultiredditEntry implements BaseColumns {
        public static final String TABLE_NAME = "multireddits";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PATH = "path";
        public static final String COLUMN_OWNER = "owner";
        public static final String COLUMN_THEME_PRIMARY = "theme_primary";
        public static final String COLUMN_THEME_ACCENT = "theme_accent";
        public static final String COLUMN_PREVIEW_BIG = "preview_big";
        public static final String COLUMN_PREVIEW_SELF = "preview_self";
    }

    private static class dbHelper extends SQLiteOpenHelper {
        public static final int DATABASE_VERSION = 1;
        // TODO: Final name
        public static final String DATABASE_NAME = "slide-001.db";

        public dbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + UserEntry.TABLE_NAME + "(" +
                    UserEntry._ID + " INTEGER PRIMARY KEY," +
                    UserEntry.COLUMN_NAME + " TEXT COLLATE NOCASE)");

            db.execSQL("CREATE INDEX ix_users_name ON " + UserEntry.TABLE_NAME + "(" +
                    UserEntry.COLUMN_NAME + " COLLATE NOCASE)");

            final String userReference = " REFERENCES " + UserEntry.TABLE_NAME +
                    "(" + UserEntry._ID + "),";


            db.execSQL("CREATE TABLE " + SortingEntry.TABLE_NAME + "(" +
                    SortingEntry._ID + " INTEGER PRIMARY KEY," +
                    SortingEntry.COLUMN_USER_ID + userReference +
                    SortingEntry.COLUMN_NAME + " TEXT COLLATE nocase," +
                    SortingEntry.COLUMN_MULTI + " BOOLEAN," +
                    SortingEntry.COLUMN_SORT_NUMBER + " INTEGER," +
                    SortingEntry.COLUMN_DETAIL_ID + " INTEGER)");

            db.execSQL("CREATE INDEX ix_sorting_user_sort_type_detail ON " + SortingEntry.TABLE_NAME + "(" +
                    SortingEntry.COLUMN_USER_ID + "," +
                    SortingEntry.COLUMN_SORT_NUMBER + "," +
                    SortingEntry.COLUMN_MULTI + "," +
                    SortingEntry.COLUMN_DETAIL_ID + ")");


            db.execSQL("CREATE TABLE " + SubredditEntry.TABLE_NAME + "(" +
                    SubredditEntry._ID + "INTEGER PRIMARY KEY," +
                    SubredditEntry.COLUMN_USER_ID + userReference +
                    SubredditEntry.COLUMN_NAME + " TEXT COLLATE nocase," +
                    SubredditEntry.COLUMN_SUBSCRIBED + " BOOLEAN," +
                    SubredditEntry.COLUMN_CASUAL + " BOOLEAN," +
                    SubredditEntry.COLUMN_HIDDEN + " BOOLEAN," +
                    SubredditEntry.COLUMN_MODERATOR + " BOOLEAN," +
                    SubredditEntry.COLUMN_NSFW + " BOOLEAN," +
                    SubredditEntry.COLUMN_PREVIEW_BIG + " BOOLEAN," +
                    SubredditEntry.COLUMN_PREVIEW_SELF + " BOOLEAN," +
                    SubredditEntry.COLUMN_THEME_PRIMARY + " INTEGER," +
                    SubredditEntry.COLUMN_THEME_ACCENT + " INTEGER," +
                    SubredditEntry.COLUMN_MOBILE_COLOR + " INTEGER)");

            db.execSQL("CREATE INDEX ix_subs_user_name ON " + SubredditEntry.TABLE_NAME + "(" +
                    SubredditEntry.COLUMN_USER_ID + "," +
                    SubredditEntry.COLUMN_NAME + " COLLATE nocase)");

            db.execSQL("CREATE INDEX ix_subs_mod ON " + SubredditEntry.TABLE_NAME + "(" +
                    SubredditEntry.COLUMN_MODERATOR + ")");


            db.execSQL("CREATE TABLE " + MultiredditEntry.TABLE_NAME + "(" +
                    MultiredditEntry.COLUMN_USER_ID + userReference +
                    MultiredditEntry.COLUMN_NAME + " TEXT COLLATE nocase," +
                    MultiredditEntry.COLUMN_PATH + " TEXT," +
                    MultiredditEntry.COLUMN_OWNER + " TEXT COLLATE nocase," +
                    MultiredditEntry.COLUMN_PREVIEW_BIG + " BOOLEAN," +
                    MultiredditEntry.COLUMN_PREVIEW_SELF + " BOOLEAN," +
                    MultiredditEntry.COLUMN_THEME_PRIMARY + " INTEGER," +
                    MultiredditEntry.COLUMN_THEME_ACCENT + " INTEGER)");

            db.execSQL("CREATE INDEX ix_multis_user_name ON " + MultiredditEntry.TABLE_NAME + "(" +
                    MultiredditEntry.COLUMN_USER_ID + "," +
                    MultiredditEntry.COLUMN_NAME + " COLLATE nocase)");
        }

        /* NOTES:

        entries recalled default all unhidden sorted in user order

         */

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Any DB changes should be completely airtight
        }
    }
}
