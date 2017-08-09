package com.sxy.markdowndemo.EditMarkDownView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.sxy.markdowndemo.R;

/**
 *
 * Created by sunxiaoyu on 2017/8/9.
 */
public class MarkDownEditView extends LinearLayout implements View.OnClickListener {

    private TabIconView tabIconView;
    private EditText title;
    private EditText content;
    private PerformEditable performEditable;
    private PerformEdit performEdit;

    public MarkDownEditView(Context context) {
        this(context, null);
    }

    public MarkDownEditView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarkDownEditView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.mark_down_edit_layout, this);
        tabIconView = (TabIconView) view.findViewById(R.id.mark_down_edit_optview);
        title = (EditText) view.findViewById(R.id.mark_down_edit_title);
        content = (EditText) view.findViewById(R.id.mark_down_edit_content);

        initContentView();
        initTabIconView();
    }

    private void initContentView() {
        performEdit = new PerformEdit(content);
        performEditable = new PerformEditable(content);
    }

    private void initTabIconView() {
        tabIconView.addTab(R.drawable.ic_action_undo, R.id.id_action_undo, this);
        tabIconView.addTab(R.drawable.ic_action_redo, R.id.id_action_redo, this);
        tabIconView.addTab(R.drawable.ic_shortcut_format_header_1, R.id.id_shortcut_format_header_1, this);
        tabIconView.addTab(R.drawable.ic_shortcut_format_header_2, R.id.id_shortcut_format_header_2, this);
        tabIconView.addTab(R.drawable.ic_shortcut_format_header_3, R.id.id_shortcut_format_header_3, this);
        tabIconView.addTab(R.drawable.ic_shortcut_insert_link, R.id.id_shortcut_insert_link, this);
        tabIconView.addTab(R.drawable.ic_shortcut_insert_photo, R.id.id_shortcut_insert_photo, this);
        tabIconView.addTab(R.drawable.ic_shortcut_console, R.id.id_shortcut_console, this);
        tabIconView.addTab(R.drawable.ic_shortcut_format_bold, R.id.id_shortcut_format_bold, this);
        tabIconView.addTab(R.drawable.ic_shortcut_format_italic, R.id.id_shortcut_format_italic, this);
        tabIconView.addTab(R.drawable.ic_shortcut_format_header_4, R.id.id_shortcut_format_header_4, this);
        tabIconView.addTab(R.drawable.ic_shortcut_format_header_5, R.id.id_shortcut_format_header_5, this);
        tabIconView.addTab(R.drawable.ic_shortcut_format_header_6, R.id.id_shortcut_format_header_6, this);
        tabIconView.addTab(R.drawable.ic_shortcut_format_quote, R.id.id_shortcut_format_quote, this);
        tabIconView.addTab(R.drawable.ic_shortcut_xml, R.id.id_shortcut_xml, this);
        tabIconView.addTab(R.drawable.ic_shortcut_minus, R.id.id_shortcut_minus, this);
        tabIconView.addTab(R.drawable.ic_shortcut_format_strikethrough, R.id.id_shortcut_format_strikethrough, this);
        tabIconView.addTab(R.drawable.ic_shortcut_grid, R.id.id_shortcut_grid, this);
        tabIconView.addTab(R.drawable.ic_shortcut_format_list_bulleted, R.id.id_shortcut_list_bulleted, this);
        tabIconView.addTab(R.drawable.ic_shortcut_format_list_numbers, R.id.id_shortcut_format_numbers, this);
    }

    public void setTitle(String titleStr){
        if (title != null) title.setText(titleStr);
    }

    public String getTitle(){
        return (title == null) ? null : title.getText().toString();
    }

    public void setContent(String contentStr){
        if (content != null) content.setText(contentStr);
    }

    public String getContent(){
        return (content == null) ? null : content.getText().toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_action_undo:
                performEdit.undo();
                break;
            case R.id.id_action_redo:
                performEdit.redo();
                break;
            case R.id.id_shortcut_insert_link: //链接
                insertLink();
//                performEditable.perform(R.id.id_shortcut_insert_link, titleStr, linkStr);
                break;
            case R.id.id_shortcut_insert_photo: //图片

//                performEditable.perform(R.id.id_shortcut_insert_photo, Uri.fromFile(new File(path)));
                break;
            case R.id.id_shortcut_grid:  //表格

//               performEditable.perform(R.id.id_shortcut_grid, Integer.parseInt(rowNumberStr), Integer.parseInt(columnNumberStr));
                break;
            default:
                performEditable.onClick(v);
                break;
        }
    }


    /**
     * 插入链接
     */
    private void insertLink() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.view_common_input_link_view, null);

        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("插入链接")
                .setView(rootView)
                .show();

        final TextInputLayout titleHint = (TextInputLayout) rootView.findViewById(R.id.inputNameHint);
        final TextInputLayout linkHint = (TextInputLayout) rootView.findViewById(R.id.inputHint);
        final EditText title = (EditText) rootView.findViewById(R.id.name);
        final EditText link = (EditText) rootView.findViewById(R.id.text);


        rootView.findViewById(R.id.sure).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleStr = title.getText().toString().trim();
                String linkStr = link.getText().toString().trim();

//            if (Check.isEmpty(titleStr)) {
//                titleHint.setError("不能为空");
//                return;
//            }
//            if (Check.isEmpty(linkStr)) {
//                linkHint.setError("不能为空");
//                return;
//            }

                if (titleHint.isErrorEnabled())
                    titleHint.setErrorEnabled(false);
                if (linkHint.isErrorEnabled())
                    linkHint.setErrorEnabled(false);

                performEditable.perform(R.id.id_shortcut_insert_link, titleStr, linkStr);

                dialog.dismiss();
            }
        });

        rootView.findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}