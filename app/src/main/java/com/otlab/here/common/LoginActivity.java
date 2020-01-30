package com.otlab.here.common;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.otlab.here.MainActivity;
import com.otlab.here.R;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private String name;

    private EditText idText;
    private EditText pwText;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.loginBtn:
                //서버에 id, pw 확인 후 name을 받아 옴
                if (requestValidationCheck()) {

                    SharedPreferences appData = getSharedPreferences(getResources().getString(R.string.appData), MODE_PRIVATE);
                    SharedPreferences.Editor editor = appData.edit();

                    if (!appData.getString(getResources().getString(R.string.userId), "").equals(Here.getStringEditText(idText))) {
                        editor.clear().apply();
                        editor.putString(getResources().getString(R.string.userId), Here.getStringEditText(idText));
                        editor.putString(getResources().getString(R.string.userName), name);
                    }
                    editor.putBoolean(getResources().getString(R.string.autoLogin), checkBox.isChecked());
                    editor.apply();

                    startActivity(new Intent(getApplication(), MainActivity.class));
                    finish();
                }
                break;

            case R.id.joinBtn:
                startActivity(new Intent(getApplication(), JoinActivity.class));
                finish();
                break;
        }
    }

    // view 불러오기
    private void init() {
        idText = findViewById(R.id.idText);
        pwText = findViewById(R.id.pwdText);
        checkBox = findViewById(R.id.checkBox);
        findViewById(R.id.loginBtn).setOnClickListener(this);
        findViewById(R.id.joinBtn).setOnClickListener(this);
    }

    // 서버에 id, pw를 전송 후 로그인 성공여부와 사용자 이름을 불러 옴
    private boolean requestValidationCheck() {
        try {
            ArrayList<String> sendMsg = new ArrayList<>();
            sendMsg.add("id");
            sendMsg.add(idText.getText().toString());
            sendMsg.add("pw");
            sendMsg.add(pwText.getText().toString());

            //MessageThread(보낼 메시지 : ArrayList<String>, 주소 : String)
            String msgReceiveFromServer = new MessageThread(sendMsg, getResources().getString(R.string.addressLogin)).execute().get();

            //연결 확인
            if ("fail".equals(msgReceiveFromServer))
                return false;

            //로그인 성공여부 확인 후 이름 추출
            String[] msgList = msgReceiveFromServer.split("/");
            if ("LOGIN_SUCCESS".equals(msgList[0])) {
                name = msgList[1];
                return true;
            } else
                return false;

        } catch (Exception e) {
            return false;
        }
    }
}