package com.yvan.pmsbr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * @author yvan
 * @date 2023/8/7
 * @description
 */
public class PMSBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // 收到你的信息了
        Toast.makeText(context, "2.接收到client的消息", Toast.LENGTH_SHORT).show();
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent1 = new Intent();
            intent1.setAction("com.yvan.client");
            context.sendBroadcast(intent1);
        }, 3000);
    }
}
