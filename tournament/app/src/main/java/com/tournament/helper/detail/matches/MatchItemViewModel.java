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

package com.tournament.helper.detail.matches;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.tournament.helper.MatchViewModel;
import com.tournament.helper.data.Match;
import com.tournament.helper.data.source.TeamsRepository;
import com.tournament.helper.detail.DetailTournamentNavigator;
import com.tournament.helper.detail.matches.dialog.FinishMatchDialog;

import java.lang.ref.WeakReference;

/**
 * Abstract class for View Models that expose a single {@link Match}.
 */
public class MatchItemViewModel extends MatchViewModel {

  // This navigator is s wrapped in a WeakReference to avoid leaks because it has references to an
  // activity. There's no straightforward way to clear it for each item in a list adapter.
  @Nullable
  private WeakReference<DetailTournamentNavigator> mNavigator;

  public MatchItemViewModel(TeamsRepository teamsRepository) {
    super(teamsRepository);
  }

  public void setNavigator(DetailTournamentNavigator navigator) {
    mNavigator = new WeakReference<>(navigator);
  }

  /**
   * Called by the Data Binding library when the row is clicked.
   */
  public void matchClicked() {
    if(mMatch == null) {
      // Click happened before task was loaded, no-op.
      return;
    }
    if(mNavigator != null && mNavigator.get() != null && TextUtils.isEmpty(mMatch.getWinnerId())) {
      mNavigator.get().onAddMatchResult(mMatch, new FinishMatchDialog.FinishMatchListener() {
        @Override
        public void onFinishMatch(String teamId) {
          setWinner(teamId);
          mNavigator.get().saveTournament();
        }
      });
    }
  }
}
