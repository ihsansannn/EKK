package com.project.esoft.ekk;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.esoft.ekk.ActivityUser.AdminActivity;
import com.project.esoft.ekk.ActivityUser.ManagerActivity;
import com.project.esoft.ekk.ActivityUser.MarketingActivity;
import com.project.esoft.ekk.ActivityUser.UnverifiedActivity;
import com.project.esoft.ekk.Constructor.Pegawai;


public class SplashActivity extends AppCompatActivity {

    private static int splashInterval = 2000;
    public final static String TAG_EMAIL = "email";
    public final static String TAG_NAME = "nama";
    public final static String TAG_USERNAME = "username";
    public final static String TAG_ID = "id";



    SharedPreferences sharedpreferences;
    Boolean session = false;
    String id, username;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";
    private String value;
    private DatabaseReference ref;
    String status, name, email, type;
    private FirebaseAuth mAuth;
    Pegawai pegawai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
        KeepStatusBar();

        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        ref = database.child("users");

        mAuth= FirebaseAuth.getInstance();


        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                if (session) {

                    FirebaseUser user = mAuth.getCurrentUser();
                    String uID = user.getUid();

                    ref.child(uID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            pegawai = dataSnapshot.getValue(Pegawai.class);
                            pegawai.setUid(dataSnapshot.getKey());
                            status = pegawai.getStatus();
                            type = pegawai.getType();
                            email = pegawai.getEmail();
                            name = pegawai.getNama();


                            if(!status.equals("unverified") && !status.equals("verified")){
                                Intent intent = new Intent(SplashActivity.this, TambahPegawaiActivity.class);
                                intent.putExtra(TAG_EMAIL, email);
                                intent.putExtra(TAG_NAME, name);
                                finish();
                                startActivity(intent);
                            }
                            else if(status.equals("unverified")){
                                Intent intent = new Intent(SplashActivity.this, UnverifiedActivity.class);
                                intent.putExtra(TAG_EMAIL, email);
                                intent.putExtra(TAG_NAME, name);
                                finish();
                                startActivity(intent);
                            }

                            else if(status.equals("verified")){

                                if(type.equals("Admin")){
                                    Intent intent = new Intent(SplashActivity.this, AdminActivity.class);
                                    intent.putExtra(TAG_EMAIL, email);
                                    intent.putExtra(TAG_NAME, name);
                                    finish();
                                    startActivity(intent);
                                }

                                else if(type.equals("Marketing")){

                                    Intent intent = new Intent(SplashActivity.this, MarketingActivity.class);
                                    intent.putExtra(TAG_EMAIL, email);
                                    intent.putExtra(TAG_NAME, name);
                                    finish();
                                    startActivity(intent);
                                }
                                else if(type.equals("Manager")){

                                    Intent intent = new Intent(SplashActivity.this, ManagerActivity.class);
                                    intent.putExtra(TAG_EMAIL, email);
                                    intent.putExtra(TAG_NAME, name);
                                    finish();
                                    startActivity(intent);
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError error) {
                        }
                    });
                }
                if (!session) {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    finish();
                    startActivity(intent);
                }

                //jeda selesai Splashscreen
                this.finish();
            }

            private void finish() {
                // TODO Auto-generated method stub
            }
        }, splashInterval);

    };
    private void KeepStatusBar(){
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
    }
}