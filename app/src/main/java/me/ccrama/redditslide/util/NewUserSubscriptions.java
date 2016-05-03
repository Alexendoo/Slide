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

import android.database.Cursor;

import java.util.List;

import me.ccrama.redditslide.Authentication;
import me.ccrama.redditslide.Models.StorableSubreddit;

public class NewUserSubscriptions {
    public static List<StorableSubreddit> getMainSubreddits() {
        Cursor cursor = DatabaseUtil.getMainSubreddits(Authentication.name);

        return null;
    }
}
