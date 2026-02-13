package com.senla.app.exceptions;

public class UnavailableAction extends RuntimeException {

    private final String action;
    private final String reason;

    public UnavailableAction(String action, String reason) {
        super("Unavailable action");
        this.action = action;
        this.reason = reason;
    }

    public String getAction() {
        return action;
    }

    public String getReason() {
        return reason;
    }
}
