package com.project.esoft.ekk;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private EditText emailReg;
    private EditText passwordReg;
    private EditText passwordRegRe;
    private EditText nameReg;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private DatabaseReference mDatabase;
    TextView loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //hide keyboard when activity open
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        nameReg = findViewById(R.id.nameRegText);
        emailReg =  findViewById(R.id.emailRegText);
        passwordReg =  findViewById(R.id.passwordRegText);
        passwordRegRe = findViewById(R.id.passwordRegRetypeText);
        progressBar = findViewById (R.id.progressBarReg);
        progressBar.setVisibility(View.INVISIBLE);
        firebaseAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        loginLink= findViewById(R.id.haveAccount);

        loginLink.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void regButton_Click (View v){
        if(emailReg.getText().toString().equals("")||passwordReg.getText().toString().equals("")||
                passwordRegRe.getText().toString().equals(""))
        {
            Toast.makeText(SignupActivity.this, "Kolom tidak boleh kosong", Toast.LENGTH_LONG).show();
        }
        else if(passwordReg.getText().toString().equals(passwordRegRe.getText().toString())&&
                !emailReg.getText().toString().equals("")) {
            progressBar.setVisibility(View.VISIBLE);
            (firebaseAuth.createUserWithEmailAndPassword(emailReg.getText().toString(), passwordReg.getText().toString())).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        String uID = user.getUid().toString();

                        user.sendEmailVerification();

                        mDatabase.child("users").child(uID).child("email").setValue(emailReg.getText().toString());
                        mDatabase.child("users").child(uID).child("nama").setValue(nameReg.getText().toString());
                        mDatabase.child("users").child(uID).child("status").setValue("");
                        mDatabase.child("users").child(uID).child("type").setValue("");

                        Toast.makeText(SignupActivity.this, "Registration Success", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(i);
                    } else {
                        Log.e("ERROR", task.getException().toString());
                        Toast.makeText(SignupActivity.this, "Registration Failed", Toast.LENGTH_LONG).show();
                    }

                    progressBar.setVisibility(View.GONE);
                }
            });
        }
        else if (!passwordReg.getText().toString().equals(passwordRegRe.getText().toString())) {
            Toast.makeText(SignupActivity.this, "Password is not match", Toast.LENGTH_LONG).show();
        }

    }

    public void loginButton_Click(){
        Intent i = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }


}

