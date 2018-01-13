package net.netne.afahzis.appmobil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import net.netne.afahzis.appmobil.Fragment.HomeFragment;
import net.netne.afahzis.appmobil.Fragment.InboxFragment;
import net.netne.afahzis.appmobil.Fragment.PerizinanFragment;
import net.netne.afahzis.appmobil.Fragment.ProfileFragment;
import net.netne.afahzis.appmobil.server.AppVar;
import net.netne.afahzis.appmobil.server.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    FrameLayout viewPage;
    LinearLayout btnHome, btnPerizinan, btnProfile;
    Fragment frg = null;
    FragmentTransaction transaction = null;
    int menu=1;
    ImageView imgHome,imgPerizinan,imgInbox,imgProfile;
    TextView txtHome,txtPerizinan,txtInbox,txtProfile;

    Toolbar toolbar;
    AppBarLayout AppBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPage = (FrameLayout)findViewById(R.id.pageView);
        btnHome = (LinearLayout)findViewById(R.id.btnHome);
        btnPerizinan = (LinearLayout)findViewById(R.id.btnPerizinan);
        btnProfile = (LinearLayout)findViewById(R.id.btnProfile);

        imgHome =(ImageView)findViewById(R.id.imgHome);
        imgPerizinan = (ImageView)findViewById(R.id.imgPerizinan);
        imgProfile = (ImageView)findViewById(R.id.imgProfile);

        txtHome =(TextView)findViewById(R.id.txtHome);
        txtPerizinan = (TextView)findViewById(R.id.txtPerizinan);
        txtProfile = (TextView)findViewById(R.id.txtProfile);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        AppBarLayout = (AppBarLayout)findViewById(R.id.AppBarLayout);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_activity_main);
        toolbar.setLogo(R.mipmap.ic_dishub_bar);


        openFragment(new HomeFragment());
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menu!=1){
                    openFragment(new HomeFragment());
                    txtHome.setTextColor(Color.parseColor("#0022ba"));
                    imgHome.setColorFilter(Color.parseColor("#0022ba"));

                    txtPerizinan.setTextColor(Color.parseColor("#cccccc"));
                    imgPerizinan.setColorFilter(Color.parseColor("#cccccc"));
                    txtProfile.setTextColor(Color.parseColor("#cccccc"));
                    imgProfile.setColorFilter(Color.parseColor("#cccccc"));
                    AppBarLayout.setVisibility(View.GONE);
                }
                menu=1;
                viewPage.setVisibility(View.VISIBLE);
            }
        });
        btnPerizinan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menu!=2){
                    openFragment(new PerizinanFragment());
                    txtPerizinan.setTextColor(Color.parseColor("#0022ba"));
                    imgPerizinan.setColorFilter(Color.parseColor("#0022ba"));

                    txtHome.setTextColor(Color.parseColor("#cccccc"));
                    imgHome.setColorFilter(Color.parseColor("#cccccc"));
                    txtProfile.setTextColor(Color.parseColor("#cccccc"));
                    imgProfile.setColorFilter(Color.parseColor("#cccccc"));
                    AppBarLayout.setVisibility(View.VISIBLE);
                }
                menu=2;
                viewPage.setVisibility(View.VISIBLE);
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menu!=3){
                    openFragment(new ProfileFragment());
                    txtProfile.setTextColor(Color.parseColor("#0022ba"));
                    imgProfile.setColorFilter(Color.parseColor("#0022ba"));

                    txtHome.setTextColor(Color.parseColor("#cccccc"));
                    imgHome.setColorFilter(Color.parseColor("#cccccc"));
                    txtPerizinan.setTextColor(Color.parseColor("#cccccc"));
                    imgPerizinan.setColorFilter(Color.parseColor("#cccccc"));
                    AppBarLayout.setVisibility(View.VISIBLE);
                }
                menu=3;
                if(!isNetworkAvailable()){
                    viewPage.setVisibility(View.GONE);
                }else{
                    viewPage.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (menu!=1){
            openFragment(new HomeFragment());
            txtHome.setTextColor(Color.parseColor("#0022ba"));
            imgHome.setColorFilter(Color.parseColor("#0022ba"));

            txtPerizinan.setTextColor(Color.parseColor("#cccccc"));
            imgPerizinan.setColorFilter(Color.parseColor("#cccccc"));
            txtInbox.setTextColor(Color.parseColor("#cccccc"));
            imgInbox.setColorFilter(Color.parseColor("#cccccc"));
            txtProfile.setTextColor(Color.parseColor("#cccccc"));
            imgProfile.setColorFilter(Color.parseColor("#cccccc"));
            AppBarLayout.setVisibility(View.GONE);
            menu=1;
        }else{
            finish();
        }
        menu=1;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void openFragment(Fragment frag) {
        frg = frag;
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fadeout, R.anim.fadein);
        transaction.replace(R.id.pageView, frg);
        transaction.commit();
        transaction.addToBackStack(null);
    }

}
