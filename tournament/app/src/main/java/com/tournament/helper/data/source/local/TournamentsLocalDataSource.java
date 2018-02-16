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

package com.tournament.helper.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.tournament.helper.data.Tournament;
import com.tournament.helper.data.source.TournamentsDataSource;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Concrete implementation of a data source as a db.
 */
public class TournamentsLocalDataSource implements TournamentsDataSource {

    private static volatile TournamentsLocalDataSource INSTANCE;

//    private TournamentsDao mTournamentsDao;
//
//    private AppExecutors mAppExecutors;

    // Prevent direct instantiation.
    private TournamentsLocalDataSource(
//        @NonNull AppExecutors appExecutors,
//                                       @NonNull TournamentsDao tasksDao
    ) {
//        mAppExecutors = appExecutors;
//        mTournamentsDao = tasksDao;
    }

    public static TournamentsLocalDataSource getInstance(
//        @NonNull AppExecutors appExecutors, TODO add needed parameters to init Firebase
//                                                         @NonNull TournamentsDao tasksDao
    ) {
        if (INSTANCE == null) {
            synchronized (TournamentsLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TournamentsLocalDataSource();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Note: {@link LoadTournamentsCallback#onDataNotAvailable()} is fired if the database doesn't exist
     * or the table is empty.
     */
    @Override
    public void getTournaments(@NonNull final LoadTournamentsCallback callback) {
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                final List<Tournament> tasks = mTournamentsDao.getTournaments();
//                mAppExecutors.mainThread().execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (tasks.isEmpty()) {
//                            // This will be called if the table is new or just empty.
//                            callback.onDataNotAvailable();
//                        } else {
//                            callback.onTournamentsLoaded(tasks);
//                        }
//                    }
//                });
//            }
//        };
//
//        mAppExecutors.diskIO().execute(runnable);
    }

    /**
     * Note: {@link GetTournamentCallback#onDataNotAvailable()} is fired if the {@link Tournament} isn't
     * found.
     */
    @Override
    public void getTournament(@NonNull final String taskId, @NonNull final GetTournamentCallback callback) {
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                final Tournament task = mTournamentsDao.getTournamentById(taskId);
//
//                mAppExecutors.mainThread().execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (task != null) {
//                            callback.onTournamentLoaded(task);
//                        } else {
//                            callback.onDataNotAvailable();
//                        }
//                    }
//                });
//            }
//        };
//
//        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void saveTournament(@NonNull final Tournament task) {
//        checkNotNull(task);
//        Runnable saveRunnable = new Runnable() {
//            @Override
//            public void run() {
//                mTournamentsDao.insertTournament(task);
//            }
//        };
//        mAppExecutors.diskIO().execute(saveRunnable);
    }

    @Override
    public void completeTournament(@NonNull final Tournament task) {
//        Runnable completeRunnable = new Runnable() {
//            @Override
//            public void run() {
//                mTournamentsDao.updateCompleted(task.getId(), true);
//            }
//        };
//
//        mAppExecutors.diskIO().execute(completeRunnable);
    }

    @Override
    public void completeTournament(@NonNull String taskId) {
        // Not required for the local data source because the {@link TournamentsRepository} handles
        // converting from a {@code taskId} to a {@link task} using its cached data.
    }

    @Override
    public void activateTournament(@NonNull final Tournament task) {
//        Runnable activateRunnable = new Runnable() {
//            @Override
//            public void run() {
//                mTournamentsDao.updateCompleted(task.getId(), false);
//            }
//        };
//        mAppExecutors.diskIO().execute(activateRunnable);
    }

    @Override
    public void activateTournament(@NonNull String taskId) {
        // Not required for the local data source because the {@link TournamentsRepository} handles
        // converting from a {@code taskId} to a {@link task} using its cached data.
    }

    @Override
    public void clearCompletedTournaments() {
//        Runnable clearTournamentsRunnable = new Runnable() {
//            @Override
//            public void run() {
//                mTournamentsDao.deleteCompletedTournaments();
//
//            }
//        };
//
//        mAppExecutors.diskIO().execute(clearTournamentsRunnable);
    }

    @Override
    public void refreshTournaments() {
        // Not required because the {@link TournamentsRepository} handles the logic of refreshing the
        // tasks from all the available data sources.
    }

    @Override
    public void deleteAllTournaments() {
//        Runnable deleteRunnable = new Runnable() {
//            @Override
//            public void run() {
//                mTournamentsDao.deleteTournaments();
//            }
//        };
//
//        mAppExecutors.diskIO().execute(deleteRunnable);
    }

    @Override
    public void deleteTournament(@NonNull final String taskId) {
//        Runnable deleteRunnable = new Runnable() {
//            @Override
//            public void run() {
//                mTournamentsDao.deleteTournamentById(taskId);
//            }
//        };
//
//        mAppExecutors.diskIO().execute(deleteRunnable);
    }

    @VisibleForTesting
    static void clearInstance() {
        INSTANCE = null;
    }
}
