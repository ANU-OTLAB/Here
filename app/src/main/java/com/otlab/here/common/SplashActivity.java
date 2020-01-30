package com.otlab.here.common;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.otlab.here.MainActivity;
import com.otlab.here.R;
import com.otlab.here.common.string.Const;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_splash);

        SharedPreferences hereData = getSharedPreferences(Const.PREF_COMMON, MODE_PRIVATE);
        boolean autoLogin = hereData.getBoolean(Const.PREF_AUTOLOGIN, false);
        String name = hereData.getString(Const.PREF_NAME, "");

        new Handler().postDelayed(() -> {
            if (autoLogin) {
                Toast.makeText(getApplicationContext(), name + "님 환영합니다.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplication(), MainActivity.class));
                finish();
            } else {
                startActivity(new Intent(getApplication(), LoginActivity.class));
                finish();
            }
        }, 3000);

    }

    //뒤로가기 버튼 방지
    @Override
    public void onBackPressed() {
    }

}