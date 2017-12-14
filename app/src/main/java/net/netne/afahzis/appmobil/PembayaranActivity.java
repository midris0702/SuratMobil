package net.netne.afahzis.appmobil;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import net.netne.afahzis.appmobil.server.AppVar;

public class PembayaranActivity extends AppCompatActivity {

    Toolbar toolbar;
    WebView mWebView;
    private ProgressDialog progressDialog;
    private ProgressDialog progressUpload;
    private LinearLayout ErrorLayout;
    private FloatingActionButton btnRefresh;
    private boolean isConnected = true;
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadMessage2;
    private final static int FILECHOOSER_RESULTCODE=1;
    public final static int REQUEST_SELECT_FILE=100;
    int cekhalaman=0;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String typeTrayek="";

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if(requestCode==FILECHOOSER_RESULTCODE)
        {
            if (null == mUploadMessage) return;
            Uri result = intent == null || resultCode != RESULT_OK ? null
                    : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }else if (requestCode == REQUEST_SELECT_FILE) {
            if (mUploadMessage2 == null) return;
            Uri[] result = intent == null || resultCode != RESULT_OK ? null
                    : new Uri[]{intent.getData()};
            try
            {
                mUploadMessage2.onReceiveValue(result);
            }
            catch (Exception e)
            {
            }
            mUploadMessage2 = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        mWebView = (WebView)findViewById(R.id.webView);
        ErrorLayout = (LinearLayout)findViewById(R.id.errorLayout);
        btnRefresh = (FloatingActionButton)findViewById(R.id.btnRefresh);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        progressDialog = new ProgressDialog(PembayaranActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressUpload = new ProgressDialog(PembayaranActivity.this);
        progressUpload.setTitle("Uploading File...");
        progressUpload.setCancelable(false);

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
            if (ni.getState() != NetworkInfo.State.CONNECTED) {
                progressDialog.dismiss();
                mWebView.setVisibility(View.GONE);
                ErrorLayout.setVisibility(View.VISIBLE);
                isConnected = false;
            }
        }

        sharedpreferences = getSharedPreferences(AppVar.PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        typeTrayek = getIntent().getExtras().getString("type");

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAllowContentAccess(true);
        mWebView.getSettings().setAllowFileAccess(true);
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
                progressUpload.dismiss();
                mWebView.setVisibility(View.VISIBLE);
                ErrorLayout.setVisibility(View.GONE);
                if(mWebView.getUrl().equals("https://trayektest.000webhostapp.com/Trayek2_3/mobile/pembayaran/?idUser=" + sharedpreferences.getString(AppVar.USER_ID,null) + "&type="+typeTrayek)){
                    cekhalaman=1;
                }
            }

        });

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                if(mUploadMessage2!=null){
                    mUploadMessage2=null;
                }
                mUploadMessage2 = filePathCallback;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                PembayaranActivity.this.startActivityForResult(Intent.createChooser(i, "File Browser"), UploadActivity.REQUEST_SELECT_FILE);
                return true;
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {

                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                PembayaranActivity.this.startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);

            }

            // For Android 3.0+
            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                PembayaranActivity.this.startActivityForResult(
                        Intent.createChooser(i, "File Browser"),
                        FILECHOOSER_RESULTCODE);
            }

            //For Android 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                PembayaranActivity.this.startActivityForResult(Intent.createChooser(i, "File Browser"), PembayaranActivity.FILECHOOSER_RESULTCODE);

            }

            @Override
            public void onProgressChanged(WebView view, int progress)
            {
                progressUpload.setProgress(0);
                if(cekhalaman==1){
                    progressUpload.show();
                    cekhalaman=0;
                }
                PembayaranActivity.this.setProgress(progress * 1000);

                progressUpload.incrementProgressBy(progress);

                if(progress == 100){
                    progressUpload.dismiss();
                }
            }
        });
        mWebView.loadUrl("https://trayektest.000webhostapp.com/Trayek2_3/mobile/pembayaran/?idUser=" + sharedpreferences.getString(AppVar.USER_ID,null)+"&type="+typeTrayek);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWebView.loadUrl("https://trayektest.000webhostapp.com/Trayek2_3/mobile/pembayaran/?idUser=" + sharedpreferences.getString(AppVar.USER_ID,null)+"&type="+typeTrayek);
            }
        });
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
