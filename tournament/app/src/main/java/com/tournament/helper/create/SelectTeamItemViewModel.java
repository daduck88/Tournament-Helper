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
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.databinding.generated.callback.OnClickListener;
import android.support.annotation.Nullable;

import com.tournament.helper.R;
import com.tournament.helper.THApp;
import com.tournament.helper.data.Team;
import com.tournament.helper.data.Tournament;
import com.tournament.helper.data.helper.SelectTeam;
import com.tournament.helper.data.source.TeamsDataSource;
import com.tournament.helper.data.source.TeamsRepository;
import com.tournament.helper.data.source.TournamentsDataSource;
import com.tournament.helper.data.source.TournamentsRepository;
import com.tournament.helper.mock.TeamMock;
import com.tournament.helper.tournaments.TournamentItemNavigator;
import com.tournament.helper.tournaments.TournamentsFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Listens to user actions from the list item in ({@link TournamentsFragment}) and redirects them to the
 * Fragment's actions listener. TODO update this
 */
public class SelectTeamItemViewModel extends BaseObservable {

    public final ObservableField<String> title = new ObservableField<>();

    private final ObservableField<SelectTeam> selectTeamObservable = new ObservableField<>();
    private final TeamsRepository mTeamsRepository;

    private CreateTournamentViewModel mCreateTournamentViewModel;

    private boolean mIsDataLoading;


    public SelectTeamItemViewModel(SelectTeam selectTeam, CreateTournamentViewModel createTournamentViewModel, TeamsRepository teamsRepository) {
        selectTeamObservable.set(selectTeam);
        this.mCreateTournamentViewModel = createTournamentViewModel;
        this.mTeamsRepository = teamsRepository;

        // Exposed observables depend on the selectTeamObservable observable:
        selectTeamObservable.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                SelectTeam task = selectTeamObservable.get();
                if (task != null && task.getTeam() != null) {
                    title.set(task.getTeam().getTitle());
                } else {
                    title.set(THApp.context.getString(R.string.create_tournament_select_team));
                }
            }
        });
    }

    public void start(String taskId) {
        if (taskId != null) {
            mIsDataLoading = true;
//            mTournamentsRepository.getTournament(taskId, this);//TODO check this
        }
    }

    public void setTournament(SelectTeam task) {//TODO check this
        selectTeamObservable.set(task);
    }

    @Bindable
    public boolean isDataAvailable() {
        return selectTeamObservable.get() != null;
    }

    @Bindable
    public boolean isDataLoading() {
        return mIsDataLoading;
    }

    // This could be an observable, but we save a call to Tournament.getTitleForList() if not needed.
    @Bindable
    public String getTitleForList() {
        if (selectTeamObservable.get() == null || selectTeamObservable.get().getTeam() == null) {
            return "No data";
        }
        return selectTeamObservable.get().getTeam().getTitle();
    }

//    @Override
//    public void onTournamentLoaded(SelectTeam task) {//TODO check this
//        selectTeamObservable.set(task);
//        mIsDataLoading = false;
//        notifyChange(); // For the @Bindable properties
//    }
//
//    @Override
//    public void onDataNotAvailable() {
//        selectTeamObservable.set(null);
//        mIsDataLoading = false;
//    }

    public void onRefresh() {
        if (selectTeamObservable.get() != null) {
//            start(selectTeamObservable.get().getId());
            //TODO check if this is needed
        }
    }


    @Nullable
    protected String getTeamtId() {
        return selectTeamObservable.get().getTeam().getId();
    }

    // This navigator is s wrapped in a WeakReference to avoid leaks because it has references to an
    // activity. There's no straightforward way to clear it for each item in a list adapter.
    @Nullable
    private WeakReference<TournamentItemNavigator> mNavigator;

    public void setNavigator(TournamentItemNavigator navigator) {
        mNavigator = new WeakReference<>(navigator);
    }

    /**
     * Called by the Data Binding library when the row is clicked.
     * TODO check this click
     */
    public void taskClicked() {
        selectTeam();
//        mCreateTournamentViewModel.selectTeam();
//        String taskId = getTournamentId();
//        if (taskId == null) {
//            // Click happened before task was loaded, no-op.
//            return;
//        }
//        if (mNavigator != null && mNavigator.get() != null) {
//            mNavigator.get().openTournamentDetails(taskId);
//        }
    }

    private void selectTeam() {
//        onTeamSelectCallback
        //Add dialog
        mCreateTournamentViewModel.getSelectedTeams().remove(selectTeamObservable.get().getTeam());

        mTeamsRepository.getTeams(new TeamsDataSource.LoadTeamsCallback() {
            @Override
            public void onTeamsLoaded(List<Team> teams) {
                Team newTeam = TeamMock.getTeamWithout(mCreateTournamentViewModel.getSelectedTeams(), teams);
                mTeamsRepository.saveTeam(newTeam, new TeamsDataSource.SaveTeamCallback() {
                    @Override
                    public void onTeamSaved(Team team) {
                        mCreateTournamentViewModel.getSelectedTeams().add(team);
                        selectTeamObservable.get().setTeam(team);
                        notifyChange();
                    }
                    @Override
                    public void onSaveNotAvailable() {

                    }
                });
            }
            @Override
            public void onDataNotAvailable() {

            }
        });



    }

    public interface OnTeamSelectCallback{
        void onTeamSelected(Team team);
    }
}
