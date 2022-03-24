package com.sallet.cold.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * external references
 * activity controller
 */
public class ActivityCollector {
    // Activity static collection
    public static List<Activity> activities = new ArrayList<>();
    // Static method to add activity to collection
    public static void addActivity(Activity activity){
        activities.add(activity);
    }
    // Static method to remove activity from collection
    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }
    // end all activities
    public static void finishAll(){
        for(Activity activity : activities){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
