package com.sxy.markdowndemo.ShowMarkDownView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.ViewUtils;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 *
 *  Created by sunxiaoyu on 2017/8/9.
 */
public class MarkdownPreviewView extends NestedScrollView {
    public WebView mWebView;
    private Context mContext;
    private OnLoadingFinishListener mLoadingFinishListener;
    private ContentListener mContentListener;

    public MarkdownPreviewView(Context context) {
        super(context);
        init(context);
    }

    public MarkdownPreviewView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public MarkdownPreviewView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled"})
    private void init(Context context) {
        if (!isInEditMode()) {
            this.mContext = context;
            if (Build.VERSION.SDK_INT >= 21) {
                WebView.enableSlowWholeDocumentDraw();
            }
            this.mWebView = new WebView(this.mContext);
            this.mWebView.getSettings().setJavaScriptEnabled(true);
            this.mWebView.setVerticalScrollBarEnabled(false);
            this.mWebView.setHorizontalScrollBarEnabled(false);
            this.mWebView.addJavascriptInterface(new JavaScriptInterface(this), "handler");
            this.mWebView.setWebViewClient(new MdWebViewClient(this));
            this.mWebView.loadUrl("file:///android_asset/markdown.html");
            addView(this.mWebView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }
    }

    public final void parseMarkdown(String str, boolean z) {
//        this.mWebView.loadUrl("http://xtc.gisoft.top/Files/Pictures/57e1cf89-ef8d-45f8-8190-1c06b971945f.md");
        this.mWebView.loadUrl("javascript:parseMarkdown(\"" + str.replace("\n", "\\n").replace("\"", "\\\"").replace("'", "\\'") + "\", " + z + ")");
    }

    public void setContentListener(ContentListener contentListener) {
        this.mContentListener = contentListener;
    }

    public void setOnLoadingFinishListener(OnLoadingFinishListener loadingFinishListener) {
        this.mLoadingFinishListener = loadingFinishListener;
    }

    public interface ContentListener {
    }

    public interface OnLoadingFinishListener {
        void onLoadingFinish();
    }

    final class JavaScriptInterface {
        final MarkdownPreviewView a;

        private JavaScriptInterface(MarkdownPreviewView markdownPreviewView) {
            this.a = markdownPreviewView;
        }

        @JavascriptInterface
        public void none() {

        }
    }


    final class MdWebViewClient extends WebViewClient {
        final MarkdownPreviewView mMarkdownPreviewView;

        private MdWebViewClient(MarkdownPreviewView markdownPreviewView) {
            this.mMarkdownPreviewView = markdownPreviewView;
        }

        public final void onPageFinished(WebView webView, String str) {
            if (this.mMarkdownPreviewView.mLoadingFinishListener != null) {
                this.mMarkdownPreviewView.mLoadingFinishListener.onLoadingFinish();
            }
        }

        public final boolean shouldOverrideUrlLoading(WebView webView, String url) {
            if (url != null && !url.isEmpty()){
                Intent webIntent = new Intent(getContext(), SeeFromWebViewActivity.class);
                webIntent.putExtra("url", url);
                getContext().startActivity(webIntent);
            }
            return true;
        }
    }

    /**
     * 截屏
     *
     * @return
     */
    public Bitmap getScreen() {
        Bitmap bmp = Bitmap.createBitmap(mWebView.getWidth(), mWebView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        mWebView.draw(canvas);
        return bmp;
    }

}

