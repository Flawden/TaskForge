package com.flawden.TaskForgeAPI.exception;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException() {
    }

    public TaskNotFoundException(String message) {
        super(message);
    }
}
