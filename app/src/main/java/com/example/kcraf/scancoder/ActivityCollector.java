package com.example.kcraf.scancoder;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kcraf on 2018/01/25.
 * 此类是用来控制所有的活动的
 * 包含四个方法
 * 添加活动 addActivity(Activity activity)
 * 删除活动 removeActivity(Activity activity)
 * 清理所有活动 finishAll（）
 * 清理除指定名称的类之外的所有活动 clearActivityExceptThis(String ActivityClassName)
 */

public class ActivityCollector {//用于控制活动的一个类
    public static List<Activity> activities = new ArrayList<>();//创建一个维护所有Activity的列表

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }//添加活动的方法

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }//移除活动的方法

    public static void finishAll() {//删除所有活动的方法
        for (Activity activity : activities) {//遍历所有活动
            if (!activity.isFinishing()) {
                activity.finish();//结束每一个活动
            }
        }
    }

    public static void clearActivityExceptThis(String ActivityClassName) {
        for (Activity activity : activities) {//遍历所有活动
            if (!activity.isFinishing() && !(activity.getClass().getSimpleName().equals(ActivityClassName))) {
                activity.finish();//结束每一个活动
            }
        }
    }
}
