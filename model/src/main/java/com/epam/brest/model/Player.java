package com.epam.brest.model;

import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

public class Player {

    @ApiModelProperty(notes = "Player identifier(number)")
    private Integer playerId;

    @ApiModelProperty(notes = "Players name")
    private String firstName;

    @ApiModelProperty(notes = "Players surname")
    private String lastName;

    @ApiModelProperty(notes = "Players nationality")
    private String nationality;

    @ApiModelProperty(notes = "Players salary")
    private Double salary;

    @ApiModelProperty(notes = "Players team identifier(number)")
    private Integer teamId;

    public Player () {}

    public Player(String firstName, String lastName, String nationality, Double salary, Integer teamId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationality = nationality;
        this.salary = salary;
        this.teamId = teamId;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String toString() {
        return "Player{" +
                "playerId=" + playerId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", nationality='" + nationality + '\'' +
                ", salary=" + salary +
                ", teamId=" + teamId +
                '}';
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player that = (Player) o;
        return Objects.equals(playerId, that.playerId) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(nationality, that.nationality) && Objects.equals(salary, that.salary) && Objects.equals(teamId, that.teamId);
    }

    public int hashCode() {
        return Objects.hash(playerId, firstName, lastName, nationality, salary, teamId);
    }
}
