package org.project.softwareproject_2018;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by my on 2018-05-18.
 */
public class JoinActivity extends AppCompatActivity {
    //TAG
    private static final String TAG = JoinActivity.class.getSimpleName();
    static final int REQ_ADD_CONTACT = 1 ;

    //UI
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextpwcheck;
    private EditText editTextGoal;
    private EditText editTextname;
    private Button buttonCheckPW;
    private Button buttonJoin;
    private Button buttontrainer;


    //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    //Toast
    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_layout);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mContext=this;

        //UI
        editTextEmail=(EditText)findViewById(R.id.join_editText_email);
        editTextPassword=(EditText)findViewById(R.id.join_editText_password);
        editTextpwcheck=(EditText)findViewById(R.id.join_editText_password_check);
        buttonCheckPW=(Button)findViewById(R.id.join_button_pw_check);
        editTextname=(EditText)findViewById(R.id.join_editText_name);
        editTextGoal=(EditText)findViewById(R.id.join_editText_goal);
        buttonJoin=(Button)findViewById(R.id.join_button_join);
        buttontrainer=(Button)findViewById(R.id.join_button_trainer);

        buttonJoin.setEnabled(false);

        buttonCheckPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passwd=editTextPassword.getText().toString();
                String chk=editTextpwcheck.getText().toString();
                if(passwd.equals(chk)){
                    Toast.makeText(mContext, "비밀번호가 일치합니다.", Toast.LENGTH_SHORT).show();
                    buttonJoin.setEnabled(true);
                }
                else{
                    Toast.makeText(mContext, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                       //버튼 비활성화
                }
            }
        });

        buttontrainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JoinActivity.this, TrainerInquireActivity.class);
                startActivityForResult(intent, REQ_ADD_CONTACT);
            }
        });

        //Textfield를 다 채우지 않으면 emailLogin 비활성화
        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=editTextEmail.getText().toString();
                String passwd=editTextPassword.getText().toString();
                String name=editTextname.getText().toString();
                String goal=editTextGoal.getText().toString();
                String tid="";
                if(email.equals(""))
                    Toast.makeText(mContext, "이메일을 입력해주세요.",
                            Toast.LENGTH_SHORT).show();
                else if(passwd.equals(""))
                    Toast.makeText(mContext, "비밀번호를 입력해주세요.",
                            Toast.LENGTH_SHORT).show();
                else if(name.equals(""))
                    Toast.makeText(mContext, "이름을 입력해주세요.",

                            Toast.LENGTH_SHORT).show();
                else {
                    createUser(email, passwd, goal, name, tid);
                }
            }
        });
    }

    @Override
    //트레이너 선택 창에서 선택한 트레이너 반환-표시
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQ_ADD_CONTACT) {
            if (resultCode == RESULT_OK) {

                TextView textViewName = (TextView) findViewById(R.id.join_selected_trainer) ;
                String trainer = intent.getStringExtra("contact_trainer") ;
                textViewName.setText(trainer) ;
            }
        }
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    public void createUser(final String email, final String password, final String goal, final String name, final String tid){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();
                            writeNewUser(user.getUid(), name, email, goal, tid);
                            //Toast.makeText(mContext, "회원가입 완료", Toast.LENGTH_SHORT).show();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(JoinActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //                       updateUI(null);
                        }
                    }
                });
    }
    //menu에서 사용
    private void getCurrentUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show();
        }
    }

    private void writeNewUser(String userId, String name, String email, String goal, String tid) {
        /*
        User user = new User(name, email, goal);
        mDatabase.child("users").child(userId).setValue(user);
        */
        Customer customer = new Customer(name, email, goal, tid);
        mDatabase.child("users").child(userId).setValue(customer);
    }
}
