package com.tournament.helper.tournaments;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tournament.helper.data.Tournament;
import com.tournament.helper.data.source.TournamentsRepository;
import com.tournament.helper.databinding.TournamentItemBinding;

import java.util.List;

/**
 * Created by destevancardozoj on 2/20/18.
 */



public class TournamentsAdapter extends BaseAdapter {

  @Nullable
  private TournamentItemNavigator mTournamentItemNavigator;

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