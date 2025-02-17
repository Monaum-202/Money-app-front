package com.monaum.money.dbUtill;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

//    public Database(@Nullable Context context, @Nullable String name, int version, @NonNull SQLiteDatabase.OpenParams openParams) {
//        super(context, name, version, openParams);
//    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        String userQuery = " create table users( id integer PRIMARY KEY AUTOINCREMENT," +
                " name text, email text, pass text,cpass text, dob text)";
        db.execSQL(userQuery);
    }

    public void addUser(String name,String email, String pass,String cpass,String dob){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("NAME",name);
        values.put("EMAIL",email);
        values.put("PASS",pass);
        values.put("Cpass",cpass);
        values.put("DOB",dob);

        db.insert("USERS",null,values);
        db.close();
    }

    public int loginUser(String email, String password){
        String[] arr = new String[2];
        arr[0] = email;
        arr[1] = password;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(" select * from users where email=? and pass=? ", arr);
        if (c.moveToFirst()){
            return 1;
        }
        return 0;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
