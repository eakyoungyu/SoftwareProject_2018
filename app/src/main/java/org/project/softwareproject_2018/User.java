package org.project.softwareproject_2018;

/**
 * Created by my on 2018-05-18.
 */

public class User {
    //private으로 member 선언시 DB접근 오류
    protected String email;
    //    protected String password;
    protected String name;
    protected String uid;


    public User(String name, String email, String uid) {
        this.name = name;
        this.email = email;
        this.uid=uid;
    }
    public User(String name, String email){
        this.name = name;
        this.email = email;
    }
}
