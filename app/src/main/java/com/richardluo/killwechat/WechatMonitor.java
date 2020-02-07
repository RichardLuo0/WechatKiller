package com.richardluo.killwechat;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.SharedPreferences;
import android.view.accessibility.AccessibilityEvent;

public class WechatMonitor extends AccessibilityService {
    static int pendingTime = 1000 * 60 * 3;
    String launcher = "com.google.android.apps.nexuslauncher";

    String[] wechat = {"com.tencent.mm"};
    String[] desktop = {launcher};
    static boolean isWechat = false;
    static boolean isPending = false;

    @Override
    protected void onServiceConnected() {
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        pendingTime = preferences.getInt("pendingTime", 1000 * 60 * 3);
        launcher = preferences.getString("launcher", "com.google.android.apps.nexuslauncher");
        super.onServiceConnected();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getPackageName().equals("com.tencent.mm")) {
            isWechat = true;
            AccessibilityServiceInfo info = getServiceInfo();
            info.packageNames = desktop;
            setServiceInfo(info);
        } else {
            isWechat = false;
            AccessibilityServiceInfo info = getServiceInfo();
            info.packageNames = wechat;
            setServiceInfo(info);
            delayKillWechat();
        }
    }

    @Override
    public void onInterrupt() {

    }

    public static void delayKillWechat() {
        if (!isPending) {
            isPending = true;
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(pendingTime);
                        if (!isWechat)
                            Runtime.getRuntime().exec(new String[]{"su", "-c", "am", "kill", "com.tencent.mm"});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    isPending = false;
                }
            };
            thread.start();
        }
    }
}