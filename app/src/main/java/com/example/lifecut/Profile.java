package com.example.lifecut;

public class Profile {
    String id;
    String user;
    String comment;
    String nickname;
    String gender;

    public Profile(String id, String user, String comment, String nickname, String gender) {
        this.id = id;
        this.user = user;
        this.comment = comment;
        this.nickname = nickname;
        this.gender = gender;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public String getComment() {
        return comment;
    }

    public String getNickname() {
        return nickname;
    }

    public String getGender() {
        return gender;
    }


}
