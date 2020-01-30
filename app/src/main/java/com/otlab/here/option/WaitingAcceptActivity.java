package com.otlab.here.option;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.otlab.here.common.MessageThread;
import com.otlab.here.R;
import com.otlab.here.option.model.WaitingAcceptItem;
import com.otlab.here.option.adapter.WaitingAcceptListViewCustomAdapter;

import java.util.ArrayList;

public class WaitingAcceptActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ArrayList<WaitingAcceptItem> waitingAcceptList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_accept);

        init();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = new Intent(getApplicationContext(), ChooseActivity.class);
        intent.putExtra(getResources().getString(R.string.requestId), waitingAcceptList.get(position).getFriendName());
        intent.putExtra(getResources().getString(R.string.validTime), waitingAcceptList.get(position).getTime());
        startActivity(intent);
        finish();
    }

    private void init() {
        try {
            waitingAcceptList = new ArrayList<>();
            WaitingAcceptListViewCustomAdapter customAdapter = new WaitingAcceptListViewCustomAdapter(this, R.layout.waiting_accept_item, waitingAcceptList);
            ((ListView) findViewById(R.id.waitingAcceptList)).setAdapter(customAdapter);
            // 나의 아이디를 가져오기 위해서 SharedPreferneces 선언
            ArrayList<String> sendMsg = new ArrayList<>();
            sendMsg.add("target");
            sendMsg.add(getSharedPreferences(getResources().getString(R.string.appData), MODE_PRIVATE).getString(getResources().getString(R.string.userId), ""));
            String recv = new MessageThread(sendMsg, getResources().getString(R.string.addressAuthority)).execute().get();

            if (recv.length() >= 2) {
                // 친구목록을 가져올 ArrayList
                String[] recvData = recv.split("/");
                for (String recvDatum : recvData) {
                    String[] buffer = recvDatum.split(" ");
                    waitingAcceptList.add(new WaitingAcceptItem(buffer[0], buffer[1], "REQUEST"));
                }
            } else {
                waitingAcceptList.add(new WaitingAcceptItem("", "EMPTY", ""));
            }
            customAdapter.notifyDataSetChanged();
        } catch (Exception ignored) {
        }
    }
}
