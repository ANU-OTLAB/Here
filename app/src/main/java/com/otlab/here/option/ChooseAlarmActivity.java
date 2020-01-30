package com.otlab.here.option;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.otlab.here.R;

public class ChooseAlarmActivity extends AppCompatActivity implements View.OnClickListener {
    private MediaPlayer audioPlayer;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_alarm);

        init();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.alarmSelect:
                SharedPreferences.Editor editor = getSharedPreferences(getResources().getString(R.string.appData), MODE_PRIVATE).edit();
                editor.putString(getResources().getString(R.string.userAlarmPath), path);
                editor.apply();
                stopTest();
                finish();
                break;
            case R.id.alarmCancel:
                stopTest();
                finish();
                break;
        }
    }

    private void init() {
        Intent intent = getIntent();
        path = intent.getStringExtra(getResources().getString(R.string.alarmPath));
        ((TextView) findViewById(R.id.alarmTxt)).setText(intent.getStringExtra(getResources().getString(R.string.alarmName)));
        (findViewById(R.id.alarmSelect)).setOnClickListener(this);
        (findViewById(R.id.alarmCancel)).setOnClickListener(this);
        playTest();
    }

    private void playTest() {
        //샘플반복재생
        try {
            audioPlayer = new MediaPlayer();
            audioPlayer.setDataSource(path);
            audioPlayer.setAudioStreamType(AudioManager.MODE_RINGTONE);
            audioPlayer.setLooping(true);
            audioPlayer.prepare();
            audioPlayer.start();
        } catch (Exception ignored) {
        }
    }

    private void stopTest() {
        //샘플재생멈춤
        if (audioPlayer != null && audioPlayer.isPlaying())
            audioPlayer.stop();
    }
}
