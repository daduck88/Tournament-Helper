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

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.tournament.helper.Injection;
import com.tournament.helper.R;
import com.tournament.helper.ViewModelHolder;
import com.tournament.helper.create.CreateTournamentActivity;
import com.tournament.helper.detail.DetailTournamentActivity;
import com.tournament.helper.detail.matches.DetailTournamentMatchesFragment;
import com.tournament.helper.utils.ActivityUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

public class TournamentsActivity extends AppCompatActivity implements TournamentItemNavigator, TournamentsNavigator {

  private DrawerLayout mDrawerLayout;

  public static final String TASKS_VIEWMODEL_TAG = "TASKS_VIEWMODEL_TAG";

  private TournamentsViewModel mViewModel;
  private InterstitialAd mInterstitialAd;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.tournaments_act);

    setupToolbar();

    setupNavigationDrawer();

    TournamentsFragment tournamentsFragment = findOrCreateViewFragment();

    mViewModel = findOrCreateViewModel();
    mViewModel.setNavigator(this);

    // Link View and ViewModel
    tournamentsFragment.setViewModel(mViewModel);
    initInterstitialAd();
  }

  private void initInterstitialAd() {
    mInterstitialAd = new InterstitialAd(this);
    mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
    mInterstitialAd.setAdListener(new AdListener() {
      @Override
      public void onAdLoaded() {}

      @Override
      public void onAdFailedToLoad(int errorCode) {}

      @Override
      public void onAdOpened() {}

      @Override
      public void onAdLeftApplication() { }
      @Override
      public void onAdClosed() {
        navigateToCreateTournament();
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
      }
    });
    mInterstitialAd.loadAd(new AdRequest.Builder().build());
  }

  @Override
  protected void onDestroy() {
    mViewModel.onActivityDestroyed();
    super.onDestroy();
  }

  private TournamentsViewModel findOrCreateViewModel() {
    // In a configuration change we might have a ViewModel present. It's retained using the
    // Fragment Manager.
    @SuppressWarnings("unchecked")
    ViewModelHolder<TournamentsViewModel> retainedViewModel =
        (ViewModelHolder<TournamentsViewModel>) getSupportFragmentManager()
            .findFragmentByTag(TASKS_VIEWMODEL_TAG);

    if(retainedViewModel != null && retainedViewModel.getViewmodel() != null) {
      // If the model was retained, return it.
      return retainedViewModel.getViewmodel();
    } else {
      // There is no ViewModel yet, create it.
      TournamentsViewModel viewModel = new TournamentsViewModel(
          Injection.provideTournamentsRepository(getApplicationContext()));
      // and bind it to this Activity's lifecycle using the Fragment Manager.
      ActivityUtils.addFragmentToActivity(
          getSupportFragmentManager(),
          ViewModelHolder.createContainer(viewModel),
          TASKS_VIEWMODEL_TAG);
      return viewModel;
    }
  }

  @NonNull
  private TournamentsFragment findOrCreateViewFragment() {
    TournamentsFragment tournamentsFragment =
        (TournamentsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
    if(tournamentsFragment == null) {
      // Create the fragment
      tournamentsFragment = TournamentsFragment.newInstance();
      ActivityUtils.addFragmentToActivity(
          getSupportFragmentManager(), tournamentsFragment, R.id.contentFrame);
    }
    return tournamentsFragment;
  }

  private void setupToolbar() {
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    ActionBar ab = getSupportActionBar();
    //        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
//    ab.setDisplayHomeAsUpEnabled(true);
  }

  private void setupNavigationDrawer() {
    mDrawerLayout = findViewById(R.id.drawer_layout);
    mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
    NavigationView navigationView = findViewById(R.id.nav_view);
    if(navigationView != null) {
      setupDrawerContent(navigationView);
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch(item.getItemId()) {
      case android.R.id.home:
        // Open the navigation drawer when the home icon is selected from the toolbar.
        mDrawerLayout.openDrawer(GravityCompat.START);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void setupDrawerContent(NavigationView navigationView) {
    navigationView.setNavigationItemSelectedListener(
        new NavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(MenuItem menuItem) {
            switch(menuItem.getItemId()) {
              case R.id.list_navigation_menu_item:
                // Do nothing, we're already on that screen
                break;
              case R.id.statistics_navigation_menu_item:
                //                                Intent intent =
                //                                        new Intent(TournamentsActivity.this, StatisticsActivity.class);
                //                                startActivity(intent);
                //TODO REMOVE THIS?
                break;
              default:
                break;
            }
            // Close the navigation drawer when an item is selected.
            menuItem.setChecked(true);
            mDrawerLayout.closeDrawers();
            return true;
          }
        });
  }

  //    @VisibleForTesting todo idling resource for testing
  //    public IdlingResource getCountingIdlingResource() {
  //        return EspressoIdlingResource.getIdlingResource();
  //    }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    mViewModel.handleActivityResult(requestCode, resultCode);
  }

  @Override
  public void openTournamentDetails(String tournamentId) {
    Intent intent = new Intent(this, DetailTournamentActivity.class);
    intent.putExtra(DetailTournamentMatchesFragment.ARGUMENT_TOURNAMENT_ID, tournamentId);
    startActivityForResult(intent, DetailTournamentActivity.REQUEST_CODE);
  }

  @Override
  public void addNewTournament() {
    if(mInterstitialAd.isLoaded()) {
      mInterstitialAd.show();
    } else {
      navigateToCreateTournament();
    }
  }

  private void navigateToCreateTournament() {
    Intent intent = new Intent(this, CreateTournamentActivity.class);
    startActivityForResult(intent, CreateTournamentActivity.REQUEST_CODE);
  }
}
