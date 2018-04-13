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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.tournament.helper.Injection;
import com.tournament.helper.R;
import com.tournament.helper.data.helper.SelectTeam;
import com.tournament.helper.databinding.CreateTournamentFragBinding;
import com.tournament.helper.utils.SnackbarUtils;

import java.util.ArrayList;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Main UI for the add task screen. Users can enter a task team1Name and team2Name.
 */
public class CreateTournamentFragment extends Fragment {

  public static final String ARGUMENT_EDIT_TASK_ID = "EDIT_TASK_ID";

  private CreateTournamentViewModel mViewModel;

  private CreateTournamentFragBinding mViewDataBinding;

  private Observable.OnPropertyChangedCallback mSnackbarCallback;

  public static CreateTournamentFragment newInstance() {
    return new CreateTournamentFragment();
  }

  public CreateTournamentFragment() {
    // Required empty public constructor
  }

  @Override
  public void onResume() {
    super.onResume();
  }

  public void setViewModel(@NonNull CreateTournamentViewModel viewModel) {
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
    RecyclerView listView = mViewDataBinding.createTournamentTeams;
    listView.setLayoutManager(new LinearLayoutManager(getActivity()));
    // init selectTeam list
    ArrayList<SelectTeam> selectTeams = new ArrayList<>();
    for(int count = 0; count < 8; count++) {
      selectTeams.add(new SelectTeam());
    }
    listView.setAdapter(new CreateTournamentTeamsAdapter(
        selectTeams,
        mViewModel,
        Injection.provideTeamsRepository(),
        (CreateTournamentActivity) getActivity()));//navigator
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    final View root = inflater.inflate(R.layout.create_tournament_frag, container, false);
    if(mViewDataBinding == null) {
      mViewDataBinding = CreateTournamentFragBinding.bind(root);
    }

    mViewDataBinding.setViewmodel(mViewModel);

    setHasOptionsMenu(true);
    setRetainInstance(false);

    return mViewDataBinding.getRoot();
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.create_tournament_menu, menu);
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch(item.getItemId()) {
      case R.id.menu_create:
        mViewModel.saveTournament();
        return true;
    }
    return super.onOptionsItemSelected(item);
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
    //        if (getArguments().get(ARGUMENT_TOURNAMENT_ID) != null) {
    //            actionBar.setTitle(R.string.edit_task);
    //        } else {
    //            actionBar.setTitle(R.string.add_task);
    //        }
  }
}
