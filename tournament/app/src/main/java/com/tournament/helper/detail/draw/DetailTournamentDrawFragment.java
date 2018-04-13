package com.tournament.helper.detail.draw;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tournament.helper.R;
import com.tournament.helper.databinding.DetailTournamentDrawFragBinding;
import com.tournament.helper.detail.DetailTournamentViewModel;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class DetailTournamentDrawFragment extends Fragment {

  private DetailTournamentViewModel mViewModel;
  private DetailTournamentDrawFragBinding mViewDataBinding;

  public static DetailTournamentDrawFragment newInstance() {
    return new DetailTournamentDrawFragment();
  }

  public void setViewModel(@NonNull DetailTournamentViewModel viewModel) {
    mViewModel = checkNotNull(viewModel);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    final View root = inflater.inflate(R.layout.detail_tournament_draw_frag, container, false);
    if(mViewDataBinding == null) {
      mViewDataBinding = DetailTournamentDrawFragBinding.bind(root);
    }
    mViewDataBinding.setViewmodel(mViewModel);
    setRetainInstance(false);
    return mViewDataBinding.getRoot();
  }
}
