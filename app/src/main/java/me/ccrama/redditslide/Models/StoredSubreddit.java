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

package me.ccrama.redditslide.Models;

import android.support.annotation.Nullable;

import net.dean.jraw.models.Subreddit;

import java.util.EnumSet;

public class StoredSubreddit {
    private EnumSet<Flags> flags = EnumSet.noneOf(Flags.class);
    private String name;

    public StoredSubreddit(Subreddit subreddit, @Nullable String user, @Nullable EnumSet<Flags> flags) {
        if (flags != null)
            this.flags.addAll(flags);

        if (subreddit.isNsfw())
            this.flags.add(Flags.NSFW);
        if (subreddit.isUserSubscriber())
            this.flags.add(Flags.SUBSCRIBER);
        if (subreddit.isUserModerator())
            this.flags.add(Flags.MODERATOR);

        this.name = subreddit.getDisplayName();
    }

    public Boolean isNsfw() {
        return flags.contains(Flags.NSFW);
    }

    public Boolean isUserSubscriber() {
        return flags.contains(Flags.SUBSCRIBER);
    }

    public Boolean isUserModerator() {
        return flags.contains(Flags.MODERATOR);
    }

    public Boolean isHidden() {
        return flags.contains(Flags.HIDDEN);
    }

    public Boolean isCasual() {
        return flags.contains(Flags.CASUAL);
    }

    public enum Flags {
        SUBSCRIBER, CASUAL, HIDDEN, HISTORY, NSFW, MODERATOR
    }
}
