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

import com.project.esoft.ekk.Constructor.Pegawai;
import com.project.esoft.ekk.Manager.EditJobActivity;
import com.project.esoft.ekk.Manager.MarketingPerformanceActivity;
import com.project.esoft.ekk.R;

import java.util.ArrayList;

public class AdapterList extends RecyclerView.Adapter<AdapterList.ViewHolder> {

    private ArrayList<Pegawai> daftarPegawai;
    private Context context;

    public AdapterList(ArrayList<Pegawai> pegawais, Context ctx){

        daftarPegawai = pegawais;
        context = ctx;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView noid;
        TextView nama;
        TextView divisi;

        ViewHolder(View v) {
            super(v);
            noid = v.findViewById(R.id.noidPegawai);
            nama = v.findViewById(R.id.namaPegawai);
            divisi = v.findViewById(R.id.divisiPegawai);
        }
    }

    @Override
    public AdapterList.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pegawai, parent, false);
        AdapterList.ViewHolder vh = new AdapterList.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(AdapterList.ViewHolder holder, final int position) {

        Pegawai pegawai = daftarPegawai.get(position);
        final String noid = pegawai.getNoid();
        final String nama = pegawai.getNama();
        final String divisi = pegawai.getType();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.dialog_editjob);
                dialog.setTitle("Pilih Aksi");
                dialog.show();

                Button editButton = dialog.findViewById(R.id.bt_edit_job);
                Button kineja = dialog.findViewById(R.id.bt_kinerja);

                editButton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                context.startActivity(EditJobActivity.getActIntent
                                        ((Activity) context).putExtra("data", daftarPegawai.get(position)));
                            }
                        }
                );

                kineja.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                context.startActivity(MarketingPerformanceActivity.getActIntent
                                        ((Activity) context).putExtra("data", daftarPegawai.get(position)));
                            }
                        }
                );
                return true;
            }
        });


        holder.noid.setText(noid);
        holder.nama.setText(nama);
        holder.divisi.setText(divisi);
    }

    @Override
    public int getItemCount() {
        return daftarPegawai.size();
    }
}
