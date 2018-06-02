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
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class customerMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    private TextView nameTextView;
    private TextView trainerTextView;
    private TextView goalTextView;
    private CalendarView calendarView;

    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    private Customer currentCust;
    private Trainer currentTrai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);




        auth = FirebaseAuth.getInstance();
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


        nameTextView.setText(auth.getCurrentUser().getEmail()+"님");
        goalTextView.setText("-");
        //메뉴창 고객 이름, 트레이너, 목표 설정
        getCurrentUserInfo(auth.getCurrentUser().getUid());

        calendarView = (CalendarView) findViewById(R.id.calendar);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                ShowDialog(year, month, dayOfMonth);
            }
        });

    }

    private void ShowDialog(int year, int month, int day) {
        LayoutInflater dialog = LayoutInflater.from(customerMainActivity.this);
        final View dialogLayout = dialog.inflate(R.layout.activity_rec_popup, null);
        final Dialog myDialog = new Dialog(customerMainActivity.this);

        myDialog.setContentView(dialogLayout);
        myDialog.show();

        TextView DialogDate = (TextView)dialogLayout.findViewById(R.id.rec_date);
        DialogDate.setText(year+"-"+month+"-"+day);
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
                        if(!dataSnapshot.exists()){
                            mDatabase.child("trainers").child(uid).addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            currentTrai.name = dataSnapshot.child("name").getValue(String.class);
                                            updateUI("Trainer");
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    }
                            );
                        }else {
                            currentCust.name = dataSnapshot.child("name").getValue(String.class);
                            currentCust.goal = dataSnapshot.child("goal").getValue(String.class);
                            updateUI("Customer");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }
    private void updateUI(String user){
        if(user=="Customer") {
            trainerTextView.setText("고객: " +currentCust.name);
            goalTextView.setText(currentCust.goal);
        }
        if(user=="Trainer"){
            trainerTextView.setText("트레이너: " +currentTrai.name);
            //나의 정보 버튼 없애기
        }
    }
}
