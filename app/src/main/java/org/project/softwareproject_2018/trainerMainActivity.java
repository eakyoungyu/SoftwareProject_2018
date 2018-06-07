package org.project.softwareproject_2018;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class trainerMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView nameTextView;
    private CalendarView calendarView;
    private ArrayList<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>() ;
    private HashMap<String, String> item;
    private SimpleAdapter adapter;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private String curTid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trainer_main);
        auth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        curTid=auth.getCurrentUser().getUid();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);

        nameTextView = (TextView)view.findViewById(R.id.header_name_textview);
        nameTextView.setText(auth.getCurrentUser().getEmail()+"님");

        // listview 생성 및 adapter 지정.
        final ListView listview = (ListView) findViewById(R.id.listview) ;
        adapter = new SimpleAdapter(this, items, android.R.layout.simple_list_item_2,
                new String[]{"time", "customer"},
                new int[]{android.R.id.text1, android.R.id.text2}) ;
        listview.setAdapter(adapter) ;

        calendarView = (CalendarView) findViewById(R.id.calendar);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String curdate=year+"-"+(month+1)+"-"+dayOfMonth;

                //Arraylist 초기화
                adapter.notifyDataSetChanged();
                items.clear();
                item = new HashMap<String, String>();
                Query getRes =database.getReference().child("trainers").child(curTid).child("reservtimes").child(curdate);
                getRes.addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                    String cid=snapshot.getKey();
                                    String startTime=snapshot.getValue(String.class).trim();
                                    updateListView(startTime, cid);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                // listview 갱신

            }
        });

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
        getMenuInflater().inflate(R.menu.trainer_menu, menu);
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

        if (id == R.id.nav_share) {
            logoutUser();
            finish();
            Intent intent = new Intent(trainerMainActivity.this, LoginActivity.class);
            startActivity(intent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logoutUser(){
        FirebaseAuth.getInstance().signOut();
    }
    private void updateListView(final String startTime, final String cid){
        database.getReference().child("customers").child(cid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name=dataSnapshot.child("name").getValue(String.class);
                        item.put("customer", name);
                        item.put("time", startTime+":00");
                        items.add(item);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(trainerMainActivity.this,name, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }
}
