package com.monaum.money;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.navigation.NavigationView;
import com.monaum.money.dbUtill.Database;

import com.monaum.money.entity.AddIncome1;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IncomeChart extends AppCompatActivity {

    // Main Activity UI components
    DrawerLayout drawerLayout;
    ImageButton buttonDrawerToggle, btnAdd, btnExpense, btnTransfer, btnLoan, btnRefresh;
    NavigationView navigationView;
    private Database dbHelper;
    private TextView tvBalance;

    // Expense Chart UI components
    PieChart pieChart;
    Spinner spinnerMonth;
    Database database;
    String selectedMonth; // Holds the selected month in "MM" format

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_income_chart);

        // Initialize Main Activity UI components
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

        // Initialize Expense Chart UI components
        pieChart = findViewById(R.id.piChart);
        spinnerMonth = findViewById(R.id.spinnerMonth);
        database = new Database(this);

        updateBalance();

        // Open drawer on button click
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
                startActivity( new Intent(IncomeChart.this, MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if (itemId == R.id.navWalletStatus) {
                startActivity( new Intent(IncomeChart.this, WalletActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);            } else if (itemId == R.id.navIncomeStatus) {
                startActivity(new Intent(IncomeChart.this, IncomeChart.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if (itemId == R.id.navExpenseStatus) {
                startActivity(new Intent(IncomeChart.this, ExpenseChart.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if (itemId == R.id.navBudget) {
                startActivity(new Intent(IncomeChart.this, BarChartActivity2.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if (itemId == R.id.navPlan) {
                Toast.makeText(IncomeChart.this, "Plan clicked", Toast.LENGTH_SHORT).show();
            } else if (itemId == R.id.navHistory) {
                startActivity(new Intent(IncomeChart.this, History.class));  // Assume HomeActivity is your landing page
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
            else if (itemId == R.id.navLogout) {
                // Clear shared preferences to remove stored username
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear(); // Clears all saved data in "myData"
                editor.apply();

                // Redirect to Login Activity
                Intent intent = new Intent(IncomeChart.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
                startActivity(intent);
                finish(); // Close MainActivity
            }


            drawerLayout.close();
            return true;
        });

        // Button Listeners
        btnAdd.setOnClickListener(v -> startActivity(new Intent(IncomeChart.this, AddIncome.class)));
        btnExpense.setOnClickListener(v -> startActivity(new Intent(IncomeChart.this, AddExpence.class)));
        btnRefresh.setOnClickListener(v -> recreate());

        // Setup Pie Chart and Spinner for ExpenseChart
        setupPieChart();
        setupSpinner();
    }

    private void updateBalance() {
        double balance = dbHelper.getCurrentBalance();
        tvBalance.setText("Balance: " + String.format("%.2f", balance) + " taka");
    }

    // Pie Chart Setup
    private void setupPieChart() {
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText("Income Chart");
        pieChart.setCenterTextSize(10);
        pieChart.setDrawEntryLabels(true);
    }

    // Setup Spinner for Month Selection
    private void setupSpinner() {
        String[] months = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, months);
        spinnerMonth.setAdapter(adapter);

        // Get current month and set default selection
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        spinnerMonth.setSelection(currentMonth);

        selectedMonth = String.format("%02d", currentMonth + 1); // Convert to "MM" format

        // Listener to detect when user changes the month
        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMonth = String.format("%02d", position + 1); // Convert to MM format
                updateChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Load initial chart data
        updateChart();
    }

    // Fetch Expenses Filtered by Month (for "dd-MM-yyyy" format)
    private List<AddIncome1> getExpensesByMonth(String month) {
        List<AddIncome1> incomesList = new ArrayList<>();
        SQLiteDatabase db = database.getReadableDatabase();

        // Fix query to extract month from "dd-MM-yyyy" format
        String query = "SELECT * FROM income WHERE substr(date, 4, 2) = ?";
        Cursor cursor = db.rawQuery(query, new String[]{month});

        if (cursor.moveToFirst()) {
            do {
                AddIncome1 income1 = new AddIncome1();
                income1.setId(cursor.getLong(cursor.getColumnIndexOrThrow("id")));
                income1.setAmount(cursor.getDouble(cursor.getColumnIndexOrThrow("amount")));
                income1.setCategory(cursor.getString(cursor.getColumnIndexOrThrow("category")));
                income1.setWallet(cursor.getString(cursor.getColumnIndexOrThrow("wallet")));
                income1.setNotes(cursor.getString(cursor.getColumnIndexOrThrow("notes")));
                income1.setDate(cursor.getString(cursor.getColumnIndexOrThrow("date")));
                income1.setTime(cursor.getString(cursor.getColumnIndexOrThrow("time")));

                incomesList.add(income1);

                // Debugging Log
                Log.d("ExpenseChart", "Fetched: " + income1.getDate() + " - " + income1.getAmount());
            } while (cursor.moveToNext());
        } else {
            Log.d("ExpenseChart", "No data found for month: " + month);
        }

        cursor.close();
        db.close();

        return incomesList;
    }

    private Map<String, Float> calculateCategoryTotals(List<AddIncome1> incomes) {
        Map<String, Float> categoryTotals = new HashMap<>();

        for (AddIncome1 income : incomes) {
            String category = income.getCategory();
            float amount = income.getAmount().floatValue();

            if (categoryTotals.containsKey(category)) {
                categoryTotals.put(category, categoryTotals.get(category) + amount);
            } else {
                categoryTotals.put(category, amount);
            }
        }
        return categoryTotals;
    }

    private void updateChart() {
        List<AddIncome1> incomes = getExpensesByMonth(selectedMonth);
        Map<String, Float> categoryTotals = calculateCategoryTotals(incomes);

        if (categoryTotals.isEmpty()) {
            pieChart.clear();
            pieChart.setNoDataText("No data available for this month");
            return;
        }

        addDataSet(categoryTotals);
    }

    private void addDataSet(Map<String, Float> categoryTotals) {
        ArrayList<PieEntry> yEntries = new ArrayList<>();

        for (Map.Entry<String, Float> entry : categoryTotals.entrySet()) {
            yEntries.add(new PieEntry(entry.getValue(), entry.getKey())); // Amount & Category
        }

        PieDataSet pieDataSet = new PieDataSet(yEntries, "Incomes by Category");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);
        pieDataSet.setValueTextColor(Color.WHITE);

        // Adding Colors
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.BLACK);      // Good contrast with white text
        colors.add(Color.MAGENTA);     // Dark gray with good contrast
        colors.add(Color.CYAN);       // Lighter gray, still readable with white text
        colors.add(Color.RED);        // Red has good contrast with white text
        colors.add(Color.BLUE);       // Blue is fine for white text
        colors.add(Color.GREEN);      // Green is readable with white text
        colors.add(Color.GRAY);       // Cyan works with white text
        colors.add(Color.DKGRAY);    // Magenta is a good choice for white text
        pieDataSet.setColors(colors);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate(); // Refresh chart
    }
}