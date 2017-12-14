package net.netne.afahzis.appmobil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import net.netne.afahzis.appmobil.server.AppVar;
import net.netne.afahzis.appmobil.server.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SingupActivity extends AppCompatActivity {

    Toolbar toolbar;
    String getFrom="";

    private ProgressDialog progressDialog;
    EditText etUsername,etPassword,etNama,etRepassword;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    CardView btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);
        etUsername = (EditText)findViewById(R.id.username);
        etPassword = (EditText)findViewById(R.id.password);
        etNama = (EditText)findViewById(R.id.nama);
        etRepassword = (EditText)findViewById(R.id.repassword);
        btnRegister = (CardView)findViewById(R.id.btnRegister);

        sharedpreferences = getSharedPreferences(AppVar.PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Daftar");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFrom = getIntent().getExtras().getString("from");

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cek();
            }
        });
    }

    public void cek(){
        if(etNama.getText().toString().length()==0){
            etNama.setError("Nama harus di-isi!");
        }else if(etUsername.getText().toString().length()==0){
            etUsername.setError("Email harus di-isi!");
        }else if(etPassword.getText().toString().length()==0){
            etPassword.setError("Password harus di-isi!");
        }else if(etRepassword.getText().toString().length()==0){
            etRepassword.setError("Retype Password harus di-isi!");
        }else if(!isValidateEmail(etUsername.getText().toString())){
            etUsername.setError("Email tidak valid!");
        }else if(!etPassword.getText().toString().equals(etRepassword.getText().toString())){
            etRepassword.setError("Retype Password tidak sama!");
        }else{
            cekInfo();
        }
    }

    private boolean isValidateEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                if(getFrom.equals("1")){
                    Intent i = new Intent(SingupActivity.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }else {
                    Intent i = new Intent(SingupActivity.this,LoginActivity.class);
                    startActivity(i);
                    finish();
                }
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    public void onBackPressed() {
        if(getFrom.equals("1")){
            Intent i = new Intent(SingupActivity.this,MainActivity.class);
            startActivity(i);
            finish();
        }else {
            Intent i = new Intent(SingupActivity.this,LoginActivity.class);
            startActivity(i);
            finish();
        }
    }

    public void cekInfo() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Sedang mengirim data..!", true);
        new CekAsync().execute(
                AppVar.PREF_NAME,
                AppVar.KEY_REGISTER,
                etUsername.getText().toString().trim(),
                etPassword.getText().toString().trim(),
                etNama.getText().toString().trim()
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
                params.put(AppVar.KEY_EMAIL, args[2]);
                params.put(AppVar.KEY_PASSWORD, args[3]);
                params.put(AppVar.KEY_NAMA, args[4]);
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
                    String id = "";
                    if (success.equals("1")) {
                        id = userDetails.getString("var_id");

                        editor.remove(AppVar.USER_ID);

                        editor.putString(AppVar.SET_LOGIN, "1");
                        editor.putString(AppVar.USER_ID, id);

                        editor.commit();
                        MainActivity mActivity = new MainActivity();
                        mActivity.finish();
                        Intent i = new Intent(SingupActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        progressDialog.dismiss();
                        etUsername.setError("Email sudah tersedia, silahkan coba yang lain.");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }

            }
            progressDialog.dismiss();
        }
    }
}
