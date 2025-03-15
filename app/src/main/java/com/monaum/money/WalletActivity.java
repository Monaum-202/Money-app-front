package com.monaum.money;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.monaum.money.dbUtill.Database;

public class WalletActivity extends AppCompatActivity {
    private TextView cashBalance, bankBalance, creditBalance, paypalBalance, otherBalance;
    private Database dbHelper;

    // Main Activity UI components
    DrawerLayout drawerLayout;
    ImageButton buttonDrawerToggle, btnAdd, btnExpense, btnTransfer, btnLoan, btnRefresh;
    NavigationView navigationView;
    private TextView tvBalance;


    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        // Initialize UI components
        cashBalance = findViewById(R.id.cashBalance);
        bankBalance = findViewById(R.id.bankBalance);
        creditBalance = findViewById(R.id.creditCardBalance);
        paypalBalance = findViewById(R.id.mobileBankingBalance);
        otherBalance = findViewById(R.id.otherBalance);
        drawerLayout = findViewById(R.id.drawerLayout);
        buttonDrawerToggle = findViewById(R.id.buttonDrawerToggle);
        navigationView = findViewById(R.id.navigationView);
        btnAdd = findViewById(R.id.btn_add);
        btnExpense = findViewById(R.id.btn_remove);
        btnTransfer = findViewById(R.id.btn_transfer);
        btnLoan = findViewById(R.id.btn_loan);
        btnRefresh = findViewById(R.id.btn_refresh);
        tvBalance = findViewById(R.id.tv_balance);
        dbHelper = new Database(this);


        database = new Database(this);

        updateBalance();

        // Initialize Database Helper
        dbHelper = new Database(this);

        // Update balances for each wallet
        updateWalletBalances();

        buttonDrawerToggle.setOnClickListener(v -> drawerLayout.open());

        View headerView = navigationView.getHeaderView(0);

        // Find the TextView by its ID from the header view
        TextView headerText = headerView.findViewById(R.id.h_name); // Corrected this line

        // Retrieve username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("myData", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("headerTextKey", "Guest");  // Default to "Guest" if not found

        // Set the username to headerText TextView
        headerText.setText(username);

        // Navigation Drawer Item Clicks
        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navHome) {
                startActivity( new Intent(WalletActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if (itemId == R.id.navWalletStatus) {
                Toast.makeText(WalletActivity.this, "Wallet Status clicked", Toast.LENGTH_SHORT).show();
            } else if (itemId == R.id.navIncomeStatus) {
                startActivity(new Intent(WalletActivity.this, IncomeChart.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if (itemId == R.id.navExpenseStatus) {
                startActivity(new Intent(WalletActivity.this, ExpenseChart.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if (itemId == R.id.navBudget) {
                startActivity(new Intent(WalletActivity.this, BarChartActivity2.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if (itemId == R.id.navPlan) {
                Toast.makeText(WalletActivity.this, "Plan clicked", Toast.LENGTH_SHORT).show();
            } else if (itemId == R.id.navHistory) {
                startActivity(new Intent(WalletActivity.this, History.class));  // Assume HomeActivity is your landing page
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
            else if (itemId == R.id.navLogout) {
                // Clear shared preferences to remove stored username
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear(); // Clears all saved data in "myData"
                editor.apply();

                // Redirect to Login Activity
                Intent intent = new Intent(WalletActivity.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
                startActivity(intent);
                finish(); // Close MainActivity
            }


            drawerLayout.close();
            return true;
        });

        // Button Listeners
        btnAdd.setOnClickListener(v -> startActivity(new Intent(WalletActivity.this, AddIncome.class)));
        btnExpense.setOnClickListener(v -> startActivity(new Intent(WalletActivity.this, AddExpence.class)));
        btnRefresh.setOnClickListener(v -> recreate());


    }


    private void updateWalletBalances() {
        cashBalance.setText(String.format("%.2f", getTotalBalanceForWallet("Cash")) + " taka");
        bankBalance.setText(String.format("%.2f", getTotalBalanceForWallet("Bank Transfer")) + " taka");
        creditBalance.setText(String.format("%.2f", getTotalBalanceForWallet("Credit Card")) + " taka");
        paypalBalance.setText(String.format("%.2f", getTotalBalanceForWallet("Paypal")) + " taka");
        otherBalance.setText(String.format("%.2f", getTotalBalanceForWallet("Other")) + " taka");
    }

    private double getTotalBalanceForWallet(String walletName) {
        double totalIncome = 0.0;
        double totalExpense = 0.0;
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = dbHelper.getReadableDatabase();

            // Get total income for the wallet
            cursor = db.rawQuery("SELECT IFNULL(SUM(amount), 0) FROM income WHERE wallet = ?", new String[]{walletName});
            if (cursor.moveToFirst()) {
                totalIncome = cursor.getDouble(0);
            }
            cursor.close(); // Close cursor before reusing

            // Get total expenses for the wallet
            cursor = db.rawQuery("SELECT IFNULL(SUM(amount), 0) FROM expence WHERE wallet = ?", new String[]{walletName});
            if (cursor.moveToFirst()) {
                totalExpense = cursor.getDouble(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close(); // Close database
        }

        // Calculate and return the current balance
        return totalIncome - totalExpense;
    }

    private void updateBalance() {
        double balance = dbHelper.getCurrentBalance();
        tvBalance.setText("Balance: " + String.format("%.2f", balance) + " taka");
    }

}
