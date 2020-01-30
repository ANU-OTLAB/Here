package com.otlab.here.setting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.otlab.here.R;
import com.otlab.here.setting.model.SettingItem;

import java.util.ArrayList;

public class ListViewCustomAdapter extends BaseAdapter {

    LayoutInflater inflater;
    ArrayList<SettingItem> arrayList;
    int layout;

    public ListViewCustomAdapter(Context context, int layout, ArrayList<SettingItem> arrayList) {
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

        TextView NameText = convertView.findViewById(R.id.Name);
        NameText.setText(arrayList.get(position).getSettingName());

        TextView DistanceText = convertView.findViewById(R.id.Distance);
        DistanceText.setText(arrayList.get(position).getDistance());

        TextView DestinationText = convertView.findViewById(R.id.Destination);
        DestinationText.setText(arrayList.get(position).getDestination());

        TextView TimeText = convertView.findViewById(R.id.Time);
        TimeText.setText(arrayList.get(position).getValidity());

        return convertView;
    }
}


