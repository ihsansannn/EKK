package com.project.esoft.ekk.ActivityUser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.esoft.ekk.Constructor.Pegawai;
import com.project.esoft.ekk.LoginActivity;
import com.project.esoft.ekk.R;

public class UnverifiedActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener {

    Button logout;

    SharedPreferences sharedpreferences;
    public static final String session_status = "session_status";

    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private static final String WEB_CLIENT_ID = "399080402993-0taoq5nh3cp8ldun40ibgbg1eimtnbrv.apps.googleusercontent.com";

    DatabaseReference mDatabase;
    private DatabaseReference ref;
    Pegawai pegawai;

    String uID, status, type;

    public final static String TAG_EMAIL = "email";
    public final static String TAG_NAME = "nama";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unverified);

        logout = findViewById(R.id.buttonLogoutUnverified);

        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uID = user.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        ref = mDatabase.child("users");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(WEB_CLIENT_ID)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revokeAccess();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(session_status, false);
                editor.commit();
                Intent i = new Intent(UnverifiedActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        ref.child(uID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                pegawai = dataSnapshot.getValue(Pegawai.class);
                pegawai.setUid(dataSnapshot.getKey());
                status = pegawai.getStatus();
                type = pegawai.getType();
                if(status.equals("verified")){

                    if(type.equals("Admin")){
                        Intent intent = new Intent(UnverifiedActivity.this, AdminActivity.class);
                        intent.putExtra(TAG_EMAIL, pegawai.getEmail());
                        intent.putExtra(TAG_NAME, pegawai.getNama());
                        finish();
                        startActivity(intent);
                    }

                    else if(type.equals("Marketing")){

                        Intent intent = new Intent(UnverifiedActivity.this, MarketingActivity.class);
                        intent.putExtra(TAG_EMAIL, pegawai.getEmail());
                        intent.putExtra(TAG_NAME, pegawai.getNama());
                        finish();
                        startActivity(intent);
                    }
                    else if(type.equals("Manager")){
                        Intent intent = new Intent(UnverifiedActivity.this, ManagerActivity.class);
                        intent.putExtra(TAG_EMAIL, pegawai.getEmail());
                        intent.putExtra(TAG_NAME, pegawai.getNama());
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

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    private void revokeAccess() {
        mAuth.signOut();
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                    }
                });
    }
}
