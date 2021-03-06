package com.hhj73.pic.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.hhj73.pic.Objects.Picture;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private Context context;

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer sb = new StringBuffer();
        sb.append(" CREATE TABLE DATA ( ");
        sb.append(" _ID INTEGER PRIMARY KEY AUTOINCREMENT, "); // 번호
        sb.append(" PATH TEXT, "); // 이미지 경로
        sb.append(" CONTENTS TEXT, "); // 추출한 텍스트
        sb.append(" DATE TEXT, "); // 저장 날짜
        sb.append(" CATEGORY INTEGER, "); // 카테고리
        sb.append(" KEYWORD TEXT ) "); // 키워드

        // SQLite Database로 쿼리 실행
        db.execSQL(sb.toString());

        // 카테고리로 가져오는 인덱스
        db.execSQL("CREATE INDEX IF NOT EXISTS DATA_CATEGORY ON DATA(_ID, PATH, CONTENTS, DATE, CATEGORY)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /*
        0: _id (INTEGER)
        1: path (TEXT)
        2: contents (TEXT)
        3: date (TEXT) yyyy-MM-dd hh:mm:ss
        4: category (INTEGER)
        5: keyword (TEXT)
     */

    public void insertData(Picture picture) {
        // Picture 데이터 삽입
        SQLiteDatabase db = getWritableDatabase();

        StringBuffer sb = new StringBuffer();
        sb.append(" INSERT INTO DATA ( ");
        sb.append(" PATH, CONTENTS, DATE, CATEGORY, KEYWORD ) ");
        sb.append(" VALUES ( ?, ?, ?, ?, ? ) ");

        db.execSQL(sb.toString(),
                new Object[] {
                        picture.getPath(),
                        picture.getContents(),
                        picture.getDate(),
                        picture.getCategory(),
                        picture.getKeyword()
                });
    }

    public List getAllData() {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT * FROM DATA ");

        // 읽기 전용 DB 객체를 만든다.
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sb.toString(), null);
        List allData = new ArrayList();

        // moveToNext 다음에 데이터가 있으면 true 없으면 false
        while( cursor.moveToNext() ) {
            Picture picture = new Picture();
            picture.setPath(cursor.getString(1));
            picture.setContents(cursor.getString(2));
            picture.setDate(cursor.getString(3));
            picture.setCategory(cursor.getInt(4));
            picture.setKeyword(cursor.getString(5));
            allData.add(picture);
        }

        cursor.close();
        return allData;
    }

    public List getCategoryData(int num) { // 해당 카테고리 가져오기
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT * FROM DATA WHERE CATEGORY = ? ");

        // 읽기 전용 DB 객체를 만든다.
        SQLiteDatabase db = getReadableDatabase();
        String[] params = {String.valueOf(num)};
        Cursor cursor = db.rawQuery(sb.toString(), params);
        List data = new ArrayList();

        // moveToNext 다음에 데이터가 있으면 true 없으면 false
        while( cursor.moveToNext() ) {
            Picture picture = new Picture();
            picture.setPath(cursor.getString(1));
            picture.setContents(cursor.getString(2));
            picture.setDate(cursor.getString(3));
            picture.setCategory(cursor.getInt(4));
            picture.setKeyword(cursor.getString(5));
            data.add(picture);
        }

        cursor.close();
        return data;
    }

    public List getSearchingData(String input) {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT * FROM DATA WHERE CONTENTS LIKE ?");

        // 읽기 전용 DB 객체를 만든다.
        SQLiteDatabase db = getReadableDatabase();
        String[] params = {"%" + input + "%"};
        Cursor cursor = db.rawQuery(sb.toString(), params);
        List data = new ArrayList();

        // moveToNext 다음에 데이터가 있으면 true 없으면 false
        while( cursor.moveToNext() ) {
            Picture picture = new Picture();
            picture.setPath(cursor.getString(1));
            picture.setContents(cursor.getString(2));
            picture.setDate(cursor.getString(3));
            picture.setCategory(cursor.getInt(4));
            picture.setKeyword(cursor.getString(5));
            data.add(picture);
        }

        cursor.close();
        return data;
    }

}
