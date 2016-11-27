package com.example.han.medic;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Created by HAN on 2016-11-27.
 */

public class FileManageActivity extends Activity {

    private TableLayout layout;

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
            id.setPadding(20, 20, 20, 20);
            date.setText(c.getString(1));
            date.setPadding(20, 20, 20, 20);

            Log.d("File", "id:" + id.getText() + ",date:" + date.getText());

            row.addView(id);
            row.addView(date);

            row.setId(Integer.parseInt(id.getText().toString()));

            row.setOnClickListener(DetailClickListener);

            layout.addView(row);
        }

        setContentView(layout);
    }

    void initLayout() {
        layout = new TableLayout(this);
        layout.setStretchAllColumns(true);

        TableRow row = new TableRow(this);
        TextView column1 = new TextView(this);
        column1.setText("Index");
        row.addView(column1);

        TextView column2 = new TextView(this);
        column2.setText("Date");
        row.addView(column2);
        layout.addView(row);
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

            startActivity(intent);
        }
    };

}
