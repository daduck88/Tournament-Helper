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

package com.tournament.helper.create;

import android.content.Context;
import android.support.annotation.Nullable;

import com.tournament.helper.TeamViewModel;
import com.tournament.helper.create.dialog.SelectTeamDialog;
import com.tournament.helper.data.Team;
import com.tournament.helper.data.source.TeamsRepository;
import com.tournament.helper.tournaments.TournamentItemNavigator;

import java.lang.ref.WeakReference;

/**
 * Listens to user actions from the list item in ({@link com.tournament.helper.create.dialog.SelectTeamDialog})
 * and use the action listener.
 */
public class TeamItemViewModel extends TeamViewModel {

    // This navigator is s wrapped in a WeakReference to avoid leaks because it has references to an
    // activity/Dialog. There's no straightforward way to clear it for each item in a list adapter.
    @Nullable
    private WeakReference<SelectTeamDialog.SelectTeamListener> mNavigator;

    public TeamItemViewModel(Context context, TeamsRepository teamsRepository) {
        super(context, teamsRepository);
    }

    public void setListener(SelectTeamDialog.SelectTeamListener navigator) {
        mNavigator = new WeakReference<>(navigator);
    }

    /**
     * Called by the Data Binding library when the row is clicked.
     */
    public void teamClicked() {
        Team team = getTeam();
        if (team == null) {
            // Click happened before task was loaded, no-op.
            return;
        }
        if (mNavigator != null && mNavigator.get() != null) {
            mNavigator.get().onTeamSelected(team);
        }
    }
}
