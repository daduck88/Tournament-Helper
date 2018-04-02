package com.tournament.helper.detail.matches.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tournament.helper.Injection;
import com.tournament.helper.R;
import com.tournament.helper.data.Match;
import com.tournament.helper.data.Team;
import com.tournament.helper.data.source.TeamsDataSource;
import com.tournament.helper.data.source.TeamsRepository;

/**
 * Created by destevancardozoj on 3/9/18.
 */

public class FinishMatchDialog extends DialogFragment {

  private TeamsRepository mTeamsRepository;
  private FinishMatchListener mListener;
  private TextView mTeam1Bttn;
  private TextView mTeam2Bttn;
  private Match mMatch;

  @NonNull
  public static FinishMatchDialog newInstance() {
    FinishMatchDialog dialog = new FinishMatchDialog();
    return dialog;
  }

  public void launchAddTeamDialog(@NonNull FragmentManager fragmentManager,
                                  @NonNull FinishMatchListener listener,
                                  @NonNull Match match) {
    mListener = listener;
    mMatch = match;
    mTeamsRepository = Injection.provideTeamsRepository();
    show(fragmentManager, FinishMatchDialog.class.getSimpleName());
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.finish_match_dial, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setupViews(view);
    initTeams();
  }

  private void setupViews(View view) {
    mTeam1Bttn = view.findViewById(R.id.finish_match_team_1_bttn);
    mTeam2Bttn = view.findViewById(R.id.finish_match_team_2_bttn);

    mTeam1Bttn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mListener.onFinishMatch(mMatch.getTeam1Id());
        dismissAllowingStateLoss();
      }
    });
    mTeam2Bttn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mListener.onFinishMatch(mMatch.getTeam2Id());
        dismissAllowingStateLoss();
      }
    });
  }

  private void initTeams() {
    mTeamsRepository.getTeam(mMatch.getTeam1Id(), new TeamsDataSource.GetTeamCallback() {
      @Override
      public void onTeamLoaded(Team team) {
        mTeam1Bttn.setText(team.getTitle());
      }

      @Override
      public void onDataNotAvailable() {

      }
    });
    mTeamsRepository.getTeam(mMatch.getTeam2Id(), new TeamsDataSource.GetTeamCallback() {
      @Override
      public void onTeamLoaded(Team team) {
        mTeam2Bttn.setText(team.getTitle());
      }

      @Override
      public void onDataNotAvailable() {

      }
    });
  }

  public interface FinishMatchListener {
    void onFinishMatch(String teamId);
  }
}
