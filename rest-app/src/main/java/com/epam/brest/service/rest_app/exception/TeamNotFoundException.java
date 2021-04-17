package com.epam.brest.service.rest_app.exception;

public class TeamNotFoundException extends RuntimeException {
    public TeamNotFoundException(Integer id) {
        super("Team not found for id: " + id);
    }
}
