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

package com.tournament.helper;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.tournament.helper.data.Match;
import com.tournament.helper.data.Team;
import com.tournament.helper.data.Tournament;
import com.tournament.helper.data.source.TeamsDataSource;
import com.tournament.helper.data.source.TeamsRepository;

/**
 * Abstract class for View Models that expose a single {@link Tournament}.
 */
public abstract class MatchViewModel extends BaseObservable
        implements TeamsDataSource.GetTeamCallback {

    public final ObservableField<String> snackbarText = new ObservableField<>();

    public final ObservableField<String> team1Name = new ObservableField<>();

    public final ObservableField<String> team2Name = new ObservableField<>();

    public final ObservableField<String> winnerName = new ObservableField<>();

    private final ObservableField<Team> mTeam1Observable = new ObservableField<>();

    private final ObservableField<Team> mTeam2Observable = new ObservableField<>();

    private final TeamsRepository mTeamsRepository;

    private boolean mIsDataLoading;

    protected Match mMatch;

    public MatchViewModel(TeamsRepository teamsRepository) {
        mTeamsRepository = teamsRepository;
        // Exposed observables depend on the mTeam1Observable observable:
        mTeam1Observable.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                Team team = mTeam1Observable.get();
                if (team != null) {
                    team1Name.set(team.getTitle());
                } else {
                    team1Name.set(THApp.context.getString(R.string.no_data));
                }
                if(mMatch.getTeam1Id().equalsIgnoreCase(mMatch.getWinnerId())){
                    winnerName.set(team.getTitle());
                }
            }
        });
        mTeam2Observable.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                Team team = mTeam2Observable.get();
                if (team != null) {
                    team2Name.set(team.getTitle());
                } else {
                    team2Name.set(THApp.context.getString(R.string.no_data));
                }
                if(mMatch.getTeam2Id().equalsIgnoreCase(mMatch.getWinnerId())){
                    winnerName.set(team.getTitle());
                }
            }
        });
    }

    public void start(Match match) {
        mMatch = match;
        if (mMatch != null) {
            mIsDataLoading = true;
            mTeamsRepository.getTeam(mMatch.getTeam1Id(), this);
            mTeamsRepository.getTeam(mMatch.getTeam2Id(), this);
        }
    }

    @Bindable
    public boolean isDataAvailable() {
        return mTeam1Observable.get() != null && mTeam2Observable.get() != null;
    }

    @Bindable
    public boolean isDataLoading() {
        return mIsDataLoading;
    }

    // This could be an observable, but we save a call to Tournament.getTitleForList() if not needed.
    @Bindable
    public String getTitleForList() {
        if (mTeam1Observable.get() == null) {
            return "No data";
        }
        return mTeam1Observable.get().getTitle();//TODO check if this is need
    }

    @Override
    public void onTeamLoaded(Team team) {
        if(team.getId().equals(mMatch.getTeam1Id())){
            mTeam1Observable.set(team);
        } else {
            mTeam2Observable.set(team);
        }
        mIsDataLoading = mTeam1Observable.get() == null || mTeam2Observable.get() == null;
        notifyChange(); // For the @Bindable properties
    }

    @Override
    public void onDataNotAvailable() {
        mTeam1Observable.set(null);
        mIsDataLoading = false;
        //TODO check this 2 possibled fails (also how to spell that)
    }

    public void deleteTournament() {
//        if (mTeam1Observable.get() != null) {
//            mTeamsRepository.deleteTournament(mTeam1Observable.get().getId());
//        }
        //TODO CHECK THIS AND HOW TO perform CLICK
    }

    public void onRefresh() {
//        if (mTeam1Observable.get() != null) {
//            start(mTeam1Observable.get().getId());
//        }
    }

    public String getSnackbarText() {
        return snackbarText.get();
    }

    @Bindable
    public boolean isEmpty() {
        return !TextUtils.isEmpty(mMatch.getWinnerId());
    }

    @Nullable
    protected String getTournamentId() {
        return mTeam1Observable.get().getId();
    }
}
