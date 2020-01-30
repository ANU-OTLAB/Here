package com.otlab.here.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.otlab.here.R;

import java.util.ArrayList;

public class JoinActivity extends Activity implements View.OnClickListener {

    private EditText nameText;
    private EditText phText;
    private EditText idText;
    private EditText pwText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        init();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.submit:
                if (TextUtils.isEmpty(nameText.getText()) && TextUtils.isEmpty(phText.getText()) && TextUtils.isEmpty(idText.getText()) && TextUtils.isEmpty(pwText.getText())) {
                    Toast.makeText(getApplicationContext(), "빈칸을 모두 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else if (connectServer()) {
                    Toast.makeText(getApplicationContext(), "회원가입 성공!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplication(), LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "회원가입 실패!", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.cancel:
                startActivity(new Intent(getApplication(), LoginActivity.class));
                finish();
                break;
        }
    }

    private void init() {
        nameText = findViewById(R.id.name);
        phText = findViewById(R.id.ph);
        idText = findViewById(R.id.id);
        pwText = findViewById(R.id.pw);
        findViewById(R.id.submit).setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);
    }

    private boolean connectServer() {
        try {
            ArrayList<String> sendMsg = new ArrayList<>();
            sendMsg.add("name");
            sendMsg.add(nameText.getText().toString());
            sendMsg.add("ph");
            sendMsg.add(phText.getText().toString());
            sendMsg.add("id");
            sendMsg.add(idText.getText().toString());
            sendMsg.add("pw");
            sendMsg.add(pwText.getText().toString());
            MessageThread messageThread = new MessageThread(sendMsg, getResources().getString(R.string.addressJoin));
            String msg = messageThread.execute().get();
            return msg.equals("JOIN_SUCCESS");
        } catch (Exception e) {
            init();
            return false;
        }
    }
}