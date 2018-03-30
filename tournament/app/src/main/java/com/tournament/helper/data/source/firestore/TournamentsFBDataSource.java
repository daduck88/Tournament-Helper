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

package com.tournament.helper.data.source.firestore;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Lists;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.tournament.helper.Injection;
import com.tournament.helper.data.Team;
import com.tournament.helper.data.Tournament;
import com.tournament.helper.data.source.TeamsDataSource;
import com.tournament.helper.data.source.TournamentsDataSource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Implementation of a remote data source with static access to the data for easy testing.
 */
public class TournamentsFBDataSource implements TournamentsDataSource {

    private static final String TOURNAMENTS = "tournaments";

    private static TournamentsFBDataSource INSTANCE;

    private FirebaseFirestore mDB;

    // Prevent direct instantiation.
    private TournamentsFBDataSource() {
        //TODO check if this parameter should be received
        mDB = FirebaseFirestore.getInstance();
        mDB.setFirestoreSettings(Injection.provideFirebaseSettings());
    }

    public static TournamentsFBDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TournamentsFBDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getTournaments(@NonNull final LoadTournamentsCallback callback) {
        mDB.collection(TOURNAMENTS)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    List tournaments = new ArrayList();
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            if (document.exists()) {
                                // convert document to POJO
                                Tournament tournament = document.toObject(Tournament.class);
                                tournament.setId(document.getId());
                                tournaments.add(tournament);
                            }
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());

                    }
                    callback.onTournamentsLoaded(tournaments);
                }
            });
    }

    @Override
    public void getTournament(@NonNull String taskId, @NonNull GetTournamentCallback callback) {
//        Tournament task = TASKS_SERVICE_DATA.get(taskId);TODO implement
        callback.onTournamentLoaded(null);
    }

    @Override
    public void saveTournament(@NonNull final Tournament tournament, @NonNull final SaveTournamentCallback callback) {
        mDB.collection(TOURNAMENTS)
            .add(tournament.getMap())
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    tournament.setId(documentReference.getId());
                    callback.onTournamentSaved(tournament);
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error adding document", e);
                }
            });
    }

    @Override
    public void updateTournament(@NonNull final Tournament tournament, @NonNull final SaveTournamentCallback callback) {
        //todo check this
        mDB.collection(TOURNAMENTS)
            .document(tournament.getId())
            .set(tournament.getMap())
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    callback.onSaveNotAvailable();
                    Log.w(TAG, "Error adding document", e);
                }
            });
    }

    @Override
    public void completeTournament(@NonNull Tournament task) {
//        Tournament completedTournament = new Tournament(task.getTitle(), task.getDescription(), task.getId(), true);
//        TASKS_SERVICE_DATA.put(task.getId(), task);
    }

    @Override
    public void completeTournament(@NonNull String taskId) {
        // Not required for the remote data source.
    }

    @Override
    public void activateTournament(@NonNull Tournament task) {
//        Tournament activeTournament = new Tournament(task.getTitle(), task.getDescription(), task.getId());
//        TASKS_SERVICE_DATA.put(task.getId(), task);
    }

    @Override
    public void activateTournament(@NonNull String taskId) {
        // Not required for the remote data source.
    }

    @Override
    public void clearCompletedTournaments() {
//        Iterator<Map.Entry<String, Tournament>> it = TASKS_SERVICE_DATA.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry<String, Tournament> entry = it.next();
//            if (false) {
//                it.remove();
//            }
//        }
        //todo check this
    }

    public void refreshTournaments() {
        // Not required because the {@link TournamentsRepository} handles the logic of refreshing the
        // tasks from all the available data sources.
    }

    @Override
    public void deleteTournament(@NonNull String taskId) {
//        TASKS_SERVICE_DATA.remove(taskId);
        //todo check delete if needed
    }

    @Override
    public void deleteAllTournaments() {
//        TASKS_SERVICE_DATA.clear();
        //todo also this
    }

    @VisibleForTesting
    public void addTournaments(Tournament... tasks) {
        if (tasks != null) {
            for (Tournament task : tasks) {
                //todo check this too
//                TASKS_SERVICE_DATA.put(task.getId(), task);
            }
        }
    }
}
