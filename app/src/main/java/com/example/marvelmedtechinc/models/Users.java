package com.example.marvelmedtechinc.models;

public class Users {
    String profilepic, username, mail, userid,pass, time,name;



    public Users(String profilepic, String username, String mail, String userid, String pass, String time) {

        this.profilepic = profilepic;
        this.username = username;
        this.mail = mail;
        this.userid = userid;
        this.pass = pass;
        this.time = time;
    }
    public Users(){}

    public Users(String name,String mail, String pass){

        this.mail = mail;
        this.pass = pass;
        this.name=name;
    }


    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

