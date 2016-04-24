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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class Databases {
    public static abstract class RedditEntry implements BaseColumns {
        public static final String TABLE_NAME = "reddits";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_HIDDEN = "hidden";
        public static final String COLUMN_DETAIL_ID = "detail_id";
        public static final String COLUMN_SORT_ORDER = "sort_order";
    }

    public static abstract class SubredditDetail implements BaseColumns {
        public static final String TABLE_NAME = "subreddit_detail";
        public static final String COLUMN_SUBSCRIBED = "subscribed";
        public static final String COLUMN_MODERATOR = "moderator";
    }

    public static abstract class MultiredditDetail implements BaseColumns {
        public static final String TABLE_NAME = "multireddit_detail";
        public static final String COLUMN_PATH = "path";
        public static final String COLUMN_OWNER = "owner";
    }

    public class Reddits extends SQLiteOpenHelper {
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "reddits.db";

        public Reddits(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + RedditEntry.TABLE_NAME + "(" +
                    RedditEntry._ID + " INTEGER PRIMARY KEY," +
                    RedditEntry.COLUMN_NAME + " TEXT," +
                    RedditEntry.COLUMN_TYPE + " TEXT," +
                    RedditEntry.COLUMN_HIDDEN + " BOOLEAN," +
                    RedditEntry.COLUMN_DETAIL_ID + " INTEGER," +
                    RedditEntry.COLUMN_SORT_ORDER + " INTEGER" +
                    ")"
            );
            db.execSQL("CREATE TABLE " + SubredditDetail.TABLE_NAME + "(" +
                    SubredditDetail._ID + " INTEGER PRIMARY KEY," +
                    SubredditDetail.COLUMN_SUBSCRIBED + " BOOLEAN," +
                    SubredditDetail.COLUMN_MODERATOR + " BOOLEAN" +
                    ")"
            );
            db.execSQL("CREATE TABLE " + MultiredditDetail.TABLE_NAME + "(" +
                    MultiredditDetail._ID + " INTEGER PRIMARY KEY," +
                    MultiredditDetail.COLUMN_PATH + "path," +
                    ")"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
    }
}
