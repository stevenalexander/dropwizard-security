package com.example.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class User {

    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_EDITOR = "editor";

    @NotEmpty
    @JsonProperty
    private String username;

    @NotEmpty
    @JsonProperty
    private String password;

    @NotEmpty
    @JsonProperty
    private String displayName;

    @NotEmpty
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User that = (User) o;

        if (!getUsername().equals(that.getUsername())) return false;
        if (!getPassword().equals(that.getPassword())) return false;
        if (!getDisplayName().equals(that.getDisplayName())) return false;
        if (!getDisplayRole().equals(that.getDisplayRole())) return false;

        return true;
    }
}
