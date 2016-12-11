package com.example.han.medic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<SearchListItem> data;
    private int layout;

    public SearchListAdapter(Context context, int layout, ArrayList<SearchListItem> data){
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i).getText();
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

        SearchListItem listviewitem = data.get(i);
        TextView name = (TextView) view.findViewById(R.id.searchListText);
        name.setText(listviewitem.getText());
        return view;
    }
}
