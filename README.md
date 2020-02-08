# WechatKiller
Prevent wechat from maintaining its own heartbeat connection instead of using FCM push.(root needed)

## Why
Wechat (android) will send TCP packets every 5 or 6 mins in the background which consumes your battery.

This app will kill wechat in 3 mins in the following cases：
+ When you back to home from wechat
+ When you receive a Wechat message (the notification will not dissmis)

Also, this app will not affect FCM push, so it is safe to use.

## 为什么？
微信（安卓版）会在后台每隔5，6分钟发送TCP包，这将会消耗电量。

这个app将会在以下情况下在3分钟内杀死微信：
+ 当你回到桌面时
+ 当你收到微信消息时（通知不会消失）

并且，这个app不会影响FCM推送，所以你还是可以正常收到消息。
