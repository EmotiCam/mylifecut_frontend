package com.example.lifecut.ObjectFiles;

import java.io.Serializable;

public class NickComm implements Serializable {

    public String nickname = "";
    public String comment= "";

    public NickComm(String nickname, String comment) {
        this.nickname = nickname;
        this.comment = comment;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
