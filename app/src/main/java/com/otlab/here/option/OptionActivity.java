package com.otlab.here.option;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.otlab.here.MainActivity;
import com.otlab.here.R;
import com.otlab.here.common.SplashActivity;


public class OptionActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_option);
        init();
    }

    @Override
    public void onClick(View view) {
        LinearLayout whoamiLayout = findViewById(R.id.whoami_layout);
        switch (view.getId()) {
            case R.id.logout:
                logout();
                break;
            case R.id.whoami:
                whoami(whoamiLayout);
                break;
            case R.id.whoami_close:
                whoamiClose(whoamiLayout);
                break;
            case R.id.alarm:
                startActivity(new Intent(getApplicationContext(), AlarmSettingActivity.class));
                break;
            case R.id.friend:
                startActivity(new Intent(getApplicationContext(), FriendActivity.class));
                break;
            case R.id.accept:
                startActivity(new Intent(getApplicationContext(), WaitingAcceptActivity.class));
                break;
            case R.id.developer:
                startActivity(new Intent(getApplicationContext(), DeveloperActivity.class));
                break;
        }
    }

    private void init() {
        findViewById(R.id.logout).setOnClickListener(this);
        findViewById(R.id.whoami).setOnClickListener(this);
        findViewById(R.id.whoami_close).setOnClickListener(this);
        findViewById(R.id.alarm).setOnClickListener(this);
        findViewById(R.id.friend).setOnClickListener(this);
        findViewById(R.id.developer).setOnClickListener(this);
        findViewById(R.id.accept).setOnClickListener(this);
    }

    private void logout() {
        //로그아웃 확인창과 그 메세지
        new AlertDialog.Builder(OptionActivity.this)
                .setTitle("로그아웃")
                .setMessage("로그아웃 하시겠습니까?\n로그아웃 시 저장 된 알람이 삭제됩니다.\n(취소 시 돌아감)")
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                    // 확인시 처리 로직
                    Toast.makeText(getApplicationContext(), "로그아웃을 완료했습니다.", Toast.LENGTH_SHORT).show();
                    getSharedPreferences(getResources().getString(R.string.appData), MODE_PRIVATE).edit().clear().apply();
                    startActivity(new Intent(getApplication(), SplashActivity.class));
                    finish();
                })
                .setNegativeButton(android.R.string.no, (dialog, whichButton) -> {
                    // 취소시 처리 로직
                    Toast.makeText(getApplicationContext(), "취소하였습니다.", Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    //whoami 레이아웃 숨기기/보여주기
    protected void whoami(LinearLayout layout) {
        SharedPreferences hereData = getSharedPreferences(getResources().getString(R.string.appData), MODE_PRIVATE);
        ((TextView) findViewById(R.id.whoami_name)).setText(hereData.getString(getResources().getString(R.string.userName), "name"));
        ((TextView) findViewById(R.id.whoami_id)).setText(hereData.getString(getResources().getString(R.string.userId), "id"));

        if (layout.getVisibility() == View.GONE) {
            Animation ani = new AlphaAnimation(0, 1);
            ani.setDuration(1000);
            layout.setVisibility(View.VISIBLE);
            layout.setAnimation(ani);
        } else {
            layout.setVisibility(View.GONE);
        }
    }

    protected void whoamiClose(LinearLayout layout) {
        if (layout.getVisibility() == View.VISIBLE) {
            layout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}