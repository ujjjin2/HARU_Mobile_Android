package com.object.haru;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Search_register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_register);

        WebView webView = findViewById(R.id.webview_register);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new BridgeInterface(),"Android");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //Android -> JS 함수 호출!
                webView.loadUrl("javascript:sample2_execDaumPostcode();");
            }
        });

        //최초 웹뷰 로드
        webView.loadUrl("http://10.0.2.2:8080/roadSearch.html");
    }

    private class BridgeInterface {
        @JavascriptInterface
        public void processDATA(String data){
            //다음(카카오) 주소 검색 API의 결과 값이 브릿지 통로를 통해 전달 받는다. (Form JS)
            Intent intent = new Intent();
            intent.putExtra("data", data);
            setResult(RESULT_OK,intent);
            finish();
        }
    }
}