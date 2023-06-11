package com.example.project_semester_4;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class edit_password extends AppCompatActivity {

    private EditText password_lama, password_baru, password_baru_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        password_lama = findViewById(R.id.edit_password_lama);
        password_baru = findViewById(R.id.edit_password_baru);
        password_baru_confirm = findViewById(R.id.edit_konfirmasi_password_baru);

        // Set the status bar to transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button kirimButton = findViewById(R.id.button_simpan_edit);
        kirimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();
            }
        });


    }

    void sendData(){
        SharedPreferences sharedPreferences = getSharedPreferences("siswaPreferences", Context.MODE_PRIVATE);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = api.api_siswa + "change_password";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", response);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String code = jsonResponse.getString("code");
                            String message = jsonResponse.getString("message");
                            if (code.equals("200")) {
                                startActivity(new Intent(getApplicationContext(), dashboard.class));
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new  Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.toString());
                    }
                }
        ) {
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nisn", sharedPreferences.getString("nisn", ""));
                params.put("password", password_lama.getText().toString());
                params.put("password_baru", password_baru.getText().toString());
                params.put("password_baru_confirm", password_baru_confirm.getText().toString());

                return params;
            }
        };

        requestQueue.add(stringRequest);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}