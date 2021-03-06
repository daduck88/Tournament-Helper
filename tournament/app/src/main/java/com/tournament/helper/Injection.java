/*
 * Copyright (C) 2015 The Android Open Source Project
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
import android.support.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.tournament.helper.data.source.TeamsRepository;
import com.tournament.helper.data.source.TournamentsDataSource;
import com.tournament.helper.data.source.TournamentsRepository;
import com.tournament.helper.data.source.firestore.TeamsFBDataSource;
import com.tournament.helper.data.source.firestore.TournamentsFBDataSource;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * TODO check this doc
 * Enables injection of mock implementations for
 * {@link TournamentsDataSource} at compile time. This is useful for testing, since it allows us to use
 * a fake instance of the class to isolate the dependencies and run a test hermetically.
 */
public class Injection {

  public static TournamentsRepository provideTournamentsRepository(@NonNull Context context) {
    checkNotNull(context);
    return TournamentsRepository.getInstance(TournamentsFBDataSource.getInstance());
  }

  public static TeamsRepository provideTeamsRepository() {
    return TeamsRepository.getInstance(TeamsFBDataSource.getInstance());
  }

  private static FirebaseFirestoreSettings settings;

  public static FirebaseFirestoreSettings provideFirebaseSettings() {
    if(settings == null) {
      settings = new FirebaseFirestoreSettings.Builder()
          .setPersistenceEnabled(true)
          .build();
    }
    return settings;
  }
}
