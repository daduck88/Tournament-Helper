/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tournament.helper.data.source;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tournament.helper.data.Team;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.android.gms.common.internal.zzbq.checkNotNull;

/**
 * Concrete implementation to load Teams from the data sources into a cache.
 * <p>
 * For simplicity, this implements a dumb synchronisation between locally persisted data and data
 * obtained from the server, by using the remote data source only if the local database doesn't
 * exist or is empty.
 */
public class TeamsRepository implements TeamsDataSource {

    private static TeamsRepository INSTANCE = null;

    private final TeamsDataSource mTeamsFBDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    Map<String, Team> mCachedTeams;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    boolean mCacheIsDirty = false;

    // Prevent direct instantiation.
    private TeamsRepository(@NonNull TeamsDataSource tournamentsFBDataSource) {
        mTeamsFBDataSource = checkNotNull(tournamentsFBDataSource);
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param tournamentsFBDataSource the backend data source
     * @return the {@link TeamsRepository} instance
     */
    public static TeamsRepository getInstance(TeamsDataSource tournamentsFBDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new TeamsRepository(tournamentsFBDataSource);
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance(TeamsDataSource)} to create a new instance
     * next time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }

    /**
     * Gets Teams from cache, local data source (SQLite) or remote data source, whichever is
     * available first.
     * <p>
     * Note: {@link LoadTeamsCallback#onDataNotAvailable()} is fired if all data sources fail to
     * get the data.
     */
    @Override
    public void getTeams(@NonNull final LoadTeamsCallback callback) {
        checkNotNull(callback);

        // Respond immediately with cache if available and not dirty
        if (mCachedTeams != null && !mCacheIsDirty) {
            callback.onTeamsLoaded(new ArrayList<>(mCachedTeams.values()));
        }
        getTeamsFromRemoteDataSource(callback);
    }

    @Override
    public void saveTeam(@NonNull Team team, @NonNull final SaveTeamCallback callback) {
        checkNotNull(team);
        if(team.getId() == null) {
            mTeamsFBDataSource.saveTeam(team, new SaveTeamCallback() {
                @Override
                public void onTeamSaved(Team team) {
                    if (mCachedTeams == null) {
                        mCachedTeams = new LinkedHashMap<>();
                    }
                    // Do in memory cache update to keep the app UI up to date
                    if(!mCachedTeams.containsKey(team.getId())) {
                        mCachedTeams.put(team.getId(), team);
                    }
                    callback.onTeamSaved(team);
                }

                @Override
                public void onSaveNotAvailable() {
                    callback.onSaveNotAvailable();
                }
            });
        }

    }

    /**
     * Gets Teams from local data source (sqlite) unless the table is new or empty. In that case it
     * uses the network data source. This is done to simplify the sample.
     * <p>
     * Note: {@link TeamsDataSource.GetTeamCallback#onDataNotAvailable()} is fired if both data sources fail to
     * get the data.
     */
    @Override
    public void getTeam(@NonNull final String tournamentId, @NonNull final TeamsDataSource.GetTeamCallback callback) {
        checkNotNull(tournamentId);
        checkNotNull(callback);

        Team cachedTeam = getTeamWithId(tournamentId);

        // Respond immediately with cache if available
        if (cachedTeam != null) {
            callback.onTeamLoaded(cachedTeam);
            return;
        }

        // Load from server/persisted if needed.

        // Is the Team in the local data source? If not, query the network.

        mTeamsFBDataSource.getTeam(tournamentId, new TeamsDataSource.GetTeamCallback() {
            @Override
            public void onTeamLoaded(Team Team) {
                // Do in memory cache update to keep the app UI up to date
                if (mCachedTeams == null) {
                    mCachedTeams = new LinkedHashMap<>();
                }
                mCachedTeams.put(Team.getId(), Team);
                callback.onTeamLoaded(Team);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void refreshTeams() {
        mCacheIsDirty = true;
    }

    private void getTeamsFromRemoteDataSource(@NonNull final LoadTeamsCallback callback) {
        mTeamsFBDataSource.getTeams(new LoadTeamsCallback() {
            @Override
            public void onTeamsLoaded(List<Team> Teams) {
                refreshCache(Teams);
                callback.onTeamsLoaded(new ArrayList<>(mCachedTeams.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshCache(List<Team> tournaments) {
        if (mCachedTeams == null) {
            mCachedTeams = new LinkedHashMap<>();
        }
        mCachedTeams.clear();
        for (Team Team : tournaments) {
            mCachedTeams.put(Team.getId(), Team);
        }
        mCacheIsDirty = false;
    }

    @Nullable
    private Team getTeamWithId(@NonNull String id) {
        checkNotNull(id);
        if (mCachedTeams == null || mCachedTeams.isEmpty()) {
            return null;
        } else {
            return mCachedTeams.get(id);
        }
    }
}
