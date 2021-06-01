package com.epam.brest.model.dto;

import io.swagger.annotations.ApiModelProperty;

public class TeamDto {

    @ApiModelProperty(notes = "Team identifier(number)")
    private Integer teamId;

    @ApiModelProperty(notes = "Team name")
    private String teamName;

    @ApiModelProperty(notes = "The most popular players nationality in current team")
    private String prefNationality;
    
    public TeamDto() {
    }

    public TeamDto(String teamName) {
        this.teamName = teamName;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(final Integer teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(final String teamName) {
        this.teamName = teamName;
    }

    public String getPrefNationality() {
        return prefNationality;
    }

    public void setPrefNationality(final String prefNationality) {
        this.prefNationality = prefNationality;
    }

    public String toString() {
        return "TeamDto{"
                + "teamId=" + teamId
                + ", teamName='" + teamName + '\''
                + ", prefNationality=" + prefNationality
                + '}';
    }

}
