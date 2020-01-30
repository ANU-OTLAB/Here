package com.otlab.here.common;

import android.util.Log;

import com.otlab.here.common.Here;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Messenger extends Thread {
    private String sendMsg = "";
    private String receiveMsg;
    private String address;

    public Messenger(ArrayList<String> sendMsgList,  String address) {
        this.address = address;

        if (sendMsgList.size() % 2 == 0) {
            for (int i = 0; i < sendMsgList.size(); i += 2) {
                if (i >= 2) sendMsg += "&";
                sendMsg += sendMsgList.get(i);
                sendMsg += "=";
                sendMsg += sendMsgList.get(i + 1);
            }
        } else {
            sendMsg = "";
        }
    }

    @Override
    public void run() {
        super.run();

        String readMsg;

        if (Here.checkVersion()) {
            try {
                URL url = new URL(address);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                osw.write(sendMsg);
                osw.flush();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));

                    StringBuffer buffer = new StringBuffer();
                    while ((readMsg = reader.readLine()) != null) {
                        buffer.append(readMsg);
                    }
                    receiveMsg = buffer.toString();
                } else {
                    receiveMsg = "fail";
                }
            } catch (Exception e) {
                receiveMsg = "fail";
                Log.d("???????????", e.toString());
            }
        }
    }

    public String getMsg(){
        return receiveMsg;
    }
}
