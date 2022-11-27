package com.cloudStorage.exeptions;

public enum Errors {
    USER_DIRECTORIES_NOT_CREATED("User directories not created "),

    UNAUTHORIZED_REQUEST("Unauthorized request ");

    private final String description;

    Errors(String description) {
        this.description = description;
    }

    public String value() {
        return description;
    }
}