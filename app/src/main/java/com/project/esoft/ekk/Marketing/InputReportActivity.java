package com.project.esoft.ekk.Marketing;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.esoft.ekk.Constructor.History;
import com.project.esoft.ekk.Constructor.Pegawai;
import com.project.esoft.ekk.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class InputReportActivity extends AppCompatActivity {

    Uri filePath1, filePath2, filePath3, filePath4;
    private final int PICK_IMAGE_REQUEST = 71;

    public static final String TAG_JENIS = "jenis";

    EditText jumlah;
    EditText tanggal;

    private ImageView image1;
    private ImageView image2;
    private ImageView image3;
    private ImageView image4;

    Calendar calendar;
    int year, month, day;
    DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    Button inputButton, pilihTanggal;

    private Integer set;

    History dataIn;

    FirebaseStorage storage;
    StorageReference storageReference;

    private DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    FirebaseUser user;

    Pegawai pegawai;

    String uID, nama, jenis, unique;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_report);

        jenis = getIntent().getStringExtra(TAG_JENIS);

        jumlah = findViewById(R.id.editTextJumlah);
        tanggal = findViewById(R.id.editTextTanggal);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        image1 = findViewById(R.id.imageViewAdd1);
        image2 = findViewById(R.id.imageViewAdd2);
        image3 = findViewById(R.id.imageViewAdd3);
        image4 = findViewById(R.id.imageViewAdd4);

        inputButton = findViewById(R.id.buttonInput);
        pilihTanggal = findViewById(R.id.pilihTanggalButton);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        uID = user.getUid();


        unique = UUID.randomUUID().toString();



        mDatabase.child("users").child(uID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    pegawai = dataSnapshot.getValue(Pegawai.class);
                    pegawai.setUid(dataSnapshot.getKey());
                nama = pegawai.getNama().toString();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
            }
        });

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set = 1;
                chooseImage();
            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set = 2;
                chooseImage();
            }
        });

        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set = 3;
                chooseImage();
            }
        });

        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set = 4;
                chooseImage();
            }
        });

        inputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!jumlah.getText().toString().isEmpty() && !tanggal.getText().toString().isEmpty() && filePath1!=null) {
                    submitData(new History(
                            jenis,
                            nama,
                            uID,
                            jumlah.getText().toString(),
                            "pending",
                            tanggal.getText().toString()
                    ), unique);
                    uploadImage(unique);
                }
            }
        });

        pilihTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            try {
                if(set==1) {
                    filePath1 = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath1);
                    image1.setImageBitmap(bitmap);
                    image1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }else  if(set==2) {
                    filePath2 = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath2);
                    image2.setImageBitmap(bitmap);
                    image2.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }else  if(set==3) {
                    filePath3 = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath3);
                    image3.setImageBitmap(bitmap);
                    image3.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }else {
                    filePath4 = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath4);
                    image4.setImageBitmap(bitmap);
                    image4.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadImage(final String unik) {

        if(filePath1 != null && filePath2 != null && filePath3 != null && filePath4 != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            progressDialog.setMessage("Uploaded 0%");
            StorageReference ref = storageReference.child("image_bukti/"+unik+"/1");
            ref.putFile(filePath1)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.setMessage("Uploaded 25%");

                            StorageReference ref = storageReference.child("image_bukti/"+unik+"/2");
                            ref.putFile(filePath2)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            progressDialog.setMessage("Uploaded 50%");

                                            StorageReference ref = storageReference.child("image_bukti/"+unik+"/3");
                                            ref.putFile(filePath3)
                                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                            progressDialog.setMessage("Uploaded 75%");

                                                            StorageReference ref = storageReference.child("image_bukti/"+unik+"/4");
                                                            ref.putFile(filePath4)
                                                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                        @Override
                                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                            progressDialog.setMessage("Uploaded 100%");
                                                                            progressDialog.dismiss();
                                                                            Toast.makeText(InputReportActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                                                            onBackPressed();
                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            progressDialog.dismiss();
                                                                            Toast.makeText(InputReportActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    })
                                                                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                                        @Override
                                                                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                                                        }
                                                                    });
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(InputReportActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(InputReportActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(InputReportActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    //.getTotalByteCount());
                            //progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void submitData(History data, String unik) {


        mDatabase.child("penjualan")
                .child(unik)
                .setValue(data)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                });
    }

    private void showDateDialog(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                    tanggal.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}
