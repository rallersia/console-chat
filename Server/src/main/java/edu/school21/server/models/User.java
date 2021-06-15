package edu.school21.server.models;

import java.util.Objects;

public class User {
    Long identifier;
    String username;
    String password;


    public User(Long identifier, String username, String password) {
        this.identifier = identifier;
        this.username = username;
        this.password = password;
    }

    public Long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Long identifier) {
        this.identifier = identifier;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(identifier, user.identifier) && Objects.equals(username, user.username) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, username, password);
    }

    @Override
    public String toString() {
        return "User{" +
                "identifier=" + identifier +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
