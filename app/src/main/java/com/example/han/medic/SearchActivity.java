package com.example.han.medic;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends Activity {

    private ListView listView;
    private EditText keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        listView = (ListView) findViewById(R.id.searchList);

        findViewById(R.id.search).setOnClickListener(searchClickListener);
    }

    Button.OnClickListener searchClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            try {
                keyword = (EditText) findViewById(R.id.search_keyword);
                ArrayList<SearchListItem> data = new ArrayList<>();
                String request = HttpRequest.searchKeyword(keyword.getText().toString());
                JSONArray list = new JSONArray(request);

                if (list.length() == 0) {
                    Toast.makeText(getApplicationContext(), "검색 결과가 존재하지 않습니다.", Toast.LENGTH_LONG).show();
                }

                for (int i = 0; i < list.length(); i++) {
                    String str = list.getString(i);
                    SearchListItem listItem = new SearchListItem(str);
                    data.add(listItem);
                }

                SearchListAdapter adapter = new SearchListAdapter(SearchActivity.this, R.layout.search_list_item, data);
                listView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}
