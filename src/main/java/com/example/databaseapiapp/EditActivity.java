package com.example.databaseapiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.databaseapiapp.api.ClientAsyncTask;
import com.example.databaseapiapp.model.Jadwal;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.sql.Date;
import java.util.ArrayList;

public class EditActivity extends AppCompatActivity {
    EditText edtTempat, edtNama, edtWaktu;
    Button btnSimpan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        edtTempat = findViewById(R.id.edt_tempat);
        edtNama = findViewById(R.id.edt_nama);
        edtWaktu = findViewById(R.id.edt_waktu);

        final Jadwal jadwal = new Jadwal();
        if (getIntent().hasExtra("id")) {
            String id = getIntent().getStringExtra("id");
            String nama = getIntent().getStringExtra("nama");
            String tempat = getIntent().getStringExtra("tempat");
            String waktu = getIntent().getStringExtra("waktu");
            edtNama.setText(nama);
            edtTempat.setText(tempat);
            edtWaktu.setText(waktu);
            jadwal.setId(Integer.valueOf(id));
        } else {
            jadwal.setId(0);
        }

        btnSimpan = findViewById(R.id.btn_simpan);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama = edtNama.getText().toString();
                String tempat = edtTempat.getText().toString();
                Date waktu = Date.valueOf(edtWaktu.getText().toString());
                simpanData(jadwal.getId(), nama, tempat, waktu);
            }
        });
    }

    private void simpanData(int id, String nama, String tempat, Date waktu) {
        ArrayList<NameValuePair> params = new ArrayList<>();
        if (id != 0) {
            params.add(new BasicNameValuePair("id", String.valueOf(id)));
        }
        params.add(new BasicNameValuePair("nama", nama));
        params.add(new BasicNameValuePair("tempat", tempat));
        params.add(new BasicNameValuePair("waktu", String.valueOf(waktu)));

        try {
            ClientAsyncTask task = new ClientAsyncTask(this, new ClientAsyncTask.OnPostExecuteListener() {
                @Override
                public void onPostExecute(String result) {
                    Log.d("TAG", "savedata:" + result);

                    if (result.contains("Error description")) {
                        Toast.makeText(getBaseContext(), "Tidak Dapat terkoneksi Dengan Server", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent in = new Intent(getApplicationContext(), MainActivity.class);
                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(in);
                    }
                }
            });
            task.request_type = "post";
            task.api_url = "save_data.php";
            task.showDialog = true;
            task.setParams(params);
            task.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
