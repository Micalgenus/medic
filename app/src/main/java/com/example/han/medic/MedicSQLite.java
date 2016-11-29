package com.example.han.medic;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by HAN on 2016-11-26.
 */

public class MedicSQLite extends SQLiteOpenHelper {

    private final String audioTable = "audio";
    private final String translateTable = "translate";
    private final String LogTag = "SQLite";

    private SQLiteDatabase DB;

    MedicSQLite(MainActivity context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

        try {
            DB = this.getWritableDatabase(); // 읽고 쓸수 있는 DB
            Log.d(LogTag, "데이터베이스 생성");
        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.e(LogTag, "데이터베이스를 얻어올 수 없음");
            context.finish(); // 액티비티 종료
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table " + audioTable + "(`id` integer primary key autoincrement, `src` varchar(128), `date` datetime);";
        sqLiteDatabase.execSQL(sql);

        sql = "create table " + translateTable + "(`id` integer primary key autoincrement, `file_id` integer not null, `text` text, foreign key(`file_id`) references " + audioTable + "(`id`));";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "drop table " + audioTable; // 테이블 드랍
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase); // 다시 테이블 생성
    }

    void insertAudio(String src) {
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        DB.execSQL("INSERT INTO " + audioTable + "(src, date) values (\"" + src + "\", \"" + currentDateTimeString + "\");");
    }

    void insertTranslate(int id, String text) {
        DB.execSQL("INSERT INTO " + translateTable + "(file_id, text) values (\'" + id + "\', \'" + text + "\');");

    }

    void deleteAudio(int id) {
        DB.execSQL("DELETE FROM " + translateTable + " WHERE `file_id` = " + id);
        DB.execSQL("DELETE FROM " + audioTable + " WHERE `id` = " + id);
    }

    String getAudioRegisterDate(int id) {
        String str = "select `date` from " + audioTable + " where `id` = " + id + ";";
        Cursor c = DB.rawQuery(str, null);

        if (c.moveToNext()) {
            String date = c.getString(0);
            Log.d(LogTag,"date:" + date);
            return date;
        } else {
            return null;
        }
    }

    String getTranslateText(int file_id) {
        Cursor c = DB.rawQuery("select `text` from " + translateTable + " where `file_id` = " + file_id + ";", null);

        if (c.moveToNext()) {
            String text = c.getString(0);
            Log.d(LogTag, "text:" + text);
            return text;
        } else {
            return null;
        }
    }

    String getFileSrc(int file_id) {
        Cursor c = DB.rawQuery("select `src` from " + audioTable + " where id = " + file_id + ";", null);

        if (c.moveToNext()) {
            String src = c.getString(0);
            return src;
        } else {
            return null;
        }
    }

     Cursor getAllFile() {
        Cursor c = DB.rawQuery("select `id`, `date` from " + audioTable + ";", null);
        return c;
    }
}
