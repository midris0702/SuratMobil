package net.netne.afahzis.appmobil.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import net.netne.afahzis.appmobil.ComingsoonActivity;
import net.netne.afahzis.appmobil.LoginActivity;
import net.netne.afahzis.appmobil.MainActivity;
import net.netne.afahzis.appmobil.MenuActivity;
import net.netne.afahzis.appmobil.R;
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
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    CardView btnTaxi,btnOplet,btnBus,btnAkap,btnAjap,btnKaryawan,btnBarang,btnKir,btnLogin;
    RelativeLayout layoutBanner;
    int chek=0;
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private TextView[] dots;
    private LinearLayout dotsLayouts,layout_btnLogin,layout_dot;
    private int[] layaout;

    List<String> ID_NEWS = new ArrayList<String>();
    List<String> IMAGE_NEWS = new ArrayList<String>();
    List<String> LINK_NEWS = new ArrayList<String>();
    List<String> DESKRIPSI_NEWS = new ArrayList<String>();

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    int i;

    Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        btnTaxi = (CardView) view.findViewById(R.id.btn_taxi);
        btnOplet = (CardView) view.findViewById(R.id.btn_oplet);
        btnBus = (CardView) view.findViewById(R.id.btn_bus);
        btnAkap = (CardView) view.findViewById(R.id.btn_akap);
        btnAjap = (CardView) view.findViewById(R.id.btn_ajap);
        btnKaryawan = (CardView) view.findViewById(R.id.btn_karyawan);
        btnBarang = (CardView) view.findViewById(R.id.btn_barang);
        btnKir = (CardView) view.findViewById(R.id.btn_kir);
        layoutBanner = (RelativeLayout) view.findViewById(R.id.layoutBanner);
        dotsLayouts = (LinearLayout) view.findViewById(R.id.layoutDots);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        btnLogin =(CardView) view.findViewById(R.id.btnLogin);
        layout_btnLogin= (LinearLayout)view.findViewById(R.id.layout_btnLogin);
        layout_dot=(LinearLayout)view.findViewById(R.id.layout_dot);

        ((MainActivity)getContext()).setSupportActionBar(toolbar);
        ((MainActivity)getContext()).getSupportActionBar().setTitle(R.string.title_activity_main);
        toolbar.setLogo(R.mipmap.ic_dishub_bar);


        sharedpreferences = ((MainActivity)getContext()).getSharedPreferences(AppVar.PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        String login = sharedpreferences.getString(AppVar.SET_LOGIN, null);
        if (login != null) {
            layout_btnLogin.setVisibility(View.GONE);
            layout_dot.setPadding(0,0,0,0);
        }else{
            layout_dot.setPadding(0,0,0,50);
        }

        btnTaxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(getActivity(),MenuActivity.class);
                i.putExtra("type","1");
                startActivity(i);
            }
        });

        btnOplet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(getActivity(),MenuActivity.class);
                i.putExtra("type","2");
                startActivity(i);
            }
        });

        btnBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(getActivity(),MenuActivity.class);
                i.putExtra("type","3");
                startActivity(i);
            }
        });

        btnAkap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(getActivity(),MenuActivity.class);
                i.putExtra("type","4");
                startActivity(i);
            }
        });

        btnAjap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(getActivity(),MenuActivity.class);
                i.putExtra("type","5");
                startActivity(i);
            }
        });

        btnKaryawan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(getActivity(),MenuActivity.class);
                i.putExtra("type","6");
                startActivity(i);
            }
        });

        btnBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(getActivity(),MenuActivity.class);
                i.putExtra("type","7");
                startActivity(i);
            }
        });

        btnKir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(getActivity(),ComingsoonActivity.class);
                i.putExtra("type","8");
                startActivity(i);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(getActivity(),LoginActivity.class);
                startActivity(i);
            }
        });

        cekInfo();
        return view;
    }

    private void addBottomDots(int position) {
        dotsLayouts.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getActivity());
            dots[i].setText(".");
            dots[i].setTextSize(45);
            dots[i].setTypeface(null, Typeface.BOLD);
            dots[i].setTextColor(Color.GRAY);
            dotsLayouts.addView(dots[i]);
        }
        if (dots.length > 0) {
            dots[position].setTextColor(this.getResources().getColor(R.color.colorSkyBlue));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public class ViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(layaout[position], container, false);
            ImageView ivImage = (ImageView)v.findViewById(R.id.imgInfo);
            TextView txtId = (TextView)v.findViewById(R.id.idInfo);
            TextView txtDeskripsi = (TextView)v.findViewById(R.id.tvDeskripsi);
            TextView txtUrl = (TextView)v.findViewById(R.id.urlSlide);
            TextView btnLaman = (TextView)v.findViewById(R.id.btn_Laman);
            RelativeLayout goLink = (RelativeLayout)v.findViewById(R.id.linkInfo);

            if(chek==1) {
                Picasso.with(getActivity())
                        .load(AppVar.URL_IMAGE + IMAGE_NEWS.get(position))
                        .placeholder(R.drawable.ic_terrain)
                        .error(R.drawable.ic_error)
                        .into(ivImage);
                txtId.setText(ID_NEWS.get(position));
                txtUrl.setText(LINK_NEWS.get(position));
                txtDeskripsi.setText(Html.fromHtml(Html.fromHtml(DESKRIPSI_NEWS.get(position)).toString()));
                if(!LINK_NEWS.get(position).equals("-")){
                    /*goLink.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String url = String.valueOf(LINK_NEWS.get(position));
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            MainActivity.this.startActivity(intent);
                        }
                    });*/
                    btnLaman.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String url = String.valueOf(LINK_NEWS.get(position));
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            getActivity().startActivity(intent);
                        }
                    });
                }
            }
            container.addView(v);
            return v;
        }

        @Override
        public int getCount() {
            return layaout.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View v = (View) object;
            container.removeView(v);
        }
    }

    public void cekInfo(){
        new CekAsync().execute(
                AppVar.PREF_NAME,
                AppVar.KEY_INFO
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
                    ID_NEWS.clear();
                    IMAGE_NEWS.clear();
                    LINK_NEWS.clear();
                    DESKRIPSI_NEWS.clear();
                    JSONArray jsonArray = json.getJSONArray("hasil");
                    if (jsonArray.length() == 0) {
                        chek=0;
                        layoutBanner.setVisibility(View.GONE);
                    } else {
                        layoutBanner.setVisibility(View.VISIBLE);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject isiArray = jsonArray.getJSONObject(i);
                            String id = isiArray.getString("var_id");
                            String link = isiArray.getString("var_link");
                            String deskripsi = isiArray.getString("var_deskripsi");
                            String image = isiArray.getString("var_image");

                            ID_NEWS.add(id);
                            LINK_NEWS.add(link);
                            DESKRIPSI_NEWS.add(deskripsi);
                            IMAGE_NEWS.add(image);
                        }
                        chek=1;

                        layaout = new int[jsonArray.length()];
                        for (int i = 0; i < jsonArray.length(); i++) {
                            layaout[i]=R.layout.slide_layout;
                        }

                        dots = new TextView[layaout.length];

                        addBottomDots(0);
                        i = 0;
                        viewPagerAdapter = new ViewPagerAdapter();
                        viewPager.setAdapter(viewPagerAdapter);
                        viewPager.addOnPageChangeListener(viewListener);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}
