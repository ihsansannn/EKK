package com.project.esoft.ekk.ActivityUser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.esoft.ekk.LoginActivity;
import com.project.esoft.ekk.Marketing.ProfileMarketingActivity;
import com.project.esoft.ekk.Marketing.ProsesRumahActivity;
import com.project.esoft.ekk.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class MarketingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "JSAGoogleSignIn";
    private static final int REQUEST_CODE_SIGN_IN = 1234;
    private static final String WEB_CLIENT_ID = "399080402993-0taoq5nh3cp8ldun40ibgbg1eimtnbrv.apps.googleusercontent.com";

    private FirebaseAuth mAuth;
    SharedPreferences sharedpreferences;
    private GoogleApiClient mGoogleApiClient;

    FirebaseStorage storage;
    StorageReference storageReference;
    StorageReference pathReference;

    String email, nama, uID;
    private LinearLayout rumah, mobil, motor;

    SmoothActionBarDrawerToggle mDrawerToggle;
    DrawerLayout drawer;
    LinearLayout profile;

    CircleImageView profilePict;

    public static final String TAG_EMAIL = "email";
    public static final String TAG_NAMA = "nama";
    public static final String TAG_JENIS = "jenis";
    public static final String TAG_UID = "uid";
    public static final String session_status = "session_status";

    TextView namaHeader, emailHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketing);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(WEB_CLIENT_ID)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uID = user.getUid();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        pathReference = storageReference.child("imagesProfile/"+uID.toString());

        rumah = findViewById(R.id.linearLayoutRumah);
        mobil = findViewById(R.id.linearLayoutMobil);
        motor = findViewById(R.id.linearLayoutMotor);

        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);

        email = getIntent().getStringExtra(TAG_EMAIL);
        nama = getIntent().getStringExtra(TAG_NAMA);

        drawer = findViewById(R.id.drawer_layout);

        mDrawerToggle = new SmoothActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);

        profile = header.findViewById(R.id.linearLayoutMarketing);
        namaHeader = header.findViewById(R.id.textViewNamaMarketing);
        emailHeader = header.findViewById(R.id.textViewEmailMarketing);
        profilePict = header.findViewById(R.id.imageViewMarketing);

        namaHeader.setText(nama);
        emailHeader.setText(email);

        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(MarketingActivity.this)
                        .load(uri.toString())
                        .into(profilePict);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        rumah.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MarketingActivity.this, ProsesRumahActivity.class);
                i.putExtra(TAG_JENIS, "rumah");
                startActivity(i);
            }
        });

        mobil.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MarketingActivity.this, ProsesRumahActivity.class);
                i.putExtra(TAG_JENIS, "mobil");
                startActivity(i);
            }
        });

        motor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MarketingActivity.this, ProsesRumahActivity.class);
                i.putExtra(TAG_JENIS, "motor");
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_rumah) {
            selectItem(1);
        } else if (id == R.id.nav_mobil) {
            selectItem(2);
        } else if (id == R.id.nav_motor) {
            selectItem(3);
        }  else if (id == R.id.nav_profile) {
            selectItem(4);
        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_logout) {
            selectItem(5);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

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

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Log.e(TAG, "signInWithCredential: Success!");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // Sign in fails
                            Log.w(TAG, "signInWithCredential: Failed!", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed!",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void signOut() {
        mAuth.signOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                    }
                });
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


    private class SmoothActionBarDrawerToggle extends ActionBarDrawerToggle {

        private Runnable runnable;

        public SmoothActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            invalidateOptionsMenu();
        }
        @Override
        public void onDrawerClosed(View view) {
            super.onDrawerClosed(view);
            invalidateOptionsMenu();
        }
        @Override
        public void onDrawerStateChanged(int newState) {
            super.onDrawerStateChanged(newState);
            if (runnable != null && newState == DrawerLayout.STATE_IDLE) {
                runnable.run();
                runnable = null;
            }
        }

        public void runWhenIdle(Runnable runnable) {
            this.runnable = runnable;
        }
    }

    private void selectItem(int position) {
        switch (position) {
            case 1: {
                mDrawerToggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(MarketingActivity.this, ProsesRumahActivity.class);
                        i.putExtra(TAG_JENIS, "rumah");
                        startActivity(i);
                    }
                });
                drawer.closeDrawers();
                break;
            }
            case 2: {
                mDrawerToggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(MarketingActivity.this, ProsesRumahActivity.class);
                        i.putExtra(TAG_JENIS, "mobil");
                        startActivity(i);
                    }
                });
                drawer.closeDrawers();
                break;
            }
            case 3: {
                mDrawerToggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(MarketingActivity.this, ProsesRumahActivity.class);
                        i.putExtra(TAG_JENIS, "motor");
                        startActivity(i);
                    }
                });
                drawer.closeDrawers();
                break;
            }
            case 4: {
                mDrawerToggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(MarketingActivity.this, ProfileMarketingActivity.class);
                        i.putExtra(TAG_NAMA, nama);
                        i.putExtra(TAG_UID, uID);
                        startActivity(i);
                    }
                });
                drawer.closeDrawers();
                break;
            }

            case 5: {
                mDrawerToggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        revokeAccess();

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean(session_status, false);
                        editor.commit();

                        Intent i = new Intent(MarketingActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
                drawer.closeDrawers();
                break;
            }
        }
    }
}
