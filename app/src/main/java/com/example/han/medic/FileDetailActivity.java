package com.example.han.medic;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class FileDetailActivity extends Activity {

    private int id;
    private String src;
    private ProgressBar spinner;
    private boolean start = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_detail_activity);

        Intent intent = this.getIntent();
        id = intent.getIntExtra("id", 0);
        src = MainActivity.SQL.getFileSrc(id);

        if (id == 0) {
            Log.d("FileDetail", "id is 0");
            Toast.makeText(getApplicationContext(), "ID값은 0이 될 수 없습니다." + id, Toast.LENGTH_LONG).show();
            finish();
        }

        findViewById(R.id.audioDelete).setOnClickListener(fileManageClickListener);
        findViewById(R.id.audioPlay).setOnClickListener(filePlayClickListener);

        getBodyInfo();
    }

    String getDate() {
        return MainActivity.SQL.getAudioRegisterDate(id);
    }

    String getText() {
        String text = MainActivity.SQL.getTranslateText(id);

        if (text == null) {
            String src = MainActivity.SQL.getFileSrc(id);
            Log.d("FileDetail", src);

            text = HttpRequest.postAudio(src);
            Log.d("text", text);

            MainActivity.SQL.insertTranslate(id, text);
        }

        return text;
    }


    Button.OnClickListener fileManageClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            MainActivity.SQL.deleteAudio(id);
            Toast.makeText(getApplicationContext(), "삭제되었습니다.", Toast.LENGTH_LONG).show();
            setResult(RESULT_OK, null);
            finish();
        }
    };


    Button.OnClickListener filePlayClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(src)), "audio/*");
            startActivity(intent);
        }
    };

    void getBodyInfo () {
        if (start) {
            new Thread(new Runnable() {
                public void run() {
                    TextView date = (TextView) findViewById(R.id.detailDate);
                    date.setText(getDate());

                    TextView text = (TextView) findViewById(R.id.detailText);
                    text.setText(getText());

                    start = false;
                }
            }).start();
        }

        while (start);

        spinner = (ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);
    }
}