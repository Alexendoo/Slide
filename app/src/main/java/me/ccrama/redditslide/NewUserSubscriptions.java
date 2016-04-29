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

package me.ccrama.redditslide;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;

import me.ccrama.redditslide.util.LogUtil;

public class NewUserSubscriptions {
    private static SQLiteDatabase db;

    private static abstract class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_USER = "user";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_SORT_NUMBER = "sort";
        public static final String COLUMN_DETAIL_ID = "detail_id";
    }

    private static abstract class SubredditEntry implements BaseColumns {
        public static final String TABLE_NAME = "subreddits";
        public static final String COLUMN_USER = "user";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_STATUS = "status"; // Subscribed|Casual|Hidden|History
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
        public static final String COLUMN_USER = "user";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PATH = "path";
        public static final String COLUMN_OWNER = "owner";
        public static final String COLUMN_THEME_PRIMARY = "theme_primary";
        public static final String COLUMN_THEME_ACCENT = "theme_accent";
        public static final String COLUMN_PREVIEW_BIG = "preview_big";
        public static final String COLUMN_PREVIEW_SELF = "preview_self";
    }

    private static class RedditsDbHelper extends SQLiteOpenHelper {
        public static final int DATABASE_VERSION = 1;
        // TODO: Final name
        public static final String DATABASE_NAME = "reddits-004.db";

        public RedditsDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + UserEntry.TABLE_NAME + "(" +
                    UserEntry._ID + " INTEGER PRIMARY KEY," +
                    UserEntry.COLUMN_USER + " TEXT COLLATE nocase," +
                    // TODO: Name in table or through JOIN?
                    UserEntry.COLUMN_TYPE + " TEXT," +
                    UserEntry.COLUMN_SORT_NUMBER + " INTEGER," +
                    UserEntry.COLUMN_DETAIL_ID + " INTEGER)");
            db.execSQL("CREATE INDEX ix_users_user_sort_type_detail ON " + UserEntry.TABLE_NAME + "(" +
                    UserEntry.COLUMN_USER + " COLLATE nocase," +
                    UserEntry.COLUMN_SORT_NUMBER + "," +
                    UserEntry.COLUMN_TYPE + "," +
                    UserEntry.COLUMN_DETAIL_ID + ")");

            db.execSQL("CREATE TABLE " + SubredditEntry.TABLE_NAME + "(" +
                    SubredditEntry._ID + "INTEGER PRIMARY KEY," +
                    SubredditEntry.COLUMN_USER + " TEXT COLLATE nocase," +
                    SubredditEntry.COLUMN_NAME + " TEXT COLLATE nocase," +
                    SubredditEntry.COLUMN_STATUS + " TEXT," +
                    SubredditEntry.COLUMN_NSFW + " BOOLEAN," +
                    SubredditEntry.COLUMN_MODERATOR + " BOOLEAN," +
                    SubredditEntry.COLUMN_PREVIEW_BIG + " BOOLEAN," +
                    SubredditEntry.COLUMN_PREVIEW_SELF + " BOOLEAN," +
                    SubredditEntry.COLUMN_THEME_PRIMARY + " INTEGER," +
                    SubredditEntry.COLUMN_THEME_ACCENT + " INTEGER," +
                    SubredditEntry.COLUMN_MOBILE_COLOR + " INTEGER)");
            db.execSQL("CREATE INDEX ix_subs_user_status_name ON " + SubredditEntry.TABLE_NAME + "(" +
                    SubredditEntry.COLUMN_USER + " COLLATE nocase," +
                    SubredditEntry.COLUMN_STATUS + "," +
                    SubredditEntry.COLUMN_NAME + " COLLATE nocase)");
            db.execSQL("CREATE INDEX ix_subs_mod ON " + SubredditEntry.TABLE_NAME + "(" +
                    SubredditEntry.COLUMN_MODERATOR + ")");

            db.execSQL("CREATE TABLE " + MultiredditEntry.TABLE_NAME + "(" +
                    MultiredditEntry.COLUMN_USER + " TEXT COLLATE nocase," +
                    MultiredditEntry.COLUMN_NAME + " TEXT COLLATE nocase," +
                    MultiredditEntry.COLUMN_PATH + " TEXT COLLATE nocase," +
                    MultiredditEntry.COLUMN_OWNER + " TEXT COLLATE nocase," +
                    MultiredditEntry.COLUMN_PREVIEW_BIG + " BOOLEAN," +
                    MultiredditEntry.COLUMN_PREVIEW_SELF + " BOOLEAN," +
                    MultiredditEntry.COLUMN_THEME_PRIMARY + " INTEGER," +
                    MultiredditEntry.COLUMN_THEME_ACCENT + " INTEGER)");
            db.execSQL("CREATE INDEX ix_multis_user_name ON " + MultiredditEntry.TABLE_NAME + "(" +
                    MultiredditEntry.COLUMN_USER + " COLLATE nocase," +
                    MultiredditEntry.COLUMN_NAME + ")");
        }

        /* NOTES:

        entries recalled default all unhidden sorted in user order, optional bit flags to alter?
        e.g. RedditStorage.getSubreddit(), ...(RedditStorage.NO_NSFW|RedditStorage.ALL)

        Left outer join coalesce

         */

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Any DB changes should be completely airtight
        }
    }
}
