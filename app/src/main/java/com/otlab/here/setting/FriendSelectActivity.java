package com.otlab.here.setting;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.otlab.here.R;
import com.otlab.here.common.MessageThread;
import com.otlab.here.common.model.Destination;
import com.otlab.here.setting.FriendInfoPopupActivity;
import com.otlab.here.setting.model.SettingItem;

import java.util.ArrayList;

public class FriendSelectActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView listView;
    Intent intent;

    String receiveMsg = "";
    ArrayList<String> sendmsg;

   private String[] recvData; // 친구목록을 가져올 ArrayList
    private String id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_friend_select);

        sendmsg = new ArrayList<>();

        listView = findViewById(R.id.listview);
        findViewById(R.id.ok).setOnClickListener(this);

        id = getSharedPreferences(getResources().getString(R.string.appData), MODE_PRIVATE).getString(getResources().getString(R.string.userId), " ");

        ArrayList<String> items = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, items);

        try {
            sendmsg.add("id");
            sendmsg.add(id);

            receiveMsg = new MessageThread(sendmsg, getResources().getString(R.string.addressFriendList)).execute().get();

            // "/"는 리스트 한 줄 단위 구분 " "는 이름, id, 만료기한 구분
            if (receiveMsg.length() != 0) {
                recvData = receiveMsg.split("/");
                for (String recvDatum : recvData) {
                    String[] buffer = recvDatum.split(" ");
                    items.add(buffer[0]);
                }
            }
        } catch (Exception ignored) {
        }

        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Destination[] destination = new Destination[listView.getCheckedItemCount()];
        SparseBooleanArray checkedItemPositions =  listView.getCheckedItemPositions();
        for (int i = 0, j=0; i < listView.getCount(); i++) {
            if(checkedItemPositions.get(i))
                destination[j++] = new Destination(SettingItem.DestinationType.PERSON, listView.getItemAtPosition(i).toString().trim(), 0, 0);
        }
        setResult(RESULT_OK, getIntent().putExtra(getResources().getString(R.string.destination), destination));
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        String[] buffer = recvData[position].split(" ");

        Intent intent = new Intent(getApplicationContext(), FriendInfoPopupActivity.class);
        intent.putExtra(getResources().getString(R.string.friendId), listView.getItemAtPosition(position).toString().trim());
        intent.putExtra(getResources().getString(R.string.friendName), buffer[1]);
        intent.putExtra(getResources().getString(R.string.friendValidity), buffer.length==3?buffer[2]:"");
        startActivity(intent);
    }
}
