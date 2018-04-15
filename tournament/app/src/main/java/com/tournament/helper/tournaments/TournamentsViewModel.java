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

package com.tournament.helper.tournaments;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.graphics.drawable.Drawable;

import com.tournament.helper.BR;
import com.tournament.helper.R;
import com.tournament.helper.THApp;
import com.tournament.helper.create.CreateTournamentActivity;
import com.tournament.helper.data.Tournament;
import com.tournament.helper.data.source.TournamentsDataSource;
import com.tournament.helper.data.source.TournamentsRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Exposes the data to be used in the tournament list screen.
 * <p>
 * {@link BaseObservable} implements a listener registration mechanism which is notified when a
 * property changes. This is done by assigning a {@link Bindable} annotation to the property's
 * getter method.
 */
public class TournamentsViewModel extends BaseObservable {

    // These observable fields will update Views automatically
    public final ObservableList<Tournament> items = new ObservableArrayList<>();

    public final ObservableBoolean dataLoading = new ObservableBoolean(false);

    public final ObservableField<String> noTournamentsLabel = new ObservableField<>();

    public final ObservableField<Drawable> noTournamentIconRes = new ObservableField<>();

    public final ObservableBoolean tournamentsAddViewVisible = new ObservableBoolean();

    final ObservableField<String> snackbarText = new ObservableField<>();

    private final TournamentsRepository mTournamentsRepository;

    private final ObservableBoolean mIsDataLoadingError = new ObservableBoolean(false);

    private TournamentsNavigator mNavigator;

    public TournamentsViewModel(
            TournamentsRepository repository) {
        mTournamentsRepository = repository;

        // Set initial state
//        setFiltering(TournamentsFilterType.ALL_TASKS);
    }

    void setNavigator(TournamentsNavigator navigator) {
        mNavigator = navigator;
    }

    void onActivityDestroyed() {
        // Clear references to avoid potential memory leaks.
        mNavigator = null;
    }

    public void start() {
        loadTournaments(false);
    }

    @Bindable
    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void loadTournaments(boolean forceUpdate) {
        loadTournaments(forceUpdate, true);
    }

    public String getSnackbarText() {
        return snackbarText.get();
    }

    /**
     * Called by the Data Binding library and the FAB's click listener.
     */
    public void addNewTournament() {
        if (mNavigator != null) {
            mNavigator.addNewTournament();
        }
    }

    void handleActivityResult(int requestCode, int resultCode) {
        if (CreateTournamentActivity.REQUEST_CODE == requestCode) {
            switch (resultCode) {
//                case TournamentDetailActivity.EDIT_RESULT_OK:
//                    snackbarText.set(
//                            mContext.getString(R.string.successfully_saved_tournament_message));
//                    break;
                case CreateTournamentActivity.ADD_EDIT_RESULT_OK:
                    snackbarText.set(THApp.context.getString(R.string.completed_tournament_cleared));
                    break;
//                case TournamentDetailActivity.DELETE_RESULT_OK:
//                    snackbarText.set(
//                            mContext.getString(R.string.successfully_deleted_tournament_message));
//                    break;
            }
        }
    }

    /**
     * @param forceUpdate   Pass in true to refresh the data in the {@link TournamentsDataSource}
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    private void loadTournaments(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            dataLoading.set(true);
        }
        if (forceUpdate) {

            mTournamentsRepository.refreshTournaments();
        }

        // The network request might be handled in a different thread so make sure Espresso knows
        // that the app is busy until the response is handled.
//        EspressoIdlingResource.increment(); // App is busy until further notice

        mTournamentsRepository.getTournaments(new TournamentsDataSource.LoadTournamentsCallback() {
            @Override
            public void onTournamentsLoaded(List<Tournament> tournaments) {
                List<Tournament> tournamentsToShow = new ArrayList<Tournament>();

                // This callback may be called twice, once for the cache and once for loading
                // the data from the server API, so we check before decrementing, otherwise
                // it throws "Counter has been corrupted!" exception.
//                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
//                    EspressoIdlingResource.decrement(); // Set app as idle.
//                }
//
//                // We filter the tournaments based on the requestType
//                for (Tournament tournament : tournaments) {
//                    switch (mCurrentFiltering) {
//                        case ALL_TASKS:
//                            tournamentsToShow.add(tournament);
//                            break;
//                        case ACTIVE_TASKS:
//                            if (tournament.isActive()) {
//                                tournamentsToShow.add(tournament);
//                            }
//                            break;
//                        case COMPLETED_TASKS:
//                            if (tournament.isCompleted()) {
//                                tournamentsToShow.add(tournament);
//                            }
//                            break;
//                        default:
//                            tournamentsToShow.add(tournament);
//                            break;
//                    }
//                }
                if (showLoadingUI) {
                    dataLoading.set(false);
                }
                mIsDataLoadingError.set(false);

                items.clear();
                items.addAll(tournaments);
                notifyPropertyChanged(BR.empty); // It's a @Bindable so update manually
            }

            @Override
            public void onDataNotAvailable() {
                mIsDataLoadingError.set(true);
            }
        });
    }

}
