package org.project.softwareproject_2018;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by my on 2018-06-04.
 */

public class UserScheduleInquireSystem {

    private FirebaseDatabase database;
    private ArrayList<String> rids;
    private ArrayList<ReservationTime> rts;

    public ArrayList<ReservationTime> getReseravtion(String uid){    //current user
        rids=new ArrayList<String>();
        rts=new ArrayList<ReservationTime>();
        database = FirebaseDatabase.getInstance();

        Query getrids = database.getReference().child("uidrids").child(uid);
        getrids.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                rids.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String rid = snapshot.getValue(String.class);
                    rids.add(rid);
                }
                //Toast message 출력하면 오류
                int rcnt=rids.size();
                for(int i=0;i<rcnt;i++){
                    String cur=rids.get(i);
                    Query getReservationTimes = database.getReference().child("reservationtimes").child(cur);
                    getReservationTimes.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            rts.clear();
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                ReservationTime rt = snapshot.getValue(ReservationTime.class);
                                rts.add(rt);
                            }
                            // mainActivity로 옮길 경우 여기에서 UI변경

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return rts;     //이거 데이터 들어가기 전에 리턴될 수 있음
    }
}
