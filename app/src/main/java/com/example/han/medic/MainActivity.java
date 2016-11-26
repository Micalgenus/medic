package com.example.han.medic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private MedicSQLite SQL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Button Listener 등록
        findViewById(R.id.recodingBtn).setOnClickListener(recodingClickListener);

        // Database Init
        SQL = new MedicSQLite(this, "medic.db", null, 1);
    }

    Button.OnClickListener recodingClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            Log.d("Recoding", "recodingClick");
            Intent intent = new Intent(MainActivity.this, RecodingActivity.class);
            startActivity(intent);
        }
    };
}
