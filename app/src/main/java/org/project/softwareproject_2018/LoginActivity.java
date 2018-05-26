package org.project.softwareproject_2018;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    //TAG
    private static final String TAG = LoginActivity.class.getSimpleName();

    //UI
    private Button buttonJoin;
    private Button buttonLogin;
    private EditText editTextEmail;
    private EditText editTextPassword;


    //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    //Toast
    Context mContext;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mContext=this;

        //UI
        buttonLogin=(Button)findViewById(R.id.login_button_login);
        buttonJoin=(Button)findViewById(R.id.login_button_join);
        editTextEmail=(EditText)findViewById(R.id.login_editText_email);
        editTextPassword=(EditText)findViewById(R.id.login_editText_password);

        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Activity 전환
                Intent joinIntent = new Intent(LoginActivity.this, JoinActivity.class);
                startActivity(joinIntent);

                /*추가할 경우 loginActicity 종료
                finish();
                */
            }
        });


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=editTextEmail.getText().toString();
                String passwd=editTextPassword.getText().toString();
                if(email.equals(""))
                    Toast.makeText(mContext, "이메일을 입력해주세요.",
                            Toast.LENGTH_SHORT).show();
                else if(passwd.equals(""))
                    Toast.makeText(mContext, "비밀번호를 입력해주세요.",
                            Toast.LENGTH_SHORT).show();
                else
                    loginUser(email, passwd);
            }
        });
    }
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    private void loginUser(final String email, final String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");

                            //                       updateUI(user);
                            Toast.makeText(mContext, "로그인 성공",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, customerMainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(mContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //                      updateUI(null);
                        }

                        // ...
                    }
                });
    }
    private void SignOutUser(){
        FirebaseAuth.getInstance().signOut();
    }
}
