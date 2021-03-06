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

package com.tournament.helper.detail;

import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.Nullable;

import com.tournament.helper.data.Match;
import com.tournament.helper.data.Tournament;
import com.tournament.helper.data.source.TeamsRepository;
import com.tournament.helper.data.source.TournamentsDataSource;
import com.tournament.helper.data.source.TournamentsRepository;
import com.tournament.helper.utils.DataHelpers.TournamentHelper;

/**
 * ViewModel for the Create screen.
 * <p>
 * This ViewModel only exposes {@link ObservableField}s, so it doesn't need to extend
 * {@link android.databinding.BaseObservable} and updates are notified automatically.
 */
public class DetailTournamentViewModel extends BaseObservable implements TournamentsDataSource.GetTournamentCallback {

  public final ObservableBoolean dataLoading = new ObservableBoolean(false);

  public final ObservableField<String> snackbarText = new ObservableField<>();

  private final TournamentsRepository mTournamentsRepository;

  public final ObservableList<Match> items = new ObservableArrayList<>();

  private Tournament mTournament;


  public final ObservableList<String> teams = new ObservableArrayList<>();
  public final ObservableField<String> winner = new ObservableField<>();

  @Nullable
  private String mTournamentId;

  private boolean mIsDataLoaded = false;

  private DetailTournamentNavigator mDetailTournamentNavigator;

  DetailTournamentViewModel(TournamentsRepository tournamentsRepository) {
    mTournamentsRepository = tournamentsRepository;
  }

  void onActivityCreated(DetailTournamentNavigator navigator) {
    mDetailTournamentNavigator = navigator;
  }

  void onActivityDestroyed() {
    // Clear references to avoid potential memory leaks.
    mDetailTournamentNavigator = null;
  }

  public void start(String tournamentId) {
    if(dataLoading.get()) {
      // Already loading, ignore.
      return;
    }
    mTournamentId = tournamentId;
    if(mIsDataLoaded) {
      // No need to populate, already have data.
      return;
    }
    dataLoading.set(true);
    mTournamentsRepository.getTournament(mTournamentId, this);
  }

  @Override
  public void onTournamentLoaded(Tournament tournament) {
    mTournament = tournament;
    TournamentHelper.prepareTournamentRound(mTournament);
    //todo check how to remove and add this fields it looks odd
    items.clear();
    items.addAll(TournamentHelper.getAllMatch(mTournament));

    teams.addAll(mTournament.getTeamsId());
    winner.set(mTournament.getWinnerId());
    dataLoading.set(false);
    mIsDataLoaded = true;
  }

  @Override
  public void onDataNotAvailable() {
    dataLoading.set(false);
  }

  public void saveTournament() {
    updateTournament();
  }

  @Nullable
  public String getSnackbarText() {
    return snackbarText.get();
  }

  private void updateTournament() {
    mTournamentsRepository.updateTournament(mTournament, mUpdateTournamentCallback);
  }

  private TournamentsDataSource.SaveTournamentCallback mUpdateTournamentCallback = new TournamentsDataSource.SaveTournamentCallback() {
    @Override
    public void onTournamentSaved(Tournament tournament) {
      onTournamentLoaded(tournament);
    }

    @Override
    public void onSaveNotAvailable() {
      //Check what to do
    }
  };
}
