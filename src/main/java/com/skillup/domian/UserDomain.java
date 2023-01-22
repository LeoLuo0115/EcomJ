package com.skillup.domian;

public class UserDomain {
    private String userID;
    private String userName;
    private String password;

    public String getUserID() {
        return userID;
    }

    public UserDomain userID(String userID) {
        this.userID = userID;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public UserDomain userName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserDomain password(String password) {
        this.password = password;
        return this;
    }
}
