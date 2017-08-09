package com.sxy.markdowndemo.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.sxy.markdowndemo.R;
import com.sxy.markdowndemo.uils.ViewUtils;

public class MainActivity extends AppCompatActivity{

    private String mContent = "## MarkdownEditors\n" +
            "\n" +
            "[百度](http://www.baidu.com)\n" +
            "\n" +
            "基于Android的Markdown编辑器\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("md");
        setSupportActionBar(toolbar);
    }

    public void showMd(View view){


        Intent mdIntent = new Intent(this, ShowMarkDownActivity.class);
        mdIntent.putExtra("content", mContent);

        ViewUtils.startActivity(mdIntent, this, view);
    }

    public void editMd(View view){
        Intent edIntent = new Intent(this, EditMarkDownActivity.class);
        ViewUtils.startActivity(edIntent, this, view);
    }



}
