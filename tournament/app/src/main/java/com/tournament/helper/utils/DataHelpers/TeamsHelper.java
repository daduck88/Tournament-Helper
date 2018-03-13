package com.tournament.helper.utils.DataHelpers;

import com.tournament.helper.data.Team;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by destevancardozoj on 3/13/18.
 */

public class TeamsHelper {
  public static List<Team> freeTeams(List<Team> selectedTeams, List<Team> allTeams){
    List<Team> freeTeams = new ArrayList<>();
    allTeamsFor: for(Team team : allTeams){
      if(selectedTeams.contains(team)){
        continue allTeamsFor;
      }
      freeTeams.add(team);
    }
    return freeTeams;
  }
}
