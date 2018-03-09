package com.tournament.helper.mock;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.tournament.helper.data.Team;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by destevancardozoj on 2/22/18.
 */

public class TeamMock {

  private static List<Team> teams;
  public static List<Team> getTeams(){
    if(teams == null){
      initTeams();
    }
    return teams;
  }

  @NonNull
  private static void initTeams() {
    teams = new ArrayList<>();
    teams.add(new Team("FC Barcelona"));
    teams.add(new Team("Real Madrid"));
    teams.add(new Team("Atletico Madrid"));
    teams.add(new Team("Sevilla FC"));

    teams.add(new Team("Valencia FC"));
    teams.add(new Team("Real Betis"));
    teams.add(new Team("Villareal FC"));
    teams.add(new Team("Athletic Bilbao"));
  }

  public static Team getTeamWithout(List<Team> teamsUsed, List<Team> allTeams){
    List <Team> availableTeams = new ArrayList<>();
    for(Team team: allTeams) {
      if(!teamsUsed.contains(team)){
        availableTeams.add(team);
      }
    }
    if(availableTeams.isEmpty()){
      return null;
    }
    return availableTeams.get(0);
  }
}
