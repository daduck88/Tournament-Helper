package com.tournament.helper.create.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tournament.helper.Injection;
import com.tournament.helper.R;
import com.tournament.helper.create.AddTournamentNavigator;
import com.tournament.helper.data.Team;

import java.util.List;

/**
 * Created by destevancardozoj on 3/13/18.
 */

public class SelectTeamDialog extends DialogFragment {

  private SelectTeamListener mListener;
  private RecyclerView mSelectTeamList;
  private View mSeleteTeamCancelBttn;
  private View mSelectTeamAddTeamBttn;
  private List<Team> teams;

  @NonNull
  public static SelectTeamDialog newInstance(List<Team> teams) {
    SelectTeamDialog dialog = new SelectTeamDialog();
    dialog.setTeams(teams);
    return dialog;
  }

  public void launchSelectTeamDialog(@NonNull FragmentManager fragmentManager,
                                     @NonNull SelectTeamListener listener) {
    mListener = listener;
    show(fragmentManager, SelectTeamDialog.class.getSimpleName());
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.select_team_dial, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setupViews(view);
  }

  private void setupViews(View view) {
    mSelectTeamList = view.findViewById(R.id.select_team_list);
    mSeleteTeamCancelBttn = view.findViewById(R.id.select_team_cancel_bttn);
    mSelectTeamAddTeamBttn = view.findViewById(R.id.select_team_add_bttn);

    mSelectTeamList.setLayoutManager(new LinearLayoutManager(getContext()));
    SelectTeamAdapter adapter = new SelectTeamAdapter(teams, Injection.provideTeamsRepository(), new SelectTeamListener() {
      @Override
      public void onTeamSelected(Team team) {
        mListener.onTeamSelected(team);
        dismissAllowingStateLoss();
      }

      @Override
      public void onAddTeamClick() {

      }
    });
    mSelectTeamList.setAdapter(adapter);

    mSeleteTeamCancelBttn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dismissAllowingStateLoss();
      }
    });
    mSelectTeamAddTeamBttn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mListener.onAddTeamClick();
        dismissAllowingStateLoss();
      }
    });
  }

  public void setTeams(List<Team> teams) {
    this.teams = teams;
  }

  public interface SelectTeamListener {
    void onTeamSelected(Team team);
    void onAddTeamClick();
  }
}
