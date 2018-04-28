package com.project.esoft.ekk.ActivityUser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.esoft.ekk.Admin.DaftarPegawaiActivity;
import com.project.esoft.ekk.LoginActivity;
import com.project.esoft.ekk.R;
import com.project.esoft.ekk.TambahPegawaiActivity;
import com.project.esoft.ekk.TestActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,  GoogleApiClient.OnConnectionFailedListener {

    ImageView tambahPegawai, daftarPegawai, setting, out;

    SharedPreferences sharedpreferences;
    public static final String TAG_EMAIL = "email";
    public static final String TAG_NAMA = "nama";
    public static final String TAG_UID= "uid";

    public static final String session_status = "session_status";
    private static final String WEB_CLIENT_ID = "399080402993-0taoq5nh3cp8ldun40ibgbg1eimtnbrv.apps.googleusercontent.com";

    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;

    FirebaseStorage storage;
    StorageReference storageReference;
    StorageReference pathReference;

    SmoothActionBarDrawerToggle mDrawerToggle;
    DrawerLayout drawer;
    LinearLayout profile;
    String email, name, uID;

    CircleImageView profilePict;

    TextView namaHeader, emailHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Toolbar toolbar = findViewById(R.id.toolbarAdmin);
        setSupportActionBar(toolbar);

        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
        email = getIntent().getStringExtra(TAG_EMAIL);
        name = getIntent().getStringExtra(TAG_NAMA);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uID = user.getUid();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        pathReference = storageReference.child("imagesProfile/"+uID.toString());

        tambahPegawai = findViewById(R.id.imageViewAdd);
        daftarPegawai = findViewById(R.id.imageViewUser);
        setting = findViewById(R.id.imageViewSettingAdmin);
        out = findViewById(R.id.imageViewOutAdmin);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(WEB_CLIENT_ID)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();



        tambahPegawai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminActivity.this, TambahPegawaiActivity.class);
                startActivity(i);
            }
        });



        daftarPegawai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminActivity.this, DaftarPegawaiActivity.class);
                startActivity(i);
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revokeAccess();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(session_status, false);
                editor.commit();
                Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        drawer = findViewById(R.id.drawer_layout_admin);
        mDrawerToggle = new SmoothActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view_admin);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);

        profile = header.findViewById(R.id.adminHeader);
        namaHeader = header.findViewById(R.id.textNamaAdmin);
        emailHeader = header.findViewById(R.id.textEmailAdmin);
        profilePict = header.findViewById(R.id.imageProfileAdmin);

        namaHeader.setText(name);
        emailHeader.setText(email);

        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(AdminActivity.this /* context */)
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

    }

    @Override
    public void onBackPressed() {
        drawer = findViewById(R.id.drawer_layout_admin);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin, menu);
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

        if (id == R.id.nav_approvalList) {
        } else if (id == R.id.tambahPegAdmin) {
            //Intent i = new Intent(AdminActivity.this, TambahPegawaiActivity.class);
            //startActivity(i);
            selectItem(1);

        } else if (id == R.id.daftarPegAdmin) {
            //Intent i = new Intent(AdminActivity.this, DaftarPegawaiActivity.class);
            //startActivity(i);
            selectItem(2);
        } else if (id == R.id.settingAdmin) {
            Intent i = new Intent(AdminActivity.this, TestActivity.class);
            startActivity(i);
        } else if (id == R.id.logoutAdmin) {
            selectItem(5);
    }

        drawer = findViewById(R.id.drawer_layout_admin);
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
        Toast.makeText(getApplicationContext(), "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    private void revokeAccess() {
        mAuth.signOut();
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

        } else {
        }
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
                        Intent intent = new Intent(AdminActivity.this, TambahPegawaiActivity.class);
                        startActivity(intent);
                    }
                });
                drawer.closeDrawers();
                break;
            }
            case 2: {
                mDrawerToggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(AdminActivity.this, DaftarPegawaiActivity.class);
                        startActivity(intent);
                    }
                });
                drawer.closeDrawers();
                break;
            }
            case 4: {
                mDrawerToggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(AdminActivity.this, ManagerActivity.class);
                        startActivity(intent);
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
                        Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                drawer.closeDrawers();
                break;
            }
        }
    }
}
