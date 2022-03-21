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
     * 压缩
     */
    public static String zipString(String unzipString) {
        /*
         *     0 ~ 9 压缩等级 低到高
         *     public static final int BEST_COMPRESSION = 9;            最佳压缩的压缩级别。
         *     public static final int BEST_SPEED = 1;                  压缩级别最快的压缩。
         *     public static final int DEFAULT_COMPRESSION = -1;        默认压缩级别。
         *     public static final int DEFAULT_STRATEGY = 0;            默认压缩策略。
         *     public static final int DEFLATED = 8;                    压缩算法的压缩方法(目前唯一支持的压缩方法)。
         *     public static final int FILTERED = 1;                    压缩策略最适用于大部分数值较小且数据分布随机分布的数据。
         *     public static final int FULL_FLUSH = 3;                  压缩刷新模式，用于清除所有待处理的输出并重置拆卸器。
         *     public static final int HUFFMAN_ONLY = 2;                仅用于霍夫曼编码的压缩策略。
         *     public static final int NO_COMPRESSION = 0;              不压缩的压缩级别。
         *     public static final int NO_FLUSH = 0;                    用于实现最佳压缩结果的压缩刷新模式。
         *     public static final int SYNC_FLUSH = 2;                  用于清除所有未决输出的压缩刷新模式; 可能会降低某些压缩算法的压缩率。
         *
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
        //使用指定的压缩级别创建一个新的压缩器。
        //Create a new compressor with the specified compression level。
        Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);
        //设置压缩输入数据。
        //Set compressed input data。
        deflater.setInput(unzipString.getBytes());
        //当被调用时，表示压缩应该以输入缓冲区的当前内容结束。
        //When called, indicates that the compression should end with the current contents of the input buffer。
        deflater.finish();

        final byte[] bytes = new byte[256];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(256);

        while (!deflater.finished()) {
            //压缩输入数据并用压缩数据填充指定的缓冲区。
            //Compresses the input data and fills the specified buffer with the compressed data。
            int length = deflater.deflate(bytes);
            outputStream.write(bytes, 0, length);
        }
        //关闭压缩器并丢弃任何未处理的输入。
        //Turn off the compressor and discard any unprocessed input。
        deflater.end();
        return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP);
    }

    /**
     * 解压缩
     * unzip
     */
    @Nullable
    public static String unzipString(String zipString) {
        byte[] decode = Base64.decode(zipString, Base64.NO_WRAP);
        //创建一个新的解压缩器
        //Create a new decompressor
        Inflater inflater = new Inflater();
        //设置解压缩的输入数据。
        //Set decompressed input data。
        inflater.setInput(decode);
        final byte[] bytes = new byte[256];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(256);
        try {
            //finished() 如果已到达压缩数据流的末尾，则返回true。
            //finished() Returns true if the end of the compressed data stream has been reached.
            while (!inflater.finished()) {
                //将字节解压缩到指定的缓冲区中。
                //Decompress bytes into the specified buffer。
                int length = inflater.inflate(bytes);
                outputStream.write(bytes, 0, length);
            }
        } catch (DataFormatException e) {
            e.printStackTrace();
            return null;
        } finally {
            //关闭解压缩器并丢弃任何未处理的输入。
            //closes the decompressor and discards any unprocessed input。
            inflater.end();
        }

        return outputStream.toString();
    }
}