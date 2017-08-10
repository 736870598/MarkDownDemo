package com.sxy.markdowndemo.ShowMarkDownView;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextSwitcher;

/**
 * 封装了带进度条显示的webview，而且实现了webview的基本配置
 * Created by Administrator on 2017/8/9/009.
 */
public class ProgressWebView extends WebView {

    private ProgressBar progressbar;

    private final static int FILECHOOSER_RESULTCODE = 101;
    private final static int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 102;
    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> mUploadMessageForAndroid5;

    public ProgressWebView(Context context) {
        this(context, null);
    }

    public ProgressWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initWebView(context);
        init();

    }

    private void initWebView(Context context){
        progressbar = new ProgressBar(context, null,
                android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 10, 0, 0));
        addView(progressbar);
    }


    private void init(){
        enableJavascript();
        enableCaching();
        enableClient();
        enableAdjust();
        zoomedOut();
    }

    /**
     * 打开javaScript支持
     * Enable javascript.
     */
    private void enableJavascript() {
        this.getSettings().setJavaScriptEnabled(true);// 能够执行Javascript脚本
        this.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
    }

    /**
     * Enable caching.打开缓存
     */
    private void enableCaching() {
        //TODO 缓存路径
        this.getSettings().setAppCachePath(getContext().getExternalCacheDir() + "/cache");
        this.getSettings().setAppCacheEnabled(true);
        this.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        this.getSettings().setAllowFileAccess(true);// 访问文件数据
    }

    private void zoomedOut() {
        this.getSettings().setSupportZoom(false);// 不支持缩放
        this.getSettings().setUseWideViewPort(true);// 将图片调整到适合webview的大小
        this.getSettings().setBuiltInZoomControls(false);// 不显示缩放工具
    }

    private void enableClient() {
        this.setWebChromeClient(new ChromeClient());
        this.setWebViewClient(new WebClient());
    }

    private void enableAdjust() {
        this.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);// 把所有内容放在webview等宽的一列中
        this.getSettings().setLoadWithOverviewMode(true);// 加载一个页面是否与概述模式
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != Activity.RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;

        } else if (requestCode == FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {
            if (null == mUploadMessageForAndroid5)
                return;
            Uri result = (intent == null || resultCode != Activity.RESULT_OK) ? null : intent.getData();
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
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Snackbar.make(view, "加载错误errorCode:" + errorCode, Snackbar.LENGTH_LONG).show();
        }
    }

    private class ChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            progressbar.setProgress(newProgress);
            if (newProgress == 100) {
                progressbar.setVisibility(View.GONE);
            } else {
                progressbar.setVisibility(View.VISIBLE);
            }
        }


        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
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
        ((Activity)getContext()).startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
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

        ((Activity)getContext()).startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
    }

}
