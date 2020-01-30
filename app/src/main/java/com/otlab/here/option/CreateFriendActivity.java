package com.otlab.here.option;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.otlab.here.R;

public class CreateFriendActivity extends AppCompatActivity {

    TextView id;
    TextView name;
    Button newfriendbtn;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_create_friend);

        //FriendActivity 에서 넘겨는주는 정보 받기
        Intent intent = getIntent();
        position = intent.getIntExtra("position",0);

        newfriendbtn = (Button)findViewById(R.id.NewFriend);
        newfriendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newfriend();
            }
        });
    }
    // 확인 버튼을 누르면 리스트에 아이템 생성 되게 하는 함수
    protected void newfriend(){
        id = (TextView)findViewById(R.id.NewFriendId);
        name = (TextView)findViewById(R.id.NewFriendName);

        //((FriendActivity)FriendActivity.mContext). 하면 FriendActivity 에있는 것들 사용가능
       // ((FriendActivity)FriendActivity.).customAdapter.itemadd(id.getText().toString(),name.getText().toString());
        finish();
    }
}