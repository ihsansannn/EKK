package com.project.esoft.ekk;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.SharedPreferences;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.project.esoft.ekk.ActivityUser.AdminActivity;
import com.project.esoft.ekk.ActivityUser.ManagerActivity;
import com.project.esoft.ekk.ActivityUser.MarketingActivity;
import com.project.esoft.ekk.ActivityUser.UnverifiedActivity;
import com.project.esoft.ekk.Constructor.Pegawai;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "JSAGoogleSignIn";
    private static final int REQUEST_CODE_SIGN_IN = 1234;
    private static final String WEB_CLIENT_ID = "399080402993-0taoq5nh3cp8ldun40ibgbg1eimtnbrv.apps.googleusercontent.com";

    private static final int REQUEST_SIGNUP = 0;
    private EditText emailLogin;
    private EditText passwordLogin;
    private TextView googleLogin, forgetLogin;
    private FirebaseAuth mAuth;
    TextView _signupLink;

    private Pegawai pegawai;

    String bugg;

    private DatabaseReference mDatabase;

    private GoogleApiClient mGoogleApiClient;

    public final static String TAG_EMAIL = "email";
    public final static String TAG_NAME = "nama";
    public final static String TAG_TYPE = "type";
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    SharedPreferences sharedpreferences;

    Boolean session = false;
    String email;
    String name;
    String uID;
    private String status, type;

    String dataCatch;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        _signupLink = findViewById(R.id.link_signup);
        emailLogin = findViewById(R.id.emailText);
        passwordLogin = findViewById(R.id.passwordText);
        googleLogin = findViewById(R.id.loginGoogleButton);
        forgetLogin = findViewById(R.id.forgotLogin);
        mAuth= FirebaseAuth.getInstance();
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();


        mDatabase = FirebaseDatabase.getInstance().getReference();
        ref = database.child("users");


        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        email = sharedpreferences.getString(TAG_EMAIL, null);
        email = sharedpreferences.getString(TAG_NAME, null);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(WEB_CLIENT_ID)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
            }
        });

        googleLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the SignIn with Google
                signIn();
            }
        });

        forgetLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ForgetPasswordActivity.class);
                i.putExtra(TAG_EMAIL, emailLogin.getText().toString() );
                startActivity(i);
            }
        });
    }

    public void buttonLogin_Click(View v){

        if(emailLogin.getText().toString().equals("")||passwordLogin.getText().toString().equals(""))
        {
            Toast.makeText(LoginActivity.this, "Field cannot be empty", Toast.LENGTH_LONG).show();
        }
        else {
            final ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this, "Please wait..", "Processing", true);
            (mAuth.signInWithEmailAndPassword(emailLogin.getText().toString(), passwordLogin.getText().toString()))
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();

                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                if (user.isEmailVerified()) {
                                    email = mAuth.getCurrentUser().getEmail().toString();
                                    setPreferences("name", TAG_NAME);
                                    setPreferences("type", TAG_TYPE);
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putBoolean(session_status, true);
                                    editor.putString(TAG_EMAIL, email);
                                    editor.commit();
                                    Log.e(TAG, "signInWithCredential: Success!");
                                    uID = user.getUid();


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
                                                Intent intent = new Intent(LoginActivity.this, TambahPegawaiActivity.class);
                                                intent.putExtra(TAG_EMAIL, email);
                                                intent.putExtra(TAG_NAME, name);
                                                finish();
                                                startActivity(intent);
                                            }
                                            else if(status.equals("unverified")){
                                                Intent intent = new Intent(LoginActivity.this, UnverifiedActivity.class);
                                                intent.putExtra(TAG_EMAIL, email);
                                                intent.putExtra(TAG_NAME, name);
                                                finish();
                                                startActivity(intent);
                                            }

                                            else if(status.equals("verified")){

                                                if(type.equals("Admin")){
                                                    Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                                                    intent.putExtra(TAG_EMAIL, email);
                                                    intent.putExtra(TAG_NAME, name);
                                                    finish();
                                                    startActivity(intent);
                                                }

                                                else if(type.equals("Marketing")){

                                                    Intent intent = new Intent(LoginActivity.this, MarketingActivity.class);
                                                    intent.putExtra(TAG_EMAIL, email);
                                                    intent.putExtra(TAG_NAME, name);
                                                    finish();
                                                    startActivity(intent);
                                                }
                                                else if(type.equals("Manager")){

                                                    Intent intent = new Intent(LoginActivity.this, ManagerActivity.class);
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

                                } else {
                                    // email is not verified, so just prompt the message to the user and restart this activity.
                                    FirebaseAuth.getInstance().signOut();
                                    Toast.makeText(LoginActivity.this, "Unverified account, please check your email!", Toast.LENGTH_LONG).show();
                                    //restart this activity
                                }

                            } else {
                                String CurrentString = task.getException().toString();
                                String[] separated = CurrentString.split(":");
                                Toast.makeText(LoginActivity.this, separated[1], Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }

    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void setPreferences (String childName, final String preferencesTag){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uID = user.getUid().toString();

        ref.child(uID).child(childName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataCatch = dataSnapshot.getValue(String.class);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                Log.i("information", "onDataChange:" + dataCatch);
                editor.putString(preferencesTag, dataCatch);
                editor.commit();
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed():" + connectionResult);
        Toast.makeText(getApplicationContext(), "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    private void signIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent, REQUEST_CODE_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                Toast.makeText(getApplicationContext(), "SignIn: failed!",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.e(TAG, "firebaseAuthWithGoogle():" + acct.getId());
        final ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this, "Please wait..", "Processing", true);

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Log.e(TAG, "signInWithCredential: Success!");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uID = user.getUid();

                            boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                            Log.d("MyTAG", "onComplete: " + (isNew ? "new user" : "old user"));

                            if(isNew) {
                                mDatabase.child("users").child(uID).child("status").setValue("");
                                mDatabase.child("users").child(uID).child("type").setValue("");
                            }

                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putBoolean(session_status, true);
                            editor.putString(TAG_EMAIL, user.getEmail());
                            editor.commit();

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
                                        Intent intent = new Intent(LoginActivity.this, TambahPegawaiActivity.class);
                                        intent.putExtra(TAG_EMAIL, email);
                                        finish();
                                        startActivity(intent);
                                    }
                                    else if(status.equals("unverified")){
                                        Intent intent = new Intent(LoginActivity.this, UnverifiedActivity.class);
                                        intent.putExtra(TAG_EMAIL, email);
                                        finish();
                                        startActivity(intent);
                                    }

                                    else if(status.equals("verified")){

                                        if(type.equals("Admin")){
                                            Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                                            intent.putExtra(TAG_EMAIL, email);
                                            intent.putExtra(TAG_NAME, name);
                                            finish();
                                            startActivity(intent);
                                        }

                                        else if(type.equals("Marketing")){

                                            Intent intent = new Intent(LoginActivity.this, MarketingActivity.class);
                                            intent.putExtra(TAG_EMAIL, email);
                                            intent.putExtra(TAG_NAME, name);
                                            finish();
                                            startActivity(intent);
                                        }
                                        else if(type.equals("Manager")){

                                            Intent intent = new Intent(LoginActivity.this, ManagerActivity.class);
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

                        } else {
                            // Sign in fails
                            Log.w(TAG, "signInWithCredential: Failed!", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed!",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }
}
