package com.example.project_semester_4;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class adapter_tagihan extends RecyclerView.Adapter<adapter_tagihan.RincianViewHolder> {

    private ArrayList<tagihan> dataList;
    private Context context;

    public adapter_tagihan(ArrayList<tagihan> dataList) {
        this.dataList = dataList;
    }

    public class RincianViewHolder extends RecyclerView.ViewHolder {
        private TextView Nama;
        private Button BayarButton;

        public RincianViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            Nama = itemView.findViewById(R.id.nama);
            BayarButton = itemView.findViewById(R.id.bayar_button);
        }
    }
    @NonNull
    @Override
    public adapter_tagihan.RincianViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.tagihan, viewGroup, false);
        return new RincianViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_tagihan.RincianViewHolder RincianViewHolder, int i) {
        RincianViewHolder.Nama.setText(dataList.get(i).getNama());
        String id_tagihan = dataList.get(i).getId_tagihan();
        String namaPembayaran = dataList.get(i).getNama();
        RincianViewHolder.BayarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Pembayaran.class);
                intent.putExtra("id_tagihan", id_tagihan);                intent.putExtra("id_tagihan", id_tagihan);
                intent.putExtra("nama_tagihan", namaPembayaran);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public void clearData(){
        dataList.clear();
        notifyDataSetChanged();
    }

}
