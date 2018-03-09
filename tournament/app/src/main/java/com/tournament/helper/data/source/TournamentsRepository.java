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

import com.tournament.helper.data.Tournament;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.android.gms.common.internal.zzbq.checkNotNull;

/**
 * Concrete implementation to load Tournaments from the data sources into a cache.
 * <p>
 * For simplicity, this implements a dumb synchronisation between locally persisted data and data
 * obtained from the server, by using the remote data source only if the local database doesn't
 * exist or is empty.
 */
public class TournamentsRepository implements TournamentsDataSource {

    private static TournamentsRepository INSTANCE = null;

    private final TournamentsDataSource mTournamentsFBDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    Map<String, Tournament> mCachedTournaments;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    boolean mCacheIsDirty = false;

    // Prevent direct instantiation.
    private TournamentsRepository(@NonNull TournamentsDataSource tournamentsFBDataSource) {
        mTournamentsFBDataSource = checkNotNull(tournamentsFBDataSource);
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param tournamentsFBDataSource the backend data source
     * @return the {@link TournamentsRepository} instance
     */
    public static TournamentsRepository getInstance(TournamentsDataSource tournamentsFBDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new TournamentsRepository(tournamentsFBDataSource);
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance(TournamentsDataSource)} to create a new instance
     * next time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }

    /**
     * Gets Tournaments from cache, local data source (SQLite) or remote data source, whichever is
     * available first.
     * <p>
     * Note: {@link LoadTournamentsCallback#onDataNotAvailable()} is fired if all data sources fail to
     * get the data.
     */
    @Override
    public void getTournaments(@NonNull final LoadTournamentsCallback callback) {
        checkNotNull(callback);

        // Respond immediately with cache if available and not dirty
        if (mCachedTournaments != null && !mCacheIsDirty) {
            callback.onTournamentsLoaded(new ArrayList<>(mCachedTournaments.values()));
            return;
        }
        getTournamentsFromRemoteDataSource(callback);
    }

    @Override
    public void saveTournament(@NonNull Tournament tournament) {
        checkNotNull(tournament);
        mTournamentsFBDataSource.saveTournament(tournament);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedTournaments == null) {
            mCachedTournaments = new LinkedHashMap<>();
        }
        mCachedTournaments.put(tournament.getId(), tournament);
    }

    @Override
    public void completeTournament(@NonNull Tournament tournament) {
        checkNotNull(tournament);
        mTournamentsFBDataSource.completeTournament(tournament);

        Tournament completedTournament = new Tournament(tournament.getTitle(), tournament.getDescription(), tournament.getId(), true);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedTournaments == null) {
            mCachedTournaments = new LinkedHashMap<>();
        }
        mCachedTournaments.put(tournament.getId(), completedTournament);
    }

    @Override
    public void completeTournament(@NonNull String tournamentId) {
        checkNotNull(tournamentId);
        completeTournament(getTournamentWithId(tournamentId));
    }

    @Override
    public void activateTournament(@NonNull Tournament tournament) {
        checkNotNull(tournament);
        mTournamentsFBDataSource.activateTournament(tournament);

        Tournament activeTournament = new Tournament(tournament.getTitle(), tournament.getDescription(), tournament.getId());

        // Do in memory cache update to keep the app UI up to date
        if (mCachedTournaments == null) {
            mCachedTournaments = new LinkedHashMap<>();
        }
        mCachedTournaments.put(tournament.getId(), activeTournament);
    }

    @Override
    public void activateTournament(@NonNull String TournamentId) {
        checkNotNull(TournamentId);
        activateTournament(getTournamentWithId(TournamentId));
    }

    @Override
    public void clearCompletedTournaments() {
        mTournamentsFBDataSource.clearCompletedTournaments();

        // Do in memory cache update to keep the app UI up to date
        if (mCachedTournaments == null) {
            mCachedTournaments = new LinkedHashMap<>();
        }
        Iterator<Map.Entry<String, Tournament>> it = mCachedTournaments.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Tournament> entry = it.next();
            if (entry.getValue().isCompleted()) {
                it.remove();
            }
        }
    }

    /**
     * Gets Tournaments from local data source (sqlite) unless the table is new or empty. In that case it
     * uses the network data source. This is done to simplify the sample.
     * <p>
     * Note: {@link GetTournamentCallback#onDataNotAvailable()} is fired if both data sources fail to
     * get the data.
     */
    @Override
    public void getTournament(@NonNull final String tournamentId, @NonNull final GetTournamentCallback callback) {
        checkNotNull(tournamentId);
        checkNotNull(callback);

        Tournament cachedTournament = getTournamentWithId(tournamentId);

        // Respond immediately with cache if available
        if (cachedTournament != null) {
            callback.onTournamentLoaded(cachedTournament);
            return;
        }

        // Load from server/persisted if needed.

        // Is the Tournament in the local data source? If not, query the network.

        mTournamentsFBDataSource.getTournament(tournamentId, new GetTournamentCallback() {
            @Override
            public void onTournamentLoaded(Tournament Tournament) {
                // Do in memory cache update to keep the app UI up to date
                if (mCachedTournaments == null) {
                    mCachedTournaments = new LinkedHashMap<>();
                }
                mCachedTournaments.put(Tournament.getId(), Tournament);
                callback.onTournamentLoaded(Tournament);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void refreshTournaments() {
        mCacheIsDirty = true;
    }

    @Override
    public void deleteAllTournaments() {
        mTournamentsFBDataSource.deleteAllTournaments();

        if (mCachedTournaments == null) {
            mCachedTournaments = new LinkedHashMap<>();
        }
        mCachedTournaments.clear();
    }

    @Override
    public void deleteTournament(@NonNull String tournamentId) {
        mTournamentsFBDataSource.deleteTournament(checkNotNull(tournamentId));

        mCachedTournaments.remove(tournamentId);
    }

    private void getTournamentsFromRemoteDataSource(@NonNull final LoadTournamentsCallback callback) {
        mTournamentsFBDataSource.getTournaments(new LoadTournamentsCallback() {
            @Override
            public void onTournamentsLoaded(List<Tournament> Tournaments) {
                refreshCache(Tournaments);
                callback.onTournamentsLoaded(new ArrayList<>(mCachedTournaments.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshCache(List<Tournament> tournaments) {
        if (mCachedTournaments == null) {
            mCachedTournaments = new LinkedHashMap<>();
        }
        mCachedTournaments.clear();
        for (Tournament Tournament : tournaments) {
            mCachedTournaments.put(Tournament.getId(), Tournament);
        }
        mCacheIsDirty = false;
    }

    @Nullable
    private Tournament getTournamentWithId(@NonNull String id) {
        checkNotNull(id);
        if (mCachedTournaments == null || mCachedTournaments.isEmpty()) {
            return null;
        } else {
            return mCachedTournaments.get(id);
        }
    }
}
