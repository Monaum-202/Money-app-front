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
                    startActivity(new Intent(MainActivity.this, ExpenseChart.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else if (itemId == R.id.navBudget) {
                    Toast.makeText(MainActivity.this, "Budget clicked", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.navPlan) {
                    Toast.makeText(MainActivity.this, "Plan clicked", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.navHistory) {
                    startActivity(new Intent(MainActivity.this, History.class));  // Assume HomeActivity is your landing page
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else if (itemId == R.id.navEHistory) {
                    startActivity(new Intent(MainActivity.this, ExpenseHistory.class));  // Assume HomeActivity is your landing page
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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


    private void updateBalance() {
        double balance = dbHelper.getCurrentBalance();
        tvBalance.setText("Balance: " + String.format("%.2f", balance)+"taka");
    }

    private void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
