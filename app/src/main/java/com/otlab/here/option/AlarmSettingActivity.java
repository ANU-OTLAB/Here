package com.otlab.here.option;

import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.otlab.here.R;
import com.otlab.here.option.model.AlarmSettingItem;
import com.otlab.here.option.adapter.AlarmSettingListViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class AlarmSettingActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ArrayList<AlarmSettingItem> alarmItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);
        init();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = new Intent(getApplicationContext(), ChooseAlarmActivity.class);
        intent.putExtra(getResources().getString(R.string.alarmName), alarmItemList.get(position).getAlarmName());
        intent.putExtra(getResources().getString(R.string.alarmPath), alarmItemList.get(position).getPathString());
        startActivity(intent);
    }

    private void init() {
        // 알람설정을 넣어야 할 액티비티
        ListView alarmsListView = findViewById(R.id.alarmList);
        List<Uri> alarmPath = new ArrayList<>();
        ArrayList<String> alarmName = new ArrayList<>();
        alarmItemList = new ArrayList<>();

        try {
            RingtoneManager ringtoneManager = new RingtoneManager(getApplicationContext());
            ringtoneManager.setType(RingtoneManager.TYPE_RINGTONE);

            Cursor alarmsCursor = ringtoneManager.getCursor();
            int alarmsCount = alarmsCursor.getCount();

            if (alarmsCount == 0 && alarmsCursor.moveToFirst()){
                alarmsCursor.close();
                return ;
            }

            while (!alarmsCursor.isAfterLast() && alarmsCursor.moveToNext())
                alarmPath.add(ringtoneManager.getRingtoneUri(alarmsCursor.getPosition()));

            for (int i = 0; i < alarmPath.size(); i++)
                alarmName.add(RingtoneManager.getRingtone(getApplicationContext(), alarmPath.get(i)).getTitle(getApplicationContext()));

        } catch (Exception ignored) {
        }

        for (int i = 0; i < alarmPath.size(); i++)
            alarmItemList.add(new AlarmSettingItem(alarmPath.get(i), alarmName.get(i)));

        alarmsListView.setAdapter(new AlarmSettingListViewAdapter(this, R.layout.alarm_setting_item, alarmItemList));
        alarmsListView.setOnItemClickListener(this);
    }
}
