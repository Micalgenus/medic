package com.example.han.medic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

/**
 * Created by HAN on 2016-11-28.
 */

public class FileDetailActivity extends Activity {

    private int id;
    private String response = "";
    private boolean endResponse = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_detail_activity);

        Intent intent = this.getIntent();
        id = intent.getIntExtra("id", 0);

        if (id == 0) {
            Log.d("FileDetail", "id is 0");
            Toast.makeText(getApplicationContext(), "ID값은 0이 될 수 없습니다." + id, Toast.LENGTH_LONG).show();
            finish();
        }

        TextView date = (TextView) findViewById(R.id.detailDate);
        date.setText(getDate());

        TextView text = (TextView) findViewById(R.id.detailText);
        text.setText(getText());
    }

    String getDate() {
        return MainActivity.SQL.getAudioRegisterDate(id);
    }

    String getText() {
        String text = MainActivity.SQL.getTranslateText(id);

        if (text == null) {
            String src = MainActivity.SQL.getFileSrc(id);
            Log.d("FileDetail", src);

            text = postAudio(src);
            Log.d("text", text);

            MainActivity.SQL.InsertTranslate(id, text);
        }

        return text;
    }

    String postAudio(String src) {
        final String file = src;

        new Thread(new Runnable() {
            public void run() {
                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                DataInputStream inStream = null;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary =  "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1024 * 1024;
                String responseFromServer = "";
                String urlString = "https://node.micalgenus.ml/speech";

                try {
                    //------------------ CLIENT REQUEST
                    FileInputStream fileInputStream = new FileInputStream(new File(file));
                    // open a URL connection to the Servlet
                    URL url = new URL(urlString);
                    // Open a HTTP connection to the URL
                    conn = (HttpURLConnection) url.openConnection();
                    // Allow Inputs
                    conn.setDoInput(true);
                    // Allow Outputs
                    conn.setDoOutput(true);
                    // Don't use a cached copy.
                    conn.setUseCaches(false);
                    // Use a post method.
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    dos = new DataOutputStream( conn.getOutputStream() );
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"audio\";filename=\\" + file + "\"" + lineEnd);
                    dos.writeBytes(lineEnd);
                    // create a buffer of maximum size
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];
                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    while (bytesRead > 0) {
                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }
                    // send multipart form data necesssary after file data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                    // close streams
                    Log.d("Debug", "File is written");
                    fileInputStream.close();
                    dos.flush();
                    dos.close();
                }
                catch (MalformedURLException ex) {
                    Log.e("Debug", "error: " + ex.getMessage(), ex);
                } catch (IOException ioe) {
                    Log.e("Debug", "error: " + ioe.getMessage(), ioe);
                }
                //------------------ read the SERVER RESPONSE
                try {

                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    //inStream = new DataInputStream ( conn.getInputStream() );
                    String str;
                    endResponse = false;

                    while (( str = br.readLine()) != null) {
                        response += str;
                        Log.d("Debug", "Server Response " + str);
                    }

                    endResponse = true;
                    br.close();

                }
                catch (IOException ioex){
                    Log.e("Debug", "error: " + ioex.getMessage(), ioex);
                }
            }
        }).start();

        while (!endResponse) ;

        return response;
    }
}
