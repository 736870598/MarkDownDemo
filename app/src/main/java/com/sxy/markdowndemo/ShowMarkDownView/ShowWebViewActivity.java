package com.sxy.markdowndemo.ShowMarkDownView;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.sxy.markdowndemo.R;

/**
 *
 * Created by sunxiaoyu on 2017/8/9.
 */
public class ShowWebViewActivity extends AppCompatActivity {

    private ProgressWebView mWebView;
    private String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_web_view_layout);

        initToolBar();
        initView();
        initData();
    }

    private void initToolBar(){
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initView(){
        mWebView = (ProgressWebView) findViewById(R.id.see_webView);
    }


    public void initData() {

        url = getIntent().getStringExtra("url");

        if (url != null && !url.isEmpty()){
            mWebView.loadUrl(url);
        }else{
            //TODO TOAST
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_refresh: //刷新
                mWebView.reload();
                break;
            case R.id.action_copy_url: //复制
                //TODO COPY
                break;
            case R.id.action_clear_cache:   //清缓存
                mWebView.clearCache(true);
                mWebView.clearHistory();
                Snackbar.make(mWebView, "清理缓存成功", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.action_clear_cookie:  //清理Cookie
                CookieSyncManager.createInstance(this);
                CookieSyncManager.getInstance().startSync();
                CookieManager.getInstance().removeSessionCookie();
                Snackbar.make(mWebView, "清理Cookie成功", Snackbar.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        mWebView.onActivityResult(requestCode, resultCode, intent);
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
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();//回退
        } else {
            super.onBackPressed();
        }
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

}
