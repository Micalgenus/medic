package com.example.han.medic;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by HAN on 2016-11-26.
 */
public class RecodingActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recoding_activity);

        Button terminate = (Button)findViewById(R.id.terminateActivity);
        terminate.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                finish();
            }
        });

    }
}
