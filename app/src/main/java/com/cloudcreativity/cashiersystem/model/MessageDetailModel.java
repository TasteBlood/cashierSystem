package com.cloudcreativity.cashiersystem.model;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cloudcreativity.cashiersystem.base.BaseModel;
import com.cloudcreativity.cashiersystem.databinding.FragmentMessageDetailBinding;
import com.cloudcreativity.cashiersystem.entity.MessageEntity;
import com.cloudcreativity.cashiersystem.utils.AppConfig;
import com.cloudcreativity.cashiersystem.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

public class MessageDetailModel extends BaseModel<FragmentActivity, FragmentMessageDetailBinding> {
    public MessageDetailModel(FragmentActivity context, FragmentMessageDetailBinding binding) {
        super(context, binding);
        initWebView();

    }

    public void showData(MessageEntity entity){
        String data =
                "<html lang=\"cn\">" +
                        "<head>" +
                        "<meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0\"/>" +
                        "<title>详情</title>" +
                        "<style type=\"text/css\">" +
                        "*{" +
                        "padding: 0;" +
                        "margin: 0;" +
                        "}" +
                        "body{" +
                        "padding: 5px;" +
                        "background-color: #f1f1f1;" +
                        "font-size: 25px;" +
                        "}" +
                        "</style>" +
                        "</head>" +
                        "<body>" +
                        "<div class=\"wrapper\">" +
                        "<div style=\"display: flex;flex-direction: row;\">" +
                        "<div style=\"width: 130px;color: #515151;text-align: right\">标题：</div>" +
                        "<div style=\"width: 85%;\">"+entity.getMessage().getTitle()+"</div>" +
                        "</div>" +
                        "<div style=\"display: flex;flex-direction: row;margin-top: 10px;\">" +
                        "<div style=\"width: 130px;color: #515151;text-align: right\">发布时间：</div>" +
                        "<div style=\"width: 85%;\">"+entity.getMessage().getCreateTime()+"</div>" +
                        "</div>" +
                        "<div style=\"display: flex;flex-direction: row;margin-top: 10px;\">" +
                        "<div style=\"width: 130px;color: #515151;text-align: right\">消息类型：</div>" +
                        "<div style=\"width: 85%;\">"+entity.getMessage().formatType()+"</div>" +
                        "</div>" +
                        "<div style=\"display: flex;flex-direction: row;margin-top: 10px;\">" +
                        "<div style=\"width: 130px;color: #515151;text-align: right\">内容：</div>" +
                        "<div style=\"width: 85%;\">"+entity.getMessage().getContent()+"</div>" +
                        "</div>" +
                        "<div style=\"display: flex;flex-direction: row;margin-top: 10px;\">" +
                        "<div style=\"width: 130px;color: #515151;text-align: right\">发布者：</div>" +
                        "<div style=\"width: 85%;\">"+entity.getMessage().getTitle()+"</div>" +
                        "</div>" +
                        "<div style=\"display: flex;flex-direction: row;margin-top: 10px;\">" +
                        "<div style=\"width: 130px;color: #515151;text-align: right\">状态：</div>" +
                        "<div style=\"width: 85%;\">"+entity.formatState()+"</div>" +
                        "</div>" +
                        "</div>" +
                        "</body>" +
                        "</html>"
                ;
        binding.wvContent.loadDataWithBaseURL(null,data,"text/html","utf-8",null);
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void initWebView() {

        WebSettings webSettings = binding.wvContent.getSettings();

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

        binding.wvContent.setWebViewClient(new WebViewClient() {
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
        binding.wvContent.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode== KeyEvent.KEYCODE_BACK){
                    if(binding.wvContent.canGoBack()) {
                        binding.wvContent.goBack();
                        return true;
                    }
                }
                return false;
            }
        });


    }

    public void onBack(){
        EventBus.getDefault().post(AppConfig.FRAGMENT_NAMES.FRAGMENT_MESSAGE_LIST);
    }
}
