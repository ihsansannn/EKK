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

import com.project.esoft.ekk.Admin.AprovalActivity;
import com.project.esoft.ekk.Constructor.Pegawai;
import com.project.esoft.ekk.R;

import java.util.ArrayList;



public class AdapterPegawai extends RecyclerView.Adapter<AdapterPegawai.ViewHolder> {

    private ArrayList<Pegawai> daftarPegawai;
    private Context context;

    public AdapterPegawai(ArrayList<Pegawai> pegawais, Context ctx){
        /**
         * Inisiasi data dan variabel yang akan digunakan
         */
        daftarPegawai = pegawais;
        context = ctx;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * Inisiasi View
         * Di tutorial ini kita hanya menggunakan data String untuk tiap item
         * dan juga view nya hanyalah satu TextView
         */
        TextView noid;
        TextView nama;
        TextView status;

        ViewHolder(View v) {
            super(v);
            noid = v.findViewById(R.id.noid);
            nama = v.findViewById(R.id.nama);
            status = v.findViewById(R.id.status);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /**
         *  Inisiasi ViewHolder
         */
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_barang, parent, false);
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        /**
         *  Menampilkan data pada view
         *
         */

        Pegawai pegawai = daftarPegawai.get(position);
        final String noid = pegawai.getNoid();
        final String nama = pegawai.getNama();
        final String status = pegawai.getStatus();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                editButton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                context.startActivity(AprovalActivity.getActIntent
                                        ((Activity) context).putExtra("data", daftarPegawai.get(position)));
                            }
                        }
                );

                delButton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                /**
                                 *  Kodingan untuk tutorial Selanjutnya :p Delete data
                                 */
                            }
                        }
                );
                return true;
            }
        });
        holder.noid.setText(noid);
        holder.nama.setText(nama);
        holder.status.setText(status);
    }

    @Override
    public int getItemCount() {
        /**
         * Mengembalikan jumlah item pada barang
         */
        return daftarPegawai.size();
    }
}
