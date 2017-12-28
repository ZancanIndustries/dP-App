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

/**
 * Created by giacomo.zancan on 19/12/2017.
 */

public class FacebookFragment extends Fragment {

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
        htmlWebView.loadData("<iframe src=\"https://www.facebook.com/plugins/page.php?href=https%3A%2F%2Fwww.facebook.com%2Fdragonspowergaming%2F&tabs=timeline&small_header=true&adapt_container_width=false&hide_cover=false&show_facepile=true&appId=1191569614311190\" width=\"340\" height=\"500\" style=\"border:none;overflow:hidden\" scrolling=\"no\" frameborder=\"0\" allowTransparency=\"true\"></iframe>", "text/html", "utf-8");
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

        OkHttpFacebook okHttpFacebook = new OkHttpFacebook();
        okHttpFacebook.execute();

        return view;
    }

    public class OkHttpFacebook extends AsyncTask {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected Object doInBackground(Object[] objects) {

            Request request = new Request.Builder()
                    .url("https://graph.facebook.com/v2.11/645380892264595/posts?access_token=EAAQ7ueuWDxYBABLxxJv5gf2jZAEUlIGKPFP0JFcvBqcYlIQC2LNoEVInRZA7hHjgyZBo6eZCHsZCAetEJlpqs1Kq31R86ETjGA9je5PL9SHw2a1xnVT4zFpzcfmuW909ar7ANYhoNJjB5PX8TwYi6bQPZA1VeibUZBXAKQFEIdIZAs8WsMAYPnCj1PwHM8q3Bt9FZCGSlu00beQZDZD")
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
