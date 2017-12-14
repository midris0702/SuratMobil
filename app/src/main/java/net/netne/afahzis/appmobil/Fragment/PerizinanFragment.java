package net.netne.afahzis.appmobil.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import net.netne.afahzis.appmobil.Adapter.AdapterPerizinan;
import net.netne.afahzis.appmobil.LoginActivity;
import net.netne.afahzis.appmobil.MainActivity;
import net.netne.afahzis.appmobil.R;
import net.netne.afahzis.appmobil.RegisterActivity;
import net.netne.afahzis.appmobil.SingupActivity;
import net.netne.afahzis.appmobil.server.AppVar;
import net.netne.afahzis.appmobil.server.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerizinanFragment extends Fragment {

    CardView btnLogin,btnRegister;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    LinearLayout notLogin;

    List<String> NO_POLISI = new ArrayList<String>();
    List<String> KETERANGAN = new ArrayList<String>();
    List<String> TANGGAL = new ArrayList<String>();
    List<String> ACC = new ArrayList<String>();

    ListView listItem;

    public PerizinanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perizinan, container, false);
        btnLogin = (CardView)view.findViewById(R.id.btnLogin);
        btnRegister = (CardView)view.findViewById(R.id.btnRegister);
        notLogin = (LinearLayout)view.findViewById(R.id.notLogin);
        listItem = (ListView) view.findViewById(R.id.listIzin);

        sharedpreferences = ((MainActivity)getContext()).getSharedPreferences(AppVar.PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        String login = sharedpreferences.getString(AppVar.SET_LOGIN, null);
        if (login != null) {
            notLogin.setVisibility(View.GONE);
            cekInfo();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivity(i);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), SingupActivity.class);
                i.putExtra("from","1");
                getActivity().startActivity(i);
            }
        });
        return view;
    }

    public void cekInfo(){
        new CekAsync().execute(
                AppVar.PREF_NAME,
                AppVar.KEY_IZIN,
                sharedpreferences.getString(AppVar.USER_ID,null)
        );
    }

    private class CekAsync extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();
        private final String SERVER_URL = AppVar.URL_SERVER;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            try {

                HashMap<String, String> params = new HashMap<>();
                params.put(AppVar.KEY_API, args[0]);
                params.put(AppVar.KEY_FUNCTION, args[1]);
                params.put(AppVar.KEY_IDUSER, args[2]);
                Log.d("request", "starting");

                JSONObject json = jsonParser.makeHttpRequest(
                        SERVER_URL, "POST", params);

                if (json != null) {
                    Log.d("JSON result", json.toString());

                    return json;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            if (json != null) {
                try{
                    NO_POLISI.clear();
                    KETERANGAN.clear();
                    TANGGAL.clear();
                    ACC.clear();
                    JSONArray jsonArray = json.getJSONArray("hasil");
                    if (jsonArray.length() == 0) {
                        listItem.setAdapter(null);
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject isiArray = jsonArray.getJSONObject(i);
                            String Tanggal = isiArray.getString("var_tanggal");
                            String NoPoll = isiArray.getString("var_no_polisi");
                            String Keterangan = isiArray.getString("var_keterangan");
                            String Acc = isiArray.getString("var_status");

                            NO_POLISI.add(NoPoll);
                            TANGGAL.add(Tanggal);
                            KETERANGAN.add(Keterangan);
                            ACC.add(Acc);
                        }
                        getAllData();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void getAllData() {
        listItem.setAdapter(null);
        AdapterPerizinan adapter = new AdapterPerizinan(getActivity(), NO_POLISI,KETERANGAN,TANGGAL,ACC);
        listItem.setAdapter(adapter);
        listItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

            }
        });
        listItem.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return true;
            }
        });
    }

}
