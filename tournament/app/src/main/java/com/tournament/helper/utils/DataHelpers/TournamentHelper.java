package com.tournament.helper.utils.DataHelpers;

import android.text.TextUtils;

import com.tournament.helper.data.Match;
import com.tournament.helper.data.Team;
import com.tournament.helper.data.Tournament;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TournamentHelper {

  private static final int START_ROUND_OF_16 = 0;
  private static final int END_ROUND_OF_16 = START_ROUND_OF_16 + 7;

  private static final int START_ROUND_QUARTER_FINALS = 0;
  private static final int END_ROUND_QUARTER_FINALS = START_ROUND_QUARTER_FINALS + 3;
  private static final int START_ROUND_SEMI_FINALS = END_ROUND_QUARTER_FINALS + 1;
  private static final int END_ROUND_SEMI_FINALS = START_ROUND_SEMI_FINALS + 1;
  private static final int START_ROUND_FINAL = END_ROUND_SEMI_FINALS + 1;
  private static final int END_ROUND_FINAL = START_ROUND_FINAL;

  public static void prepareTournamentRound(Tournament tournament) {
    prepareQuarterMatches(tournament);
    if(tournament.getRoundQuarterMatches().isEmpty()) {
      initQuarterMatches(tournament);
      return;
    }
    if(isRoundComplete(tournament.getRoundQuarterMatches())) {
      return;
    }
    prepareSemiFinalMatches(tournament);
    if(tournament.getRoundSemiFinalMatches().isEmpty()) {
      initSemiFinalMatches(tournament);
      return;
    }
    if(isRoundComplete(tournament.getRoundSemiFinalMatches())) {
      return;
    }
    prepareFinalMatch(tournament);
    if(tournament.getRoundFinalMatches().isEmpty()) {
      tournament.getRoundFinalMatches().add(new Match(tournament.getMatchWinnerId().get(START_ROUND_SEMI_FINALS), tournament.getMatchWinnerId().get(END_ROUND_SEMI_FINALS)));
    }
  }

  public static void prepareQuarterMatches(Tournament tournament) {
    if(tournament.getRoundQuarterMatches() == null) {
      tournament.setRoundQuarterMatches(getRound(tournament, START_ROUND_QUARTER_FINALS, END_ROUND_QUARTER_FINALS));
    }
  }

  public static void prepareSemiFinalMatches(Tournament tournament) {
    if(tournament.getRoundSemiFinalMatches() == null) {
      tournament.setRoundSemiFinalMatches(getRound(tournament, START_ROUND_SEMI_FINALS, END_ROUND_SEMI_FINALS));
    }
  }

  public static void prepareFinalMatch(Tournament tournament) {
    if(tournament.getRoundFinalMatches() == null) {
      tournament.setRoundFinalMatches(getRound(tournament, START_ROUND_FINAL, END_ROUND_FINAL));
    }
  }

  /**
   * init the round Quarter based on the teams
   *
   * @param tournament
   */
  public static void initQuarterMatches(Tournament tournament) {
    for(int i = 0; i < tournament.getTeamsId().size(); i += 2) {
      tournament.getRoundQuarterMatches().add(new Match(tournament.getTeamsId().get(i), tournament.getTeamsId().get(i + 1)));
    }
  }

  /**
   * init the round Semi Final based on Match Winners
   *
   * @param tournament
   */
  public static void initSemiFinalMatches(Tournament tournament) {
    for(int i = 0; i < START_ROUND_SEMI_FINALS; i += 2) {
      tournament.getRoundSemiFinalMatches().add(new Match(tournament.getMatchWinnerId().get(i), tournament.getMatchWinnerId().get(i + 1)));
    }
  }

  private static boolean isRoundComplete(List<Match> round) {
    for(Match match : round) {
      if(TextUtils.isEmpty(match.getWinnerId())) {
        return true;
      }
    }
    return false;
  }

  private static List<Match> getRound(Tournament tournament, int start, int end) {
    List round = new ArrayList();
    if(tournament.getMatchTeam1Id() != null && !tournament.getMatchTeam1Id().isEmpty()
        && tournament.getMatchTeam1Id().size() > end) {
      for(int i = start; i < end + 1; i++) {
        round.add(new Match(tournament.getMatchTeam1Id().get(i), tournament.getMatchTeam2Id().get(i), tournament.getMatchWinnerId().size() > i ? tournament.getMatchWinnerId().get(i) : null));
      }
    }
    return round;
  }

  public static Map<String, Object> getMap(Tournament tournament) {
    prepareMatchIds(tournament);
    Map<String, Object> tournamentMap = new HashMap<>();
    if(!TextUtils.isEmpty(tournament.getId())) {
      tournamentMap.put("id", tournament.getId());
    }
    tournamentMap.put("title", tournament.getTitle());
    tournamentMap.put("team1Name", tournament.getTitle());
    tournamentMap.put("teamsId", tournament.getTeams() != null ? getTeamsArrayIds(tournament.getTeams()) : tournament.getTeamsId());
    tournamentMap.put("matchTeam1Id", tournament.getMatchTeam1Id());
    tournamentMap.put("matchTeam2Id", tournament.getMatchTeam2Id());
    tournamentMap.put("matchWinnerId", tournament.getMatchWinnerId());
    return tournamentMap;
  }

  private static void prepareMatchIds(Tournament tournament) {
    tournament.setMatchTeam1Id(new ArrayList<String>());
    tournament.setMatchTeam2Id(new ArrayList<String>());
    tournament.setMatchWinnerId(new ArrayList<String>());
    if(tournament.getRoundQuarterMatches() != null) {
      setMatchIds(tournament, tournament.getRoundQuarterMatches());
    }
    if(tournament.getRoundSemiFinalMatches() != null) {
      setMatchIds(tournament, tournament.getRoundSemiFinalMatches());
    }
    if(tournament.getRoundFinalMatches() != null) {
      setMatchIds(tournament, tournament.getRoundFinalMatches());
    }
  }

  private static void setMatchIds(Tournament tournament, List<Match> matches) {
    for(Match match : matches) {
      tournament.getMatchTeam1Id().add(match.getTeam1Id());
      tournament.getMatchTeam2Id().add(match.getTeam2Id());
      tournament.getMatchWinnerId().add(match.getWinnerId());
    }
  }

  private static List getTeamsArrayIds(List<Team> teams) {
    String[] teamIds = new String[teams.size()];
    int i = 0;
    for(Team team : teams) {
      teamIds[i] = team.getId();
      i++;
    }
    return Arrays.asList(teamIds);
  }

  public static List<Match> getAllMatch(Tournament mTournament) {
    List<Match> allMatches = new ArrayList<>();
    if(mTournament.getRoundFinalMatches() != null) {
      allMatches.addAll(mTournament.getRoundFinalMatches());
    }
    if(mTournament.getRoundSemiFinalMatches() != null) {
      allMatches.addAll(mTournament.getRoundSemiFinalMatches());
    }
    if(mTournament.getRoundQuarterMatches() != null) {
      allMatches.addAll(mTournament.getRoundQuarterMatches());
    }
    return allMatches;
  }
}
