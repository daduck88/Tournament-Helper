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

import com.tournament.helper.data.Team;
import com.tournament.helper.data.Tournament;

import java.util.List;

/**
 * Main entry point for accessing Tournaments data.
 */
public interface TournamentsDataSource {

    interface LoadTournamentsCallback {

        void onTournamentsLoaded(List<Tournament> Tournaments);

        void onDataNotAvailable();
    }

    interface GetTournamentCallback {

        void onTournamentLoaded(Tournament Tournament);

        void onDataNotAvailable();
    }

    interface SaveTournamentCallback {

        void onTournamentSaved(Tournament tournament);

        void onSaveNotAvailable();
    }

    void getTournaments(@NonNull LoadTournamentsCallback callback);

    void getTournament(@NonNull String TournamentId, @NonNull GetTournamentCallback callback);

    void saveTournament(@NonNull Tournament Tournament, @NonNull SaveTournamentCallback callback);

    void updateTournament(@NonNull Tournament Tournament, @NonNull SaveTournamentCallback callback);

    void completeTournament(@NonNull Tournament Tournament);

    void completeTournament(@NonNull String TournamentId);

    void activateTournament(@NonNull Tournament Tournament);

    void activateTournament(@NonNull String TournamentId);

    void clearCompletedTournaments();

    void refreshTournaments();

    void deleteAllTournaments();

    void deleteTournament(@NonNull String TournamentId);
}
