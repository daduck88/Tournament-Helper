package com.tournament.helper.create;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tournament.helper.R;
import com.tournament.helper.data.Tournament;
import com.tournament.helper.data.helper.SelectTeam;
import com.tournament.helper.data.source.TeamsRepository;
import com.tournament.helper.data.source.TournamentsRepository;
import com.tournament.helper.databinding.SelectTeamItemBinding;
import com.tournament.helper.databinding.TournamentItemBinding;
import com.tournament.helper.tournaments.TournamentItemNavigator;
import com.tournament.helper.tournaments.TournamentItemViewModel;
import com.tournament.helper.tournaments.TournamentsActivity;
import com.tournament.helper.tournaments.TournamentsViewModel;

import java.util.List;

/**
 * Created by destevancardozoj on 2/20/18.
 */



public class SelectTeamsAdapter extends RecyclerView.Adapter<SelectTeamsAdapter.BindingHolder> {

  private CreateTournamentViewModel mCreateTournamentViewModel;
  private final TeamsRepository mTeamsRepository;
  private final AddTournamentNavigator mNavigator;

  private List<SelectTeam> selectTeams;

  public SelectTeamsAdapter(List<SelectTeam> selectTeams,
                            CreateTournamentViewModel tasksViewModel,
                            TeamsRepository teamsRepository, AddTournamentNavigator navigator) {
    mCreateTournamentViewModel = tasksViewModel;
    mTeamsRepository = teamsRepository;
    mNavigator = navigator;
    setList(selectTeams);

  }

  public void replaceData(List<SelectTeam> tasks) {
    setList(tasks);
  }

  public SelectTeam getItem(int i) {
    return selectTeams.get(i);
  }

  @Override
  public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    SelectTeamItemBinding commentsHeaderBinding = DataBindingUtil.inflate(
        LayoutInflater.from(parent.getContext()),
        R.layout.select_team_item,
        parent,
        false);
    return new BindingHolder(commentsHeaderBinding);

  }

  @Override
  public void onBindViewHolder(BindingHolder holder, int position) {
    holder.bind(getItem(position));
  }

  @Override
  public long getItemId(int i) {
    return i;
  }

  @Override
  public int getItemCount() {
    return selectTeams != null ? selectTeams.size() : 0;
  }

//  @Override
//  public View getView(int i, View view, ViewGroup viewGroup) {
//    SelectTeam selectTeam = getItem(i);
//    TournamentItemBinding binding;
//    if (view == null) {
//      // Inflate
//      LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
//
//      // Create the binding
//      binding = TournamentItemBinding.inflate(inflater, viewGroup, false);
//    } else {
//      // Recycling view
//      binding = DataBindingUtil.getBinding(view);
//    }
//
//    final TournamentItemViewModel viewmodel = new TournamentItemViewModel(
//        viewGroup.getContext().getApplicationContext(),
//        mTournamentsRepository
//    );
//
//    viewmodel.setNavigator(mTournamentItemNavigator);
//
//    binding.setViewmodel(viewmodel);
//    // To save on PropertyChangedCallbacks, wire the item's snackbar text observable to the
//    // fragment's.
//    viewmodel.snackbarText.addOnPropertyChangedCallback(
//        new Observable.OnPropertyChangedCallback() {
//          @Override
//          public void onPropertyChanged(Observable observable, int i) {
//            mCreateTournamentViewModel.snackbarText.set(viewmodel.getSnackbarText());
//          }
//        });
//    viewmodel.setTournament(selectTeam);
//
//    return binding.getRoot();
//  }

  private void setList(List<SelectTeam> selectTeams) {
    this.selectTeams = selectTeams;
    notifyDataSetChanged();
  }

  public class BindingHolder extends RecyclerView.ViewHolder {
    private SelectTeamItemBinding binding;

    public BindingHolder(SelectTeamItemBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
    public void bind(SelectTeam selectTeam){
      SelectTeamItemViewModel vModel = new SelectTeamItemViewModel(selectTeam, mCreateTournamentViewModel, mTeamsRepository);
      vModel.setNavigator(mNavigator);
      binding.setViewmodel(vModel);//todo u
    }
  }
}