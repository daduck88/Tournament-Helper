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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.tournament.helper.Injection;
import com.tournament.helper.R;
import com.tournament.helper.ViewModelHolder;
import com.tournament.helper.utils.ActivityUtils;

/**
 * Displays an add or edit task screen.
 */
public class AddTournamentActivity extends AppCompatActivity implements AddTournamentNavigator {

    public static final int REQUEST_CODE = 1;

    public static final int ADD_EDIT_RESULT_OK = RESULT_FIRST_USER + 1;

    public static final String ADD_EDIT_VIEWMODEL_TAG = "ADD_EDIT_VIEWMODEL_TAG";

    private AddTournamentViewModel mViewModel;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

//    @VisibleForTesting TODO expreso
//    public IdlingResource getCountingIdlingResource() {
//        return EspressoIdlingResource.getIdlingResource();
//    }

    @Override
    public void onTournamentSaved() {
        setResult(ADD_EDIT_RESULT_OK);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_tournament_act);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        AddTournamentFragment addTournamentFragment = findOrCreateViewFragment();

        mViewModel = findOrCreateViewModel();

        // Link View and ViewModel
        addTournamentFragment.setViewModel(mViewModel);

        mViewModel.onActivityCreated(this);
    }

    @Override
    protected void onDestroy() {
        mViewModel.onActivityDestroyed();
        super.onDestroy();
    }

    @NonNull
    private AddTournamentFragment findOrCreateViewFragment() {
        // View Fragment
        AddTournamentFragment addTournamentFragment = (AddTournamentFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (addTournamentFragment == null) {
            addTournamentFragment = AddTournamentFragment.newInstance();

            // Send the task ID to the fragment
            Bundle bundle = new Bundle();
            bundle.putString(AddTournamentFragment.ARGUMENT_EDIT_TASK_ID,
                    getIntent().getStringExtra(AddTournamentFragment.ARGUMENT_EDIT_TASK_ID));
            addTournamentFragment.setArguments(bundle);

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                addTournamentFragment, R.id.contentFrame);
        }
        return addTournamentFragment;
    }

    private AddTournamentViewModel findOrCreateViewModel() {
        // In a configuration change we might have a ViewModel present. It's retained using the
        // Fragment Manager.
        @SuppressWarnings("unchecked")
        ViewModelHolder<AddTournamentViewModel> retainedViewModel =
                (ViewModelHolder<AddTournamentViewModel>) getSupportFragmentManager()
                        .findFragmentByTag(ADD_EDIT_VIEWMODEL_TAG);

        if (retainedViewModel != null && retainedViewModel.getViewmodel() != null) {
            // If the model was retained, return it.
            return retainedViewModel.getViewmodel();
        } else {
            // There is no ViewModel yet, create it.
            AddTournamentViewModel viewModel = new AddTournamentViewModel(
                    getApplicationContext(),
                    Injection.provideTournamentsRepository(getApplicationContext())
            );

            // and bind it to this Activity's lifecycle using the Fragment Manager.
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    ViewModelHolder.createContainer(viewModel),
                    ADD_EDIT_VIEWMODEL_TAG);
            return viewModel;
        }
    }
}
