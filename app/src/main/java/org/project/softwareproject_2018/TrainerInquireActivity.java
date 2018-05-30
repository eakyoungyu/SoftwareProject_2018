package org.project.softwareproject_2018;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TrainerInquireActivity extends AppCompatActivity {

    private RadioGroup radioGroup;

    private TextView textViewName1;
    private TextView textViewName2;
    private TextView textViewName3;
    private TextView textViewName4;
    private TextView textViewName5;

    private ImageView imageViewTrainer1;
    private ImageView imageViewTrainer2;
    private ImageView imageViewTrainer3;
    private ImageView imageViewTrainer4;
    private ImageView imageViewTrainer5;

    private TextView textViewType1;
    private TextView textViewType2;
    private TextView textViewType3;
    private TextView textViewType4;
    private TextView textViewType5;

    private Button buttonCheck;
    private Button buttonCancel;

    private FirebaseDatabase database;
    Context mContext;

    private List<Trainer> trainers = new ArrayList<>();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trainer_inquire);

        textViewName1=(TextView)findViewById(R.id.trainerInquireUI_textView_name1);
        textViewName2=(TextView)findViewById(R.id.trainerInquireUI_textView_name2);
        textViewName3=(TextView)findViewById(R.id.trainerInquireUI_textView_name3);
        textViewName4=(TextView)findViewById(R.id.trainerInquireUI_textView_name4);
        textViewName5=(TextView)findViewById(R.id.trainerInquireUI_textView_name5);

        textViewType1=(TextView)findViewById(R.id.trainerInquireUI_textView_type1);
        textViewType2=(TextView)findViewById(R.id.trainerInquireUI_textView_type2);
        textViewType3=(TextView)findViewById(R.id.trainerInquireUI_textView_type3);
        textViewType4=(TextView)findViewById(R.id.trainerInquireUI_textView_type4);
        textViewType5=(TextView)findViewById(R.id.trainerInquireUI_textView_type5);

        /*
        database = FirebaseDatabase.getInstance();

        database.getReference().child("trainer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Trainer trainer = snapshot.getValue(Trainer.class);
                    trainers.add(trainer);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(mContext, "트레이너정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        textViewName1.setText(trainers.get(0).name);
        textViewType1.setText(trainers.get(0).type);
        textViewName2.setText(trainers.get(1).name);
        textViewType2.setText(trainers.get(1).type);
        textViewName3.setText(trainers.get(2).name);
        textViewType3.setText(trainers.get(2).type);
        textViewName4.setText(trainers.get(3).name);
        textViewType4.setText(trainers.get(3).type);
        textViewName5.setText(trainers.get(4).name);
        textViewType5.setText(trainers.get(4).type);

        */

        textViewName1.setText("1번");
        textViewType1.setText("상");
        textViewName2.setText("2번");
        textViewType2.setText("중");
        textViewName3.setText("3번");
        textViewType3.setText("하");
        textViewName4.setText("4번");
        textViewType4.setText("중");
        textViewName5.setText("5번");
        textViewType5.setText("상");

        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
        buttonCheck=(Button)findViewById(R.id.trainerInquireUI_button_check);
        buttonCancel=(Button)findViewById(R.id.trainerInquireUI_button_cancel);

        buttonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                int selected = radioGroup.getCheckedRadioButtonId();


                if(selected == -1)
                    //트레이너를 선택하지 않았을 때
                    Toast.makeText(TrainerInquireActivity.this, "트레이너를 선택해주세요.", Toast.LENGTH_SHORT).show();
                else {
                    //트레이너를 선택했을 때
                    switch (radioGroup.getCheckedRadioButtonId()) {
                        case R.id.trainerInquireUI_radioButton_selectTrainer1:
                            intent.putExtra("contact_trainer", textViewName1.getText().toString());
                            break;
                        case R.id.trainerInquireUI_radioButton_selectTrainer2:
                            intent.putExtra("contact_trainer", textViewName2.getText().toString());
                            break;
                        case R.id.trainerInquireUI_radioButton_selectTrainer3:
                            intent.putExtra("contact_trainer", textViewName3.getText().toString());
                            break;
                        case R.id.trainerInquireUI_radioButton_selectTrainer4:
                            intent.putExtra("contact_trainer", textViewName4.getText().toString());
                            break;
                        case R.id.trainerInquireUI_radioButton_selectTrainer5:
                            intent.putExtra("contact_trainer", textViewName5.getText().toString());
                            break;
                    }

                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
