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

import me.ccrama.redditslide.util.DatabaseUtil;


public class StorableSubreddit extends Storable {
    private EnumSet<Flags> flags = EnumSet.noneOf(Flags.class);
    private String name;
    private String username;

    /**
     * Create a new storable subreddit, to store the subreddit call {@link #store()}
     *
     * @param subreddit Subreddit to retrieve information from
     * @param username The username of the user the subreddit belongs to, or null to add a default
     *                 subreddit. (Note: belonging to a user doesn't mean subscribed)
     * @param flags Optional set of {@link #flags}, the values provided by {@code username} still
     *              apply so the set may be incomplete
     */
    public StorableSubreddit(Subreddit subreddit, @Nullable String username, @Nullable EnumSet<Flags> flags) {
        if (flags != null) {
            this.flags.addAll(flags);
        }
        if (subreddit.isNsfw()) {
            this.flags.add(Flags.NSFW);
        }
        if (subreddit.isUserSubscriber()) {
            this.flags.add(Flags.SUBSCRIBER);
        }
        if (subreddit.isUserModerator()) {
            this.flags.add(Flags.MODERATOR);
        }

        this.name = subreddit.getDisplayName();
        this.username = username;
    }

    /**
     * Create a new storable subreddit
     *
     * @param name The name of the subreddit
     * @param username The username of the user the subreddit belongs to, or null to add a default
     *                 subreddit. (Note: belonging to a user doesn't mean subscribed)
     * @param flags A set of {@link #flags}, or null for no flags
     * @param stored True if the subreddit exists in the database
     */
    public StorableSubreddit(String name, @Nullable String username, @Nullable EnumSet<Flags> flags, Boolean stored) {
        if (flags != null) {
            this.flags.addAll(flags);
        }

        this.name = name;
        this.stored = stored;
        this.username = username;
    }

    /**
     * Store the subreddit in the database
     *
     * @return The row ID of the stored subreddit, or -1 if an error occurred
     */
    @Override
    public long store() {
        if (rowId != -1) {
            rowId = DatabaseUtil.storeSubreddit(this);
            stored = true;
        }
        return rowId;
    }

    /**
     * Returns the displayable name of the subreddit, may be mixed case
     *
     * @return the displayable name of the subreddit, may be mixed case
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the user's name that the subreddit is related to, for instance a subscription or
     * history of theirs
     *
     * @return The username or null for default subreddits
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns true if the subreddit is not safe for work (over 18)
     *
     * @return true is the subreddit is not safe for work (over 18)
     */
    public Boolean isNsfw() {
        return flags.contains(Flags.NSFW);
    }

    /**
     * Returns true if the user is subscribed to the current subreddit
     *
     * @return true if the user is subscribed to the current subreddit
     */
    public Boolean isUserSubscriber() {
        return flags.contains(Flags.SUBSCRIBER);
    }

    /**
     * Returns true if the user is a moderator of the current subreddit
     *
     * @return true if the user is a moderator of the current subreddit
     */
    public Boolean isUserModerator() {
        return flags.contains(Flags.MODERATOR);
    }

    /**
     * Returns true if the user explicitly hid this subreddit by removing it from their list,
     * {@link #isUserSubscriber()} may still also be true
     *
     * @return true if the subreddit has been hidden by the user
     */
    public Boolean isHidden() {
        return flags.contains(Flags.HIDDEN);
    }

    /**
     * Returns true if the user has added the subreddit to their list, but without subscribing to it
     *
     * @return true if the subreddit is a casual subscription
     */
    public Boolean isCasual() {
        return flags.contains(Flags.CASUAL);
    }

    public enum Flags {
        SUBSCRIBER, CASUAL, HIDDEN, HISTORY, NSFW, MODERATOR
    }
}
