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

import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tournament.helper.Injection;
import com.tournament.helper.R;
import com.tournament.helper.data.Match;
import com.tournament.helper.databinding.DetailTournamentFragBinding;
import com.tournament.helper.detail.DetailTournamentActivity;
import com.tournament.helper.detail.DetailTournamentViewModel;
import com.tournament.helper.utils.SnackbarUtils;

import java.util.ArrayList;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Main UI for the add task screen. Users can enter a task team1Name and team2Name.
 */
public class DetailTournamentMatchesFragment extends Fragment {

  public static final String ARGUMENT_TOURNAMENT_ID = "TOURNAMENT_ID";

  private DetailTournamentViewModel mViewModel;

  private DetailTournamentFragBinding mViewDataBinding;

  private Observable.OnPropertyChangedCallback mSnackbarCallback;

  public static DetailTournamentMatchesFragment newInstance() {
    return new DetailTournamentMatchesFragment();
  }

  public DetailTournamentMatchesFragment() {
    // Required empty public constructor
  }

  @Override
  public void onResume() {
    super.onResume();
    if(getArguments() != null) {
      mViewModel.start(getArguments().getString(ARGUMENT_TOURNAMENT_ID));
    } else {
      mViewModel.start(null);
    }
  }

  public void setViewModel(@NonNull DetailTournamentViewModel viewModel) {
    mViewModel = checkNotNull(viewModel);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    setHasOptionsMenu(true);

    setupListAdapter();

    setupSnackbar();

    setupActionBar();
  }

  private void setupListAdapter() {

    RecyclerView listView = mViewDataBinding.matchesList;
    listView.setLayoutManager(new LinearLayoutManager(getActivity()));
    // init selectTeam list
    listView.setAdapter(new MatchesAdapter(
        new ArrayList<Match>(),
        mViewModel,
        Injection.provideTeamsRepository(),
        (DetailTournamentActivity) getActivity()));//navigator
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    final View root = inflater.inflate(R.layout.detail_tournament_frag, container, false);
    if(mViewDataBinding == null) {
      mViewDataBinding = DetailTournamentFragBinding.bind(root);
    }

    mViewDataBinding.setViewmodel(mViewModel);

    setHasOptionsMenu(true);
    setRetainInstance(true);

    return mViewDataBinding.getRoot();
  }

  @Override
  public void onDestroy() {
    if(mSnackbarCallback != null) {
      mViewModel.snackbarText.removeOnPropertyChangedCallback(mSnackbarCallback);
    }
    super.onDestroy();
  }

  private void setupSnackbar() {
    mSnackbarCallback = new Observable.OnPropertyChangedCallback() {
      @Override
      public void onPropertyChanged(Observable observable, int i) {
        if(!TextUtils.isEmpty(mViewModel.getSnackbarText())) {
          SnackbarUtils.showSnackbar(getView(), mViewModel.getSnackbarText(), new SnackbarUtils.UtilsCallback() {
            @Override
            public void onDismissed() {
              mViewModel.snackbarText.set(null);
            }
          });
        }
      }
    };
    mViewModel.snackbarText.addOnPropertyChangedCallback(mSnackbarCallback);
  }

  private void setupActionBar() {
    ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
    if(actionBar == null) {
      return;
    }
  }
}
