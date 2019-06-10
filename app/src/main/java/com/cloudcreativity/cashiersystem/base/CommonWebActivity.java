package com.cloudcreativity.cashiersystem.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.utils.LogUtils;

/**
 * 这是最基本的加载web网页界面
 */
public class CommonWebActivity extends AppCompatActivity implements View.OnClickListener {

    private WebView webView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseApp.app.addActivity(this);
        setContentView(R.layout.activity_common_web);
        findViewById(R.id.ib_close).setOnClickListener(this);
        findViewById(R.id.ib_previous).setOnClickListener(this);
        webView = findViewById(R.id.wv_content);
        TextView tv_title = findViewById(R.id.tv_title);
        String title = getIntent().getStringExtra("title");
        String url = getIntent().getStringExtra("url");
        String data = getIntent().getStringExtra("data");
        tv_title.setText(title);
        initWebView();
        if(!TextUtils.isEmpty(url)){
            webView.loadUrl(url);
        }else{
            webView.loadData("<p style='font-size:40px;line-height:45px;margin:10px'>"+data+"</p>","text/html;charset=utf-8","UTF-8");
        }

    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void initWebView() {

        WebSettings webSettings = webView.getSettings();

        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);

        //支持插件
        //webSettings.setPluginsEnabled(true);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 重加载

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webSettings.setDomStorageEnabled(true);

        //需要在这里注入js对象
        webView.addJavascriptInterface(new OnQrCodeClick(),"wkSaleApi");



        webView.setWebViewClient(new WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                LogUtils.e("xuxiwu",request.getUrl().toString());
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                LogUtils.e("xuxiwu",url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @SuppressLint("AddJavascriptInterface")
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                switch (error.getErrorCode()){
                    case 404:
                        break;
                    case 500:
                        break;
                    case 400:
                        break;
                }
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {

            }

            @Override
            public void onReceivedTitle(WebView view, String title) {

            }
        });
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode== KeyEvent.KEYCODE_BACK){
                    if(webView.canGoBack()) {
                        webView.goBack();
                        return true;
                    }
                }
                return false;
            }
        });


    }

    public static void startActivity(Context context , String title, String url) {
        Intent intent = new Intent(context,CommonWebActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("url",url);
        context.startActivity(intent);
    }

    public static void startActivityByData(Context context , String title, String data) {
        Intent intent = new Intent(context,CommonWebActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("data",data);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, String url) {
        Intent intent = new Intent(context,CommonWebActivity.class);
        intent.putExtra("title","");
        intent.putExtra("url",url);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_close:
                finish();
                break;
            case R.id.ib_previous:
                if (webView.canGoBack())
                    webView.goBack();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView!= null) {
            //这句话是停止js加载或者其他function timeout操作，所以注释，存在bug
            //webView.pauseTimers();
            webView.clearFormData();
            webView.clearCache(true);
            webView.clearHistory();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView= null;
        }
    }

    //这是添加js interface的接口
    public class OnQrCodeClick{
        @JavascriptInterface
        public void onClick() {
            startActivity(getPackageManager().getLaunchIntentForPackage("com.tencent.mm"));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(webView!=null)
            webView.pauseTimers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(webView!=null)
            webView.resumeTimers();
    }
}
