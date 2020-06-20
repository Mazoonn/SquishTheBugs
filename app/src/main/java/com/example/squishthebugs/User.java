package com.example.squishthebugs;

public class User
{
    public String email;
    public String birthday;
    public  String nickname;
    public int coins;

    public User(String email, String nickname, String birthday,int coins) {
        this.email=email;
        this.birthday = birthday;
        this.coins=coins;
        this.nickname=nickname;
    }
}
