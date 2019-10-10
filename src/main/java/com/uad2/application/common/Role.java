package com.uad2.application.common;

/*
 * @USER Jongyeob Kim
 * @DATE 2019-10-08
 */
public enum Role {
    ADMIN("ADMIN"),
    USER("USER");

    private String role;

    Role(String role) {
        this.role = role;
    }

    public String role() {
        return role;
    }
}
