package com.sallet.cold.utils;


import android.graphics.Paint;
import android.text.TextPaint;

/**
 * Text Control Tool Class
 */
public class TextDrawUtils {

	/**
	 * Given the center of the text, obtain the base line of the text
	 */
	public static float getTextBaseLineByCenter(float center, TextPaint paint, int size) {
		paint.setTextSize(size);
		Paint.FontMetrics fontMetrics = paint.getFontMetrics();
		float height = fontMetrics.bottom - fontMetrics.top;
		return center + height / 2 - fontMetrics.bottom;
	}

	public static float getTextBaseLineByCenter(float center, TextPaint paint) {
		Paint.FontMetrics fontMetrics = paint.getFontMetrics();
		float height = fontMetrics.bottom - fontMetrics.top;
		return center + height / 2 - fontMetrics.bottom;
	}
}
