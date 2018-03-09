package com.tournament.helper.data;

/**
 * Created by destevancardozoj on 2/15/18.
 */

public class Team {

  private String title;
  private String id;

  public Team() {
  }

  public Team(String title) {
    //    id = title;
    this.title = title;
  }

  public String getId() {
    return id;
  }

  public String getTitle() {
  return title;
  }

  @Override
  public String toString() {
    return "Team{" +
        "id='" + title + '\'' + //TODO Update this when have the correct ids
        '}';
  }

  public void setId(String id) {
    this.id = id;
  }
}
