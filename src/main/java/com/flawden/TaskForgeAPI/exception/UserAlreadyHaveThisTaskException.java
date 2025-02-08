package com.flawden.TaskForgeAPI.exception;

public class UserAlreadyHaveThisTaskException extends RuntimeException {

    public UserAlreadyHaveThisTaskException() {
    }

    public UserAlreadyHaveThisTaskException(String message) {
        super(message);
    }
}
