package com.example.project_semester_4;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class adapter_tagihan extends RecyclerView.Adapter<adapter_tagihan.RincianViewHolder> {

    private ArrayList<tagihan> dataList;

    public adapter_tagihan(ArrayList<tagihan> dataList) {
        this.dataList = dataList;
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
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class RincianViewHolder extends RecyclerView.ViewHolder {
        private TextView Nama;

        public RincianViewHolder(@NonNull View itemView) {
            super(itemView);
            Nama = (TextView) itemView.findViewById(R.id.nama);
        }
    }
}
