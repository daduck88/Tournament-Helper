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
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.Nullable;

import com.tournament.helper.R;
import com.tournament.helper.data.Tournament;
import com.tournament.helper.data.source.TournamentsDataSource;
import com.tournament.helper.data.source.TournamentsRepository;

/**
 * ViewModel for the Add/Edit screen.
 * <p>
 * This ViewModel only exposes {@link ObservableField}s, so it doesn't need to extend
 * {@link android.databinding.BaseObservable} and updates are notified automatically. See
 * {@link com.example.android.architecture.blueprints.todoapp.statistics.StatisticsViewModel} for
 * how to deal with more complex scenarios.
 */
public class AddTournamentViewModel implements TournamentsDataSource.GetTournamentCallback {

    public final ObservableField<String> title = new ObservableField<>();

    public final ObservableField<String> description = new ObservableField<>();

    public final ObservableBoolean dataLoading = new ObservableBoolean(false);

    public final ObservableField<String> snackbarText = new ObservableField<>();

    private final TournamentsRepository mTournamentsRepository;

    private final Context mContext;  // To avoid leaks, this must be an Application Context.

    @Nullable
    private String mTournamentId;

    private boolean mIsNewTournament;

    private boolean mIsDataLoaded = false;

    private AddTournamentNavigator mAddTournamentNavigator;

    AddTournamentViewModel(Context context, TournamentsRepository tasksRepository) {
        mContext = context.getApplicationContext(); // Force use of Application Context.
        mTournamentsRepository = tasksRepository;
    }

    void onActivityCreated(AddTournamentNavigator navigator) {
        mAddTournamentNavigator = navigator;
    }

    void onActivityDestroyed() {
        // Clear references to avoid potential memory leaks.
        mAddTournamentNavigator = null;
    }

    public void start(String taskId) {
        if (dataLoading.get()) {
            // Already loading, ignore.
            return;
        }
        mTournamentId = taskId;
        if (taskId == null) {
            // No need to populate, it's a new task
            mIsNewTournament = true;
            return;
        }
        if (mIsDataLoaded) {
            // No need to populate, already have data.
            return;
        }
        mIsNewTournament = false;
        dataLoading.set(true);
        mTournamentsRepository.getTournament(taskId, this);
    }

    @Override
    public void onTournamentLoaded(Tournament task) {
        title.set(task.getTitle());
        description.set(task.getDescription());
        dataLoading.set(false);
        mIsDataLoaded = true;

        // Note that there's no need to notify that the values changed because we're using
        // ObservableFields.
    }

    @Override
    public void onDataNotAvailable() {
        dataLoading.set(false);
    }

    // Called when clicking on fab.
    public void saveTournament() {
        if (isNewTournament()) {
            createTournament(title.get(), description.get());
        } else {
            updateTournament(title.get(), description.get());
        }
    }

    @Nullable
    public String getSnackbarText() {
        return snackbarText.get();
    }

    private boolean isNewTournament() {
        return mIsNewTournament;
    }

    private void createTournament(String title, String description) {
        Tournament newTournament = new Tournament(title, description);
        if (newTournament.isEmpty()) {
            snackbarText.set(mContext.getString(R.string.empty_task_message));
        } else {
            mTournamentsRepository.saveTournament(newTournament);
            navigateOnTournamentSaved();
        }
    }

    private void updateTournament(String title, String description) {
        if (isNewTournament()) {
            throw new RuntimeException("updateTournament() was called but task is new.");
        }
        mTournamentsRepository.saveTournament(new Tournament(title, description, mTournamentId));
        navigateOnTournamentSaved(); // After an edit, go back to the list.
    }

    private void navigateOnTournamentSaved() {
        if (mAddTournamentNavigator != null) {
            mAddTournamentNavigator.onTournamentSaved();
        }
    }
}
