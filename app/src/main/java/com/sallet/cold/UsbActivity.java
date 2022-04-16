package com.sallet.cold;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mjdev.libaums.UsbMassStorageDevice;
import com.github.mjdev.libaums.fs.FileSystem;
import com.github.mjdev.libaums.fs.UsbFile;
import com.github.mjdev.libaums.fs.UsbFileInputStream;
import com.sallet.cold.utils.ApkUtils;
import com.sallet.cold.utils.ConfigContent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Check the update page
 * Check whether the latest installation package already exists in the U disk by accessing the U disk to determine whether to update it.
 * If there is a new version of the installation package,
 * it will be automatically updated
 */
public class UsbActivity extends AppCompatActivity {
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    int apkInstall = 10008; // Install APK request code
    /**
     * Bind UI
     */
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.tv_submit2)
    TextView tvSubmit2;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    private USBBroadcastReceiver usbBroadcastReceiver = new USBBroadcastReceiver();// usb plug-in broadcast monitoring
    File apkFile;// installation package file
    private String localRootPath = Environment.getExternalStorageDirectory().getAbsolutePath();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usb);
        ButterKnife.bind(this);

        //New Intent Display Object
        IntentFilter usbDeviceStateFilter = new IntentFilter();
        //increase intent
        usbDeviceStateFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        //increase intent
        usbDeviceStateFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        //Register for broadcast
        registerReceiver(usbBroadcastReceiver, usbDeviceStateFilter);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        //Register for USB request permission broadcast
        registerReceiver(usbBroadcastReceiver, filter);
    }

    @OnClick(R.id.rl_back)
    public void onClick() {
        finish();
    }

    public class USBBroadcastReceiver extends BroadcastReceiver {
        private static final String TAG = "USBBroadcastReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case ACTION_USB_PERMISSION:
                    //Get USB permissions
                    UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (usbDevice != null) {
                            //Read USB list
                            readDevice(context, getUsbMass(usbDevice));
                        } else {
                            tvSubmit.setText("");
                        }
                    } else {
                        tvSubmit.setText(R.string.input_u);
                    }
                    break;
                case UsbManager.ACTION_USB_DEVICE_ATTACHED:
                    //plug in usb radio
                    UsbDevice device_add = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (device_add != null) {
                        //Read USB list
                        redUDiskDevsList(context);
                    }
                    break;
                case UsbManager.ACTION_USB_DEVICE_DETACHED:
                    //unplug the usb radio
                    tvSubmit.setText(R.string.input_u);
                    break;
            }


        }

        private UsbMassStorageDevice[] storageDevices;
        private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
        private UsbFile cFolder;
        private final static String U_DISK_FILE_NAME = "Sallet-one-cold";
        String verName;

        /**
         *Read U disk list
         */
        private void redUDiskDevsList(Context context) {
            UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
            storageDevices = UsbMassStorageDevice.getMassStorageDevices(context);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                    new Intent(ACTION_USB_PERMISSION), 0);
            for (UsbMassStorageDevice device : storageDevices) {
                if (usbManager.hasPermission(device.getUsbDevice())) {
                    //Read the USB file if you have permission
                    readDevice(context, device);
                } else {
                    //No permission to request permission
                    usbManager.requestPermission(device.getUsbDevice(), pendingIntent);
                }
            }
            if (storageDevices.length == 0) {
                tvSubmit.setText(R.string.u_no_file);
            }
        }

        /**
         * get usb device
         * @param usbDevice
         * @return
         */
        private UsbMassStorageDevice getUsbMass(UsbDevice usbDevice) {
            for (UsbMassStorageDevice device : storageDevices) {
                if (usbDevice.equals(device.getUsbDevice())) {
                    return device;
                }
            }
            return null;
        }

        /**
         *How to read usb file
         * @param context
         * @param device
         */
        private void readDevice(Context context, UsbMassStorageDevice device) {
            //Time-consuming operation, read U disk list in child thread
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        device.init();
                        // Only uses the first partition on the device
                        FileSystem currentFs = device.getPartitions().get(0).getFileSystem();
                        //Toast.makeText(context, "getRootDirectory: " + currentFs.getRootDirectory().getName(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Capacity: " + currentFs.getCapacity());
                        //Toast.makeText(context, "Capacity: " + currentFs.getCapacity(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Occupied Space: " + currentFs.getOccupiedSpace());
                        //Toast.makeText(context, "Occupied Space: " + currentFs.getOccupiedSpace(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Free Space: " + currentFs.getFreeSpace());
                        //Toast.makeText(context, "Free Space: " + currentFs.getFreeSpace(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Chunk size: " + currentFs.getChunkSize());
                        //Toast.makeText(context, "Chunk size: " + currentFs.getChunkSize(), Toast.LENGTH_SHORT).show();
                        cFolder = currentFs.getRootDirectory();
                        readFromUDisk(context);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }

        /**
         *Traverse USB files
         * @param context
         */
        private void readFromUDisk(Context context) {
            //U disk file group
            UsbFile[] usbFiles = new UsbFile[0];
            try {
                usbFiles = cFolder.listFiles();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (null != usbFiles && usbFiles.length > 0) {
                //Is it a new version
                boolean newVs = false;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        tvSubmit.setText(R.string.read_file);

                    }
                });
                //Display the name of the file read
                final String[] content = {getString(R.string.file_content)+ "\n"};
                for (UsbFile usbFile : usbFiles) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            content[0] += usbFile.getName() + "\n";
                            tvSubmit2.setText(content[0]);
                        }
                    });
                    //If the file name read is the name of our APP, compare the version number, if it is greater than it can be updated
                    if (usbFile.getName().contains(U_DISK_FILE_NAME)) {
                        //intercept string
                        String str1 = usbFile.getName().substring(0, usbFile.getName().indexOf("v"));
                        //Intercept version number
                        String str2 = usbFile.getName().substring(str1.length() + 1);

                        if (str2.compareTo(ConfigContent.deviceVersion) > 0) {

                            //Compare versions
                            readTxtFromUDisk(usbFile, usbFiles);

                            //is the new version changes the variable state
                            newVs = true;
                            verName = "v" + str2;
                        }

                    }
                }
                if (!newVs) {
                    //No new version prompts no latest version, and format the contents of the U disk
                    for (UsbFile usbFile : usbFiles) {
                        //Traverse U disk files
                        try {
                            if (!usbFile.getName().contains("System Volume")) {
                                //Delete all files except system files
                                usbFile.delete();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            tvSubmit.setText(R.string.no_new_verinu);

                        }
                    });
                }
            }
        }


        /**
         * Copy USB files to local
         *
         */
        public boolean saveUSbFileToLocal(UsbFile targetFile, String savePath,
                                          DownloadProgressListener progressListener) {
            boolean result;
            try {
                //file input stream and output stream
                UsbFileInputStream uis = new UsbFileInputStream(targetFile);
                FileOutputStream fos = new FileOutputStream(savePath);
                long avi = targetFile.getLength();
                int writeCount = 0;
                int bytesRead;
                byte[] buffer = new byte[1024];
                while ((bytesRead = uis.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                    writeCount += bytesRead;
                    if (progressListener != null) {
                        //Callback download progress
                        progressListener.downloadProgress((int) (writeCount * 100 / avi));
                    }
                }
                assert progressListener != null;
                fos.flush();
                uis.close();
                fos.close();
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UsbActivity.this, e.getMessage() + "errr", Toast.LENGTH_SHORT).show();
                    }
                });
                result = false;
            }
            return result;
        }


        /**
         * Read files from U disk to local
         * @param usbFile
         */
        private void readTxtFromUDisk(UsbFile usbFile, UsbFile[] usbFiles) {

            //local filename path
            String filrDir = getExternalCacheDir().getPath() + File.separator + "app" + File.separator;
            File saveFile = new File(filrDir);
            // If the directory does not exist
            if (!saveFile.exists()) {
                saveFile.mkdirs();
            }
            //local files
//            apkFile = new File(filrDir + "/SalletOnecold");
            apkFile = new File(filrDir + "/SalletOnecold"+ ConfigContent.deviceVersion);
            apkFile.delete();
            //Open sub-threads to process time-consuming tasks

            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean result = saveUSbFileToLocal(usbFile, filrDir  + "/SalletOnecold"+ ConfigContent.deviceVersion, new DownloadProgressListener() {
                        @Override
                        public void downloadProgress(int progress) {
                            //save progress callback
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvSubmit.setText(getString(R.string.installing) + verName + "-" + progress + "%");
                                }
                            });

                        }

                    });
                    if (result) {
                        //Updating UI on UI thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                installApk(UsbActivity.this);
                            }
                        });
                        //No new version prompts no latest version, and format the contents of the U disk
                        for (UsbFile usbFile : usbFiles) {
                            //Traverse U disk files
                            try {
                                if (!usbFile.getName().contains("System Volume")) {
                                    //Delete all files except system files
                                    usbFile.delete();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).start();


        }

        /**
         * Install APK
         * @param context 上下文
         */
        public void installApk(Context context) {


            boolean haveInstallPermission;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                haveInstallPermission = context.getPackageManager().canRequestPackageInstalls();
                if (haveInstallPermission) {
                    //Have permission to install directly
                    startActivity(ApkUtils.getApkFileIntent(context, apkFile));
                } else {
                    //No permission to request permission
                    Uri packageUri = Uri.parse("package:" + App.context.getApplicationInfo().packageName);
                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageUri);
                    startActivityForResult(intent, apkInstall);
                    tvSubmit.setText(R.string.request_install);
                }
            } else {
                //Direct installation below Android 26
                startActivity(ApkUtils.getApkFileIntent(context, apkFile));
            }


        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == apkInstall) {
            //Request permission callback to install
            startActivity(ApkUtils.getApkFileIntent(this, apkFile));
        }

    }

    /**
     * download callback
     */
    public interface DownloadProgressListener {
        void downloadProgress(int progress);
    }
}
