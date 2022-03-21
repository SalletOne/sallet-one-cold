package com.sallet.cold.utils;

import android.view.View;

/**
 * 防止控件连续点击
 * Prevent continuous clicks of controls
 */
public class DoubleClickUtil {

    private static long mLastClick;

    /**
     * 间隔时间内的点击为无效点击
     * Clicks within the interval are invalid clicks
     * @param milliseconds 间隔时间 Intervals
     * @return
     */
    public static boolean isDoubleClick(long milliseconds){
        if (System.currentTimeMillis() - mLastClick <= milliseconds){
            return true;
        }
        mLastClick = System.currentTimeMillis();
        return false;
    }
    public static void shakeClick(final View v, long milliseconds) {
        v.setClickable(false);
        v.postDelayed(new Runnable(){
            @Override
            public void run() {
                v.setClickable(true);
            }
        }, milliseconds);
    }
}
