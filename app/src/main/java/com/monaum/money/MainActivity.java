package com.monaum.money;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.monaum.money.dbUtill.Database;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageButton buttonDrawerToggle, btnAdd, btnExpense, btnTransfer, btnLoan, btnRefresh;
    NavigationView navigationView;

    private Database dbHelper;
    private TextView tvBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

//        // Check if activity needs to be refreshed
//        if (shouldRefresh()) {
//            restartActivity();
//            return; // Stop further execution to avoid duplicate setup
//        }

        setContentView(R.layout.activity_main);

        // Initialize UI components
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

        updateBalance();

        // Open drawer on button click
        buttonDrawerToggle.setOnClickListener(v -> drawerLayout.open());

        // Navigation Drawer Item Clicks
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.navHome) {
                    Toast.makeText(MainActivity.this, "Home clicked", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.navWalletStatus) {
                    Toast.makeText(MainActivity.this, "Wallet Status clicked", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.navIncomeStatus) {
                    Toast.makeText(MainActivity.this, "Income Status clicked", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.navExpenseStatus) {
                    Toast.makeText(MainActivity.this, "Expense Status clicked", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.navBudget) {
                    Toast.makeText(MainActivity.this, "Budget clicked", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.navPlan) {
                    Toast.makeText(MainActivity.this, "Plan clicked", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.navHistory) {
                    Toast.makeText(MainActivity.this, "History clicked", Toast.LENGTH_SHORT).show();
                }

                drawerLayout.close();
                return true;
            }
        });
        btnAdd.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AddIncome.class)));
        btnExpense.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AddExpence.class)));

        // Refresh button click
        btnRefresh.setOnClickListener(v -> recreate());
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        // Clear the refresh flag to prevent unintended restarts
//        getIntent().removeExtra("REFRESH");
//    }
//
//    // Check if the activity needs to be restarted
//    private boolean shouldRefresh() {
//        return getIntent().getBooleanExtra("REFRESH", false) == false;
//    }
//
//    // Restart the activity and set a flag to prevent infinite loops
//    private void restartActivity() {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra("REFRESH", true);
//        startActivity(intent);
//        finish();
//    }

    private void updateBalance() {
        double balance = dbHelper.getCurrentBalance();
        tvBalance.setText("Balance: " + String.format("%.2f", balance)+"taka");
    }

    private void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
