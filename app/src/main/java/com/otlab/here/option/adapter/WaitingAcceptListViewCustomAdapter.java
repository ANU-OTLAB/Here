package com.otlab.here.option.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.otlab.here.R;
import com.otlab.here.option.model.WaitingAcceptItem;

import java.util.ArrayList;

public class WaitingAcceptListViewCustomAdapter extends BaseAdapter {

    LayoutInflater inflater;
    ArrayList<WaitingAcceptItem> arrayList;
    int layout;

    public WaitingAcceptListViewCustomAdapter(Context context, int layout, ArrayList<WaitingAcceptItem> arrayList) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout = layout;
        this.arrayList = arrayList;
    }

    public int getCount() {
        return arrayList.size();
    }

    public Object getItem(int position) {
        return arrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }
        TextView friendName = (TextView)convertView.findViewById(R.id.friendName);
        friendName.setText(arrayList.get(position).getFriendName());

        TextView time = (TextView)convertView.findViewById(R.id.time);
        time.setText(arrayList.get(position).getTime());

        TextView validity = (TextView)convertView.findViewById(R.id.validity);
        validity.setText(arrayList.get(position).getValidity());

        return convertView;
    }
}
