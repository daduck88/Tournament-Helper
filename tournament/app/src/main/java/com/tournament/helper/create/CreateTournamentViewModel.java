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
import android.text.TextUtils;

import com.tournament.helper.data.Team;
import com.tournament.helper.data.Tournament;
import com.tournament.helper.data.source.TeamsRepository;
import com.tournament.helper.data.source.TournamentsDataSource;
import com.tournament.helper.data.source.TournamentsRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel for the Create screen.
 * <p>
 * This ViewModel only exposes {@link ObservableField}s, so it doesn't need to extend
 * {@link android.databinding.BaseObservable} and updates are notified automatically.
 */
public class CreateTournamentViewModel implements TournamentsDataSource.GetTournamentCallback {

  public final ObservableField<String> title = new ObservableField<>();

  public final ObservableBoolean dataLoading = new ObservableBoolean(false);

  public final ObservableField<String> snackbarText = new ObservableField<>();

  private final TournamentsRepository mTournamentsRepository;

  List<Team> selectedTeams = new ArrayList<>();

  @Nullable
  private String mTournamentId;

  private boolean mIsNewTournament;

  private boolean mIsDataLoaded = false;

  private AddTournamentNavigator mAddTournamentNavigator;

  CreateTournamentViewModel(TournamentsRepository tournamentsRepository) {
    mTournamentsRepository = tournamentsRepository;
  }

  void onActivityCreated(AddTournamentNavigator navigator) {
    mAddTournamentNavigator = navigator;
  }

  void onActivityDestroyed() {
    // Clear references to avoid potential memory leaks.
    mAddTournamentNavigator = null;
  }

  public void start(String taskId) {
    if(dataLoading.get()) {
      // Already loading, ignore.
      return;
    }
    mTournamentId = taskId;
    if(taskId == null) {
      // No need to populate, it's a new task
      mIsNewTournament = true;
      return;
    }
    if(mIsDataLoaded) {
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
    dataLoading.set(false);
    mIsDataLoaded = true;

    // Note that there's no need to notify that the values changed because we're using
    // ObservableFields.
  }

  @Override
  public void onDataNotAvailable() {
    dataLoading.set(false);
  }

  // Called when clicking menu "create".
  public void saveTournament() {
    if(isTournamentReady()) {
      if(isNewTournament()) {
        createTournament(title.get(), selectedTeams);
      } else {
        updateTournament(title.get(), selectedTeams);
      }
    }
  }

  private boolean isTournamentReady() {
    if(TextUtils.isEmpty(title.get())) {
      snackbarText.set("empty Title temp");
      return false;
    }
    if(selectedTeams.size() != 8) {
      snackbarText.set("Select all the 8 teams temp");
      return false;
    }
    return true;
  }

  @Nullable
  public String getSnackbarText() {
    return snackbarText.get();
  }

  private boolean isNewTournament() {
    return mIsNewTournament;
  }

  private void createTournament(String title, List<Team> teamList) {
    Tournament newTournament = new Tournament(title, teamList);
    mTournamentsRepository.saveTournament(newTournament, mSaveTournamentCallback);
  }

  private void updateTournament(String title, List<Team> teamList) {
    //        TODO check to let update tournament
    if(isNewTournament()) {
      throw new RuntimeException("updateTournament() was called but task is new.");
    }
    //        mTournamentsRepository.updateTournament(new Tournament(title, description, mTournamentId));
    navigateOnTournamentSaved(); // After an edit, go back to the list.
  }

  private void navigateOnTournamentSaved() {
    if(mAddTournamentNavigator != null) {
      mAddTournamentNavigator.onTournamentSaved();
    }
  }

  public List<Team> getSelectedTeams() {
    return selectedTeams;
  }

  private TournamentsDataSource.SaveTournamentCallback mSaveTournamentCallback = new TournamentsDataSource.SaveTournamentCallback() {
    @Override
    public void onTournamentSaved(Tournament tournament) {
      navigateOnTournamentSaved();
    }

    @Override
    public void onSaveNotAvailable() {
      //Check what to do
    }
  };
}
