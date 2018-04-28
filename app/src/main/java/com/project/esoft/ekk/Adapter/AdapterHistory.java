package com.project.esoft.ekk.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.project.esoft.ekk.Constructor.History;
import com.project.esoft.ekk.R;

import java.util.ArrayList;

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.ViewHolder> {

private ArrayList<History> daftarHistory;
private Context context;

public AdapterHistory(ArrayList<History> hystorys, Context ctx){
        daftarHistory = hystorys;
        context = ctx;
        }

class ViewHolder extends RecyclerView.ViewHolder {
    TextView tanggal;
    TextView qty;
    TextView status;

    ViewHolder(View v) {
        super(v);
        tanggal = v.findViewById(R.id.tanggalHistory);
        qty = v.findViewById(R.id.qty);
        status = v.findViewById(R.id.statusHistory);
    }
}
    @Override
    public AdapterHistory.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        AdapterHistory.ViewHolder vh = new AdapterHistory.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(AdapterHistory.ViewHolder holder, final int position) {
        History history = daftarHistory.get(position);
        final String tanggal = history.getTanggal();
        final String qty = history.getQty();
        final String status = history.getStatus_penjualan();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 *  Kodingan untuk tutorial Selanjutnya :p Read detail data
                 */
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
                                //dialog.dismiss();
                                //context.startActivity(AprovalActivity.getActIntent
                                        //((Activity) context).putExtra("data", daftarHistory.get(position)));
                            }
                        }
                );

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
        holder.tanggal.setText(tanggal);
        holder.qty.setText(qty);
        holder.status.setText(status);
    }

    @Override
    public int getItemCount() {
        return daftarHistory.size();
    }
}