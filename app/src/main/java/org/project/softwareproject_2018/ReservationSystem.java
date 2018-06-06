package org.project.softwareproject_2018;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by my on 2018-06-04.
 */

public class ReservationSystem {
    private static DatabaseReference mDatabase;
    public static void makeReservation(ReservationTime rt){
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mDatabase.child("customers").child(rt.cid).child("reservtimes").child(rt.date).child("tid").setValue(rt.tid);
        mDatabase.child("customers").child(rt.cid).child("reservtimes").child(rt.date).child("startTime").setValue(rt.startTime);
        mDatabase.child("trainers").child(rt.tid).child("reservtimes").child(rt.date).child(rt.cid).setValue(rt.startTime);
        //mDatabase.child("uidrids").orderByValue().equalTo(true) 되나 확인
        //9시 어캄?
    }
    public static void cancelReservation(ReservationTime rt){
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mDatabase.child("customers").child(rt.cid).child("reservtimes").child(rt.date).removeValue();
        mDatabase.child("trainers").child(rt.tid).child("reservtimes").child(rt.date).removeValue();
    }
}
