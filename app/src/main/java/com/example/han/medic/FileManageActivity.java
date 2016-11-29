package com.example.han.medic;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by HAN on 2016-11-27.
 */

public class FileManageActivity extends Activity {

    private ScrollView layout;
    private TableLayout tlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initLayout();

        Cursor c = MainActivity.SQL.getAllFile();

        while (c.moveToNext()) {
            TableRow row = new TableRow(this);
            TextView id = new TextView(this);
            TextView date = new TextView(this);
            id.setText(c.getString(0));
            id.setTextSize(20);
            id.setGravity(Gravity.CENTER);
            date.setText(c.getString(1));
            date.setTextSize(20);
            date.setGravity(Gravity.CENTER);

            Log.d("File", "id:" + id.getText() + ",date:" + date.getText());

            row.setPadding(0, 20, 0, 20);
            row.addView(id);
            row.addView(date);

            row.setId(Integer.parseInt(id.getText().toString()));

            row.setOnClickListener(DetailClickListener);

            tlayout.addView(row);
        }

        setContentView(layout);
    }

    void initLayout() {
        layout = new ScrollView(this);
        tlayout = new TableLayout(this);

        TableLayout.LayoutParams lp = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(50, 0, 0, 0);

        tlayout.setStretchAllColumns(true);
        tlayout.setLayoutParams(lp);

        TableRow row = new TableRow(this);
        TextView column1 = new TextView(this);
        column1.setText("Index");
        column1.setTextSize(20);
        column1.setGravity(Gravity.CENTER);
        row.addView(column1);

        TextView column2 = new TextView(this);
        column2.setText("Date");
        column2.setTextSize(20);
        column2.setGravity(Gravity.CENTER);
        row.addView(column2);
        row.setPadding(0, 100, 0, 30);
        layout.addView(tlayout);
        tlayout.addView(row);
    }

    View.OnClickListener DetailClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String str = "id:" + v.getId();
            int id = v.getId();
            Log.d("Click", str);

            Intent intent = new Intent(FileManageActivity.this, FileDetailActivity.class);

            Bundle b = new Bundle();
            b.putInt("id", id);
            intent.putExtras(b);
            startActivityForResult(intent, 0);
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                this.finish();

            }
        }
    }
}
