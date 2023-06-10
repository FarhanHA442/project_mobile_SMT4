package com.example.project_semester_4;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Pembayaran extends AppCompatActivity {

    ImageView imgGallery;
    TextView Judul, Harga;
    private String judul, id_tagihan;

    private Bitmap bitmap;
    private String filePath;
    private Uri picUri;


    private final int GALLERY_REQ_CODE = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran);

        setData();

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

        Button kirimButton = findViewById(R.id.kirim_Button);
        kirimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show Snackbar here
                Snackbar.make(view, "Dokumen telah dikirim, silahkan tunggu konfirmasi dari admin", Snackbar.LENGTH_SHORT).show();
            }
        });

        imgGallery = findViewById(R.id.imgGallery);
        Button btnGallery = findViewById(R.id.btn_Camera);
        Button kirimBtn = findViewById(R.id.kirim_Button);

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, GALLERY_REQ_CODE);
            }
        });

        kirimBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (picUri != null) {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), picUri);
                        uploadBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(
                            getApplicationContext(),"Masukkan bukti terlebih dahulu",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    void setData(){
        Judul = (TextView) findViewById(R.id.Judul);
        Harga = findViewById(R.id.Harga);

        Bundle extras = getIntent().getExtras();
        judul = extras.getString("nama_tagihan");
        id_tagihan = extras.getString("id_tagihan");

        Judul.setText(judul);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = api.api_tagihan + "tagihan/" + id_tagihan + "/detail";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONObject jsonData = jsonResponse.getJSONObject("data");
                            Harga.setText(convertToCurrency(Double.parseDouble(jsonData.getString("harga_tagihan"))));


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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK){
            if (requestCode==GALLERY_REQ_CODE){
                imgGallery.setImageURI(data.getData());
                picUri = data.getData();
            }
        }
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

    public String getPath(Uri uri) {
        try {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            String document_id = cursor.getString(0);
            Log.d("documentID" , cursor.moveToFirst() +  " tes");
            document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
            cursor.close();

            cursor = getContentResolver().query(
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
            cursor.moveToFirst();
            String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
            cursor.close();

            return "test";
        } catch(Exception error){
            Log.d("errorPath", error.getMessage());
            return "false";
        }

    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadBitmap(final Bitmap bitmap) {

        String url = api.api_validasi;
        Bundle extras = getIntent().getExtras();
        SharedPreferences sharedPreferences = getSharedPreferences("siswaPreferences", Context.MODE_PRIVATE);
        String nisn = sharedPreferences.getString("nisn", "");
        id_tagihan = extras.getString("id_tagihan");

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            if (obj.getString("code").equals("200")){
                                Intent i = new Intent(getApplicationContext(), dashboard.class);
                                startActivity(i);
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(), "Permintaan gagal! Coba beberapa saat lagi.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("GotError",""+error.getMessage());
                    }
                }) {


            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("nisn", nisn);
                params.put("id_tagihan", id_tagihan);

                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("gambar_pembayaran", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    public String convertToCurrency(double number) {
        // Create a NumberFormat instance with the desired locale and currency style
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

        // Convert the number to currency format
        String currencyString = currencyFormat.format(number);

        // Return the currency string
        return currencyString;
    }
}