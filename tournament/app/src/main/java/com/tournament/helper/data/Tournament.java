package com.tournament.helper.data;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by destevancardozoj on 2/15/18.
 */

public class Tournament {

  private String id;
  private String title;
  private List<Team> teams;
  private List<String> teamsId;
  private List<String> activeTeamsId;
  private List<String> matchTeam1Id;
  private List<String> matchTeam2Id;
  private List<String> matchWinnerId;

  private List<Match> roundQuarterMatches;
  private List<Match> roundSemiFinalMatches;
  private List<Match> roundFinalMatches;
  private String winnerId;

  public Tournament() {//used by DocumentSnapshot
  }

  public Tournament(String title, List<Team> teams) {
    this.title = title;
    this.teams = teams;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public List<Team> getTeams() {
    return teams;
  }

  public void setTeams(List<Team> teams) {
    this.teams = teams;
  }

  public List<String> getTeamsId() {
    return teamsId;
  }

  public void setTeamsId(List<String> teamsId) {
    this.teamsId = teamsId;
  }

  public List<String> getActiveTeamsId() {
    return activeTeamsId;
  }

  public void setActiveTeamsId(List<String> activeTeamsId) {
    this.activeTeamsId = activeTeamsId;
  }

  public List<String> getMatchTeam1Id() {
    return matchTeam1Id;
  }

  public void setMatchTeam1Id(List<String> matchTeam1Id) {
    this.matchTeam1Id = matchTeam1Id;
  }

  public List<String> getMatchTeam2Id() {
    return matchTeam2Id;
  }

  public void setMatchTeam2Id(List<String> matchTeam2Id) {
    this.matchTeam2Id = matchTeam2Id;
  }

  public List<String> getMatchWinnerId() {
    return matchWinnerId;
  }

  public void setMatchWinnerId(List<String> matchWinnerId) {
    this.matchWinnerId = matchWinnerId;
  }

  public List<Match> getRoundQuarterMatches() {
    return roundQuarterMatches;
  }

  public void setRoundQuarterMatches(List<Match> roundQuarterMatches) {
    this.roundQuarterMatches = roundQuarterMatches;
  }

  public List<Match> getRoundSemiFinalMatches() {
    return roundSemiFinalMatches;
  }

  public void setRoundSemiFinalMatches(List<Match> roundSemiFinalMatches) {
    this.roundSemiFinalMatches = roundSemiFinalMatches;
  }

  public List<Match> getRoundFinalMatches() {
    return roundFinalMatches;
  }

  public void setRoundFinalMatches(List<Match> roundFinalMatches) {
    this.roundFinalMatches = roundFinalMatches;
  }

  public String getTitleForList() {
    return title;//maybe add a not team1Name if needed
  }

  /**
   * show the current status of the tournament based on the current active teams
   *
   * @return
   */
  public String getDescription() {
    return "started Tournament";
  }

  public void setWinnerId(String winnerId) {
    this.winnerId = winnerId;
  }

  public String getWinnerId() {
    return winnerId;
  }
}
