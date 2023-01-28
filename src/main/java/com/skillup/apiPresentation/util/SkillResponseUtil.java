package com.skillup.apiPresentation.util;

public class SkillResponseUtil {
    public static final int SUCCESS = 200;
    public static final int BAD_REQUEST = 400;
    public static final int INTERNAL_ERROR = 500;

    public static final String USER_EXISTS = "User already exists";
    public static final String USER_ID_WRONG = "UserID: %s, is wrong.";
    public static final String USER_NAME_WRONG = "UserName: %s, is wrong.";
    public static final String PASSWORD_NOT_MATCH = "Password provided not match.";

    public static final String COM_EXISTS = "Commodity already exists";

    public static final String COM_NOT_EXISTS = "We dont have this!";

    public static final String PROMO_ALREADY_EXISTS = "we already have this promotion";

    public static final String NO_SUCH_PROMO_ID = "we can't find this promo by id";
}
