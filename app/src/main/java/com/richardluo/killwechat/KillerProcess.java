package com.richardluo.killwechat;

import java.io.IOException;
import java.io.OutputStream;

public class KillerProcess {

    private final Process process = Runtime.getRuntime().exec("su");

    private static KillerProcess instance;

    public static KillerProcess getInstance() throws IOException {
        if (instance == null) {
            synchronized (KillerProcess.class) {
                if (instance == null) {
                    instance = new KillerProcess();
                }
            }
        }
        return instance;
    }

    public KillerProcess() throws IOException {
    }

    public void kill(String[] apps) throws IOException {
        StringBuilder command = new StringBuilder();
        for (String app : apps) {
            command.append("su -c am kill ").append(app).append("&");
        }
        OutputStream out = process.getOutputStream();
        out.write(command.toString().getBytes());
        out.flush();
    }
}
