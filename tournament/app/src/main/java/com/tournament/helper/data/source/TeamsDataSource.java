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

import java.util.List;

/**
 * Main entry point for accessing Teams data.
 */
public interface TeamsDataSource {

    interface LoadTeamsCallback {

        void onTeamsLoaded(List<Team> teams);

        void onDataNotAvailable();
    }

    interface GetTeamCallback {

        void onTeamLoaded(Team team);

        void onDataNotAvailable();
    }

    interface SaveTeamCallback {

        void onTeamSaved(Team team);

        void onSaveNotAvailable();
    }

    void getTeams(@NonNull LoadTeamsCallback callback);
    
    void getTeam(@NonNull String TeamId, @NonNull GetTeamCallback callback);
    
    void saveTeam(@NonNull Team Team, @NonNull SaveTeamCallback callback);

    void refreshTeams();

}
