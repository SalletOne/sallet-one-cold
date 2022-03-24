package com.sallet.cold.utils;

import android.util.Base64;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 */
public class DeflaterUtils {
    /**
     */
    public static String zipString(String unzipString) {
        /*

         *      0 ~ 9 compression level low to high
         *      public static final int BEST_COMPRESSION = 9; Compression level for best compression.
         *      public static final int BEST_SPEED = 1; The fastest compression for the compression level.
         *      public static final int DEFAULT_COMPRESSION = -1; Default compression level.
         *      public static final int DEFAULT_STRATEGY = 0; Default compression strategy.
         *      public static final int DEFLATED = 8; The compression method of the compression algorithm (the only supported compression method at present).
         *      public static final int FILTERED = 1; The compression strategy is most suitable for data with mostly small values ​​and random distribution of data.
         *      public static final int FULL_FLUSH = 3; Compression flush mode to clear all pending output and reset the disassembler.
         *      public static final int HUFFMAN_ONLY = 2; Compression strategy for Huffman encoding only.
         *      public static final int NO_COMPRESSION = 0; The compression level for no compression.
         *      public static final int NO_FLUSH = 0; Compression flush mode for best compression results.
         *      public static final int SYNC_FLUSH = 2; Compression flush mode for clearing all pending outputs; may reduce compression ratio for some compression algorithms.
         */
        //Create a new compressor with the specified compression level。
        Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);
        //Set compressed input data。
        deflater.setInput(unzipString.getBytes());
        //When called, indicates that the compression should end with the current contents of the input buffer。
        deflater.finish();

        final byte[] bytes = new byte[256];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(256);

        while (!deflater.finished()) {
            //Compresses the input data and fills the specified buffer with the compressed data。
            int length = deflater.deflate(bytes);
            outputStream.write(bytes, 0, length);
        }
        //Turn off the compressor and discard any unprocessed input。
        deflater.end();
        return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP);
    }

    /**
     * unzip
     */
    @Nullable
    public static String unzipString(String zipString) {
        byte[] decode = Base64.decode(zipString, Base64.NO_WRAP);
        //Create a new decompressor
        Inflater inflater = new Inflater();
        //Set decompressed input data。
        inflater.setInput(decode);
        final byte[] bytes = new byte[256];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(256);
        try {
            //finished() Returns true if the end of the compressed data stream has been reached.
            while (!inflater.finished()) {
                //Decompress bytes into the specified buffer。
                int length = inflater.inflate(bytes);
                outputStream.write(bytes, 0, length);
            }
        } catch (DataFormatException e) {
            e.printStackTrace();
            return null;
        } finally {
            //closes the decompressor and discards any unprocessed input。
            inflater.end();
        }

        return outputStream.toString();
    }
}