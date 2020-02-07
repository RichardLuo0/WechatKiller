package com.richardluo.killwechat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    TextView wechatStatus;
    TextView pendingKill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Runtime.getRuntime().exec(new String[]{"su"});
        } catch (IOException e) {
            e.printStackTrace();
        }

        final SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);

        Intent infoIntent = new Intent(Intent.ACTION_MAIN);
        infoIntent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo res = getPackageManager().resolveActivity(infoIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (res != null)
            preferences.edit().putString("launcher", res.activityInfo.packageName).apply();

        LinearLayout line = new LinearLayout(this);
        line.setOrientation(LinearLayout.VERTICAL);

        wechatStatus = new TextView(this);
        pendingKill = new TextView(this);
        update();

        Button update = new Button(this);
        update.setText(R.string.update);
        update.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });

        final EditText time = new EditText(this);
        time.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        time.setText(String.valueOf(preferences.getInt("pendingTime", 1000 * 60 * 3)));

        Button timeSetting = new Button(this);
        timeSetting.setText(R.string.timeSetting);
        timeSetting.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = Integer.parseInt(time.getText().toString());
                if (temp > 0) {
                    WechatMonitor.pendingTime = temp;
                    preferences.edit().putInt("pendingTime", temp).apply();
                    Toast.makeText(getApplicationContext(), String.format(getResources().getString(R.string.timeToast), temp), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), "do not below 0!", Toast.LENGTH_SHORT).show();
                time.clearFocus();
            }
        });

        Button accessSetting = new Button(this);
        accessSetting.setText(R.string.accessSetting);
        accessSetting.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        Button notifySetting = new Button(this);
        notifySetting.setText(R.string.notifySetting);
        notifySetting.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        line.addView(wechatStatus);
        line.addView(pendingKill);
        line.addView(update);
        line.addView(time);
        line.addView(timeSetting);
        line.addView(accessSetting);
        line.addView(notifySetting);

        setContentView(line);
        super.onCreate(savedInstanceState);
    }

    void update() {
        wechatStatus.setText(String.format(getResources().getString(R.string.wechatStatus), WechatMonitor.isWechat ? "Yes" : "No"));
        pendingKill.setText(String.format(getResources().getString(R.string.pendingKill), WechatMonitor.isPending ? "Yes" : "No"));
    }

}
