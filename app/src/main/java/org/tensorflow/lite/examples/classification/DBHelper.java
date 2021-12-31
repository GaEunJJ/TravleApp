package org.tensorflow.lite.examples.classification;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "accountbook.db";

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 데이터 베이스가 생성될 때 호출
        db.execSQL("CREATE TABLE IF NOT EXISTS TodoList (id INTEGER PRIMARY KEY AUTOINCREMENT, writeDate TEXT NOT NULL, sort TEXT NOT NULL, user TEXT NOT NULL, ex_money TEXT NOT NULL, total REAL NOT NULL)");
        // TodoList는 테이블 명. AUTOINCREMENT는 자동으로 하나씩 증가해주는 것. 뒤에 쓰여진건 DB 구성요소(날짜, 종류, 구매내역, 환전금액)-> 테이블 내 컬럼.
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    // SELECT문 (소비 목록을 조회)
    public ArrayList<TodoItem> getTodoList(){
        ArrayList<TodoItem> todoItems = new ArrayList<>();

        SQLiteDatabase db  = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM TodoList ORDER BY writeDate DESC", null); // 내림차순 정렬
        if(cursor.getCount() != 0){         // 조회된 데이터가 있을 경우 내부 수행
            while(cursor.moveToNext()) {    // 마지막까지 조회되면 while문 탈출
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String writeDate = cursor.getString(cursor.getColumnIndex("writeDate"));
                String sort = cursor.getString(cursor.getColumnIndex("sort"));
                String user = cursor.getString(cursor.getColumnIndex("user"));
                String ex_money = cursor.getString(cursor.getColumnIndex("ex_money"));
                Double total = cursor.getDouble(cursor.getColumnIndex("total"));

                TodoItem todoItem = new TodoItem();
                todoItem.setId(id);
                todoItem.setWriteDate(writeDate);
                todoItem.setSort(sort);
                todoItem.setUser(user);
                todoItem.setEx_money(ex_money);
                todoItem.setTotal(total);
                todoItems.add(todoItem);

            }
        }
        cursor.close();

        return todoItems;
    }


    //INSERT문 (소비 목록을 DB에 넣는다). id는 자동으로 입력되므로 안썼음.
    public void InsertTodo(String _writeDate, String _sort, String _user, String _ex_money, Double _total){
        SQLiteDatabase db = getWritableDatabase();  // 쓰기가 가능한
        db.execSQL("INSERT INTO TodoList(writeDate, sort, user, ex_money, total) VALUES('" + _writeDate + "','" + _sort + "', '" + _user + "', '" + _ex_money + "', '" + _total + "');");
    }

    // UPDATE 문 (소비 목록을 수정한다)
    public void UpdateTodo(String _writeDate, String _sort, String _user, String _ex_money, Double _total, String _beforeDate){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE TodoList SET writeDate ='" + _writeDate + "', sort ='" + _sort + "', user ='" + _user + "', ex_money ='" + _ex_money + "', total ='" + _total + "' WHERE writeDate ='" + _beforeDate + "'"); //where 절은 이전에 받은 날짜와
    }

    // DELETE 문 (소비 목록을 제거한다)
    public void  deleteTodo(String _beforeDate){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM TodoList WHERE writeDate = '" + _beforeDate + "'");

    }

}