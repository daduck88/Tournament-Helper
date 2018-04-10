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

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.tournament.helper.Injection;
import com.tournament.helper.R;
import com.tournament.helper.ViewModelHolder;
import com.tournament.helper.data.Match;
import com.tournament.helper.databinding.DetailTournamentActBinding;
import com.tournament.helper.detail.draw.DetailTournamentDrawFragment;
import com.tournament.helper.detail.matches.DetailTournamentMatchesFragment;
import com.tournament.helper.detail.matches.dialog.FinishMatchDialog;
import com.tournament.helper.utils.ActivityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays an add or edit task screen.
 */
public class DetailTournamentActivity extends AppCompatActivity implements DetailTournamentNavigator {

  public static final int REQUEST_CODE = 1;

  public static final int ADD_EDIT_RESULT_OK = RESULT_FIRST_USER + 1;

  public static final String ADD_EDIT_VIEW_MODEL_TAG = "ADD_EDIT_VIEW_MODEL_TAG";

  private DetailTournamentViewModel mViewModel;

  private List<Fragment> mFragments;
  private TabLayout mTabLayout;
  private ViewPager mViewPager;

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
  public void saveTournament() {
    mViewModel.saveTournament();
//    setResult(ADD_EDIT_RESULT_OK);
//    finish();
  }

  public void onAddMatchResult(Match match, FinishMatchDialog.FinishMatchListener finishMatchListener) {
    FinishMatchDialog.newInstance().launchAddTeamDialog(getSupportFragmentManager(), finishMatchListener, match);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    DetailTournamentActBinding binding = DataBindingUtil.setContentView(this, R.layout.detail_tournament_act);
    setUpToolBar();
    mViewModel = findOrCreateViewModel();
    binding.setViewmodel(mViewModel);
    mViewModel.onActivityCreated(this);

    initViewPager();
  }

  private void initViewPager() {
    DetailTournamentMatchesFragment detailTournamentMatchesFragment = findOrCreateMatchFragment();
    // Link View and ViewModel
    detailTournamentMatchesFragment.setViewModel(mViewModel);

    DetailTournamentDrawFragment detailTournamentDrawFragment = findOrCreateDrawFragment();
    // Link View and ViewModel
    detailTournamentDrawFragment.setViewModel(mViewModel);
    mFragments = new ArrayList<>();
    mFragments.add(detailTournamentMatchesFragment);
    mFragments.add(detailTournamentDrawFragment);

    mViewPager = findViewById(R.id.vp_fragments_container);
    mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), mFragments));

    mTabLayout = findViewById(R.id.tl_tabs_container);
    mTabLayout.setupWithViewPager(mViewPager);
  }

  private void setUpToolBar() {
    // Set up the toolbar.
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    ActionBar actionBar = getSupportActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setDisplayShowHomeEnabled(true);
  }

  @Override
  protected void onDestroy() {
    mViewModel.onActivityDestroyed();
    super.onDestroy();
  }

  @NonNull
  private DetailTournamentMatchesFragment findOrCreateMatchFragment() {
    // View Fragment
    DetailTournamentMatchesFragment detailTournamentMatchesFragment = DetailTournamentMatchesFragment.newInstance();

    // Send the task ID to the fragment
    Bundle bundle = new Bundle();
    bundle.putString(DetailTournamentMatchesFragment.ARGUMENT_TOURNAMENT_ID,
        getIntent().getStringExtra(DetailTournamentMatchesFragment.ARGUMENT_TOURNAMENT_ID));
    detailTournamentMatchesFragment.setArguments(bundle);
    return detailTournamentMatchesFragment;
  }

  @NonNull
  private DetailTournamentDrawFragment findOrCreateDrawFragment() {
    // View Fragment
    DetailTournamentDrawFragment detailTournamentMatchesFragment = DetailTournamentDrawFragment.newInstance();

    // Send the task ID to the fragment
    Bundle bundle = new Bundle();
    bundle.putString(DetailTournamentMatchesFragment.ARGUMENT_TOURNAMENT_ID,
        getIntent().getStringExtra(DetailTournamentMatchesFragment.ARGUMENT_TOURNAMENT_ID));
    detailTournamentMatchesFragment.setArguments(bundle);
    return detailTournamentMatchesFragment;
  }

  private DetailTournamentViewModel findOrCreateViewModel() {
    // In a configuration change we might have a ViewModel present. It's retained using the
    // Fragment Manager.
    @SuppressWarnings("unchecked")
    ViewModelHolder<DetailTournamentViewModel> retainedViewModel =
        (ViewModelHolder<DetailTournamentViewModel>) getSupportFragmentManager()
            .findFragmentByTag(ADD_EDIT_VIEW_MODEL_TAG);

    if(retainedViewModel != null && retainedViewModel.getViewmodel() != null) {
      // If the model was retained, return it.
      return retainedViewModel.getViewmodel();
    } else {
      // There is no ViewModel yet, create it.
      DetailTournamentViewModel viewModel = new DetailTournamentViewModel(
          Injection.provideTournamentsRepository(getApplicationContext()),
          Injection.provideTeamsRepository());

      // and bind it to this Activity's lifecycle using the Fragment Manager.
      ActivityUtils.addFragmentToActivity(
          getSupportFragmentManager(),
          ViewModelHolder.createContainer(viewModel),
          ADD_EDIT_VIEW_MODEL_TAG);
      return viewModel;
    }
  }
}
