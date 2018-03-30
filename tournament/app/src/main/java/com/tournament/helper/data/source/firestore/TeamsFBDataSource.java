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
import com.tournament.helper.data.source.TeamsDataSource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of a remote data source with static access to the data for easy testing.
 */
public class TeamsFBDataSource implements TeamsDataSource {

    private static final String TAG = "TAG TEAMSFBDataSource";
    private static final String TEAMS = "teams";

    private static TeamsFBDataSource INSTANCE;

    FirebaseFirestore mDB;

    // Prevent direct instantiation.
    private TeamsFBDataSource() {
        mDB = FirebaseFirestore.getInstance();
        mDB.setFirestoreSettings(Injection.provideFirebaseSettings());
    }

    public static TeamsFBDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TeamsFBDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getTeams(@NonNull final LoadTeamsCallback callback) {
        mDB.collection(TEAMS)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    List teams = new ArrayList();
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            if (document.exists()) {
                                // convert document to POJO
                                Team team = document.toObject(Team.class);
                                team.setId(document.getId());
                                teams.add(team);
                            }
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());

                    }
                    callback.onTeamsLoaded(teams);
                }
            });

    }

    @Override
    public void getTeam(@NonNull String TeamId, @NonNull GetTeamCallback callback) {
        //implement
    }

    @Override
    public void saveTeam(@NonNull final Team team, @NonNull final SaveTeamCallback callback) {
        mDB.collection(TEAMS)
            .add(team)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    team.setId(documentReference.getId());
                    callback.onTeamSaved(team);
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error adding document", e);
                }
            });
    }

    public void refreshTeams() {
        // Not required because the {@link TeamsRepository} handles the logic of refreshing the
        // tasks from all the available data sources.
    }

    @VisibleForTesting
    public void addTeams(Team... tasks) {
        if (tasks != null) {
            for (Team task : tasks) {
//                TASKS_SERVICE_DATA.put(task.getId(), task);
                //todo check if it needed to implement
            }
        }
    }
}
