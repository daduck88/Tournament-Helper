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

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.tournament.helper.Injection;
import com.tournament.helper.R;
import com.tournament.helper.ScrollChildSwipeRefreshLayout;
import com.tournament.helper.data.Tournament;
import com.tournament.helper.data.source.TournamentsRepository;
import com.tournament.helper.databinding.TournamentItemBinding;
import com.tournament.helper.databinding.TournamentsFragBinding;
import com.tournament.helper.utils.SnackbarUtils;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear:
                mTournamentsViewModel.clearCompletedTournaments();
                break;
            case R.id.menu_filter:
                showFilteringPopUpMenu();
                break;
            case R.id.menu_refresh:
                mTournamentsViewModel.loadTournaments(true);
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.tournaments_fragment_menu, menu);
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
        if (mSnackbarCallback != null) {
            mTournamentsViewModel.snackbarText.removeOnPropertyChangedCallback(mSnackbarCallback);
        }
        super.onDestroy();
    }

    private void setupSnackbar() {
        mSnackbarCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                SnackbarUtils.showSnackbar(getView(), mTournamentsViewModel.getSnackbarText());
            }
        };
        mTournamentsViewModel.snackbarText.addOnPropertyChangedCallback(mSnackbarCallback);
    }

    private void showFilteringPopUpMenu() {
        //TODO check filter options //remove
//        PopupMenu popup = new PopupMenu(getContext(), getActivity().findViewById(R.id.menu_filter));
//        popup.getMenuInflater().inflate(R.menu.filter_tasks, popup.getMenu());
//
//        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.active:
//                        mTournamentsViewModel.setFiltering(TournamentsFilterType.ACTIVE_TASKS);
//                        break;
//                    case R.id.completed:
//                        mTournamentsViewModel.setFiltering(TournamentsFilterType.COMPLETED_TASKS);
//                        break;
//                    default:
//                        mTournamentsViewModel.setFiltering(TournamentsFilterType.ALL_TASKS);
//                        break;
//                }
//                mTournamentsViewModel.loadTournaments(false);
//                return true;
//            }
//        });
//
//        popup.show();
    }

    private void setupFab() {
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_add_tournament);

        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTournamentsViewModel.addNewTournament();
            }
        });
    }

    private void setupListAdapter() {
        ListView listView =  mTournamentsFragBinding.tournamentsList;

        mListAdapter = new TournamentsAdapter(
                new ArrayList<Tournament>(0),
                (TournamentsActivity) getActivity(),
                Injection.provideTournamentsRepository(getContext().getApplicationContext()),
                mTournamentsViewModel);
        listView.setAdapter(mListAdapter);
    }

    private void setupRefreshLayout() {
        ListView listView =  mTournamentsFragBinding.tournamentsList;
        final ScrollChildSwipeRefreshLayout swipeRefreshLayout = mTournamentsFragBinding.refreshLayout;
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        // Set the scrolling view in the custom SwipeRefreshLayout.
        swipeRefreshLayout.setScrollUpChild(listView);
    }

    public static class TournamentsAdapter extends BaseAdapter {

        @Nullable private TournamentItemNavigator mTournamentItemNavigator;

        private final TournamentsViewModel mTournamentsViewModel;

        private List<Tournament> mTournaments;

        private TournamentsRepository mTournamentsRepository;

        public TournamentsAdapter(List<Tournament> tasks, TournamentsActivity taskItemNavigator,
                            TournamentsRepository tasksRepository,
                            TournamentsViewModel tasksViewModel) {
            mTournamentItemNavigator = taskItemNavigator;
            mTournamentsRepository = tasksRepository;
            mTournamentsViewModel = tasksViewModel;
            setList(tasks);

        }

        public void onDestroy() {
            mTournamentItemNavigator = null;
        }

        public void replaceData(List<Tournament> tasks) {
            setList(tasks);
        }

        @Override
        public int getCount() {
            return mTournaments != null ? mTournaments.size() : 0;
        }

        @Override
        public Tournament getItem(int i) {
            return mTournaments.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            Tournament task = getItem(i);
            TournamentItemBinding binding;
            if (view == null) {
                // Inflate
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

                // Create the binding
                binding = TournamentItemBinding.inflate(inflater, viewGroup, false);
            } else {
                // Recycling view
                binding = DataBindingUtil.getBinding(view);
            }

            final TournamentItemViewModel viewmodel = new TournamentItemViewModel(
                    viewGroup.getContext().getApplicationContext(),
                    mTournamentsRepository
            );

            viewmodel.setNavigator(mTournamentItemNavigator);

            binding.setViewmodel(viewmodel);
            // To save on PropertyChangedCallbacks, wire the item's snackbar text observable to the
            // fragment's.
            viewmodel.snackbarText.addOnPropertyChangedCallback(
                    new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable observable, int i) {
                    mTournamentsViewModel.snackbarText.set(viewmodel.getSnackbarText());
                }
            });
            viewmodel.setTournament(task);

            return binding.getRoot();
        }


        private void setList(List<Tournament> tasks) {
            mTournaments = tasks;
            notifyDataSetChanged();
        }
    }
}
