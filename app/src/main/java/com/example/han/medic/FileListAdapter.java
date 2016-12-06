package com.example.han.medic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 송재형 on 2016-12-02.
 */

public class FileListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<FileListItem> data;
    private int layout;

    public FileListAdapter(Context context, int layout, ArrayList<FileListItem> data){
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position).getId() ;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {
            view = inflater.inflate(layout, viewGroup, false);
        }

        FileListItem listviewitem = data.get(i);
        TextView id = (TextView) view.findViewById(R.id.fileId);
        TextView date = (TextView) view.findViewById(R.id.fileDate);
        id.setText(listviewitem.getId());
        date.setText(listviewitem.getDate());
        return view;
    }
}