package com.example.han.medic;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class HttpRequest {
    private static String response = "";
    private static boolean endResponse = false;

    private static String result;
    private static String keyword;

    static String postAudio(String src) {
        final String file = src;

        response = "";
        endResponse = false;

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
                String urlString = "https://node.vrmedic.ml/speech";

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


    static String searchKeyword(String key) {
        result = "";
        keyword = key;
        new Thread(new Runnable() {
            public void run() {
                try {
                    URL url = null;
                    String sendUrl = "https://node.vrmedic.ml/search/" + keyword;
                    url = new URL(new String(sendUrl.getBytes("euc-kr"), "euc-kr"));

                    URLConnection con = null;
                    con = url.openConnection();
                    String encoding = con.getContentEncoding();
                    if (encoding == null) {
                        encoding = "UTF-8";
                    }
                    BufferedReader r = new BufferedReader(new InputStreamReader(con.getInputStream(), encoding));
                    StringBuilder sb = new StringBuilder();
                    try {
                        String s;
                        while ((s = r.readLine()) != null) {
                            sb.append(s);
                            sb.append("\n");
                        }
                    } finally {
                        r.close();
                    }
                    Log.d("test", sb.toString());
                    result = sb.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        while (result == "") ;

        return result;
    }
}
