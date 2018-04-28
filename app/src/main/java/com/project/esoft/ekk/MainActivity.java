package com.project.esoft.ekk;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Network;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import static com.project.esoft.ekk.LoginActivity.session_status;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "JSAGoogleSignIn";
    private static final int REQUEST_CODE_SIGN_IN = 1234;
    private static final String WEB_CLIENT_ID = "399080402993-0taoq5nh3cp8ldun40ibgbg1eimtnbrv.apps.googleusercontent.com";

    private FirebaseAuth mAuth;
    SharedPreferences sharedpreferences;
    private GoogleApiClient mGoogleApiClient;
    private TextView txtStatus;
    private TextView txtDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtStatus = findViewById(R.id.txtStatus);
        txtDetail = findViewById(R.id.txtDetail);

        findViewById(R.id.btn_sign_in).setOnClickListener(this);
        findViewById(R.id.btn_sign_out).setOnClickListener(this);
        findViewById(R.id.btn_disconnect).setOnClickListener(this);

        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(WEB_CLIENT_ID)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onBackPressed() {

            super.onBackPressed();

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed():" + connectionResult);
        Toast.makeText(getApplicationContext(), "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_sign_in) {
            signIn();
        } else if (i == R.id.btn_sign_out) {
            signOut();
        } else if (i == R.id.btn_disconnect) {
            revokeAccess();
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean(session_status, false);
            editor.commit();
            Intent g = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(g);
            finish();
        }
    }

    private void signIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent, REQUEST_CODE_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent();
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // successful -> authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // failed -> update UI
                updateUI(null);
                Toast.makeText(getApplicationContext(), "SignIn: failed!",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.e(TAG, "firebaseAuthWithGoogle():" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Log.e(TAG, "signInWithCredential: Success!");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // Sign in fails
                            Log.w(TAG, "signInWithCredential: Failed!", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed!",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });
    }

    private void signOut() {
        // sign out Firebase
        mAuth.signOut();

        // sign out Google
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null);
                    }
                });
    }

    private void revokeAccess() {
        // sign out Firebase
        mAuth.signOut();

        // revoke access Google
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            txtStatus.setText("Google User email: " + user.getEmail());
            txtDetail.setText("Firebase User ID: " + user.getUid());

            findViewById(R.id.btn_sign_in).setVisibility(View.GONE);
            findViewById(R.id.layout_sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
            txtStatus.setText("Signed Out");
            txtDetail.setText(null);

            findViewById(R.id.btn_sign_in).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }
}