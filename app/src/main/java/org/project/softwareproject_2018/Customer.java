package org.project.softwareproject_2018;

/**
 * Created by my on 2018-05-31.
 */

public class Customer extends User{
    protected String goal;
    protected String tid;

    public Customer(){}
    public Customer(String name, String email, String uid, String goal, String tid){
        super(name, email, uid);
        this.goal=goal;
        this.tid=tid;
    }
    public Customer(String name, String email, String goal, String tid){
        super(name, email);
        this.goal=goal;
        this.tid=tid;
    }

}
