package com.epam.brest.model;

import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

public class Team {

    @ApiModelProperty(notes = "Team identifier(number)")
    private Integer teamId;

    @ApiModelProperty(notes = "Teams name")
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

    public String toString() {
        return "Team{" +
                "teamId=" + teamId +
                ", teamName='" + teamName + '\'' +
                '}';
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team that = (Team) o;
        return Objects.equals(teamId, that.teamId) && Objects.equals(teamName, that.teamName);
    }

    public int hashCode() {
        return Objects.hash(teamId, teamName);
    }
}
