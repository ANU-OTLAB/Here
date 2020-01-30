package com.otlab.here.option;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.otlab.here.common.MessageThread;
import com.otlab.here.R;
import com.otlab.here.option.model.FriendItem;
import com.otlab.here.option.adapter.FriendListViewCustomAdapter;

import java.util.ArrayList;

public class FriendActivity extends Activity implements View.OnClickListener {

    private ListView friendListView;
    private TextView friendIdtxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        init();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addRequest:
                addFriend();
                break;
            case R.id.delRequest:
                delFriend();
                break;
        }
    }

    private void init() {
        friendListView = findViewById(R.id.FriendList);
        friendIdtxt = findViewById(R.id.friendId);
        findViewById(R.id.addRequest).setOnClickListener(this);
        findViewById(R.id.delRequest).setOnClickListener(this);
        refreshList();
    }

    private void refreshList() {
        ArrayList<FriendItem> friendList = new ArrayList<>();
        FriendListViewCustomAdapter customAdapter = new FriendListViewCustomAdapter(this, R.layout.friendlistview_item, friendList);
        friendListView.setAdapter(customAdapter);

        ArrayList<String> sendmsg = new ArrayList<>();

        try {
            sendmsg.add("id");
            sendmsg.add(getSharedPreferences(getResources().getString(R.string.appData), MODE_PRIVATE).getString(getResources().getString(R.string.userId), ""));

            String receiveMsg = new MessageThread(sendmsg, getResources().getString(R.string.addressFriendList)).execute().get();

            // "/"는 리스트 한 줄 단위 구분 " "는 이름, id, 만료기한 구분
            if (receiveMsg.length() != 0) {
                String[] receiveData = receiveMsg.split("/");
                for (String receiveDatum : receiveData) {
                    String[] buffer = receiveDatum.split(" ");
                    friendList.add(new FriendItem(buffer[0], buffer[1], buffer.length == 3 ? buffer[2] : ""));
                }
            }
            customAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addFriend() {
        try {
            String address = getResources().getString(R.string.addressFriendAdd);
            ArrayList<String> sendmsg = new ArrayList<>();
            sendmsg.add("myid");
            sendmsg.add(getSharedPreferences(getResources().getString(R.string.appData), MODE_PRIVATE).getString(getResources().getString(R.string.userId), ""));
            sendmsg.add("friend");
            sendmsg.add(friendIdtxt.getText().toString());

            // 친구추가 성공 or 실패 반환 받기
            String msgReceiveFromServer = new MessageThread(sendmsg, address).execute().get();
            // 만약 성공했으면 새로고침
            if (msgReceiveFromServer.equals("ADD_SUCCESS")) {
                refreshList();
            }
        } catch (Exception ignored) {
        }
    }

    private void delFriend() {
        try {
            ArrayList<String> sendmsg = new ArrayList<>();
            sendmsg.add("myid");
            sendmsg.add(getSharedPreferences(getResources().getString(R.string.appData), MODE_PRIVATE).getString(getResources().getString(R.string.userId), ""));
            sendmsg.add("friend");
            sendmsg.add(friendIdtxt.getText().toString());

            String msgReceiveFromServer = new MessageThread(sendmsg, getResources().getString(R.string.addressFriendDelete)).execute().get();
            // 친구삭제가 성공했으면 새로고침
            if ("DELETE_SUCCESS".equals(msgReceiveFromServer)) {
                refreshList();
            }
        } catch (Exception ignored) {
        }
    }
}