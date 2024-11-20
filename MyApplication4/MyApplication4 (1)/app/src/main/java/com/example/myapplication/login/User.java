package com.example.myapplication.login;

public class User {
    private String id;
    private String password;
    private String name;
    private String email;
    private String phone;
    private String birthday;

    // 기본 생성자
    public User() {
    }

    // 생성자
    public User(String id, String password, String name, String email, String phone, String birthday) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
    }

    // 게터
    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getBirthday() {
        return birthday;
    }

    // 세터
    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
