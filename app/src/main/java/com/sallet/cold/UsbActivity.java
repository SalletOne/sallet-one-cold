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
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 检查更新页面
 * 通过接入U盘来检测U盘中是否已存在最新安装包，来判断是否更新，如果有新版本的安装包，就会自动更新
 * Check the update page
 * Check whether the latest installation package already exists in the U disk by accessing the U disk to determine whether to update it.
 * If there is a new version of the installation package,
 * it will be automatically updated
 */
public class UsbActivity extends AppCompatActivity {
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    int apkInstall = 10008; //安装APK请求码 Install APK request code
    /**
     * 绑定UI
     * Bind UI
     */
    @InjectView(R.id.tv_submit)
    TextView tvSubmit;
    @InjectView(R.id.tv_submit2)
    TextView tvSubmit2;
    @InjectView(R.id.rl_back)
    RelativeLayout rlBack;
    @InjectView(R.id.rl_title)
    RelativeLayout rlTitle;
    private USBBroadcastReceiver usbBroadcastReceiver = new USBBroadcastReceiver();//usb插拔广播监听 usb plug-in broadcast monitoring
    File apkFile;//安装包文件 installation package file
    private String localRootPath = Environment.getExternalStorageDirectory().getAbsolutePath();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usb);
        ButterKnife.inject(this);

        //新建意图显示器对象
        //New Intent Display Object
        IntentFilter usbDeviceStateFilter = new IntentFilter();
        //增加意图
        //increase intent
        usbDeviceStateFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        //增加意图
        //increase intent
        usbDeviceStateFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        //注册广播
        //Register for broadcast
        registerReceiver(usbBroadcastReceiver, usbDeviceStateFilter);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        //注册USB请求权限广播
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
                    //获取USB权限
                    //Get USB permissions
                    UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (usbDevice != null) {
                            //读取USB列表
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
                    //插入USB广播
                    //plug in usb radio
                    UsbDevice device_add = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (device_add != null) {
                        //读取USB列表
                        //Read USB list
                        redUDiskDevsList(context);
                    }
                    break;
                case UsbManager.ACTION_USB_DEVICE_DETACHED:
                    //拔出USB广播
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
         *读取U盘列表
         *Read U disk list
         */
        private void redUDiskDevsList(Context context) {
            UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
            storageDevices = UsbMassStorageDevice.getMassStorageDevices(context);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                    new Intent(ACTION_USB_PERMISSION), 0);
            for (UsbMassStorageDevice device : storageDevices) {
                if (usbManager.hasPermission(device.getUsbDevice())) {
                    //如果有权限就去读USB文件
                    //Read the USB file if you have permission
                    readDevice(context, device);
                } else {
                    //没有权限去请求权限
                    //No permission to request permission
                    usbManager.requestPermission(device.getUsbDevice(), pendingIntent);
                }
            }
            if (storageDevices.length == 0) {
                tvSubmit.setText(R.string.u_no_file);
            }
        }

        /**
         * 获取USB设备
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
         * 读取usb文件方法
         *How to read usb file
         * @param context
         * @param device
         */
        private void readDevice(Context context, UsbMassStorageDevice device) {
            //耗时操作，在子线程中读取U盘列表
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
         * 遍历USB文件
         *Traverse USB files
         * @param context
         */
        private void readFromUDisk(Context context) {
            //U盘文件组
            //U disk file group
            UsbFile[] usbFiles = new UsbFile[0];
            try {
                usbFiles = cFolder.listFiles();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (null != usbFiles && usbFiles.length > 0) {
                //是否是新版本
                //Is it a new version
                boolean newVs = false;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        tvSubmit.setText(R.string.read_file);

                    }
                });
                //展示读取的文件名称
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
                    //如果读取的文件名称是我们APP的名称，比较版本号大小，大于就可以更新
                    //If the file name read is the name of our APP, compare the version number, if it is greater than it can be updated
                    if (usbFile.getName().contains(U_DISK_FILE_NAME)) {
                        //截取字符串
                        //intercept string
                        String str1 = usbFile.getName().substring(0, usbFile.getName().indexOf("v"));
                        //截取版本号
                        //Intercept version number
                        String str2 = usbFile.getName().substring(str1.length() + 1);

                        if (str2.compareTo(ConfigContent.deviceVersion) > 0) {

                            //比较版本
                            //Compare versions
                            readTxtFromUDisk(usbFile, usbFiles);

                            //是新版本更改变量状态
                            //is the new version changes the variable state
                            newVs = true;
                            verName = "v" + str2;
                        }

                    }
                }
                if (!newVs) {
                    //没有新版本提示无最新版本，并格式化U盘里面的内容
                    //No new version prompts no latest version, and format the contents of the U disk
                    for (UsbFile usbFile : usbFiles) {
                        //遍历U盘文件
                        //Traverse U disk files
                        try {
                            if (!usbFile.getName().contains("System Volume")) {
                                //除了系统文件全部删除
                                //Delete all files except system files
                                usbFile.delete();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    //在UI线程更新UI
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
         * 拷贝U盘文件到本地
         * Copy USB files to local
         *
         */
        public boolean saveUSbFileToLocal(UsbFile targetFile, String savePath,
                                          DownloadProgressListener progressListener) {
            boolean result;
            try {
                //文件输入流和输出流
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
                        //回调读取进度
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
         * 从U盘中读取文件到本地
         * Read files from U disk to local
         * @param usbFile
         */
        private void readTxtFromUDisk(UsbFile usbFile, UsbFile[] usbFiles) {

            //本地文件名路径
            //local filename path
            String filrDir = getExternalCacheDir().getPath() + File.separator + "app" + File.separator;
            File saveFile = new File(filrDir);
            // If the directory does not exist
            if (!saveFile.exists()) {
                saveFile.mkdirs();
            }
            //本地文件
            //local files
            apkFile = new File(filrDir + "/SalletOnecold");
            //apkFile = new File(filrDir + verName);
            apkFile.delete();
            //开启子线程处理耗时任务
            //Open sub-threads to process time-consuming tasks

            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean result = saveUSbFileToLocal(usbFile, filrDir + "/SalletOnecold", new DownloadProgressListener() {
                        @Override
                        public void downloadProgress(int progress) {
                            //保存进度回调
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
                        //在UI线程更新UI
                        //Updating UI on UI thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                installApk(UsbActivity.this);
                            }
                        });
                        //没有新版本提示无最新版本，并格式化U盘里面的内容
                        //No new version prompts no latest version, and format the contents of the U disk
                        for (UsbFile usbFile : usbFiles) {
                            //遍历U盘文件
                            //Traverse U disk files
                            try {
                                if (!usbFile.getName().contains("System Volume")) {
                                    //除了系统文件全部删除
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
         * 安装APK
         * Install APK
         * @param context 上下文
         */
        public void installApk(Context context) {


            boolean haveInstallPermission;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                haveInstallPermission = context.getPackageManager().canRequestPackageInstalls();
                if (haveInstallPermission) {
                    //有权限直接安装
                    //Have permission to install directly
                    startActivity(ApkUtils.getApkFileIntent(context, apkFile));
                } else {
                    //没权限请求权限
                    //No permission to request permission
                    Uri packageUri = Uri.parse("package:" + App.context.getApplicationInfo().packageName);
                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageUri);
                    startActivityForResult(intent, apkInstall);
                    tvSubmit.setText(R.string.request_install);
                }
            } else {
                //安卓26以下直接安装
                //Direct installation below Android 26
                startActivity(ApkUtils.getApkFileIntent(context, apkFile));
            }


        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == apkInstall) {
            //请求权限回调去安装
            //Request permission callback to install
            startActivity(ApkUtils.getApkFileIntent(this, apkFile));
        }

    }

    /**
     * 下载回调
     * download callback
     */
    public interface DownloadProgressListener {
        void downloadProgress(int progress);
    }
}
