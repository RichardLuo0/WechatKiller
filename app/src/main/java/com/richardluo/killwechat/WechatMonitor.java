package com.richardluo.killwechat;

import android.accessibilityservice.AccessibilityService;
import android.content.SharedPreferences;
import android.view.accessibility.AccessibilityEvent;

import java.util.Arrays;

import static android.view.accessibility.AccessibilityEvent.CONTENT_CHANGE_TYPE_UNDEFINED;

public class WechatMonitor extends AccessibilityService {
    static int pendingTime = 1000 * 60 * 3;
    static boolean isInApps = false;
    static boolean isPending = false;

    private final static String[] apps = {"com.tencent.mm", "com.tencent.mobileqq"};

    @Override
    protected void onServiceConnected() {
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        pendingTime = preferences.getInt("pendingTime", 1000 * 60 * 3);
        super.onServiceConnected();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getContentChangeTypes() == CONTENT_CHANGE_TYPE_UNDEFINED)
            if (Arrays.asList(apps).contains(event.getPackageName().toString())) {
                isInApps = true;
            } else if (isInApps && event.getPackageName().equals("com.google.android.apps.nexuslauncher")) {
                isInApps = false;
                delayKillWechat();
            }
    }

    @Override
    public void onInterrupt() {
    }

    public static void delayKillWechat() {
        synchronized (apps) {
            if (!isPending) {
                isPending = true;
                Thread thread = new Thread(() -> {
                    try {
                        Thread.sleep(pendingTime);
                        if (!isInApps) {
                            KillerProcess.getInstance().kill(apps);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        synchronized (apps) {
                            isPending = false;
                        }
                    }
                });
                thread.start();
            }
        }
    }
}