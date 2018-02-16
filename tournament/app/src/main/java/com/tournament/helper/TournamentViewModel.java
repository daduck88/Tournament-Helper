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

import com.tournament.helper.data.Tournament;
import com.tournament.helper.data.source.TournamentsDataSource;
import com.tournament.helper.data.source.TournamentsRepository;

/**
 * Abstract class for View Models that expose a single {@link Tournament}.
 */
public abstract class TournamentViewModel extends BaseObservable
        implements TournamentsDataSource.GetTournamentCallback {

    public final ObservableField<String> snackbarText = new ObservableField<>();

    public final ObservableField<String> title = new ObservableField<>();

    public final ObservableField<String> description = new ObservableField<>();

    private final ObservableField<Tournament> mTournamentObservable = new ObservableField<>();

    private final TournamentsRepository mTournamentsRepository;

    private final Context mContext;

    private boolean mIsDataLoading;

    public TournamentViewModel(Context context, TournamentsRepository tasksRepository) {
        mContext = context.getApplicationContext(); // Force use of Application Context.
        mTournamentsRepository = tasksRepository;

        // Exposed observables depend on the mTournamentObservable observable:
        mTournamentObservable.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                Tournament task = mTournamentObservable.get();
                if (task != null) {
                    title.set(task.getTitle());
                    description.set(task.getDescription());
                } else {
                    title.set(mContext.getString(R.string.no_data));
                    description.set(mContext.getString(R.string.no_data_description));
                }
            }
        });
    }

    public void start(String taskId) {
        if (taskId != null) {
            mIsDataLoading = true;
            mTournamentsRepository.getTournament(taskId, this);
        }
    }

    public void setTournament(Tournament task) {
        mTournamentObservable.set(task);
    }

    // "completed" is two-way bound, so in order to intercept the new value, use a @Bindable
    // annotation and process it in the setter.
    @Bindable
    public boolean getCompleted() {
        Tournament task = mTournamentObservable.get();
        return task != null && task.isCompleted();
    }

    public void setCompleted(boolean completed) {
        if (mIsDataLoading) {
            return;
        }
        Tournament task = mTournamentObservable.get();

        // Notify repository and user
        if (completed) {
            mTournamentsRepository.completeTournament(task);
            snackbarText.set(mContext.getResources().getString(R.string.task_marked_complete));
        } else {
            mTournamentsRepository.activateTournament(task);
            snackbarText.set(mContext.getResources().getString(R.string.task_marked_active));
        }
    }

    @Bindable
    public boolean isDataAvailable() {
        return mTournamentObservable.get() != null;
    }

    @Bindable
    public boolean isDataLoading() {
        return mIsDataLoading;
    }

    // This could be an observable, but we save a call to Tournament.getTitleForList() if not needed.
    @Bindable
    public String getTitleForList() {
        if (mTournamentObservable.get() == null) {
            return "No data";
        }
        return mTournamentObservable.get().getTitleForList();
    }

    @Override
    public void onTournamentLoaded(Tournament task) {
        mTournamentObservable.set(task);
        mIsDataLoading = false;
        notifyChange(); // For the @Bindable properties
    }

    @Override
    public void onDataNotAvailable() {
        mTournamentObservable.set(null);
        mIsDataLoading = false;
    }

    public void deleteTournament() {
        if (mTournamentObservable.get() != null) {
            mTournamentsRepository.deleteTournament(mTournamentObservable.get().getId());
        }
    }

    public void onRefresh() {
        if (mTournamentObservable.get() != null) {
            start(mTournamentObservable.get().getId());
        }
    }

    public String getSnackbarText() {
        return snackbarText.get();
    }

    @Nullable
    protected String getTournamentId() {
        return mTournamentObservable.get().getId();
    }
}
