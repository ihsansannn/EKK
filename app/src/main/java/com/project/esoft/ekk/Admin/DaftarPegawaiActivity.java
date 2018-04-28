package com.project.esoft.ekk.Admin;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.esoft.ekk.Adapter.AdapterPegawai;
import com.project.esoft.ekk.Constructor.Pegawai;
import com.project.esoft.ekk.R;

import java.util.ArrayList;

public class DaftarPegawaiActivity extends AppCompatActivity {
    private DatabaseReference database;
    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Pegawai> daftarPegawai;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_pegawai);

        rvView = findViewById(R.id.rv_main);
        rvView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvView.setLayoutManager(layoutManager);

        database = FirebaseDatabase.getInstance().getReference();

        database.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                daftarPegawai = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    Pegawai pegawai = noteDataSnapshot.getValue(Pegawai.class);
                    pegawai.setUid(noteDataSnapshot.getKey());
                    daftarPegawai.add(pegawai);
                }

                adapter = new AdapterPegawai(daftarPegawai, DaftarPegawaiActivity.this);
                rvView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
            }
        });
    }

    public static Intent getActIntent(Activity activity){
        return new Intent(activity, DaftarPegawaiActivity.class);
    }
}