package net.netne.afahzis.appmobil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;

import net.netne.afahzis.appmobil.server.AppVar;

/**
 * Created by iwan on 12/17/17.
 */

public class ComingsoonActivity extends AppCompatActivity {
    Toolbar toolbar;
    LinearLayout btnSyarat, btnDaftar, btnBayar, btnUpload, btnProof, btnJadwal;
    String type;
    String setTanggal;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coming_soon);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        sharedpreferences = getSharedPreferences(AppVar.PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        final String login = sharedpreferences.getString(AppVar.SET_LOGIN, null);

        type = getIntent().getExtras().getString("type");
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