package com.otlab.here.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.otlab.here.R;

public class FriendInfoPopupActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_friend_info_popup);

        Intent intent = getIntent();
        try {
            ((TextView) findViewById(R.id.friendId)).setText(intent.getStringExtra(getResources().getString(R.string.friendId)));
            ((TextView) findViewById(R.id.friendName)).setText(intent.getStringExtra(getResources().getString(R.string.friendName)));
            ((TextView) findViewById(R.id.friendValidity)).setText(intent.getStringExtra(getResources().getString(R.string.friendValidity)));
        }catch(Exception e){
            Toast.makeText(getApplicationContext(),"오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
        findViewById(R.id.okButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        finish();
    }
}
