package com.example.questionandanswer.model;

public class User {
    private int id;
    private String name;
    private String email;
    private String URL_prifle;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getURL_prifle() {
        return URL_prifle;
    }

    public void setURL_prifle(String URL_prifle) {
        this.URL_prifle = URL_prifle;
    }
}
