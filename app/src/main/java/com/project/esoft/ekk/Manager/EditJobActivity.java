package com.project.esoft.ekk.Manager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.esoft.ekk.Constructor.DataPenjualan;
import com.project.esoft.ekk.Constructor.Pegawai;
import com.project.esoft.ekk.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditJobActivity extends AppCompatActivity {

    EditText targetRumah, targetMobil, targetMotor,
            durasiRumah, durasiMobil, durasiMotor,
            mulaiRumah, mulaiMobil, mulaiMotor;

    DataPenjualan dataRumah, dataMobil, dataMotor;

    DatabaseReference database, ref;

    Spinner spin1, spin2, spin3;
    Calendar calendar;
    int year, month, day;
    String uID;
    DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    Button pickDate1, pickDate2, pickDate3, saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_job);

        targetRumah = findViewById(R.id.editJobTargetRumah);
        targetMobil = findViewById(R.id.editJobTargetMobil);
        targetMotor = findViewById(R.id.editJobTargetMotor);

        durasiRumah = findViewById(R.id.editJobDurasiRumah);
        durasiMobil = findViewById(R.id.editJobDurasiMobil);
        durasiMotor = findViewById(R.id.editJobDurasiMotor);

        mulaiRumah = findViewById(R.id.editJobMulaiRumah);
        mulaiMobil = findViewById(R.id.editJobMulaiMobil);
        mulaiMotor = findViewById(R.id.editJobMulaiMotor);

        pickDate1 = findViewById(R.id.pickDate1Button);
        pickDate2 = findViewById(R.id.pickDate2Button);
        pickDate3 = findViewById(R.id.pickDate3Button);
        saveButton = findViewById(R.id.buttonSaveTarget);
        spin1 = findViewById(R.id.spinnerJobRumah);
        spin2 = findViewById(R.id.spinnerJobMobil);
        spin3 = findViewById(R.id.spinnerJobMotor);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        final Pegawai pegawai = (Pegawai) getIntent().getSerializableExtra("data");
        uID = pegawai.getUid();
        Log.e("uid edit", "onCreate: "+uID );
        database = FirebaseDatabase.getInstance().getReference();
        ref = database.child("users");

        database.child("users").child(uID).child("rumah").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataRumah = dataSnapshot.getValue(DataPenjualan.class);
                dataRumah.setTypeJual(dataSnapshot.getKey());
                String str = dataRumah.getDurasi().toString();
                String[] splited = str.split(" ");
                targetRumah.setText(dataRumah.getTarget().toString());
                durasiRumah.setText(splited[0]);
                mulaiRumah.setText(dataRumah.getMulai().toString());

                if(splited[1].equals("hari")){
                    spin1.setSelection(0);
                }else if(splited[1].equals("minggu")){
                    spin1.setSelection(1);
                }
                else if(splited[1].equals("bulan")){
                    spin1.setSelection(2);
                }
                else if(splited[1].equals("tahun")){
                    spin1.setSelection(3);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        database.child("users").child(uID).child("mobil").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataMobil = dataSnapshot.getValue(DataPenjualan.class);
                dataMobil.setTypeJual(dataSnapshot.getKey());
                String str = dataMobil.getDurasi().toString();
                String[] splited = str.split(" ");
                targetMobil.setText(dataMobil.getTarget().toString());
                durasiMobil.setText(splited[0]);
                mulaiMobil.setText(dataMobil.getMulai().toString());
                if(splited[1].equals("hari")){
                    spin2.setSelection(0);
                }else if(splited[1].equals("minggu")){
                    spin2.setSelection(1);
                }
                else if(splited[1].equals("bulan")){
                    spin2.setSelection(2);
                }
                else if(splited[1].equals("tahun")){
                    spin2.setSelection(3);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        database.child("users").child(uID).child("motor").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataMotor = dataSnapshot.getValue(DataPenjualan.class);
                dataMotor.setTypeJual(dataSnapshot.getKey());
                String str = dataMotor.getDurasi().toString();
                String[] splited = str.split(" ");
                targetMotor.setText(dataMotor.getTarget().toString());
                durasiMotor.setText(splited[0]);
                mulaiMotor.setText(dataMotor.getMulai().toString());
                if(splited[1].equals("hari")){
                    spin3.setSelection(0);
                }else if(splited[1].equals("minggu")){
                    spin3.setSelection(1);
                }
                else if(splited[1].equals("bulan")){
                    spin3.setSelection(2);
                }
                else if(splited[1].equals("tahun")){
                    spin3.setSelection(3);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
        pickDate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(0);
            }
        });

        pickDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(1);
            }
        });

        pickDate3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(2);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.child(uID).child("mobil").child("durasi").setValue(durasiMobil.getText().toString()
                        +" "+ spin2.getSelectedItem().toString());
                ref.child(uID).child("mobil").child("target").setValue(targetMobil.getText().toString());
                ref.child(uID).child("mobil").child("mulai").setValue(mulaiMobil.getText().toString());
                ref.child(uID).child("motor").child("durasi").setValue(durasiMotor.getText().toString()
                        +" "+ spin3.getSelectedItem().toString());
                ref.child(uID).child("motor").child("target").setValue(targetMotor.getText().toString());
                ref.child(uID).child("motor").child("mulai").setValue(mulaiMotor.getText().toString());
                ref.child(uID).child("rumah").child("durasi").setValue(durasiRumah.getText().toString()
                        +" "+ spin1.getSelectedItem().toString());
                ref.child(uID).child("rumah").child("target").setValue(targetRumah.getText().toString());
                ref.child(uID).child("rumah").child("mulai").setValue(mulaiRumah.getText().toString());

                Toast.makeText(getApplicationContext(), "Data berhasil disimpan",
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    public static Intent getActIntent(Activity activity) {
        return new Intent(activity, EditJobActivity.class);
    }

    private void showDateDialog(final int pick){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                if(pick==0){
                    mulaiRumah.setText(dateFormatter.format(newDate.getTime()));
                }else if(pick==1){
                    mulaiMobil.setText(dateFormatter.format(newDate.getTime()));
                }else if(pick == 2){
                    mulaiMotor.setText(dateFormatter.format(newDate.getTime()));
                }
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
