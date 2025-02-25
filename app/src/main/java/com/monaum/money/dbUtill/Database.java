package com.monaum.money.dbUtill;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import com.monaum.money.entity.AddIncome1;
import com.monaum.money.entity.Users;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    private static final String DB_NAME = "myDB";

    private static final int DB_VERSION = 1;

    public Database(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {


        String userQuery = " create table users( id integer PRIMARY KEY AUTOINCREMENT," +
                " name text, email text, pass text,cpass text, dob text)";


        String incomeQuery = "CREATE TABLE income ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "amount REAL NOT NULL, " +
                "category TEXT NOT NULL, " +
                "wallet TEXT NOT NULL, " +
                "notes TEXT, " +
                "date TEXT NOT NULL, " +
                "time TEXT NOT NULL )";



        db.execSQL(userQuery);
        db.execSQL(incomeQuery);
    }

    public long insertUser(Users user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NAME", user.username);
        values.put("EMAIL", user.email);
        values.put("PASS", user.password);
        values.put("DOB", user.dob);

        return db.insert("USERS", null, values);
    }



    // Get a user by username
    public Users getUserByUserID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("USERS", null, "id = ?", new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            @SuppressLint("Range")
            Users user = new Users(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("email")),
                    cursor.getString(cursor.getColumnIndex("PASS")),
                    cursor.getString(cursor.getColumnIndex("cpass")),
                    cursor.getString(cursor.getColumnIndex("DOB"))
            );
            cursor.close();
            return user;
        } else {
            return null; // user not found
        }
    }

    // Get all users from the database
    public ArrayList<Users> getAllUsers() {
        ArrayList<Users> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.query("USERS", null, null, null, null, null, null);
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("select * from USERS ", null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Users user = new Users(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)
                );
                userList.add(user);
            }
            cursor.close();
        }

        return userList;
    }

    // Update user details
    public int updateUser(Users user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", user.username);
        values.put("email", user.email);
        values.put("pass", user.password);
        values.put("dob", user.dob);

        return db.update("USERS", values, "id" + " = ?", new String[]{String.valueOf(user.id)});
    }

    // Delete user by username
    public void deleteUser(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("USERS", "id" + " = ?", new String[]{String.valueOf(id)});
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


    // Create - Insert Income
    public long insertIncome(AddIncome1 income) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("amount", income.getAmount());
        values.put("category", income.getCategory());
        values.put("wallet", income.getWallet());
        values.put("notes", income.getNotes());
        values.put("date", income.getDate());
        values.put("time", income.getTime());

        return db.insert("income", null, values);
    }

    // Read - Get Income by ID
    public AddIncome1 getIncomeById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("income", null, "id = ?", new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            AddIncome1 income = new AddIncome1(
                    cursor.getLong(0),
                    cursor.getDouble(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6)
            );
            cursor.close();
            return income;
        }
        return null; // Return null if no income found
    }

    // Read - Get All Income Records
    public List<AddIncome1> getAllIncome() {
        List<AddIncome1> incomeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM income", null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                AddIncome1 income = new AddIncome1(
                        cursor.getLong(0),
                        cursor.getDouble(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6)
                );
                incomeList.add(income);
            }
            cursor.close();
        }
        return incomeList;
    }

    // Update - Update Income
    public int updateIncome(AddIncome1 income) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("amount", income.getAmount());
        values.put("category", income.getCategory());
        values.put("wallet", income.getWallet());
        values.put("notes", income.getNotes());
        values.put("date", income.getDate());
        values.put("time", income.getTime());

        return db.update("income", values, "id = ?", new String[]{String.valueOf(income.getId())});
    }

    // Delete - Delete Income
    public void deleteIncome(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("income", "id = ?", new String[]{String.valueOf(id)});
    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
