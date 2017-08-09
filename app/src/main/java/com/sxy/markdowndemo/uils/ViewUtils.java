package com.sxy.markdowndemo.uils;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

/**
 * Created by sunxiaoyu on 2017/8/9.
 */

public class ViewUtils {

    public static void startActivity(Intent intent, Activity activity, View sharedElement) {
        ActivityOptionsCompat optionsCompat
                = ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity, sharedElement, "SHARED_ELEMENT_NAME");
        try {
            ActivityCompat.startActivity(activity, intent,
                    optionsCompat.toBundle());
            //界面共享该图片元素
        } catch (IllegalArgumentException e) {
            activity.startActivity(intent);//如果异常 直接启动
        }
    }
}
