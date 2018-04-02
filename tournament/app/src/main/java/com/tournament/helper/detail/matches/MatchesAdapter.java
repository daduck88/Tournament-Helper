package com.tournament.helper.detail.matches;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tournament.helper.R;
import com.tournament.helper.data.Match;
import com.tournament.helper.data.source.TeamsRepository;
import com.tournament.helper.databinding.MatchItemBinding;
import com.tournament.helper.databinding.TournamentItemBinding;
import com.tournament.helper.detail.DetailTournamentNavigator;
import com.tournament.helper.detail.DetailTournamentViewModel;

import java.util.List;

/**
 * Created by destevancardozoj on 2/20/18.
 */



public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.BindingHolder> {

  @Nullable
  private DetailTournamentNavigator mMatchItemNavigator;

  private final DetailTournamentViewModel mDetailTournamentViewModel;

  private List<Match> mMatches;

  private TeamsRepository mTeamsRepository;

  public MatchesAdapter(List<Match> matches,
                        DetailTournamentViewModel detailTournamentViewModel,
                        TeamsRepository teamsRepository,
                        DetailTournamentNavigator matchItemNavigator) {
    mMatchItemNavigator = matchItemNavigator;
    mTeamsRepository = teamsRepository;
    mDetailTournamentViewModel = detailTournamentViewModel;
    setList(matches);

  }

  public void onDestroy() {
    mMatchItemNavigator = null;
  }

  public void replaceData(List<Match> tasks) {
    setList(tasks);
  }

  @Override
  public int getItemCount() {
    return mMatches != null ? mMatches.size() : 0;
  }

  public Match getItem(int i) {
    return mMatches.get(i);
  }

  @Override
  public long getItemId(int i) {
    return i;
  }

  @Override
  public MatchesAdapter.BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    MatchItemBinding matchItemBinding = DataBindingUtil.inflate(
        LayoutInflater.from(parent.getContext()),
        R.layout.match_item,
        parent,
        false);
    return new MatchesAdapter.BindingHolder(matchItemBinding);
  }

  @Override
  public void onBindViewHolder(MatchesAdapter.BindingHolder holder, int position) {
    holder.bind(getItem(position));
  }


  private void setList(List<Match> tasks) {
    mMatches = tasks;
    notifyDataSetChanged();
  }

  public class BindingHolder extends RecyclerView.ViewHolder {
    private MatchItemBinding binding;

    public BindingHolder(MatchItemBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    public void bind(Match match) {
      final MatchItemViewModel vModel = new MatchItemViewModel(mTeamsRepository);
      vModel.setNavigator(mMatchItemNavigator);
      vModel.snackbarText.addOnPropertyChangedCallback(
          new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
              mDetailTournamentViewModel.snackbarText.set(vModel.getSnackbarText());
            }
          });
      vModel.start(match);
      binding.setViewmodel(vModel);
    }
  }
}