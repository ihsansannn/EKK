package com.project.esoft.ekk.Admin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.esoft.ekk.Constructor.Pegawai;
import com.project.esoft.ekk.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class AprovalActivity extends AppCompatActivity {

    private EditText noId;
    private EditText nameAdd;
    private EditText emailAdd;

    private CircleImageView photo;


    private Spinner spinnerType;

    private Button submitButton;

    private TextView changePhoto;
    private TextView status;
    private String posisi;


    private DatabaseReference database, ref;


    FirebaseStorage storage;
    StorageReference storageReference;

    SharedPreferences sharedpreferences;

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aproval);

        noId = findViewById(R.id.textIDApp);
        nameAdd = findViewById(R.id.textNameApp);
        emailAdd = findViewById(R.id.textEmailApp);

        changePhoto = findViewById(R.id.text_change_photo_setting_app);

        photo = findViewById(R.id.image_profile_setting_app);
        status = findViewById(R.id.textViewVerified);

        spinnerType = findViewById(R.id.spinnerTypeApp);
        submitButton = findViewById(R.id.buttonSubmitApp);

        database = FirebaseDatabase.getInstance().getReference();
        ref = database.child("users");


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        final Pegawai pegawai = (Pegawai) getIntent().getSerializableExtra("data");

        if (pegawai != null) {
            noId.setText(pegawai.getNoid());
            nameAdd.setText(pegawai.getNama());
            emailAdd.setText(pegawai.getEmail());
            status.setText(pegawai.getStatus());

            if(pegawai.getStatus().equals("verified")){
                submitButton.setText("UPDATE");
            }else if(pegawai.getStatus().equals("unverified")){
                submitButton.setText("APPROVE");
            }

            posisi = pegawai.getType();
            Log.e("posisi", "onCreate: " + posisi );
            if(posisi.equals("Marketing")){
                spinnerType.setSelection(1);
            }else if(posisi.equals("Manager")){
                spinnerType.setSelection(2);
            }


            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pegawai.setNoid(noId.getText().toString());
                    pegawai.setNama(nameAdd.getText().toString());
                    pegawai.setEmail(emailAdd.getText().toString());
                    pegawai.setStatus("verified");
                    pegawai.setType(spinnerType.getSelectedItem().toString());
                    //updatePegawai(pegawai);

                    ref.child(pegawai.getUid().toString()).child("status").setValue("verified");

                    Snackbar.make(findViewById(R.id.buttonSubmitApp), "Data berhasil diupdate", Snackbar.LENGTH_LONG).setAction("Oke", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    }).show();
                }
            });
        } else {
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!noId.getText().toString().isEmpty() && !nameAdd.getText().toString().isEmpty() && !emailAdd.getText().toString().isEmpty())
                        submitPegawai(new Pegawai(noId.getText().toString(), nameAdd.getText().toString(), emailAdd.getText().toString(), "verified", spinnerType.getSelectedItem().toString()));
                    else
                        Snackbar.make(findViewById(R.id.buttonSubmitApp), "Data tidak boleh kosong", Snackbar.LENGTH_LONG).show();

                    InputMethodManager imm = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(
                            nameAdd.getWindowToken(), 0);
                }
            });
        }
    }



    private void updatePegawai(Pegawai pegawai) {
        database.child("users")
                .child(pegawai.getUid())
                .setValue(pegawai)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Snackbar.make(findViewById(R.id.buttonSubmitApp), "Data berhasil diupdate", Snackbar.LENGTH_LONG).setAction("Oke", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                            }
                        }).show();
                    }
                });
    }

    private void submitPegawai(Pegawai pegawai) {
        /**
         * Ini adalah kode yang digunakan untuk mengirimkan data ke Firebase Realtime Database
         * dan juga kita set onSuccessListener yang berisi kode yang akan dijalankan
         * ketika data berhasil ditambahkan
         */
        database.child("users").push().setValue(pegawai).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                noId.setText("");
                nameAdd.setText("");
                emailAdd.setText("");
                Snackbar.make(findViewById(R.id.buttonSubmitApp), "Data berhasil ditambahkan", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public static Intent getActIntent(Activity activity) {
        // kode untuk pengambilan Intent
        return new Intent(activity, AprovalActivity.class);
    }
}
