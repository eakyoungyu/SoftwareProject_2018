package org.project.softwareproject_2018;

/**
 * Created by my on 2018-06-02.
 */

public class ReservationTime {
    protected String cid;
    protected String tid;
    protected String date;
    protected String startTime;
    public ReservationTime(){}
    public ReservationTime(String cid, String tid, String date, String startTime){
        this.cid=cid;
        this.tid=tid;
        this.date=date;
        this.startTime=startTime;
    }
}
