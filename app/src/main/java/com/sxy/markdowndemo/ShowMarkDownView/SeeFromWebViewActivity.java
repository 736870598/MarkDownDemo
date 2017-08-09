package com.sxy.markdowndemo.ShowMarkDownView;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Network;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.sxy.markdowndemo.R;

/**
 * Created by sunxiaoyu on 2017/8/9.
 */
public class SeeFromWebViewActivity extends AppCompatActivity {

    private WebView mWebView;
    private TextSwitcher titleText;

    private String url;
    private String content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_web_view_layout);

        initToolBar();
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mWebView != null) mWebView.onResume();
    }

    @Override
    protected void onPause() {
        if (mWebView != null) mWebView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.pauseTimers();
            mWebView.stopLoading();
            mWebView.setFocusable(true);
            mWebView.removeAllViews();
            mWebView.clearHistory();
            mWebView.destroy();
        }
        super.onDestroy();
    }

    private void initToolBar(){
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initView(){
        mWebView = (WebView) findViewById(R.id.see_webView);
        titleText = (TextSwitcher) findViewById(R.id.tv_title);
        initTextSwitcher();
        initWebView();
    }

    private void initTextSwitcher(){
        titleText.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(SeeFromWebViewActivity.this);
                textView.setTextAppearance(SeeFromWebViewActivity.this, R.style.WebTitle);
                textView.setSingleLine(true);
                textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                textView.setSelected(true);
                return textView;
            }
        });
    }

    private void initWebView(){
        enableJavascript();
        enableCaching();
        enableClient();
        enableAdjust();
        zoomedOut();
    }

    public void initData() {

        url = getIntent().getStringExtra("url");
        content = getIntent().getStringExtra("content");

        if (url == null || url.isEmpty()){
            if (content == null || content.isEmpty()){
                mWebView.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null);
            }
        }else{
            mWebView.loadUrl(url);
        }
    }

    /**
     * 打开javaScript支持
     * Enable javascript.
     */
    private void enableJavascript() {
        mWebView.getSettings().setJavaScriptEnabled(true);// 能够执行Javascript脚本
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
    }

    /**
     * Enable caching.打开缓存
     */
    private void enableCaching() {
        mWebView.getSettings().setAppCachePath(getFilesDir() + getPackageName() + "/cache");
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebView.getSettings().setAllowFileAccess(true);// 访问文件数据
    }

    private void zoomedOut() {
        mWebView.getSettings().setSupportZoom(false);// 不支持缩放
        mWebView.getSettings().setUseWideViewPort(true);// 将图片调整到适合webview的大小
        mWebView.getSettings().setBuiltInZoomControls(false);// 不显示缩放工具
    }

    private void enableClient() {
        mWebView.setWebChromeClient(new ChromeClient());
        mWebView.setWebViewClient(new WebClient());
    }

    private void enableAdjust() {
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);// 把所有内容放在webview等宽的一列中
        mWebView.getSettings().setLoadWithOverviewMode(true);// 加载一个页面是否与概述模式
    }


    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();//回退
        } else {
            super.onBackPressed();
        }
    }


    private class ChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
//            if (mProgressBar == null) return;
//            mProgressBar.setProgress(newProgress);
//            if (newProgress == 100) {
//                mProgressBar.setVisibility(View.GONE);
//            } else {
//                mProgressBar.setVisibility(View.VISIBLE);
//            }
        }


        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            titleText.setText(title);
        }

        //扩展支持alert事件
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("注意").setMessage(message).setPositiveButton("确定", null);
            builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();
            result.confirm();
            return true;
        }

        //扩展浏览器上传文件
        //3.0++版本
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            openFileChooserImpl(uploadMsg, acceptType);
        }

        //3.0--版本
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooserImpl(uploadMsg, "");
        }

        // Android 4.1++
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            openFileChooserImpl(uploadMsg, acceptType);
        }

        // For Android > 5.0
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadMsg, FileChooserParams fileChooserParams) {
            String acceptType = null;
            if (Build.VERSION.SDK_INT >= 21) {
                String[] acceptTypes = fileChooserParams.getAcceptTypes();
                if (acceptTypes != null && acceptTypes.length > 0) {
                    acceptType = acceptTypes[0];
                }
            }
            openFileChooserImplForAndroid5(uploadMsg, acceptType);
            return true;
        }
    }

    private final static int FILECHOOSER_RESULTCODE = 101;
    private final static int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 102;
    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> mUploadMessageForAndroid5;

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;

        } else if (requestCode == FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {
            if (null == mUploadMessageForAndroid5)
                return;
            Uri result = (intent == null || resultCode != RESULT_OK) ? null : intent.getData();
            if (result != null) {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
            } else {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
            }
            mUploadMessageForAndroid5 = null;
        }
    }


    private class WebClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url != null) view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            Snackbar.make(view, "加载错误", Snackbar.LENGTH_LONG).show();
        }

        public void onPageFinished(WebView view, String url) {
//            Snackbar.make(view, "加载完成", Snackbar.LENGTH_SHORT).show();
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Snackbar.make(view, "加载错误errorCode:" + errorCode, Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * 5.--文件选择
     * Open file chooser.
     *
     * @param uploadMsg  the upload msg
     * @param acceptType the accept type
     */
    private void openFileChooserImpl(ValueCallback<Uri> uploadMsg, String acceptType) {
        if (TextUtils.isEmpty(acceptType.trim()))
            acceptType = "image/*";
        mUploadMessage = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType(acceptType);
        startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
    }

    /**
     * 5.++文件选择
     * Open file chooser impl for android 5.
     *
     * @param uploadMsg  the upload msg
     * @param acceptType the accept type
     */
    private void openFileChooserImplForAndroid5(ValueCallback<Uri[]> uploadMsg, String acceptType) {
        if (TextUtils.isEmpty(acceptType.trim()))
            acceptType = "image/*";
        mUploadMessageForAndroid5 = uploadMsg;
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType(acceptType);

        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");

        startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
    }
}
