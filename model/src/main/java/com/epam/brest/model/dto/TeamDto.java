package com.epam.brest.model.dto;

public class TeamDto {

    private Integer teamId;

    private String teamName;

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
