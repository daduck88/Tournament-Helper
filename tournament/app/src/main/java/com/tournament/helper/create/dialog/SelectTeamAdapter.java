package com.tournament.helper.create.dialog;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tournament.helper.R;
import com.tournament.helper.create.AddTournamentNavigator;
import com.tournament.helper.create.TeamItemViewModel;
import com.tournament.helper.data.Team;
import com.tournament.helper.data.source.TeamsRepository;
import com.tournament.helper.databinding.TeamItemBinding;

import java.util.List;

/**
 * Created by destevancardozoj on 2/20/18.
 */

public class SelectTeamAdapter extends RecyclerView.Adapter<SelectTeamAdapter.BindingHolder> {

  private final TeamsRepository mTeamsRepository;
  private SelectTeamDialog.SelectTeamListener mNavigator;
  private List<Team> mTeams;

  public SelectTeamAdapter(List<Team> teams, TeamsRepository teamsRepository,
                           SelectTeamDialog.SelectTeamListener navigator) {
    this.mTeamsRepository = teamsRepository;
    mNavigator = navigator;
    setList(teams);
  }

  public Team getItem(int i) {
    return mTeams.get(i);
  }

  public void replaceData(List<Team> teams) {
    setList(teams);
  }

  @Override
  public SelectTeamAdapter.BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    TeamItemBinding commentsHeaderBinding = DataBindingUtil.inflate(
        LayoutInflater.from(parent.getContext()),
        R.layout.team_item,
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
    return mTeams != null ? mTeams.size() : 0;
  }

  //  @Override
  //  public int getCount() {
  //    return mTeams != null ? mTeams.size() : 0;
  //  }
  //
  //  @Override
  //  public Team getItem(int i) {
  //    return mTeams.get(i);
  //  }
  //
  //  @Override
  //  public long getItemId(int i) {
  //    return i;
  //  }
  //
  //  @Override
  //  public View getView(int i, View view, ViewGroup viewGroup) {
  //    Team team = getItem(i);
  //    TeamItemBinding binding;
  //    if(view == null) {
  //      // Inflate
  //      LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
  //      // Create the binding
  //      binding = TeamItemBinding.inflate(inflater, viewGroup, false);
  //    } else {
  //      // Recycling view
  //      binding = DataBindingUtil.getBinding(view);
  //    }
  //
  //    final TeamItemViewModel viewmodel = new TeamItemViewModel(
  //        viewGroup.getContext().getApplicationContext(),
  //        mTeamsRepository
  //    );
  //
  //    viewmodel.setListener(mNavigator);
  //
  //    binding.setViewmodel(viewmodel);
  //    viewmodel.setTeam(team);
  //
  //    return binding.getRoot();
  //  }
  //
    private void setList(List<Team> teams) {
      mTeams = teams;
      notifyDataSetChanged();
    }

  public class BindingHolder extends RecyclerView.ViewHolder {
    private TeamItemBinding binding;

    public BindingHolder(TeamItemBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    public void bind(Team team) {
      TeamItemViewModel vModel = new TeamItemViewModel(itemView.getContext(), mTeamsRepository);
      vModel.setListener(mNavigator);
      vModel.setTeam(team);
      binding.setViewmodel(vModel);
    }
  }
}
