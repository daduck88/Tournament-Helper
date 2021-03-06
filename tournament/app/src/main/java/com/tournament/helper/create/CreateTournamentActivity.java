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

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tournament.helper.Injection;
import com.tournament.helper.R;
import com.tournament.helper.ViewModelHolder;
import com.tournament.helper.create.dialog.AddTeamDialog;
import com.tournament.helper.create.dialog.SelectTeamDialog;
import com.tournament.helper.data.Team;
import com.tournament.helper.utils.ActivityUtils;

import java.util.List;

/**
 * Displays an add or edit task screen.
 */
public class CreateTournamentActivity extends AppCompatActivity implements CreateTournamentNavigator {

  public static final int REQUEST_CODE = 1;

  public static final int ADD_EDIT_RESULT_OK = RESULT_FIRST_USER + 1;

  public static final String ADD_EDIT_VIEW_MODEL_TAG = "ADD_EDIT_VIEW_MODEL_TAG";

  private CreateTournamentViewModel mViewModel;
  private FirebaseAnalytics mFireBaseAnalytics;

  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }

  //    @VisibleForTesting TODO expresso
  //    public IdlingResource getCountingIdlingResource() {
  //        return EspressoIdlingResource.getIdlingResource();
  //    }

  @Override
  public void onTournamentSaved() {
    logTournamentCreated();
    setResult(ADD_EDIT_RESULT_OK);
    finish();
  }

  SelectTeamDialog tempDialog;

  @Override
  public void onSelectTeam(List<Team> teams, SelectTeamDialog.SelectTeamListener selectTeamListener) {
    if(tempDialog == null) {
      tempDialog = SelectTeamDialog.newInstance(teams);
    } else {
      tempDialog.setTeams(teams);
    }
    tempDialog.launchSelectTeamDialog(getSupportFragmentManager(), selectTeamListener);
  }

  @Override
  public void onAddTeam(AddTeamDialog.AddTeamListener addTeamListener) {
    AddTeamDialog.newInstance().launchAddTeamDialog(getSupportFragmentManager(), addTeamListener);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.create_tournament_act);

    // Set up the toolbar.
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    ActionBar actionBar = getSupportActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setDisplayShowHomeEnabled(true);

    CreateTournamentFragment createTournamentFragment = findOrCreateViewFragment();

    mViewModel = findOrCreateViewModel();

    // Link View and ViewModel
    createTournamentFragment.setViewModel(mViewModel);

    mViewModel.onActivityCreated(this);
    mFireBaseAnalytics = FirebaseAnalytics.getInstance(this);
  }

  @Override
  protected void onDestroy() {
    mViewModel.onActivityDestroyed();
    super.onDestroy();
  }

  @NonNull
  private CreateTournamentFragment findOrCreateViewFragment() {
    // View Fragment
    CreateTournamentFragment createTournamentFragment = (CreateTournamentFragment) getSupportFragmentManager()
        .findFragmentById(R.id.contentFrame);

    if(createTournamentFragment == null) {
      createTournamentFragment = CreateTournamentFragment.newInstance();

      // Send the task ID to the fragment
      Bundle bundle = new Bundle();
      bundle.putString(CreateTournamentFragment.ARGUMENT_EDIT_TASK_ID,
          getIntent().getStringExtra(CreateTournamentFragment.ARGUMENT_EDIT_TASK_ID));
      createTournamentFragment.setArguments(bundle);

      ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
          createTournamentFragment, R.id.contentFrame);
    }
    return createTournamentFragment;
  }

  private CreateTournamentViewModel findOrCreateViewModel() {
    // In a configuration change we might have a ViewModel present. It's retained using the
    // Fragment Manager.
    @SuppressWarnings("unchecked")
    ViewModelHolder<CreateTournamentViewModel> retainedViewModel =
        (ViewModelHolder<CreateTournamentViewModel>) getSupportFragmentManager()
            .findFragmentByTag(ADD_EDIT_VIEW_MODEL_TAG);

    if(retainedViewModel != null && retainedViewModel.getViewmodel() != null) {
      // If the model was retained, return it.
      return retainedViewModel.getViewmodel();
    } else {
      // There is no ViewModel yet, create it.
      CreateTournamentViewModel viewModel = new CreateTournamentViewModel(
          Injection.provideTournamentsRepository(getApplicationContext()));

      // and bind it to this Activity's lifecycle using the Fragment Manager.
      ActivityUtils.addFragmentToActivity(
          getSupportFragmentManager(),
          ViewModelHolder.createContainer(viewModel),
          ADD_EDIT_VIEW_MODEL_TAG);
      return viewModel;
    }
  }

  private void logTournamentCreated() {
    Bundle bundle = new Bundle();
    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, mViewModel.getTournamentId());
    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "tournament");
    mFireBaseAnalytics.logEvent("tournament_created", bundle);
  }
}
