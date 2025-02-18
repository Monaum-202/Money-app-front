package com.monaum.money.entity;

public class Users {


    public int id;
    public String username, email,password,cpassword, dob;


    public Users() {
    }

    public Users(String username, String email, String password, String cpassword, String dob) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.cpassword = cpassword;
        this.dob = dob;
    }

    public Users(int id, String username, String email, String password, String cpassword, String dob) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.cpassword = cpassword;
        this.dob = dob;
    }


}
