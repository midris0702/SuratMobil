package net.netne.afahzis.appmobil;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import net.netne.afahzis.appmobil.server.AppVar;
import net.netne.afahzis.appmobil.server.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MenuActivity extends AppCompatActivity {

    Toolbar toolbar;
    LinearLayout btnSyarat,btnDaftar,btnBayar,btnUpload,btnProof;
    String type;
    String setTanggal;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    private ProgressDialog progressDialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        sharedpreferences = getSharedPreferences(AppVar.PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        final String login = sharedpreferences.getString(AppVar.SET_LOGIN,null);

        type = getIntent().getExtras().getString("type");

        if(type.equals("1")){
            getSupportActionBar().setTitle("Taxi");
        }else if(type.equals("2")){
            getSupportActionBar().setTitle("Oplet");
        }else if(type.equals("3")){
            getSupportActionBar().setTitle("Bus Kota");
        }else if(type.equals("4")){
            getSupportActionBar().setTitle("Akap");
        }else if(type.equals("5")){
            getSupportActionBar().setTitle("Ajap");
        }else if(type.equals("6")){
            getSupportActionBar().setTitle("Angkutan Karyawan");
        }else if(type.equals("7")){
            getSupportActionBar().setTitle("Angkutan Orang/Barang");
        }else if(type.equals("8")){
            getSupportActionBar().setTitle("Uji Kir");
        }

        btnSyarat = (LinearLayout)findViewById(R.id.btn_syarat);
        btnBayar = (LinearLayout)findViewById(R.id.btn_bayar);
        btnDaftar = (LinearLayout)findViewById(R.id.btn_daftar);


        btnSyarat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this,PersyaratanActivity.class);
                startActivity(i);
            }
        });

        btnBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this,BiayaActivity.class);
                startActivity(i);
            }
        });

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(login!=null) {
                    Intent i = new Intent(MenuActivity.this, RegisterActivity.class);
                    i.putExtra("type", type);
                    startActivity(i);
                }else{
                    Toast.makeText(MenuActivity.this,"Silahkan Login Terlebih Dahulu.", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    public void cekInfo() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Sedang mengambil data....!", true);
        new CekAsync().execute(
                AppVar.PREF_NAME,
                AppVar.KEY_JADWAL,
                sharedpreferences.getString(AppVar.USER_ID,null),
                setTanggal
        );
    }

    class CekAsync extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();
        private final String LOGIN_URL = AppVar.URL_SERVER;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected JSONObject doInBackground(String... args) {

            try {

                HashMap<String, String> params = new HashMap<>();
                params.put(AppVar.KEY_API, args[0]);
                params.put(AppVar.KEY_FUNCTION, args[1]);
                params.put(AppVar.KEY_IDUSER, args[2]);
                params.put(AppVar.KEY_TANGGAL, args[3]);
                Log.d("request", "starting");

                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", params);

                if (json != null) {
                    Log.d("JSON result", json.toString());
                    return json;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(JSONObject json) {
            if (json != null) {
//                Toast.makeText(getApplicationContext(),json.toString(),Toast.LENGTH_SHORT).show();

                try {
                    JSONObject parentObject = new JSONObject(json.toString());
                    JSONObject userDetails = parentObject.getJSONObject("hasil");
                    String success = userDetails.getString("success");
                    if (success.equals("1")) {
                        Toast.makeText(MenuActivity.this,"Jadwal telah diPilih, mohon kehadiran Anda pada jadwal tersebut", Toast.LENGTH_LONG).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Jadwal gagal ditentukan", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }

            }
            progressDialog.dismiss();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
