package com.project.esoft.ekk.Marketing;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.esoft.ekk.Adapter.AdapterHistory;
import com.project.esoft.ekk.Constructor.History;
import com.project.esoft.ekk.R;

import java.util.ArrayList;

public class ProsesRumahActivity extends AppCompatActivity {

    private DatabaseReference database;
    private DatabaseReference ref;
    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    private ArrayList<History> daftarHistory;
    private String uID;
    FloatingActionButton fabProses;
    Toolbar toolbar;
    private ProgressBar progress;
    TextView target, dari, terjual, durasi;
    private String dura, jenis;
    private int jual=1;
    private int trget=1;

    public static final String TAG_JENIS = "jenis";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proses_rumah);

        rvView = findViewById(R.id.rv_rumah);
        rvView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvView.setLayoutManager(layoutManager);
        fabProses = findViewById(R.id.fabProses);
        toolbar = findViewById(R.id.toolbarProses);

        target = findViewById(R.id.textViewTarget2);
        terjual = findViewById(R.id.textViewTerjual2);
        dari = findViewById(R.id.textViewTerjual3);
        durasi = findViewById(R.id.textViewDurasi2);

        progress = findViewById(R.id.progressBar2);

        jenis = getIntent().getStringExtra(TAG_JENIS);

        if(jenis.equals("rumah")) {
            toolbar.setTitle("Penjualan Rumah");
        } else if(jenis.equals("mobil")) {
            toolbar.setTitle("Penjualan Mobil");
        } else if(jenis.equals("motor")) {
            toolbar.setTitle("Penjualan Motor");
        }




        fabProses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProsesRumahActivity.this, InputReportActivity.class);
                i.putExtra(TAG_JENIS, jenis);
                startActivity(i);
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        uID = user.getUid().toString();

        /**
         * Inisialisasi dan mengambil Firebase Database Reference
         */
        database = FirebaseDatabase.getInstance().getReference();

        try {
            setDataRumah("durasi", durasi);
            setDataRumah("target", target);
            setDataRumah("terjual", terjual);
            setDataRumah("target", dari);
        } catch (Exception e){}

        try{
            database.child("penjualan").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    daftarHistory = new ArrayList<>();
                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {

                        String cekJenis = (String) noteDataSnapshot.child("jenis").getValue();
                        String cekID = (String) noteDataSnapshot.child("penjual").getValue();
                        if (cekID.equals(uID) && cekJenis.equals(jenis)) {
                            try {
                                History history = noteDataSnapshot.getValue(History.class);
                                history.setNotransaksi(noteDataSnapshot.getKey());

                                daftarHistory.add(history);
                            } catch (Exception e) {
                                Log.e("UID", "onDataChange: " + e);

                            }
                        }
                    }

                    adapter = new AdapterHistory(daftarHistory, ProsesRumahActivity.this);
                    rvView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println(databaseError.getDetails() + " " + databaseError.getMessage());
                }
            });
        }
        catch(Exception e){

        }
    }


    private  void setDataRumah(final String child, final  TextView text){
        try {

            database.child("users").child(uID).child(jenis).child(child).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    dura = dataSnapshot.getValue(String.class);
                    Log.e("dura", "onDataChange: " + dura);
                    text.setText(dura);
                    if (child.equals("target")) {
                        trget = Integer.parseInt(dura);
                        Log.e("target", "onDataChange: " + trget);
                    } else if (child.equals("terjual")) {
                        jual = Integer.parseInt(dura);
                        Log.e("target", "onDataChange: " + jual);
                    }

                    int test = jual * 100 / trget;
                    Log.e("cekangka", "onCreate: " + test);

                    progress.setProgress(test);

                    int color = 0xFF3380D9;
                    //progress.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
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
