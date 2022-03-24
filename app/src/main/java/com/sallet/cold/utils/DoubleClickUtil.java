package com.sallet.cold.utils;

import android.view.View;

/**
 * Prevent continuous clicks of controls
 */
public class DoubleClickUtil {

    private static long mLastClick;

    /**
     * Clicks within the interval are invalid clicks
     * @param milliseconds  Intervals
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
