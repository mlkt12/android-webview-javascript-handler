package com.mlkt.development.javascripthandler;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "JSHANDLER";

    private WebView webView;
    private YaMapBuilder builder;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webview);
        webView.clearCache(true);
        webView.clearHistory();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.addJavascriptInterface(new JavaScriptHandler(), "Android");

        webView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right,int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                builder = new YaMapBuilder(webView.getWidth()/3, webView.getHeight()/3);

                String latitude = getRandomCoordinate();
                String longitude = getRandomCoordinate();
                builder.addMap(latitude,longitude)
                       .addPlaceMark(1,latitude,longitude,"Location",String.format("#%06X", (0xFFFFFF & getRandomColor())))
                       .addPlaceMarkListener(1,"PlaceMark clicked");

                webView.loadDataWithBaseURL(null, builder.getMap(), mime, encoding, null);
            }
        });

    }

    public class JavaScriptHandler {

        public JavaScriptHandler() {

        }

        @JavascriptInterface
        public void returnResult(final String result) {
            Log.d(TAG, result);

            if (!result.equals("init")) {
                Snackbar.make(webView, result, Snackbar.LENGTH_LONG)
                        .setAction("Call", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                callFunction(webView);
                            }
                        })
                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                        .show();
            }

        }
    }

    public void callFunction(View view) {
        webView.loadUrl("javascript:init()");
    }

    private String getRandomCoordinate(){
        return String.valueOf(new Random().nextInt(100)+1);
    }

    public int getRandomColor() {
        Random random= new Random();
        return Color.argb(255, random.nextInt(256), random.nextInt(256),
                random.nextInt(256));
    }

}

