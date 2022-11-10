package com.tnh.chatmessagesservice.utils.exception;

public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String message) {
        super(message);
    }
}
