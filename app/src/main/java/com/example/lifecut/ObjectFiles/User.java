package com.example.lifecut.ObjectFiles;

public class User {
    private String key;

    public User(String token) {

        this.key = token;
    }


    public String getKey(){
        return key;
    }
}