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

import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import com.tournament.helper.ScrollChildSwipeRefreshLayout;
import com.tournament.helper.data.Tournament;
import com.tournament.helper.databinding.TournamentsFragBinding;
import com.tournament.helper.utils.SnackbarUtils;

import java.util.ArrayList;

/**
 * Display a grid of {@link Tournament}s. User can choose to view all, active or completed tasks.
 */
public class TournamentsFragment extends Fragment {

  private TournamentsViewModel mTournamentsViewModel;

  private TournamentsFragBinding mTournamentsFragBinding;

  private TournamentsAdapter mListAdapter;

  private Observable.OnPropertyChangedCallback mSnackbarCallback;

  public TournamentsFragment() {
    // Requires empty public constructor
  }

  public static TournamentsFragment newInstance() {
    return new TournamentsFragment();
  }

  @Override
  public void onResume() {
    super.onResume();
    mTournamentsViewModel.start();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    mTournamentsFragBinding = TournamentsFragBinding.inflate(inflater, container, false);

    mTournamentsFragBinding.setView(this);

    mTournamentsFragBinding.setViewmodel(mTournamentsViewModel);

    setHasOptionsMenu(true);

    View root = mTournamentsFragBinding.getRoot();

    return root;
  }

  public void setViewModel(TournamentsViewModel viewModel) {
    mTournamentsViewModel = viewModel;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    setupSnackbar();

    setupFab();

    setupListAdapter();

    setupRefreshLayout();
  }

  @Override
  public void onDestroy() {
    mListAdapter.onDestroy();
    if(mSnackbarCallback != null) {
      mTournamentsViewModel.snackbarText.removeOnPropertyChangedCallback(mSnackbarCallback);
    }
    super.onDestroy();
  }

  private void setupSnackbar() {
    mSnackbarCallback = new Observable.OnPropertyChangedCallback() {
      @Override
      public void onPropertyChanged(Observable observable, int i) {
        if(!TextUtils.isEmpty(mTournamentsViewModel.getSnackbarText())) {
          SnackbarUtils.showSnackbar(getView(), mTournamentsViewModel.getSnackbarText(), new SnackbarUtils.UtilsCallback() {
            @Override
            public void onDismissed() {
              mTournamentsViewModel.snackbarText.set(null);
            }
          });
        }
      }
    };
    mTournamentsViewModel.snackbarText.addOnPropertyChangedCallback(mSnackbarCallback);
  }

  private void setupFab() {
    FloatingActionButton fab = getActivity().findViewById(R.id.fab_add_tournament);

    fab.setImageResource(R.drawable.ic_add);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mTournamentsViewModel.addNewTournament();
      }
    });
  }

  private void setupListAdapter() {
    RecyclerView listView = mTournamentsFragBinding.tournamentsList;

    listView.setLayoutManager(new LinearLayoutManager(getActivity()));
    mListAdapter = new TournamentsAdapter(
        new ArrayList<Tournament>(0),
        (TournamentsActivity) getActivity(),
        Injection.provideTournamentsRepository(getContext().getApplicationContext()),
        mTournamentsViewModel);
    listView.setAdapter(mListAdapter);
  }

  private void setupRefreshLayout() {
    RecyclerView listView = mTournamentsFragBinding.tournamentsList;
    final ScrollChildSwipeRefreshLayout swipeRefreshLayout = mTournamentsFragBinding.refreshLayout;
    swipeRefreshLayout.setColorSchemeColors(
        ContextCompat.getColor(getActivity(), R.color.colorPrimary),
        ContextCompat.getColor(getActivity(), R.color.colorAccent),
        ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
    );
    // Set the scrolling view in the custom SwipeRefreshLayout.
    swipeRefreshLayout.setScrollUpChild(listView);
  }
}
