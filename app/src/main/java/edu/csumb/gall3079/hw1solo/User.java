package edu.csumb.gall3079.hw1solo;

public class User {
    private String username;
    private String name;
    private int id;
    private String email;

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public User(int id, String name, String username, String email) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
    }
}
