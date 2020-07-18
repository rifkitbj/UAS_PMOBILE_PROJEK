package com.example.databaseapiapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.databaseapiapp.api.ClientAsyncTask;
import com.example.databaseapiapp.model.Jadwal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private ListJadwalAdapter mAdapter;
    private List<Jadwal> listJadwal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent formIntent = new Intent(MainActivity.this, EditActivity.class);
                startActivity(formIntent);
            }
        });

        listView = (ListView)findViewById(R.id.list_data);

        listJadwal = new ArrayList<Jadwal>();
        mAdapter = new ListJadwalAdapter(this, listJadwal);

        listView.setAdapter(mAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                showActionDialog(position);
                return false;
            }
        });

        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Perintah buat load data disini ya
    private void loadData() {
        try {
            ClientAsyncTask task = new ClientAsyncTask(this, new ClientAsyncTask.OnPostExecuteListener() {
                @Override
                public void onPostExecute(String result) {
                    if (result.equals("error")) {
                        Toast.makeText(getBaseContext(), "Tidak Dapat Terkoneksi Dengan Server", Toast.LENGTH_SHORT).show();
                    } else {
                        processResponse(result);
                    }
                }
            });
            task.request_type = "get";
            task.api_url = "list_jadwal.php";
            task.showDialog = true;
            task.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // sama ini proses rubah datanya jadi JSON kayanya wkwk gatau w
    private void processResponse(String response) {
        Log.d("JSON_DATA", response);
        try {
            JSONObject jsonobj = new JSONObject(response);
            JSONArray jsonArray = jsonobj.getJSONArray("jadwal");

            Jadwal jadwal = null;
            for ( int i = 0 ; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                jadwal = new Jadwal();
                jadwal.setId(obj.getInt( "id"));
                jadwal.setNama(obj.getString( "nama"));
                jadwal.setTempat(obj.getString( "tempat"));
                jadwal.setWaktu(Date.valueOf(obj.getString("waktu")));
                listJadwal.add(jadwal);
            }
            mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showActionDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Choose Option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Intent formIntent = new Intent(MainActivity. this, EditActivity.class);
                    Jadwal jadwal = listJadwal.get( position );
                    formIntent.putExtra("id", jadwal.getId().toString());
                    formIntent.putExtra("nama", jadwal.getNama());
                    formIntent.putExtra("tempat", jadwal.getTempat());
                    formIntent.putExtra("waktu", jadwal.getWaktu()).toString();
                    startActivity(formIntent);
                } else {
                    deleteData(position);
                }
            }
        });
        builder.show();
    }

    private void deleteData(int position) {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id",String.valueOf(listJadwal.get(position).getId())));
        try {
            ClientAsyncTask task = new ClientAsyncTask(this, new ClientAsyncTask.OnPostExecuteListener() {
                @Override
                public void onPostExecute(String result) {
                    Log.d("TAG", "delete:" + result);
                    if (result.contains("Error description")) {
                        Toast.makeText(getBaseContext(), "Tidak Dapat Terkoneksi Dengan Server", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent in = new Intent(getApplicationContext(), MainActivity.class);
                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(in);
                    }
                }
            });
            task.request_type = "post";
            task.api_url = "delete_data.php";
            task.showDialog = true;
            task.setParams(params);
            task.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
