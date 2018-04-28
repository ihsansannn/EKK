package com.project.esoft.ekk.Manager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.project.esoft.ekk.Constructor.DataPenjualan;
import com.project.esoft.ekk.Constructor.History;
import com.project.esoft.ekk.R;

public class ApproveDataActivity extends AppCompatActivity {

    TextView nama, tanggal, jumlah;
    String noID, uID, jenis;
    Button approveButton, declineButton;
    int jumlahPenjualan;
    int jumlahTerjual;
    int total=0, penjualanTotal;
    float ratingPenjual;
    ImageView bukti1, bukti2, bukti3, bukti4;

    DataPenjualan dataPenjualan;

    Bitmap image1;

    FirebaseStorage storage;
    StorageReference storageReference;
    StorageReference pathReference1, pathReference2, pathReference3, pathReference4;

    private DatabaseReference database, ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_data);

        nama = findViewById(R.id.editTextNamaApp);
        tanggal = findViewById(R.id.editTextTanggalApp);
        jumlah = findViewById(R.id.editTextJumlahApp);

        database = FirebaseDatabase.getInstance().getReference();

        bukti1 = findViewById(R.id.imageViewAddApp1);
        bukti2 = findViewById(R.id.imageViewAddApp2);
        bukti3 = findViewById(R.id.imageViewAddApp3);
        bukti4 = findViewById(R.id.imageViewAddApp4);

        approveButton = findViewById(R.id.buttonApprove);
        declineButton = findViewById(R.id.buttonDecline);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        final History history = (History) getIntent().getSerializableExtra("data");

        if (history != null) {
            nama.setText(history.getNama());
            tanggal.setText(history.getTanggal());
            jumlah.setText(history.getQty());
            noID = history.getNotransaksi();
            jumlahPenjualan = Integer.parseInt(history.getQty().toString());
            uID = history.getPenjual();
            jenis = history.getJenis();
        }

        ref = database.child("users").child(uID);

        ref.child("rating").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String dura = dataSnapshot.getValue(String.class);
                ratingPenjual = Float.parseFloat(dura);
                Log.e("rating", "onDataChange: "+ratingPenjual );
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        ref.child("terjual").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String dura = dataSnapshot.getValue(String.class);
                penjualanTotal = Integer.parseInt(dura);
                Log.e("penjualan", "onDataChange: "+penjualanTotal );
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        pathReference1 = storageReference.child("image_bukti/"+noID+"/1");
        pathReference2 = storageReference.child("image_bukti/"+noID+"/2");
        pathReference3 = storageReference.child("image_bukti/"+noID+"/3");
        pathReference4 = storageReference.child("image_bukti/"+noID+"/4");

        pathReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ApproveDataActivity.this /* context */)
                        .load(uri.toString())
                        .into(bukti1);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });

        pathReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ApproveDataActivity.this /* context */)
                        .load(uri.toString())
                        .into(bukti2);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });

        pathReference3.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ApproveDataActivity.this /* context */)
                        .load(uri.toString())
                        .into(bukti3);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });

        pathReference4.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ApproveDataActivity.this /* context */)
                        .load(uri.toString())
                        .into(bukti4);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });

        database.child("users").child(uID).child(jenis).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    dataPenjualan = dataSnapshot.getValue(DataPenjualan.class);
                    jumlahTerjual = Integer.parseInt(dataPenjualan.getTerjual().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
            }
        });

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ApproveDataActivity.this);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.dialog_decline);
                dialog.setTitle("Decline");
                dialog.show();

                EditText inputDecline = dialog.findViewById(R.id.editTextDec);
                Button ok = dialog.findViewById(R.id.buttonDeclineOK);
                Button cancel = dialog.findViewById(R.id.buttonDeclineCancel);

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        database.child("penjualan").child(noID).child("status_penjualan").setValue("declined");
                        onBackPressed();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });

        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ApproveDataActivity.this);
                dialog.setContentView(R.layout.dialog_approve);
                dialog.setTitle("Approve request?");
                dialog.show();

                Button yesButton = dialog.findViewById(R.id.buttonYes);
                Button cancelButton = dialog.findViewById(R.id.buttonCancel);
                final RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
                LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
                stars.getDrawable(2).setColorFilter(0xFFE9D32E, PorterDuff.Mode.SRC_ATOP);

                yesButton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                float ratting = ratingBar.getRating();
                                dialog.dismiss();
                                total = jumlahTerjual + jumlahPenjualan;
                                if(penjualanTotal==0){
                                    ratingPenjual = ratting+ratingPenjual;
                                } else{
                                    ratingPenjual = (ratting+(ratingPenjual*penjualanTotal))/(penjualanTotal+1);
                                }

                                String ttl = Integer.toString(total);
                                database.child("penjualan").child(noID).child("status_penjualan").setValue("approved");
                                database.child("penjualan").child(noID).child("rating").setValue(String.valueOf(ratting));
                                database.child("users").child(uID).child(jenis).child("terjual").setValue(ttl);
                                ref.child("rating").setValue(String.valueOf(ratingPenjual));
                                ref.child("terjual").setValue(String.valueOf(penjualanTotal+1));
                                onBackPressed();
                            }
                        }
                );

                cancelButton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                               dialog.dismiss();
                            }
                        }
                );

            }
        });

        bukti1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ApproveDataActivity.this);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.tampil_bukti);
                dialog.setTitle("Bukti 1");
                dialog.show();
                ImageView buktiA1 = dialog.findViewById(R.id.imageViewBukti);
                buktiA1.setImageDrawable(bukti1.getDrawable());
            }
        });

        bukti2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ApproveDataActivity.this);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.tampil_bukti);
                dialog.setTitle("Bukti 2");
                dialog.show();
                ImageView buktiA1 = dialog.findViewById(R.id.imageViewBukti);
                buktiA1.setImageDrawable(bukti2.getDrawable());
            }
        });

        bukti3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ApproveDataActivity.this);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.tampil_bukti);
                dialog.setTitle("Bukti 3");
                dialog.show();
                ImageView buktiA1 = dialog.findViewById(R.id.imageViewBukti);
                buktiA1.setImageDrawable(bukti3.getDrawable());
            }
        });

        bukti4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ApproveDataActivity.this);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.tampil_bukti);
                dialog.setTitle("Bukti 4");
                dialog.show();
                ImageView buktiA1 = dialog.findViewById(R.id.imageViewBukti);
                buktiA1.setImageDrawable(bukti4.getDrawable());
            }
        });

    }

    public static Intent getActIntent(Activity activity) {
        return new Intent(activity, ApproveDataActivity.class);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
