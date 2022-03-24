package com.sallet.cold.utils;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;

public class BitmapUtils {


    /**

     * @return BitMap
     *
     * Generate simple QR code
     *
     * @param content String content
     * @param width QR code width
     * @param height QR code height
     * @param character_set encoding method (usually using UTF-8)
     * @param error_correction_level error tolerance L: 7% M: 15% Q: 25% H: 35%
     * @param margin margin (the blank area of QR code and border)
     * @return BitMap
     */

    public static Bitmap createQRCodeBitmap(String content, int width, int height,
                                            String character_set, String error_correction_level,
                                            String margin) {
        // String content is empty
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        // width and height>=0
        if (width < 0 || height < 0) {
            return null;
        }
        try {
            /* 1.Set QR code related configuration */
            Hashtable<EncodeHintType, String> hints = new Hashtable<>();
            // Character transcoding format settings
            if (!TextUtils.isEmpty(character_set)) {
                hints.put(EncodeHintType.CHARACTER_SET, character_set);
            }
            // Fault tolerance setting
            if (!TextUtils.isEmpty(error_correction_level)) {
                hints.put(EncodeHintType.ERROR_CORRECTION, error_correction_level);
            }
            // white space settings
            if (!TextUtils.isEmpty(margin)) {
                hints.put(EncodeHintType.MARGIN, margin);
            }
            /** 2.Pass the configuration parameters into the encode method of QRCodeWriter to generate a BitMatrix (bit matrix) object */
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            /** 3.Create a pixel array and assign color values to the array elements according to the BitMatrix (bit matrix) object */
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000;
                    } else {
                        pixels[y * width + x] = 0xffffffff;
                    }
                }
            }
            /** 4.Create a Bitmap object, set the color value of each pixel of the Bitmap according to the pixel array, and return the Bitmap object*/
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
}
