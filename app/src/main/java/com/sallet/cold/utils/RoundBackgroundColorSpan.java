package com.sallet.cold.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;

import com.sallet.cold.App;

public class RoundBackgroundColorSpan extends ReplacementSpan {
    private int bgColor;
    private int textColor;
    public RoundBackgroundColorSpan(int bgColor, int textColor) {
        super();
        this.bgColor = bgColor;
        this.textColor = textColor;
    }
    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        return ((int)paint.measureText(text, start, end)+px2Dp(16));
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        int originalColor = paint.getColor();
        paint.setColor(this.bgColor);
        canvas.drawRoundRect(new RectF(x,
                        top+ px2Dp(3),
                        x + ((int) paint.measureText(text, start, end)+ px2Dp(16)),
                        bottom-px2Dp(1)),

                px2Dp(4),
                px2Dp(4),
                paint);
        paint.setColor(this.textColor);
        canvas.drawText(text, start, end, x+px2Dp(8), y, paint);
        paint.setColor(originalColor);
    }


    private int px2Dp(float dpValue){

        Context context = App.context;
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}