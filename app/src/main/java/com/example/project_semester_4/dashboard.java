package com.example.project_semester_4;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class dashboard extends AppCompatActivity {

    //recycleview
    private RecyclerView recyclerView;
    private adapter_tagihan adapterTagihan;
    private ArrayList<tagihan> tagihanArrayList;

    AutoCompleteTextView autoCompleteTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Set the status bar to transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


        //dropdown
        autoCompleteTextView = findViewById(R.id.AutoCompleteTextview);

        //array daftar bulan
        String[] Subjects = new String[]{"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};

        //membuat adapter array dan berikan parameter yang diperlukan
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_dashboard, Subjects);
        autoCompleteTextView.setAdapter(adapter);

        //untuk mendapatkan nilai yang dipilih ketika klik item
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "" + autoCompleteTextView.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        //batas akhir dropdown

        //recycleview
        String jsonData = "[{'name':'Pembayaran 1'}," + "{'name': 'Pembayaran 2'}," + "{'name': 'Pembayaran 3'}," + "{'name': 'Pembayaran 4'}," + "{'name': 'Pembayaran 5'}," +
                "{'name': 'Pembayaran 6'}," + "{'name': 'Pembayaran 7'}," + "{'name': 'Pembayaran 8'}," + "{'name': 'Pembayaran 9'}," + "{'name': 'Pembayaran 10'}," + "{'name': 'Pembayaran 11'}," +
                "{'name': 'Pembayaran 12'}," + "{'name': 'Pembayaran 13'}," + "{'name': 'Pembayaran 14'}," + "{'name': 'Pembayaran 15'}," + "{'name': 'Pembayaran 16'}]";


        setData(jsonData);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapterTagihan = new adapter_tagihan(tagihanArrayList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(dashboard.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterTagihan);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return  true;
    }

    void setData(String jsonString){
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            tagihanArrayList = new ArrayList<>();
            for(int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                tagihan murid = new tagihan(jsonObject.getString("name"));

                tagihanArrayList.add(murid);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.edit_password){
            Intent intent = new Intent(dashboard.this, edit_password.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.exit){
            // Menghapus Status login dan kembali ke Login Activity
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}