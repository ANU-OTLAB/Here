package com.otlab.here.option.adapter;

// CustomAdapter 클래스

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.otlab.here.option.model.AlarmSettingItem;
import com.otlab.here.R;

import java.util.ArrayList;

public class AlarmSettingListViewAdapter extends BaseAdapter {
    LayoutInflater inflater;
    ArrayList<AlarmSettingItem> arrayList;
    int layout;

    //생성자
    public AlarmSettingListViewAdapter(Context context, int layout, ArrayList<AlarmSettingItem> arrayList) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout = layout;
        this.arrayList = arrayList;
    }

    // ListView 랑 연결될 arrayList 의 크기 측정
    public int getCount() {
        return arrayList.size();
    }

    // position(아이템의 위치값)을 이용해 아이템 정보 가져오는데 사용
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    // settinglistview_item이랑 매칭
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);

            TextView alarmNameTxt = convertView.findViewById(R.id.alarmName);
            alarmNameTxt.setText(arrayList.get(position).getAlarmName());

        }
        return convertView;
    }
}