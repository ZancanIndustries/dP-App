package zi.dpapp;

import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class InstagramFragment extends Fragment {

    WebView htmlWebView;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_facebook, container, false);
        htmlWebView = (WebView) view.findViewById(R.id.webView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        WebSettings webSetting = htmlWebView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        htmlWebView.loadUrl("https://www.instagram.com/dragonspowergaming/");
        webSetting.setDisplayZoomControls(true);
        htmlWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                progressBar.setVisibility(View.VISIBLE);
                view.loadUrl(url);
                return true;

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });

        OkHttpInstagram okHttpInstagram = new OkHttpInstagram();
        okHttpInstagram.execute();

        return view;
    }

    public class OkHttpInstagram extends AsyncTask {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected Object doInBackground(Object[] objects) {

            Request request = new Request.Builder()
                    .url("https://api.instagram.com/v1/users/5496981462/media/recent/?access_token=5496981462.1677ed0.0618fa371f1d40a198b375c0a9db7b4e")
                    .get()
                    .addHeader("Cache-Control", "no-cache")
                    .addHeader("Postman-Token", "34f2a35e-aa98-c061-4b3f-1f12f36fff46")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Log.d("okhttp", o.toString());

        }
    }

}
