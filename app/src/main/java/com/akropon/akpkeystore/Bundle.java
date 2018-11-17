package com.akropon.akpkeystore;

import java.io.Serializable;

public class Bundle implements Serializable {
    static final int UNKNOWN_ID = -1;

    private int id;
    private String name;
    private String description;
    private String login;
    private String password;


    public Bundle(int id, String name, String description, String login, String password) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.login = login;
        this.password = password;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
