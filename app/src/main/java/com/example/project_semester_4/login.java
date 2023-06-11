package com.example.project_semester_4;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sharedPreferences = getSharedPreferences("siswaPreferences", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (!sharedPreferences.getString("nisn", "").equals("")){
            startActivity(new Intent(getApplicationContext(), dashboard.class));
        }

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = api.api_siswa;

        // Set the status bar to transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }




        usernameEditText = findViewById(R.id.username_edit);
        passwordEditText = findViewById(R.id.password_edit);
        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e("Response", response);
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    String code = jsonResponse.getString("code");
                                    String message = jsonResponse.getString("message");
                                    JSONObject jsonData = jsonResponse.getJSONObject("data");
                                    if (code.equals("200")) {
                                        editor.putString("nisn", jsonData.getString("nisn"));
                                        editor.putString("nama", jsonData.getString("nama"));
                                        editor.apply();
                                        Log.d("SharedPreferences", sharedPreferences.getString("nisn", ""));
                                        startActivity(new Intent(getApplicationContext(), dashboard.class));
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                    } else if (code.equals("201")){
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
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
                        params.put("nisn", usernameEditText.getText().toString());
                        params.put("password", passwordEditText.getText().toString());

                        return params;
                    }
                };
                requestQueue.add(stringRequest);

            }
        });
    }
}