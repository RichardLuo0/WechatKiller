package com.richardluo.killwechat;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

public class WechatNotificationMonitor extends NotificationListenerService {

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (!WechatMonitor.isInApps && sbn.getPackageName().equals("com.tencent.mm"))
            WechatMonitor.delayKillWechat();
        super.onNotificationPosted(sbn);
    }
}
