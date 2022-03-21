package com.sallet.cold.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * 外部引用
 * activity控制器
 * external references
 * activity controller
 */
public class ActivityCollector {
    //Activity静态集合 Activity static collection
    public static List<Activity> activities = new ArrayList<>();
    //把activity加入集合的静态方法 Static method to add activity to collection
    public static void addActivity(Activity activity){
        activities.add(activity);
    }
    //把activity移除集合的静态方法 Static method to remove activity from collection
    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }
    //结束所有的activity end all activities
    public static void finishAll(){
        for(Activity activity : activities){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
