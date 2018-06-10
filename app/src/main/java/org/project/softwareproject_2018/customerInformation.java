package org.project.softwareproject_2018;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class customerInformation extends AppCompatActivity {
    private CustInfo custInfo;
    private EditText addWeight;
    private EditText addFat;
    private EditText addMuscle;

    private CalendarView calendarView;
    private DatabaseReference mDatabase;
    private String uid;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_information);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        custInfo=new CustInfo();

        calendarView = (CalendarView)findViewById(R.id.calendar);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                final String date=year+"-"+(month+1)+"-"+dayOfMonth;
                //해당 날짜 나의 정보 불러오기
                final Query getMyinfo=mDatabase.child("customers").child(uid).child("info").child(date);
                getMyinfo.addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()) {
                                    custInfo = dataSnapshot.getValue(CustInfo.class);
                                    updateUI();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        }
                );

                showInformationAdd(year, month, dayOfMonth);

            }
        });
    }

    private void showInformationAdd(int year, int month, int dayOfMonth){
        LayoutInflater dialog = LayoutInflater.from(customerInformation.this);
        final View dialogLayout = dialog.inflate(R.layout.customer_information_popup, null);
        final Dialog myDialog = new Dialog(customerInformation.this);
        final String date=year+"-"+(month+1)+"-"+dayOfMonth;

        myDialog.setContentView(dialogLayout);
        myDialog.show();

        addWeight = (EditText)dialogLayout.findViewById(R.id.add_weight);
        addFat = (EditText)dialogLayout.findViewById(R.id.add_bmi);
        addMuscle = (EditText)dialogLayout.findViewById(R.id.add_muscle);
        Button buttonStore = (Button)dialogLayout.findViewById(R.id.button_store);
        Button buttonCancel = (Button)dialogLayout.findViewById(R.id.button_cancel);

        buttonStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                custInfo.fat=addFat.getText().toString();
                custInfo.muscle=addMuscle.getText().toString();
                custInfo.weight=addWeight.getText().toString();
                //DB 저장
                mDatabase.child("customers").child(uid).child("info").child(date).setValue(custInfo);
                myDialog.cancel();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel();
            }
        });
    }
    public void updateUI(){

        addWeight.setText(custInfo.weight);
        addFat.setText(custInfo.fat);
        addMuscle.setText(custInfo.muscle);
    }
}
