package com.monaum.money;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {


    DrawerLayout drawerLayout;
    ImageButton buttonDrawerToggle, btnAdd, btnExpense, btnTransfer, btnLoan,btnRefresh; // Added btnAdd here
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayout);
        buttonDrawerToggle = findViewById(R.id.buttonDrawerToggle);
        navigationView = findViewById(R.id.navigationView);
        btnAdd = findViewById(R.id.btn_add); // Initialize Add Button
        btnExpense= findViewById(R.id.btn_remove);
        btnTransfer = findViewById(R.id.btn_transfer);
        btnLoan = findViewById(R.id.btn_loan);
        btnRefresh = findViewById(R.id.btn_refresh);


        // Open drawer on button click
        buttonDrawerToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.open();
            }
        });

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

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddIncome.class);
                startActivity(intent);
            }
        });


        // Set click listener for the refresh button
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshData(); // Call the refresh function
            }
        });
    }

    // Function to refresh data
    private void refreshData() {
//        myRecyclerViewAdapter.notifyDataSetChanged();
        Toast.makeText(MainActivity.this, "Refreshing...", Toast.LENGTH_SHORT).show();

    }
}
