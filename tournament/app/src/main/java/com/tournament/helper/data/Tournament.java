package com.tournament.helper.data;

import android.text.TextUtils;

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
  private List<Team> activeTeams;
  private List<Team> teamsId;
  private List<Team> activeTeamsId;

  public Tournament() {//used by DocumentSnapshot
  }

  public Tournament(String title, List<Team> teams) {
    this.title = title;
    this.teams = teams;
    this.activeTeams = teams;
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

  public List<Team> getActiveTeams() {
    return activeTeams;
  }

  public void setActiveTeams(List<Team> activeTeams) {
    this.activeTeams = activeTeams;
  }

  public List<Team> getTeamsId() {
    return teamsId;
  }

  public void setTeamsId(List<Team> teamsId) {
    this.teamsId = teamsId;
  }

  public List<Team> getActiveTeamsId() {
    return activeTeamsId;
  }

  public void setActiveTeamsId(List<Team> activeTeamsId) {
    this.activeTeamsId = activeTeamsId;
  }

  public String getTitleForList() {
    return title;//maybe add a not title if needed
  }

  public Map<String, Object> getMap(){
    Map<String, Object> tournamentMap = new HashMap<>();
    if(TextUtils.isEmpty(id))
      tournamentMap.put("id", id);
    tournamentMap.put("title", title);
    tournamentMap.put("teamsId", getTeamsArrayIds(teams));
    tournamentMap.put("activeTeamsId", getTeamsArrayIds(activeTeams));
    return tournamentMap;
  }

  private static List getTeamsArrayIds(List<Team> teams) {
    String[] teamIds = new String[teams.size()];
    int i = 0;
    for(Team team:teams){
      teamIds[i] = team.getId();
      i++;
    }
    return Arrays.asList(teamIds);
  }
}
