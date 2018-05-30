package org.project.softwareproject_2018;

public class Trainer extends User {
    protected String image;   //경로 저장
    protected String type;

    public Trainer(String uid, String email, String image, String name, String type) {
        super(name, email, uid);
    //    this.uid=uid;
    //    this.email=email;
        this.image= image;
    //    this.name = name;
        this.type = type;
    }
}
