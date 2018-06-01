package org.project.softwareproject_2018;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class TrainerInquireActivity extends AppCompatActivity {


    //Firebase
    private DatabaseReference mDatabase;
    private FirebaseStorage storage;
    private StorageReference gsReference;
    private StorageReference pathReference;
    private Uri retUri;
    private Uri tUri;

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

    private Button buttonTrainer1;
    private Button buttonTrainer2;
    private Button buttonTrainer3;
    private Button buttonTrainer4;
    private Button buttonTrainer5;

    private Button buttonCancel;

    private FirebaseDatabase database;
    Context mContext;

    private ArrayList<Trainer> trainers = new ArrayList<Trainer>();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trainer_inquire);

        //image URI
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();


        imageViewTrainer1=(ImageView)findViewById(R.id.trainerInquireUI_imageView_image1);
        imageViewTrainer1.setImageURI(tUri);

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

        buttonTrainer1 = (Button)findViewById(R.id.trainerInquireUI_Button_selectTrainer1) ;
        buttonTrainer2 = (Button)findViewById(R.id.trainerInquireUI_Button_selectTrainer2) ;
        buttonTrainer3 = (Button)findViewById(R.id.trainerInquireUI_Button_selectTrainer3) ;
        buttonTrainer4 = (Button)findViewById(R.id.trainerInquireUI_Button_selectTrainer4) ;
        buttonTrainer5 = (Button)findViewById(R.id.trainerInquireUI_Button_selectTrainer5) ;

        database = FirebaseDatabase.getInstance();
        Query getTrainerData = database.getReference().child("trainer");
        getTrainerData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                trainers.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String uid=snapshot.getKey();
                    String email=snapshot.child("email").getValue(String.class);
                    String image=snapshot.child("image").getValue(String.class);    //path
                    String name=snapshot.child("name").getValue(String.class);
                    String type=snapshot.child("type").getValue(String.class);
                    Trainer trainer=new Trainer(uid, email, image, name, type);
                    trainers.add(trainer);
                }
                //Toast message 출력하면 오류
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

                tUri=getUri(trainers.get(0).image);     //uri
                Glide.with(TrainerInquireActivity.this).load(tUri).into(imageViewTrainer1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(mContext, "트레이너정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });



        buttonCancel=(Button)findViewById(R.id.trainerInquireUI_button_cancel);

        buttonTrainer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                intent.putExtra("contact_trainer", trainers.get(0).uid);

                setResult(RESULT_OK, intent);
                finish();
            }
        });

        buttonTrainer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                intent.putExtra("contact_trainer", trainers.get(1).uid);

                setResult(RESULT_OK, intent);
                finish();
            }
        });

        buttonTrainer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                intent.putExtra("contact_trainer", trainers.get(2).uid);

                setResult(RESULT_OK, intent);
                finish();
            }
        });

        buttonTrainer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                intent.putExtra("contact_trainer", trainers.get(3).uid);

                setResult(RESULT_OK, intent);
                finish();
            }
        });

        buttonTrainer5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                intent.putExtra("contact_trainer", trainers.get(4).uid);

                setResult(RESULT_OK, intent);
                finish();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
    public Uri getUri(String path){
        gsReference = storage.getReferenceFromUrl("gs://temp-30f22.appspot.com");
        pathReference = gsReference.child(path);
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                retUri= uri;
                //Toast.makeText(getApplicationContext(), "다운로드 성공 : "+ tUri, Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(getApplicationContext(), "다운로드 실패", Toast.LENGTH_SHORT).show();
            }
        });
        return retUri;
    }
}
