package com.example.lifecut;

public class SignupInfo {
    String email;
    String password;
    String confirm;

    public SignupInfo(String email, String password, String confirm) {
        this.email = email;
        this.password = password;
        this.confirm = confirm;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirm() {
        return confirm;
    }
}
