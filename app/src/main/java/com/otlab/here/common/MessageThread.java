package com.otlab.here.common;

import android.os.AsyncTask;

import com.otlab.here.common.Here;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * 생성자에 msg를 넣어주고 getMessage를 실행하거나
 * 객체를 생성 후 getMessage에 msg를 입력받아 서버로 부터 전송 받은 값 반환
 */
public class MessageThread extends AsyncTask <Object, Void, String>{

    private String sendMsg = "";
    private String receiveMsg;
    private String address;

    public MessageThread(ArrayList<String> sendMsgList, String address) {
        this.address = address;
        this.sendMsg = makeParameterMessage(sendMsgList);
    }
    public MessageThread(ArrayList<String> sendMsgList, String s, String address) {
        this(sendMsgList, address);
    }

    @Override
    protected String doInBackground(Object[] objects) {
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
            }
        }
        return receiveMsg;
    }

    public void changeMessage(ArrayList<String> sendMsgList){
        sendMsg = makeParameterMessage(sendMsgList);

    }

    public String makeParameterMessage(ArrayList<String> sendMsgList){
        String msg = "";
        if (sendMsgList.size() % 2 == 0) {
            for (int i = 0; i < sendMsgList.size(); i += 2) {
                if (i >= 2) msg += "&";
                msg += sendMsgList.get(i);
                msg += "=";
                msg += sendMsgList.get(i + 1);
            }
        }
        return msg;
    }
}
