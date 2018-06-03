package org.project.softwareproject_2018;

/**
 * Created by my on 2018-06-02.
 */

public class ReservationTime {
    protected String cid;
    protected String tid;
    protected int year;
    protected int month;
    protected int dayOfMonth;
    protected int startTime;
    public ReservationTime(){}
    public ReservationTime(String cid, String tid, int year, int month, int dayOfMonth, int startTime){
        this.cid=cid;
        this.tid=tid;
        this.year=year;
        this.month=month;
        this.dayOfMonth=dayOfMonth;
        this.startTime=startTime;
    }
}
