package com.example.han.medic;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static MedicSQLite SQL;
    private static final int FILE_SELECT_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Button Listener 등록
        findViewById(R.id.recodingBtn).setOnClickListener(recodingClickListener);
        findViewById(R.id.fileManageBtn).setOnClickListener(fileManageClickListener);
        findViewById(R.id.searchBtn).setOnClickListener(searchClickListener);
        findViewById(R.id.registerBtn).setOnClickListener(registerClickListener);

        // Database Init
        SQL = new MedicSQLite(this, "medic.db", null, 1);
    }

    Button.OnClickListener recodingClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            Log.d("Main", "recodingClick");
            Intent intent = new Intent(MainActivity.this, RecodingActivity.class);
            startActivity(intent);
        }
    };

    Button.OnClickListener fileManageClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            Log.d("Main", "fileManageClick");
            Intent intent = new Intent(MainActivity.this, FileManageActivity.class);
            startActivity(intent);
        }
    };

    Button.OnClickListener searchClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            Log.d("Main", "searchClick");
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        }
    };

    Button.OnClickListener registerClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            showFileChooser();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    String str = data.getDataString();
                    Uri uri = data.getData();
                    String path = RealPathUtil.getRealPath(MainActivity.this, uri);

                    MainActivity.SQL.insertAudio(path);
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }
}
