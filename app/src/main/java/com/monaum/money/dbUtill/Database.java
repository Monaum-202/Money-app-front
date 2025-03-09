package com.monaum.money.dbUtill;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.monaum.money.entity.AddExpence1;
import com.monaum.money.entity.AddIncome1;
import com.monaum.money.entity.Transaction;
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
        String userQuery = "CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, email TEXT, pass TEXT, cpass TEXT, dob TEXT)";

        String incomeQuery = "CREATE TABLE income (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "amount REAL NOT NULL, category TEXT NOT NULL, wallet TEXT NOT NULL, " +
                "notes TEXT, date TEXT NOT NULL, time TEXT NOT NULL)";

        String expenseQuery = "CREATE TABLE expence (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "amount REAL NOT NULL, category TEXT NOT NULL, wallet TEXT NOT NULL, " +
                "notes TEXT, date TEXT NOT NULL, time TEXT NOT NULL)";

        db.execSQL(userQuery);
        db.execSQL(incomeQuery);
        db.execSQL(expenseQuery);
    }

    // Generic Insert Method for Users, Income, and Expense
    private long insertData(String tableName, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(tableName, null, values);
    }

    // Insert User
    public long insertUser(Users user) {
        ContentValues values = new ContentValues();
        values.put("name", user.username);
        values.put("email", user.email);
        values.put("pass", user.password);
        values.put("dob", user.dob);
        return insertData("users", values);  // Reuse insertData method
    }

    // Insert Income
    public long insertIncome(AddIncome1 income) {
        ContentValues values = new ContentValues();
        values.put("amount", income.getAmount());
        values.put("category", income.getCategory());
        values.put("wallet", income.getWallet());
        values.put("notes", income.getNotes());
        values.put("date", income.getDate());
        values.put("time", income.getTime());
        return insertData("income", values);  // Reuse insertData method
    }

    // Insert Expense
    public long insertExpence(AddExpence1 expense) {
        ContentValues values = new ContentValues();
        values.put("amount", expense.getAmount());
        values.put("category", expense.getCategory());
        values.put("wallet", expense.getWallet());
        values.put("notes", expense.getNotes());
        values.put("date", expense.getDate());
        values.put("time", expense.getTime());
        return insertData("expence", values);  // Reuse insertData method
    }

    // Get User By ID
    public Users getUserByUserID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query("users", null, "id = ?", new String[]{String.valueOf(id)},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                return new Users(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)
                );
            }
        } catch (Exception e) {
            e.printStackTrace();  // Handle the exception properly (logging, etc.)
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;  // Return null if not found
    }

    // Get All Users
    public List<Users> getAllUsers() {
        List<Users> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM users", null);
            while (cursor != null && cursor.moveToNext()) {
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
        } catch (Exception e) {
            e.printStackTrace();  // Log the error properly
        } finally {
            if (cursor != null) cursor.close();
        }
        return userList;
    }

    // Update User
    public int updateUser(Users user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", user.username);
        values.put("email", user.email);
        values.put("pass", user.password);
        values.put("dob", user.dob);

        return db.update("users", values, "id = ?", new String[]{String.valueOf(user.id)});
    }

    // Delete User by ID
    public void deleteUser(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("users", "id = ?", new String[]{String.valueOf(id)});
    }

    // Login User (Returns 1 if valid, 0 if invalid)
    public int loginUser(String name, String password) {
        String[] arr = new String[]{name, password};
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM users WHERE name=? AND pass=?", arr);
        if (c.moveToFirst()) {
            return 1;  // Successful login
        }
        return 0;  // Invalid credentials
    }

    // Get All Income Records
    public List<AddIncome1> getAllIncome() {
        List<AddIncome1> incomeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM income", null);
            while (cursor != null && cursor.moveToNext()) {
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            db.close(); // Close DB properly
        }
        return incomeList;
    }

    // Get Income By ID
    public AddIncome1 getIncomeById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query("income", null, "id = ?", new String[]{String.valueOf(id)},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                return new AddIncome1(
                        cursor.getLong(0),
                        cursor.getDouble(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6)
                );
            }
        } catch (Exception e) {
            e.printStackTrace();  // Log the error properly
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }

    // Update Income
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

    // Delete Income by ID
    public void deleteIncome(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("income", "id = ?", new String[]{String.valueOf(id)});
    }

    // Get All Expense Records
    public List<AddExpence1> getAllExpence() {
        List<AddExpence1> expenceList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM expence", null);
            while (cursor != null && cursor.moveToNext()) {
                AddExpence1 expence = new AddExpence1(
                        cursor.getLong(0),
                        cursor.getDouble(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6)
                );
                expenceList.add(expence);
            }
        } catch (Exception e) {
            e.printStackTrace();  // Log the error properly
        } finally {
            if (cursor != null) cursor.close();
        }
        return expenceList;
    }


    private List<AddExpence1> getExpenses(Context context, String month) {
        List<AddExpence1> expensesList = new ArrayList<>();
        SQLiteDatabase db = new Database(context).getReadableDatabase();

        // Filter by month using SQLite query
        Cursor cursor = db.rawQuery("SELECT * FROM expence WHERE strftime('%m', date) = ?", new String[]{month});

        if (cursor.moveToFirst()) {
            do {
                AddExpence1 expense = new AddExpence1();
                expense.setId(cursor.getLong(cursor.getColumnIndexOrThrow("id")));
                expense.setAmount(cursor.getDouble(cursor.getColumnIndexOrThrow("amount")));
                expense.setCategory(cursor.getString(cursor.getColumnIndexOrThrow("category")));
                expense.setWallet(cursor.getString(cursor.getColumnIndexOrThrow("wallet")));
                expense.setNotes(cursor.getString(cursor.getColumnIndexOrThrow("notes")));
                expense.setDate(cursor.getString(cursor.getColumnIndexOrThrow("date")));
                expense.setTime(cursor.getString(cursor.getColumnIndexOrThrow("time")));

                expensesList.add(expense);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return expensesList;
    }

    // Get Expense By ID
    public AddExpence1 getExpenceById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query("expence", null, "id = ?", new String[]{String.valueOf(id)},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                return new AddExpence1(
                        cursor.getLong(0),
                        cursor.getDouble(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6)
                );
            }
        } catch (Exception e) {
            e.printStackTrace();  // Log the error properly
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }

    // Update Expense
    public int updateExpence(AddExpence1 expence) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("amount", expence.getAmount());
        values.put("category", expence.getCategory());
        values.put("wallet", expence.getWallet());
        values.put("notes", expence.getNotes());
        values.put("date", expence.getDate());
        values.put("time", expence.getTime());

        return db.update("expence", values, "id = ?", new String[]{String.valueOf(expence.getId())});
    }

    // Delete Expense by ID
    public void deleteExpence(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("expence", "id = ?", new String[]{String.valueOf(id)});
    }

    // Get Total Income
    public double getTotalIncome() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(amount) FROM income", null);
        double totalIncome = 0;
        if (cursor.moveToFirst()) {
            totalIncome = cursor.getDouble(0);
        }
        cursor.close();
        return totalIncome;
    }

    // Get Total Expense
    public double getTotalExpense() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(amount) FROM expence", null);
        double totalExpense = 0;
        if (cursor.moveToFirst()) {
            totalExpense = cursor.getDouble(0);
        }
        cursor.close();
        return totalExpense;
    }

    // Get Current Balance (Income - Expense)
    public double getCurrentBalance() {
        return getTotalIncome() - getTotalExpense();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle schema changes (if required)
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS users");
            db.execSQL("DROP TABLE IF EXISTS income");
            db.execSQL("DROP TABLE IF EXISTS expence");
            onCreate(db);  // Recreate the tables
        }
    }



    public List<Transaction> getAllTransactions() {
        List<Transaction> transactionList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT id, amount, category, wallet, date, time, 'income' AS type FROM income " +
                    "UNION ALL " +
                    "SELECT id, amount, category, wallet, date, time, 'expence' AS type FROM expence " +
                    "ORDER BY time DESC, date DESC";

            cursor = db.rawQuery(query, null);
            while (cursor != null && cursor.moveToNext()) {
                Transaction transaction = new Transaction(
                        cursor.getLong(0),  // ID
                        cursor.getDouble(1), // Amount
                        cursor.getString(2), // Category
                        cursor.getString(3), // Wallet
                        cursor.getString(4), // Date
                        cursor.getString(5),  // Type (income/expense)
                        cursor.getString(6)
                );
                transactionList.add(transaction);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            db.close(); // Close DB properly
        }
        return transactionList;
    }




    public double getTotalIncomeForMonthYear(String month, String year) {
        double totalIncome = 0.0;

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(amount) FROM income WHERE strftime('%m', date) = ? AND strftime('%Y', date) = ?";
        Cursor cursor = db.rawQuery(query, new String[]{month, year});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                totalIncome = cursor.getDouble(0); // Get the total income sum
            }
            cursor.close();
        }

        return totalIncome;
    }


}
