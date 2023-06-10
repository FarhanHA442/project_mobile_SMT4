package com.example.project_semester_4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import  com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
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
        TextView titleDashboard = findViewById(R.id.title_dashboard);
        TextView txtTotal = findViewById(R.id.total_tagihan);

        SharedPreferences sharedPreferences = getSharedPreferences("siswaPreferences", Context.MODE_PRIVATE);
        Log.d("NISNShared", sharedPreferences.getString("nisn", ""));
        if (sharedPreferences.getString("nisn", "").equals("")){
            startActivity(new Intent(getApplicationContext(), login.class));
        }

        titleDashboard.setText("Hai " + sharedPreferences.getString("nama", "") +  "!");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        //dropdown
        autoCompleteTextView = findViewById(R.id.AutoCompleteTextview);

        //array daftar bulan
        String[] Subjects = new String[]{"Semua" ,"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};

        //membuat adapter array dan berikan parameter yang diperlukan
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_dashboard, Subjects);
        autoCompleteTextView.setAdapter(adapter);

        //untuk mendapatkan nilai yang dipilih ketika klik item
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "" + autoCompleteTextView.getText().toString(), Toast.LENGTH_SHORT).show();

                recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                adapterTagihan = new adapter_tagihan(tagihanArrayList);
                String url = api.api_tagihan + sharedPreferences.getString("nisn", "") + "/" + autoCompleteTextView.getText();

                adapterTagihan.clearData();
                recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                adapterTagihan = new adapter_tagihan(tagihanArrayList);

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(dashboard.this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapterTagihan);
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    txtTotal.setText(convertToCurrency(Double.parseDouble(jsonResponse.getString("total"))));
                                    JSONArray jsonArray = jsonResponse.getJSONArray("data");
                                    tagihanArrayList = new ArrayList<>();
                                    for(int i=0; i<jsonArray.length(); i++){
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        JSONObject jsonObject1 = jsonObject.getJSONObject("list_jenis_tagihan");
                                        String textTitle = "";
                                        String id_tagihan = jsonObject.getString("id_tagihan");
                                        if (jsonObject.getString("bulan").equals("null")){
                                            textTitle = jsonObject1.getString("nama_jenis_tagihan");
                                        }else{
                                            textTitle = jsonObject1.getString("nama_jenis_tagihan") + " " + jsonObject.getString("bulan") +  " " + jsonObject.getString("tahun");
                                        }

                                        tagihan murid = new tagihan(textTitle, id_tagihan);
                                        tagihanArrayList.add(murid);

                                        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                                        adapterTagihan = new adapter_tagihan(tagihanArrayList);

                                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(dashboard.this);
                                        recyclerView.setLayoutManager(layoutManager);
                                        recyclerView.setAdapter(adapterTagihan);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("ErrorResponse", error.getMessage());
                            }
                        });
                requestQueue.add(stringRequest);
            }
        });

        setData();
    }

    public String convertToCurrency(double number) {
        // Create a NumberFormat instance with the desired locale and currency style
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

        // Convert the number to currency format
        String currencyString = currencyFormat.format(number);

        // Return the currency string
        return currencyString;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    void setData(){
        SharedPreferences sharedPreferences = getSharedPreferences("siswaPreferences", Context.MODE_PRIVATE);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = api.api_tagihan + sharedPreferences.getString("nisn", "");
        TextView txtTotal = findViewById(R.id.total_tagihan);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            txtTotal.setText(convertToCurrency(Double.parseDouble(jsonResponse.getString("total"))));
                            JSONArray jsonArray = jsonResponse.getJSONArray("data");
                            tagihanArrayList = new ArrayList<>();
                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                JSONObject jsonObject1 = jsonObject.getJSONObject("list_jenis_tagihan");
                                String textTitle = "";
                                String id_tagihan = jsonObject.getString("id_tagihan");
                                if (jsonObject.getString("bulan").equals("null")){
                                    textTitle = jsonObject1.getString("nama_jenis_tagihan");
                                }else{
                                    textTitle = jsonObject1.getString("nama_jenis_tagihan") + " " + jsonObject.getString("bulan") +  " " + jsonObject.getString("tahun");
                                }
                                tagihan murid = new tagihan(textTitle, id_tagihan);
                                tagihanArrayList.add(murid);

                                recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                                adapterTagihan = new adapter_tagihan(tagihanArrayList);

                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(dashboard.this);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setAdapter(adapterTagihan);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ErrorResponse", error.getMessage());
                    }
                });
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences sharedPreferences = getSharedPreferences("siswaPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int id = item.getItemId();

        if (id == R.id.edit_password){
            Intent intent = new Intent(dashboard.this, edit_password.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.exit){
            editor.remove("nisn");
            editor.apply();
            startActivity(new Intent(getApplicationContext(), login.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}