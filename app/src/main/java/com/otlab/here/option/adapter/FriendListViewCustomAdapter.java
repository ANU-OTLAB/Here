package com.otlab.here.option.adapter;

// CustomAdapter 클래스

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.otlab.here.R;
import com.otlab.here.option.model.FriendItem;

import java.util.ArrayList;

public class FriendListViewCustomAdapter extends BaseAdapter {
    LayoutInflater inflater;
    ArrayList<FriendItem> arrayList;
    int layout;

    //생성자
    public FriendListViewCustomAdapter(Context context, int layout, ArrayList<FriendItem> arrayList) {
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
        }
        TextView friendId = convertView.findViewById(R.id.friendId);
        friendId.setText(arrayList.get(position).getUserId());

        TextView friendName = convertView.findViewById(R.id.friendName);
        friendName.setText(arrayList.get(position).getUserName());

        TextView friendValidity = convertView.findViewById(R.id.friendValidity);
        friendValidity.setText(arrayList.get(position).getValidity());

        return convertView;
    }

    // 아이템을 생성하는 함수 구현
    public void itemadd(String id, String name, String validity) {
        FriendItem fitem = new FriendItem(id, name, validity);
        arrayList.add(fitem);
        notifyDataSetChanged();
    }

    //  아이템을 지우는 함수 구현
    public void remove(int position) {
        arrayList.remove(position);
        notifyDataSetChanged();
    }
}