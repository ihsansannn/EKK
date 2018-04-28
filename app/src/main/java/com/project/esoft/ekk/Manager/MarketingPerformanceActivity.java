package com.project.esoft.ekk.Manager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.esoft.ekk.Constructor.Pegawai;
import com.project.esoft.ekk.R;

public class MarketingPerformanceActivity extends AppCompatActivity {


    private DatabaseReference database;
    Pegawai pegawai;
    TextView namaPegawai, target, dari, terjual, durasi;
    String jenis, dura, uID;
    private int jual=1, trget=1;
    ProgressBar progress;
    Spinner jenisPenjualan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketing_performance);

        namaPegawai = findViewById(R.id.textViewNamaPeg);
        target = findViewById(R.id.textViewTargetPer);
        terjual = findViewById(R.id.textViewTerjualPer);
        dari = findViewById(R.id.textViewTerjual3Per);
        durasi = findViewById(R.id.textViewDurasi2Per);
        jenisPenjualan = findViewById(R.id.spinnerTypeJenis);
        progress = findViewById(R.id.progressBarPer);
        pegawai = (Pegawai) getIntent().getSerializableExtra("data");

        if (pegawai != null) {
            namaPegawai.setText("Nama Pegawai : "+pegawai.getNama().toString());
            uID = pegawai.getUid();
        }
        database = FirebaseDatabase.getInstance().getReference();
        jenis = "rumah";
        refreshData();

        jenisPenjualan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    jenis = "rumah";
                } else if(position==1){
                    jenis = "mobil";
                } else if(position==2){
                    jenis = "motor";
                }
                Log.e("jenisss", "onItemSelected: "+jenis );
                refreshData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public static Intent getActIntent(Activity activity) {
        return new Intent(activity, MarketingPerformanceActivity.class);
    }

    public void refreshData(){

        try {
            setDataRumah("durasi", durasi);
            setDataRumah("target", target);
            setDataRumah("terjual", terjual);
            setDataRumah("target", dari);
        } catch (Exception e){}

    }

    private  void setDataRumah(final String child, final  TextView text){
        try {
            database.child("users").child(uID).child(jenis).child(child).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    dura = dataSnapshot.getValue(String.class);
                    text.setText(dura);
                    if (child.equals("target")) {
                        trget = Integer.parseInt(dura);
                    } else if (child.equals("terjual")) {
                        jual = Integer.parseInt(dura);
                    }
                    int test = jual * 100 / trget;
                    progress.setProgress(test);
                    int color = 0xFF3380D9;
                    progress.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });
        }
        catch (Exception e){}
    }
}
