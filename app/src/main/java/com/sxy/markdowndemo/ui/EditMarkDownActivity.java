package com.sxy.markdowndemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.sxy.markdowndemo.EditMarkDownView.MarkDownEditView;
import com.sxy.markdowndemo.R;
import com.sxy.markdowndemo.uils.ViewUtils;

/**
 *
 * Created by sunxiaoyu on 2017/8/9.
 */
public class EditMarkDownActivity extends AppCompatActivity{

    private MarkDownEditView mdEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_md_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("编辑");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mdEdit = (MarkDownEditView) findViewById(R.id.md_edit_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }else if(item.getItemId() == R.id.edit_add_save){
            String title = mdEdit.getTitle();
            if (title == null || title.isEmpty()){
                Toast.makeText(EditMarkDownActivity.this, "请输入标题", Toast.LENGTH_SHORT).show();
                return true;
            }

            String content = mdEdit.getContent();
            if (content == null || content.isEmpty()){
                Toast.makeText(EditMarkDownActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                return true;
            }

            Intent mdIntent = new Intent(this, ShowMarkDownActivity.class);
            mdIntent.putExtra("title", title);
            mdIntent.putExtra("content", content);

            ViewUtils.startActivity(mdIntent, this, mdEdit);
        }

        return super.onOptionsItemSelected(item);
    }

}
