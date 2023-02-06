package com.skillup.presentation.util;

public class ResponseCode {
    public static final int SUCCESS = 200;
    public static final int BAD_REQUEST = 400;

    public static final int INTERNAL_ERROR = 500;

    public static final String USER_EXIST = "User already exists";
    public static final String USER_ID_WRONG = "User id: %s, is wrong";
    public static final String USER_NAME_WRONG = "User name %s, is wrong";
    public static final String PASSWORD_NOT_MATCH = "Password is not correct";
}
