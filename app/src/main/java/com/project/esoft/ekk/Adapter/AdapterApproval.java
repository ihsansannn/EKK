package com.project.esoft.ekk.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.project.esoft.ekk.Manager.ApproveDataActivity;
import com.project.esoft.ekk.Constructor.History;
import com.project.esoft.ekk.R;

import java.util.ArrayList;

public class AdapterApproval extends RecyclerView.Adapter<AdapterApproval.ViewHolder> {

private ArrayList<History> daftarHistory;
private Context context;

public AdapterApproval(ArrayList<History> hystorys, Context ctx){

        daftarHistory = hystorys;
        context = ctx;
        }

class ViewHolder extends RecyclerView.ViewHolder {

    TextView nama;
    TextView tanggal;
    TextView qty;
    TextView jenis;

    ViewHolder(View v) {
        super(v);
        nama = v.findViewById(R.id.namaApproval);
        tanggal = v.findViewById(R.id.tanggalApproval);
        qty = v.findViewById(R.id.qtyApproval);
        jenis = v.findViewById(R.id.jenisApproval);
    }
}

    @Override
    public AdapterApproval.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /**
         *  Inisiasi ViewHolder
         */
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_approval, parent, false);
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        AdapterApproval.ViewHolder vh = new AdapterApproval.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(AdapterApproval.ViewHolder holder, final int position) {

        History history = daftarHistory.get(position);
        //final String notransaksi = history.getNotransaksi();
        final String nama = history.getNama();
        final String tanggal = history.getTanggal();
        final String qty = history.getQty();
        final String jenis = history.getJenis();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(ApproveDataActivity.getActIntent
                        ((Activity) context).putExtra("data", daftarHistory.get(position)));
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_view);
                dialog.setTitle("Pilih Aksi");
                dialog.show();

                Button editButton = dialog.findViewById(R.id.bt_edit_data);
                Button delButton = dialog.findViewById(R.id.bt_delete_data);

                //apabila tombol edit diklik
                editButton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                //context.startActivity(AprovalActivity.getActIntent
                                  //      ((Activity) context).putExtra("data", daftarHistory.get(position)));
                            }
                        }
                );

                //apabila tombol delete diklik
                delButton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            }
                        }
                );
                return true;
            }
        });
        holder.nama.setText(nama);
        holder.tanggal.setText(tanggal);
        holder.qty.setText(qty);
        holder.jenis.setText(jenis);
    }

    @Override
    public int getItemCount() {
        return daftarHistory.size();
    }
}