package com.example.project_semester_4;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class tagihan extends AppCompatActivity {

    private String nama;
    private String id_tagihan;

    public tagihan(String nama, String id_tagihan) {
        this.nama = nama;
        this.id_tagihan = id_tagihan;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getId_tagihan() { return id_tagihan; }

    public void setId_tagihan(String id_tagihan) { this.id_tagihan = id_tagihan;}

}



