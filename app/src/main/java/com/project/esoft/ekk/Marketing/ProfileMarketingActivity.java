package com.project.esoft.ekk.Marketing;

import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.esoft.ekk.ActivityUser.MarketingActivity;
import com.project.esoft.ekk.Constructor.DataPenjualan;
import com.project.esoft.ekk.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileMarketingActivity extends AppCompatActivity {

    public static final String TAG_NAMA = "nama";
    public static final String TAG_UID = "uid";

    TextView namaView, terjualRumah, targetRumah,
            terjualMobil, targetMobil,
            terjualMotor, targetMotor;

    CircleImageView profilePict;

    DataPenjualan dataRumah, dataMobil, dataMotor;

    DatabaseReference database, ref;

    RatingBar ratingBar;

    FirebaseStorage storage;
    StorageReference storageReference;
    StorageReference pathReference;

    float ratingPenjual;

    String uID, nama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_marketing);

        uID = getIntent().getStringExtra(TAG_UID);
        nama = getIntent().getStringExtra(TAG_NAMA);

        namaView = findViewById(R.id.namaProfilePegawai);
        terjualRumah = findViewById(R.id.terjualTextRumah);
        terjualMobil = findViewById(R.id.terjualTextMobil);
        terjualMotor = findViewById(R.id.terjualTextMotor);
        targetRumah = findViewById(R.id.dariTextRumah);
        targetMobil = findViewById(R.id.dariTextMobil);
        targetMotor = findViewById(R.id.dariTextMotor);

        profilePict = findViewById(R.id.imageProfilePegawai);

        ratingBar = findViewById(R.id.ratingBarProfile);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(0xFFE9D32E, PorterDuff.Mode.SRC_ATOP);

        ratingBar.setClickable(false);
        ratingBar.setFocusable(false);
        ratingBar.setFocusableInTouchMode(false);
        ratingBar.setIsIndicator(true);

        namaView.setText(nama);

        database = FirebaseDatabase.getInstance().getReference();
        ref = database.child("users").child(uID);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        pathReference = storageReference.child("imagesProfile/"+uID.toString());

        ref.child("rumah").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataRumah = dataSnapshot.getValue(DataPenjualan.class);
                dataRumah.setTypeJual(dataSnapshot.getKey());
                terjualRumah.setText(dataRumah.getTerjual().toString());
                targetRumah.setText(dataRumah.getTarget().toString());
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        ref.child("mobil").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataMobil = dataSnapshot.getValue(DataPenjualan.class);
                dataMobil.setTypeJual(dataSnapshot.getKey());
                terjualMobil.setText(dataMobil.getTerjual().toString());
                targetMobil.setText(dataMobil.getTarget().toString());
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        ref.child("motor").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataMotor = dataSnapshot.getValue(DataPenjualan.class);
                dataMotor.setTypeJual(dataSnapshot.getKey());
                terjualMotor.setText(dataMotor.getTerjual().toString());
                targetMotor.setText(dataMotor.getTarget().toString());
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        ref.child("rating").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String dura = dataSnapshot.getValue(String.class);
                ratingPenjual = Float.parseFloat(dura);
                ratingBar.setRating(ratingPenjual);
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ProfileMarketingActivity.this)
                        .load(uri.toString())
                        .into(profilePict);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });

    }
}
