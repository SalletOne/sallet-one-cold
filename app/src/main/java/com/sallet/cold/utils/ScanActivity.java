package com.sallet.cold.utils;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import com.sallet.cold.R;
import com.sallet.cold.base.BaseActivity;
import com.sallet.cold.bean.Word;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

/**
 * Scan the QR code page
 */
public class ScanActivity extends BaseActivity implements  DecoratedBarcodeView.TorchListener {

    private DecoratedBarcodeView decoratedBarcodeView;
    private CaptureManager captureManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_parent);
        //Scan UI
        decoratedBarcodeView=this.findViewById(R.id.decoratedBarcodeView);
        captureManager = new CaptureManager(this, decoratedBarcodeView);
        captureManager.initializeFromIntent(getIntent(), savedInstanceState);
        captureManager.decode();
        decoratedBarcodeView.setTorchListener(this);
        RelativeLayout rl_back=decoratedBarcodeView.findViewById(R.id.rl_back);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onTorchOn() {

    }

    @Override
    public void onTorchOff() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        captureManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        captureManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        captureManager.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        captureManager.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return decoratedBarcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
}

