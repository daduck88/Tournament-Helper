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

import com.tournament.helper.create.dialog.AddTeamDialog;
import com.tournament.helper.create.dialog.SelectTeamDialog;
import com.tournament.helper.data.Team;

import java.util.List;

/**
 * Defines the navigation actions that can be called from the Create Tournament screen.
 */
public interface CreateTournamentNavigator {

    void onTournamentSaved();

    void onSelectTeam(List<Team> teams, SelectTeamDialog.SelectTeamListener selectTeamListener);// select team dialog listener

    void onAddTeam(AddTeamDialog.AddTeamListener addTeamListener);
}
