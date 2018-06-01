package org.project.softwareproject_2018;

/**
 * Created by my on 2018-06-02.
 */

public class ReservationTime {
    protected String uid;
    protected String tid;
    protected int year;
    protected int month;
    protected int dayOfMonth;
    public ReservationTime(String uid, String tid, int year, int month, int dayOfMonth){
        this.uid=uid;
        this.tid=tid;
        this.year=year;
        this.month=month;
        this.dayOfMonth=dayOfMonth;
    }
}
