package org.project.softwareproject_2018;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

public class customerInformation extends AppCompatActivity {

    private TextView textViewWeight;
    private TextView textViewMuscle;
    private TextView textViewFat;
    private String weight;
    private String muscle;
    private String fat;
    private CalendarView calendarView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_information);

        textViewWeight = (TextView)findViewById(R.id.weight);
        textViewMuscle = (TextView)findViewById(R.id.skeletal_muscle_mass);
        textViewFat = (TextView)findViewById(R.id.body_fat_mass);

        calendarView = (CalendarView)findViewById(R.id.calendar);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                //해당 날짜 나의 정보 불러오기
                textViewWeight.setText(weight);
                textViewFat.setText(fat);
                textViewMuscle.setText(muscle);

                showInformationAdd(year, month, dayOfMonth);
            }
        });
    }

    private void showInformationAdd(int year, int month, int dayOfMonth){
        LayoutInflater dialog = LayoutInflater.from(customerInformation.this);
        final View dialogLayout = dialog.inflate(R.layout.customer_information_popup, null);
        final Dialog myDialog = new Dialog(customerInformation.this);

        myDialog.setContentView(dialogLayout);
        myDialog.show();

        final EditText addWeight = (EditText)dialogLayout.findViewById(R.id.add_weight);
        final EditText addFat = (EditText)dialogLayout.findViewById(R.id.add_bmi);
        final EditText addMuscle = (EditText)dialogLayout.findViewById(R.id.add_muscle);
        Button buttonStore = (Button)dialogLayout.findViewById(R.id.button_store);
        Button buttonCancel = (Button)dialogLayout.findViewById(R.id.button_cancel);

        buttonStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fat = addFat.getText().toString();
                muscle = addMuscle.getText().toString();
                weight = addWeight.getText().toString();
                //DB 저장
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
}
