package com.tournament.helper.create;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tournament.helper.R;
import com.tournament.helper.data.helper.SelectTeam;
import com.tournament.helper.data.source.TeamsRepository;
import com.tournament.helper.databinding.SelectTeamItemBinding;

/**
 * Created by destevancardozoj on 2/20/18.
 */

public class CreateTournamentTeamsAdapter extends RecyclerView.Adapter<CreateTournamentTeamsAdapter.BindingHolder> {

  private CreateTournamentViewModel mCreateTournamentViewModel;
  private final TeamsRepository mTeamsRepository;
  private final CreateTournamentNavigator mNavigator;

  public CreateTournamentTeamsAdapter(CreateTournamentViewModel tasksViewModel,
                                      TeamsRepository teamsRepository, CreateTournamentNavigator navigator) {
    mCreateTournamentViewModel = tasksViewModel;
    mTeamsRepository = teamsRepository;
    mNavigator = navigator;
  }

  public SelectTeam getItem(int i) {
    return mCreateTournamentViewModel.getSelectedTeamsList().get(i);
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
    return mCreateTournamentViewModel.getSelectedTeamsList() != null ? mCreateTournamentViewModel.getSelectedTeamsList().size() : 0;
  }

  public class BindingHolder extends RecyclerView.ViewHolder {
    private SelectTeamItemBinding binding;

    public BindingHolder(SelectTeamItemBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    public void bind(SelectTeam selectTeam) {
      SelectTeamItemViewModel vModel = new SelectTeamItemViewModel(selectTeam, mCreateTournamentViewModel, mTeamsRepository);
      vModel.setNavigator(mNavigator);
      binding.setViewmodel(vModel);//todo u
    }
  }
}