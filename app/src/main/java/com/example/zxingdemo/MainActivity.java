package com.example.zxingdemo;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zxingdemo.zxing.android.CaptureActivity;
import com.example.zxingdemo.zxing.bean.ZxingConfig;
import com.example.zxingdemo.zxing.common.Constant;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView start = findViewById(R.id.tv_start);
        start.setOnClickListener(v -> {
            requestPermission();
        });

    }

    private void requestPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE)
                .onGranted(data -> {
                    Intent intent = new Intent(this, CaptureActivity.class);
                    ZxingConfig config = new ZxingConfig();
                    config.setPlayBeep(false);
                    config.setFullScreenScan(true);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
                    intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
                    mResult.launch(intent);
                }).onDenied(data -> {

        }).start();
    }

    /**
     * 扫描返回链接
     */
    ActivityResultLauncher<Intent> mResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            String codeUrl = result.getData().getStringExtra(Constant.CODED_CONTENT);
            Toast.makeText(this, codeUrl, Toast.LENGTH_LONG).show();
        }

    });
}