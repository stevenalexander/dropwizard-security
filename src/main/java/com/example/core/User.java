package com.example.core;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    @JsonProperty
    private String username;

    @JsonProperty
    private String password;

    @JsonProperty
    private String displayName;

    @JsonProperty
    private String displayRole;

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public User setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getDisplayRole() {
        return displayRole;
    }

    public User setDisplayRole(String displayRole) {
        this.displayRole = displayRole;
        return this;
    }
}
