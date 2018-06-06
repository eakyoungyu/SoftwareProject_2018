package org.project.softwareproject_2018;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class customerMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static final int REQ_ADD_CONTACT = 1;

    private TextView nameTextView;
    private TextView trainerTextView;
    private TextView goalTextView;
    private CalendarView calendarView;

    private Button reservationButton;
    private Button buttonTrainerChange;

    private FirebaseAuth auth;

    private DatabaseReference mDatabase;
    private Customer currentCust;
    private Trainer currentTrai;
    private UserScheduleInquireSystem userScheduleInquireSystem;
    private ArrayList<ReservationTime> rts;
    private String date;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        currentCust=new Customer();
        currentTrai=new Trainer();

        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);


        nameTextView = (TextView)view.findViewById(R.id.header_name_textview);
        trainerTextView = (TextView)view.findViewById(R.id.header_trainer_textView);
        goalTextView = (TextView)view.findViewById(R.id.header_goal_textView);

        calendarView = (CalendarView) findViewById(R.id.calendar);

        reservationButton = (Button)findViewById(R.id.rec_button);
        buttonTrainerChange = (Button)findViewById(R.id.trainer_change_button);


        nameTextView.setText(auth.getCurrentUser().getEmail()+"님");
        goalTextView.setText("-");
        getCurrentUserInfo(auth.getCurrentUser().getUid());
        //메뉴창 고객 이름, 트레이너, 목표 설정

        //rts=userScheduleInquireSystem.getReseravtion(auth.getCurrentUser().getUid());

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                ShowRecPopupDialog(year, month, dayOfMonth);
            }
        });

        //예약 버튼
        reservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowReservation();
           }
        });

        //다른 트레이너와 예약 버튼
        buttonTrainerChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(customerMainActivity.this, TrainerInquireActivity.class);
                startActivityForResult(intent, REQ_ADD_CONTACT);
            }
        });

    }



    private void ShowReservation(){
        LayoutInflater dialog = LayoutInflater.from(customerMainActivity.this);
        final View dialogLayout = dialog.inflate(R.layout.reservation_calendar, null);
        final Dialog myDialog = new Dialog(customerMainActivity.this);

        myDialog.setContentView(dialogLayout);
        myDialog.show();

        CalendarView calendarView = (CalendarView)dialogLayout.findViewById(R.id.calendar);

        //예약-날짜 버튼 클릭
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                date = year + "-" +month + "-" + dayOfMonth;
                ShowRecView(year, month, dayOfMonth);
                myDialog.cancel();
            }
        });
    }

    //customer_rec_view 팝업창 띄우기
    private void ShowRecView(int year, int month, int day){
        final LayoutInflater dialog = LayoutInflater.from(customerMainActivity.this);
        final View dialogLayout = dialog.inflate(R.layout.customer_rec_view, null);
        final Dialog myDialog = new Dialog(customerMainActivity.this);

        myDialog.setContentView(dialogLayout);
        myDialog.show();

        RadioGroup radioGroup = (RadioGroup)dialogLayout.findViewById(R.id.rec_Buttons);

        //예약 불가능한 시간 버튼 비활성화

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
             @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected = (RadioButton)dialogLayout.findViewById(checkedId);
                time = selected.getText().toString().substring(0,7);
                Toast.makeText(customerMainActivity.this, date+"/"+time, Toast.LENGTH_SHORT).show();
                myDialog.cancel();
            }
        });

    }



    private void ShowRecPopupDialog(int year, int month, int day) {
        LayoutInflater dialog = LayoutInflater.from(customerMainActivity.this);
        final View dialogLayout = dialog.inflate(R.layout.activity_rec_popup, null);
        final Dialog myDialog = new Dialog(customerMainActivity.this);

        myDialog.setContentView(dialogLayout);
        myDialog.show();

        TextView DialogDate = (TextView)dialogLayout.findViewById(R.id.rec_date);
        DialogDate.setText(year+"-"+month+"-"+day);

        TextView DialogTime = (TextView)dialogLayout.findViewById(R.id.rec_time);
        TextView DialogTrainer = (TextView)dialogLayout.findViewById(R.id.rec_name);

        Button buttonReservationChange = (Button)dialogLayout.findViewById(R.id.change_button);
        Button buttonReservationCancel = (Button)dialogLayout.findViewById(R.id.cancel_button);


        DialogDate.setText(year+"-"+month+"-"+day); //예약 날짜
        //예약 시간
        //예약 트레이너

        //예약 변경 버튼
        buttonReservationChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowReservation();
            }
        });

        //예약 취소 버튼
        buttonReservationCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowRecCancel();
            }
        });


    }

    //예약 취소 확인 팝업창 띄우기
    private void ShowRecCancel(){
        LayoutInflater dialog = LayoutInflater.from(customerMainActivity.this);
        final View dialogLayout = dialog.inflate(R.layout.activity_rec_cancel_popup, null);
        final Dialog myDialog = new Dialog(customerMainActivity.this);

        myDialog.setContentView(dialogLayout);
        myDialog.show();

        Button buttonYes = (Button)dialogLayout.findViewById(R.id.yes_button);
        Button buttonNo = (Button)dialogLayout.findViewById(R.id.no_button);

        //예약 삭제하시겠습니까->예
        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //예약 삭제
            }
        });

        //예약 삭제하시겠습니까->아니오
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel();
            }
        });
    }

    //다른 트레이너와 예약(트레이너 선택 후)
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQ_ADD_CONTACT) {
            if (resultCode == RESULT_OK) {
                String trainer = intent.getStringExtra("contact_trainer") ; //고객이 선택한 다른 트레이너 tid
                ShowReservation(); //reservation_calendar 팝업창 띄우기
            }
        }
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.customer_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action
            //나의 신체정보
        }
        else if(id == R.id.nav_share){
            logoutUser();
            finish();
            Intent intent = new Intent(customerMainActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void logoutUser(){
        FirebaseAuth.getInstance().signOut();
    }
    public void getCurrentUserInfo(final String uid){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("customers").child(uid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        currentCust.name = dataSnapshot.child("name").getValue(String.class);
                        currentCust.goal = dataSnapshot.child("goal").getValue(String.class);
                        updateUI();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
    private void updateUI(){
        trainerTextView.setText("고객: " +currentCust.name);
        goalTextView.setText(currentCust.goal);

    }
}
