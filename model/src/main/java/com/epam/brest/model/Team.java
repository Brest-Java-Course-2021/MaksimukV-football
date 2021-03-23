package com.epam.brest.model;

import java.util.Objects;

public class Team {

    private Integer teamId;
    private String teamName;

    public Team() {}

    public Team(String teamName) {
        this.teamName = teamName;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public String toString() {
        return "Team{" +
                "teamId=" + teamId +
                ", teamName='" + teamName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team that = (Team) o;
        return Objects.equals(teamId, that.teamId) && Objects.equals(teamName, that.teamName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamId, teamName);
    }
}
