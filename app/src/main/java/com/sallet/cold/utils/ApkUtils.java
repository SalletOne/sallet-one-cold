package com.sallet.cold.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.sallet.cold.App;

import java.io.File;

/**
 * Install APK
 */
public class ApkUtils {

    public static Intent getApkFileIntent(Context context, File file)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context,"com.sallet.cold.fileprovider",file);
            intent.setDataAndType(contentUri,"application/vnd.android.package-archive");
        }else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(
                    Uri.fromFile(file),
                    "application/vnd.android.package-archive");
        }

        return intent;
    }
}
