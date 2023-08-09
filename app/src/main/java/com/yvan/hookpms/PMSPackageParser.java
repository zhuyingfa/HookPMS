package com.yvan.hookpms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import dalvik.system.DexClassLoader;

/**
 * @author yvan
 * @date 2023/8/7
 * @description
 */
public class PMSPackageParser {

    public void parserReceivers(Context context, File apkFile) throws Exception {
        Class<?> packageParserClass = Class.forName("android.content.pm.PackageParser");
        Method parsePackageMethod = packageParserClass.getDeclaredMethod("parsePackage",
                File.class, int.class);
        parsePackageMethod.setAccessible(true);
        Object packageParser = packageParserClass.newInstance();

        Object packageObj = parsePackageMethod.invoke(packageParser, apkFile,
                PackageManager.GET_RECEIVERS);
        packageObj.hashCode();

        Field receiversField = packageObj.getClass().getDeclaredField("receivers");

        List receivers = (List) receiversField.get(packageObj);

        // AndroidManifest---> Package对象   描述信息
        DexClassLoader dexClassLoader = new DexClassLoader(apkFile.getAbsolutePath(),
                context.getDir("plugin", Context.MODE_PRIVATE).getAbsolutePath(),
                null, context.getClassLoader());
        // 动态注册
        Class<?> componentClass = Class.forName("android.content.pm.PackageParser$Component");
        Field intentsField = componentClass.getDeclaredField("intents");
        for (Object receiverObject : receivers) {
            String name = (String) receiverObject.getClass().getField("className")
                    .get(receiverObject);
            // class  --->对象
            try {
                BroadcastReceiver broadcastReceiver = (BroadcastReceiver) dexClassLoader.loadClass(name).newInstance();
                List<? extends IntentFilter> filters = (List<? extends IntentFilter>)
                        intentsField.get(receiverObject);
                for (IntentFilter filter : filters) {
                    context.registerReceiver(broadcastReceiver, filter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
