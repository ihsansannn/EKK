package com.project.esoft.ekk.Manager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.esoft.ekk.Adapter.AdapterApproval;
import com.project.esoft.ekk.Constructor.History;
import com.project.esoft.ekk.R;

import java.util.ArrayList;

public class ApprovalRequestActivity extends AppCompatActivity {

    DatabaseReference database;
    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    private ArrayList<History> daftarHistory;
    String uID;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_request);
        rvView = findViewById(R.id.rv_approval);
        rvView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvView.setLayoutManager(layoutManager);
        toolbar = findViewById(R.id.toolbarApproval);


        toolbar.setTitle("Approval Request");



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        uID = user.getUid().toString();

        database = FirebaseDatabase.getInstance().getReference();


        database.child("penjualan").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                daftarHistory = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {

                    String cekApprove = (String) noteDataSnapshot.child("status_penjualan").getValue();

                    if(cekApprove.equals("pending")) {
                        try {
                            History history = noteDataSnapshot.getValue(History.class);
                            history.setNotransaksi(noteDataSnapshot.getKey());
                            daftarHistory.add(history);
                        } catch (Exception e) {
                            Log.e("UID", "onDataChange: " + e);
                        }
                    }
                }
                adapter = new AdapterApproval(daftarHistory, ApprovalRequestActivity.this);
                rvView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        database.child("penjualan").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                daftarHistory = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    String cekApprove = (String) noteDataSnapshot.child("status_penjualan").getValue();
                    if(cekApprove.equals("pending")) {
                        try {
                            History history = noteDataSnapshot.getValue(History.class);
                            history.setNotransaksi(noteDataSnapshot.getKey());
                            daftarHistory.add(history);
                        } catch (Exception e) {
                            Log.e("UID", "onDataChange: " + e);
                        }
                    }
                }

                adapter = new AdapterApproval(daftarHistory, ApprovalRequestActivity.this);
                rvView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
            }
        });
    }
}
