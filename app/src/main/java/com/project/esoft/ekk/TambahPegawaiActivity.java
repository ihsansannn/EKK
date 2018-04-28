package com.project.esoft.ekk;



import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.esoft.ekk.ActivityUser.UnverifiedActivity;

import de.hdodenhof.circleimageview.CircleImageView;

import java.io.IOException;


public class TambahPegawaiActivity extends AppCompatActivity {


    private EditText noId;
    private EditText nameAdd;
    private EditText emailAdd;
    private EditText passwordAdd;
    private EditText passwordReenter;

    private CircleImageView photo;


    private Spinner spinnerType;

    Button submitButton;

    TextView changePhoto;

    FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference ref;
    private FirebaseUser user;

    FirebaseStorage storage;
    StorageReference storageReference;


    private Uri filePath;
    String uID;

    private final int PICK_IMAGE_REQUEST = 71;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pegawai);

        noId = findViewById(R.id.textID);
        nameAdd = findViewById(R.id.textName);
        emailAdd = findViewById(R.id.textEmail);
        passwordAdd = findViewById(R.id.textPassword);
        passwordReenter = findViewById(R.id.textReenter);

        changePhoto = findViewById(R.id.text_change_photo_setting);

        photo = findViewById(R.id.image_profile_setting);

        spinnerType = findViewById(R.id.spinnerType);
        submitButton = findViewById(R.id.buttonSubmit);

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        ref = mDatabase.child("users");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        user = FirebaseAuth.getInstance().getCurrentUser();

        uID = user.getUid();

        emailAdd.setText(user.getEmail().toString());

        changePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



        submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(noId.getText().toString().isEmpty()||
                        nameAdd.getText().toString().isEmpty()||
                        emailAdd.getText().toString().isEmpty()||
                        passwordAdd.getText().toString().isEmpty())
                {
                    Toast.makeText(TambahPegawaiActivity.this, "Field cannot be empty", Toast.LENGTH_LONG).show();

                }
                else if(!passwordAdd.getText().toString().equals(passwordReenter.getText().toString()))
                {
                    Toast.makeText(TambahPegawaiActivity.this, "Password did not match", Toast.LENGTH_LONG).show();
                }
                else if(passwordAdd.getText().length()<8)
                {
                    Toast.makeText(TambahPegawaiActivity.this, "Password minimal 8 karakter", Toast.LENGTH_LONG).show();
                }else if (filePath == null){
                    Toast.makeText(TambahPegawaiActivity.this, "Profile picture masih kosong", Toast.LENGTH_LONG).show();
                }
                else{
                    ref.child(uID).child("noid").setValue(noId.getText().toString());
                    ref.child(uID).child("nama").setValue(nameAdd.getText().toString());
                    ref.child(uID).child("email").setValue(emailAdd.getText().toString());
                    ref.child(uID).child("type").setValue(spinnerType.getSelectedItem().toString());
                    if(spinnerType.getSelectedItem().toString().equals("Marketing")){
                        ref.child(uID).child("mobil").child("durasi").setValue("12 bulan");
                        ref.child(uID).child("mobil").child("target").setValue("10");
                        ref.child(uID).child("mobil").child("terjual").setValue("0");
                        ref.child(uID).child("mobil").child("mulai").setValue("01/01/2000");
                        ref.child(uID).child("motor").child("durasi").setValue("12 bulan");
                        ref.child(uID).child("motor").child("target").setValue("10");
                        ref.child(uID).child("motor").child("terjual").setValue("0");
                        ref.child(uID).child("motor").child("mulai").setValue("01/01/2000");
                        ref.child(uID).child("rumah").child("durasi").setValue("12 bulan");
                        ref.child(uID).child("rumah").child("target").setValue("10");
                        ref.child(uID).child("rumah").child("terjual").setValue("0");
                        ref.child(uID).child("rumah").child("mulai").setValue("01/01/2000");
                        ref.child(uID).child("terjual").setValue("0");
                        ref.child(uID).child("rating").setValue("0.0");
                    }
                    user.updatePassword(passwordAdd.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    //String uID = user.getUid();
                                    //mDatabase.child("users").child(uID).child("status").setValue("unverified");
                                    Log.d("TESTPASS", "Password updated");
                                } else {
                                    Toast.makeText(TambahPegawaiActivity.this,
                                            "Password minimal 8 karakter", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    user.updatePassword(passwordReenter.getText().toString());

                    uploadImage();

                }
            }
        });

        changePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                photo.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("imagesProfile/"+uID);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String uID = user.getUid();
                            progressDialog.dismiss();
                            Toast.makeText(TambahPegawaiActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            mDatabase.child("users").child(uID).child("status").setValue("unverified");
                            Intent i = new Intent(TambahPegawaiActivity.this, UnverifiedActivity.class);
                            startActivity(i);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(TambahPegawaiActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
        else{
            String uID = user.getUid();
            mDatabase.child("users").child(uID).child("status").setValue("unverified");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
