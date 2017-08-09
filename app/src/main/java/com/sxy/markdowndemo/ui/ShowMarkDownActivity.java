package com.sxy.markdowndemo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.sxy.markdowndemo.R;
import com.sxy.markdowndemo.ShowMarkDownView.MarkdownPreviewView;

/**
 *
 * Created by sunxiaoyu on 2017/8/9.
 */
public class ShowMarkDownActivity extends AppCompatActivity  implements MarkdownPreviewView.OnLoadingFinishListener {

    private MarkdownPreviewView mdView;
    private String mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_md_layout);

        String title = getIntent().getStringExtra("title");
        if (title == null || title.isEmpty()){
            title = "预览";
        }

        mContent = getIntent().getStringExtra("content");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mdView = (MarkdownPreviewView) findViewById(R.id.md_view);
        mdView.setOnLoadingFinishListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoadingFinish() {
        mdView.parseMarkdown(mContent, true);
    }
}
