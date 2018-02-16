/*
 * Copyright 2017, The Android Open Source Project
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

package com.tournament.helper.data.source.local;
import android.content.Context;

import com.tournament.helper.data.Tournament;

/**
 * The Room Database that contains the Tournament table.
 */
//@Database(entities = {Tournament.class}, version = 1)
public abstract class ToDoDatabase
//    extends RoomDatabase TODO replace with firBase
{

    private static ToDoDatabase INSTANCE;

//    public abstract TournamentsDao taskDao();

    private static final Object sLock = new Object();

    public static ToDoDatabase getInstance(Context context) {
        synchronized (sLock) {
//            if (INSTANCE == null) {
//                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
//                        ToDoDatabase.class, "Tournaments.db")
//                        .build();
//            }
            return INSTANCE;
        }
    }

}
