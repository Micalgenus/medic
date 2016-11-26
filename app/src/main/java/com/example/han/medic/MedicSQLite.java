package com.example.han.medic;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by HAN on 2016-11-26.
 */

public class MedicSQLite extends SQLiteOpenHelper {

    private static final String audioTable = "audio";
    private static final String LogTag = "SQLite";

    private SQLiteDatabase DB;

    public MedicSQLite(MainActivity context, String name, SQLiteDatabase.CursorFactory factory, int version) {
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
        String sql = "create table " + audioTable + "(id integer primary key autoincrement, file varchar(512));";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "drop table " + audioTable; // 테이블 드랍
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase); // 다시 테이블 생성
    }
}
