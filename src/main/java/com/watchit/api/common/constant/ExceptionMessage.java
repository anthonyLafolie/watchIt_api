package com.watchit.api.common.constant;

public final class ExceptionMessage {
    private ExceptionMessage() {
    }

    public static final String CURRENT_USER_NOT_FOUND = "Current user not found";
    public static final String CURRENT_USER_CANNOT_BE_AUTHENTICATED = "Could not authenticate current user";
    public static final String USER_ALREADY_EXISTS = "User account already exists";
    public static final String PSEUDO_EMAIL_TAKEN = "Pseudo or email already taken";
}
