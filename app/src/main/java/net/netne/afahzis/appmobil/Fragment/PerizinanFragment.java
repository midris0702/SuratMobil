package net.netne.afahzis.appmobil.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.DownloadListener;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.net.Uri;
import android.app.DownloadManager;
import android.os.Environment;

import net.netne.afahzis.appmobil.LoginActivity;
import net.netne.afahzis.appmobil.MainActivity;
import net.netne.afahzis.appmobil.R;
import net.netne.afahzis.appmobil.RegisterActivity;
import net.netne.afahzis.appmobil.SingupActivity;
import net.netne.afahzis.appmobil.server.AppVar;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerizinanFragment extends Fragment {


    CardView btnLogin,btnRegister;
    LinearLayout btnLogout;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    WebView mWebView;
    private ProgressDialog progressDialog;

    private LinearLayout ErrorLayout;
    private FloatingActionButton btnRefresh;
    private boolean isConnected = true;

    LinearLayout notLogin;

    public PerizinanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        btnLogin = (CardView)view.findViewById(R.id.btnLogin);
        btnRegister = (CardView)view.findViewById(R.id.btnRegister);
        btnLogout = (LinearLayout)view.findViewById(R.id.btnLogout);
        notLogin = (LinearLayout)view.findViewById(R.id.notLogin);

        sharedpreferences = ((MainActivity)getContext()).getSharedPreferences(AppVar.PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        String login = sharedpreferences.getString(AppVar.SET_LOGIN, null);
        if (login != null) {
            notLogin.setVisibility(View.GONE);
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
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.remove(AppVar.SET_LOGIN);
                editor.remove(AppVar.USER_ID);
                editor.commit();
                Intent i = new Intent(getActivity(),LoginActivity.class);
                startActivity(i);
                ((MainActivity)getContext()).finish();
            }
        });
        mWebView = (WebView)view.findViewById(R.id.webView);
        ErrorLayout = (LinearLayout)view.findViewById(R.id.errorLayout);
        btnRefresh = (FloatingActionButton)view.findViewById(R.id.btnRefresh);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
            if (ni.getState() != NetworkInfo.State.CONNECTED) {
                progressDialog.dismiss();
                isConnected = false;
            }
        }

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setUserAgentString("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0");
        mWebView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://trayek.dishub-pekanbaru.com/mobile/trayek_saya/?idUser=" + sharedpreferences.getString(AppVar.USER_ID,null)));
                startActivity(i);
            }
        });
        mWebView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                progressDialog.show();
                if (isConnected) {
                    return false;
                } else {
                    progressDialog.dismiss();
                    mWebView.setVisibility(View.GONE);
                    ErrorLayout.setVisibility(View.VISIBLE);
                    return true;
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                progressDialog.dismiss();
                mWebView.setVisibility(View.GONE);
                ErrorLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressDialog.dismiss();
                mWebView.setVisibility(View.VISIBLE);
                ErrorLayout.setVisibility(View.GONE);
            }

        });
        mWebView.loadUrl("http://trayek.dishub-pekanbaru.com/mobile/trayek_saya/?idUser=" + sharedpreferences.getString(AppVar.USER_ID,null));
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWebView.loadUrl("http://trayek.dishub-pekanbaru.com/mobile/trayek_saya/?idUser=" + sharedpreferences.getString(AppVar.USER_ID,null));
            }
        });
        return view;

    }

}
