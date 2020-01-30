package com.otlab.here.setting;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.otlab.here.common.model.Destination;
import com.otlab.here.R;
import com.otlab.here.setting.model.SettingItem;
import com.otlab.here.setting.adapter.ListViewCustomAdapter;

import java.util.ArrayList;

public class SettingActivity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {

    String distance;
    private ArrayList<SettingItem> settingItemList;
    private ListViewCustomAdapter customAdapter;
    private SharedPreferences appData;
    private int listSize;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_setting);

        init();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        distance = settingItemList.get(position).getDistance().split("M")[0];
        Intent intent = new Intent(getApplicationContext(), SettingPopupActivity.class);
        intent.putExtra(getResources().getString(R.string.settingName), settingItemList.get(position).getSettingName())
                .putExtra(getResources().getString(R.string.distance), distance)
                .putExtra(getResources().getString(R.string.destinationName), settingItemList.get(position).getDestination())
                .putExtra(getResources().getString(R.string.destinationType), SettingItem.DestinationType.UNKNOWN)
                .putExtra(getResources().getString(R.string.validity), settingItemList.get(position).getValidity())
                .putExtra(getResources().getString(R.string.itemPosition), position)
                .putExtra(getResources().getString(R.string.service), SettingItem.ServiceType.UPDATE);

        startActivityForResult(intent, 1);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.createList) {
            Intent intent = new Intent(getApplicationContext(), SettingPopupActivity.class);
            intent.putExtra(getResources().getString(R.string.service), SettingItem.ServiceType.CREATE);
            startActivityForResult(intent, 1);
        }
    }

    private void init() {
        ListView settingItemListView = findViewById(R.id.setList);
        settingItemList = new ArrayList<>();

        //on Load 데이터 불러 와서 리스트에 보여줌
        appData = getSharedPreferences(getResources().getString(R.string.appData), MODE_PRIVATE);
        listSize = appData.getInt(getResources().getString(R.string.listSize), 0);

        for (int i = 0; i < listSize; i++) {
            settingItemList.add(new SettingItem(appData.getString("name" + i, ""), appData.getString("distance" + i, "") + "M", appData.getString("destinationName" + i, ""), appData.getString("validity" + i, "")));
        }

        customAdapter = new ListViewCustomAdapter(this, R.layout.listview_item, settingItemList);
        settingItemListView.setAdapter(customAdapter);

        settingItemListView.setOnItemClickListener(this);
        findViewById(R.id.createList).setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        SharedPreferences.Editor editor = appData.edit();
        SettingItem.ServiceType serviceType = (SettingItem.ServiceType) data.getSerializableExtra("service");
        //requestCode 확인... 예의상 놔둠(없어도 됨)
        if (requestCode == 1) {
            //PopupActivity에서 확인,저장 버튼 눌렀을 때
            if (resultCode == RESULT_OK) {

                String name = data.getStringExtra("settingName");
                distance = data.getStringExtra("distance");
                String destinationName = data.getStringExtra("destinationName");
                String validity = data.getStringExtra("validity");
                int itemPosition = data.getIntExtra("itemPosition", 0);
                Destination[] destination = (Destination[]) data.getSerializableExtra("destination");
                String destinationList;
                if (destination != null) {
                    destinationList = destination[0].getDestinationName();
                    for (int i = 1; i < destination.length; i++) {
                        destinationList += " ";
                        destinationList += destination[i].getDestinationName();
                    }
                } else {
                    destinationList = data.getStringExtra("destinationList");
                }

                if (serviceType == SettingItem.ServiceType.CREATE) {
                    //추가된 데이터 리스트에 반영
                    SettingItem settingItem = new SettingItem(name, distance, destinationName, validity);
                    settingItemList.add(settingItem);
                    customAdapter.notifyDataSetChanged();
                    //추가 된 데이터 폰에 저장
                    editor.putString("name" + listSize, name)
                            .putString("distance" + listSize, distance)
                            .putString("destinationName" + listSize, destinationName)
                            .putString("destinationList" + listSize, destinationList)
                            .putString("validity" + listSize, validity)
                            .putInt("listSize", ++listSize);

                }
                if (serviceType == SettingItem.ServiceType.UPDATE) {
                    //변경 된 데이터 리스트에 반영
                    settingItemList.get(itemPosition).setSettingName(name);
                    settingItemList.get(itemPosition).setDistance(distance + "M");
                    settingItemList.get(itemPosition).setDestination(destinationName);
                    settingItemList.get(itemPosition).setValidity(validity);
                    customAdapter.notifyDataSetChanged();
                    //변경 된 데이터 폰에 저장
                    editor.putString("name" + itemPosition, name)
                            .putString("distance" + itemPosition, distance)
                            .putString("destinationName" + itemPosition, destinationName)
                            .putString("destinationList" + itemPosition, destinationList)
                            .putString("validity" + itemPosition, validity);

                }

            } else if (resultCode == RESULT_CANCELED) {
                if (serviceType == SettingItem.ServiceType.UPDATE) {
                    int deleteposition = data.getIntExtra("itemPosition", 0);
                    settingItemList.remove(deleteposition);
                    customAdapter.notifyDataSetChanged();
                    //저장 된 데이터 한 칸씩 앞으로
                    for (int i = deleteposition; i < listSize - 1; i++) {
                        editor.putString("name" + i, appData.getString("name" + (i + 1), ""))
                                .putString("distance" + i, appData.getString("distance" + (i + 1), ""))
                                .putString("destinationName" + i, appData.getString("destinationName" + (i + 1), ""))
                                .putString("destinationList" + i, appData.getString("destinationList" + (i + 1), ""))
                                .putString("validity" + i, appData.getString("validity" + (i + 1), ""));
                    }
                    //마지막 저장 된 데이터 지우기
                    editor.remove("name" + (listSize - 1))
                            .remove("distance" + (listSize - 1))
                            .remove("destinationName" + (listSize - 1))
                            .remove("destinationList" + (listSize - 1))
                            .remove("validity" + (listSize - 1))
                            .putInt("listSize", listSize - 1);
                }
            }
            editor.apply();
        }
    }

}

