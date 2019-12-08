package com.example.lifecut;

public class Information {
    String comment;
    String nickname;
    String gender;

    public Information(String comment, String nickname, String gender) {
        this.comment = comment;
        this.nickname = nickname;
        this.gender = gender;
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

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}

