package com.example.lifecut;

public class Information2 {
    String nickname;
    String comment;
    String gender;
    int id;

    public Information2(String nickname, String comment, String gender, int id) {
        this.nickname = nickname;
        this.comment = comment;
        this.gender = gender;
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
