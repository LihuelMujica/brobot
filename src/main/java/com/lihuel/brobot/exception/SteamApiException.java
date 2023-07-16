package com.lihuel.brobot.exception;

public class SteamApiException extends Exception {

    public SteamApiException(String message) {
        super(message);
    }

    public SteamApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
