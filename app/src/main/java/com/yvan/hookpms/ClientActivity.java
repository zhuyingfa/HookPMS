package com.yvan.hookpms;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

/**
 * @author yvan
 * @date 2023/8/7
 * @description
 */
public class ClientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        checkPermission(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.yvan.client");
        registerReceiver(new FinishBroadcastReceiver(), filter);
    }
    
    public static boolean checkPermission(
            Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
        }
        return false;
    }

    public void registerBroaderCast(View view) {
        PMSPackageParser pmsPackageParser = new PMSPackageParser();
        try {
            pmsPackageParser.parserReceivers(this,
                    new File(getFilesDir(), "input.apk"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendBroaderCast(View view) {
        Toast.makeText(this, "1.发送消息给server", Toast.LENGTH_SHORT).show();
        view.postDelayed(() -> {
            Intent intent = new Intent();
            intent.setAction("com.yvan.server");
            sendBroadcast(intent);
        }, 3000);
    }

    static class FinishBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "3.收到server回复消息", Toast.LENGTH_SHORT).show();
        }
    }

}