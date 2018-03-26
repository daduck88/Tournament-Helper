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

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.support.annotation.Nullable;

import com.tournament.helper.data.Team;
import com.tournament.helper.data.Team;
import com.tournament.helper.data.source.TeamsDataSource;
import com.tournament.helper.data.source.TeamsRepository;

/**
 * Abstract class for View Models that expose a single {@link Team}.
 */
public abstract class TeamViewModel extends BaseObservable
        implements TeamsDataSource.GetTeamCallback {

    public final ObservableField<String> snackbarText = new ObservableField<>();

    public final ObservableField<String> title = new ObservableField<>();
    
    private final ObservableField<Team> mTeamObservable = new ObservableField<>();

    private final TeamsRepository mTeamsRepository;

    private final Context mContext;

    private boolean mIsDataLoading;

    public TeamViewModel(Context context, TeamsRepository tasksRepository) {
        mContext = context.getApplicationContext(); // Force use of Application Context.
        mTeamsRepository = tasksRepository;

        // Exposed observables depend on the mTeamObservable observable:
        mTeamObservable.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                Team task = mTeamObservable.get();
                if (task != null) {
                    title.set(task.getTitle());
                } else {
                    title.set(mContext.getString(R.string.no_data));
                }
            }
        });
    }

    public void start(String taskId) {
        if (taskId != null) {
            mIsDataLoading = true;
            mTeamsRepository.getTeam(taskId, this);
        }
    }

    public void setTeam(Team team) {
        mTeamObservable.set(team);
    }

    @Bindable
    public boolean isDataAvailable() {
        return mTeamObservable.get() != null;
    }

    @Bindable
    public boolean isDataLoading() {
        return mIsDataLoading;
    }

    // This could be an observable, but we save a call to Team.getTitleForList() if not needed.
    @Bindable
    public String getTitleForList() {
        if (mTeamObservable.get() == null) {
            return "No data";
        }
        return mTeamObservable.get().getTitle();
    }

    @Override
    public void onTeamLoaded(Team task) {
        mTeamObservable.set(task);
        mIsDataLoading = false;
        notifyChange(); // For the @Bindable properties
    }

    @Override
    public void onDataNotAvailable() {
        mTeamObservable.set(null);
        mIsDataLoading = false;
    }

    public void onRefresh() {
        if (mTeamObservable.get() != null) {
            start(mTeamObservable.get().getId());
        }
    }

    public String getSnackbarText() {
        return snackbarText.get();
    }

    @Nullable
    protected String getTeamId() {
        return mTeamObservable.get().getId();
    }

    protected Team getTeam() {
        return mTeamObservable.get();
    }

}
