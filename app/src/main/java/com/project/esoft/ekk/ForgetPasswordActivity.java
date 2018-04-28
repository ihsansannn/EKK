package com.project.esoft.ekk;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    public final static String TAG_EMAIL = "email";
    FirebaseAuth mAuth;
    Button btnResetPassword;
    EditText edtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        edtEmail = findViewById(R.id.textForgetPassword);
        edtEmail.setText(getIntent().getStringExtra(TAG_EMAIL));
        btnResetPassword = findViewById(R.id.buttonForgetPassword);
        mAuth= FirebaseAuth.getInstance();

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = edtEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter your email!", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgetPasswordActivity.this, "Check email to reset your password!", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                } else {
                                    Toast.makeText(ForgetPasswordActivity.this, "Fail to send reset password email!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
