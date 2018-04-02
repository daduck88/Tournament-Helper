package com.tournament.helper.data;

public class Match {
  private String team1Id;
  private String team2Id;
  private String winnerId;

  public Match(String team1Id, String team2Id) {
    this(team1Id, team2Id, null);
  }

  public Match(String team1Id, String team2Id, String winnerId) {
    this.team1Id = team1Id;
    this.team2Id = team2Id;
    this.winnerId = winnerId;
  }

  public String getTeam1Id() {
    return team1Id;
  }

  public void setTeam1Id(String team1Id) {
    this.team1Id = team1Id;
  }

  public String getTeam2Id() {
    return team2Id;
  }

  public void setTeam2Id(String team2Id) {
    this.team2Id = team2Id;
  }

  public String getWinnerId() {
    return winnerId;
  }

  public void setWinnerId(String winnerId) {
    this.winnerId = winnerId;
  }
}
