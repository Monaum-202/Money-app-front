package com.monaum.money;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

import androidx.appcompat.app.AppCompatActivity;

import androidx.drawerlayout.widget.DrawerLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.navigation.NavigationView;
import com.monaum.money.dbUtill.Database;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {



    // UI Components for Drawer and Buttons
    private DrawerLayout drawerLayout;
    private ImageButton buttonDrawerToggle, btnAdd, btnExpense, btnTransfer, btnLoan, btnRefresh;
    private NavigationView navigationView;

    // UI Components for LineChart and Spinner
    private LineChart lineChart;
    private Spinner monthSpinner, yearSpinner;

    private TextView tvBalance, tvTotalIncome, tvTotalExpense, tvTotalSaving;
    private Database dbHelper;


    private SQLiteDatabase database;

    private String selectedMonth = "03"; // Default to March
    private String selectedYear = "2025"; // Default year

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
        tvTotalIncome = findViewById(R.id.tv_total_income);
        tvTotalExpense = findViewById(R.id.tv_total_expense);
        tvTotalSaving = findViewById(R.id.tv_total_saving);
        monthSpinner = findViewById(R.id.monthSpinner);
        yearSpinner = findViewById(R.id.yearSpinner);
        lineChart = findViewById(R.id.chart);

        dbHelper = new Database(this);
        database = openOrCreateDatabase("myDB", MODE_PRIVATE, null);

        updateBalance();
        setupChart();
        setupSpinners(); // Setup both month and year spinners

        // Load data for the default month & year
        loadData(selectedMonth, selectedYear);

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
                startActivity( new Intent(MainActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if (itemId == R.id.navWalletStatus) {
                startActivity( new Intent(MainActivity.this, WalletActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if (itemId == R.id.navIncomeStatus) {
                startActivity(new Intent(MainActivity.this, IncomeChart.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if (itemId == R.id.navExpenseStatus) {
                startActivity(new Intent(MainActivity.this, ExpenseChart.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if (itemId == R.id.navBudget) {
                startActivity(new Intent(MainActivity.this, BarChartActivity2.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if (itemId == R.id.navPlan) {
                Toast.makeText(MainActivity.this, "Plan clicked", Toast.LENGTH_SHORT).show();
            } else if (itemId == R.id.navHistory) {
                startActivity(new Intent(MainActivity.this, History.class));  // Assume HomeActivity is your landing page
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
            else if (itemId == R.id.navLogout) {
                // Clear shared preferences to remove stored username
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear(); // Clears all saved data in "myData"
                editor.apply();

                // Redirect to Login Activity
                Intent intent = new Intent(MainActivity.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
                startActivity(intent);
                finish(); // Close MainActivity
            }


            drawerLayout.close();
            return true;
        });



        // Button actions for Add Income and Expense
        btnAdd.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AddIncome.class)));
        btnExpense.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AddExpence.class)));

        // Refresh button click
        btnRefresh.setOnClickListener(v -> recreate());
    }

    // Update the balance text on the UI
    private void updateBalance() {
        double balance = dbHelper.getCurrentBalance();
        tvBalance.setText("Balance: " + String.format("%.2f", balance) + " taka");
    }

    // Setup for the LineChart
    private void setupChart() {
        Description description = new Description();
        description.setText("Money Flow");
        description.setTextSize(12f);
        lineChart.setDescription(description);
        lineChart.getAxisRight().setEnabled(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis yAxis = lineChart.getAxisLeft();
    }








    // Setup the Month Spinner for selecting the month
    // Setup the Month Spinner for selecting the month
    private void setupSpinners() {
        // Month names array (no numeric prefix)
        final String[] months = {
                "January", "February", "March", "April", "May", "June", "July", "August",
                "September", "October", "November", "December"
        };

        // Create the ArrayAdapter for the months
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, months);
        monthSpinner.setAdapter(monthAdapter);

        // Get the current year and populate the Year Spinner (2024 - 2026)
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        List<String> years = new ArrayList<>();
        for (int i = currentYear - 1; i <= currentYear + 1; i++) {
            years.add(String.valueOf(i));
        }

        // Create the ArrayAdapter for the years
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, years);
        yearSpinner.setAdapter(yearAdapter);

        // Set default selections
        monthSpinner.setSelection(Calendar.getInstance().get(Calendar.MONTH)); // Set the current month
        yearSpinner.setSelection(years.indexOf(String.valueOf(currentYear))); // Set the current year

        // Update the selectedMonth & selectedYear when changed
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMonth = String.format("%02d", position + 1); // Convert month index to "01", "02", etc.
                loadData(selectedMonth, selectedYear);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedYear = years.get(position);
                loadData(selectedMonth, selectedYear);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Load data for the current month & year
        loadData(selectedMonth, selectedYear);
    }


    // Load data for selected month & year
    private void loadData(String selectedMonth, String selectedYear) {
        Map<Integer, Float> incomeMap = getMonthlyData("income", selectedMonth, selectedYear);
        Map<Integer, Float> expenseMap = getMonthlyData("expence", selectedMonth, selectedYear);

        List<Entry> incomeEntries = new ArrayList<>();
        List<Entry> expenseEntries = new ArrayList<>();

        float cumulativeIncome = 0f;
        float cumulativeExpense = 0f;

        for (int i = 1; i <= 31; i++) {
            float dailyIncome = incomeMap.getOrDefault(i, 0f);
            float dailyExpense = expenseMap.getOrDefault(i, 0f);

            cumulativeIncome += dailyIncome;
            cumulativeExpense += dailyExpense;

            incomeEntries.add(new Entry(i, cumulativeIncome));
            expenseEntries.add(new Entry(i, cumulativeExpense));
        }

        LineDataSet incomeDataSet = new LineDataSet(incomeEntries, "Cumulative Income");
        incomeDataSet.setColor(Color.GREEN);
        incomeDataSet.setDrawCircles(false);
        incomeDataSet.setDrawValues(false);
        incomeDataSet.setLineWidth(3f);

        LineDataSet expenseDataSet = new LineDataSet(expenseEntries, "Cumulative Expense");
        expenseDataSet.setColor(Color.RED);
        expenseDataSet.setDrawCircles(false);
        expenseDataSet.setDrawValues(false);
        expenseDataSet.setLineWidth(3f);

        lineChart.setData(new LineData(incomeDataSet, expenseDataSet));
        lineChart.invalidate();

        // Update Total Income in the TextView
        updateTotalIncome(selectedMonth, selectedYear);
        updateTotalExpense(selectedMonth, selectedYear);
        updateTotalSaving(selectedMonth, selectedYear);
    }

    // Get monthly data with year filter
    private Map<Integer, Float> getMonthlyData(String tableName, String selectedMonth, String selectedYear) {
        Map<Integer, Float> dataMap = new HashMap<>();
        Cursor cursor = null;

        try {
            String query = "SELECT SUM(amount) AS total, SUBSTR(date, 1, 2) AS day FROM " + tableName +
                    " WHERE SUBSTR(date, 4, 2) = ? AND SUBSTR(date, 7, 4) = ? GROUP BY day ORDER BY day";

            cursor = database.rawQuery(query, new String[]{selectedMonth, selectedYear});

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int day = Integer.parseInt(cursor.getString(cursor.getColumnIndex("day")));
                    float totalAmount = cursor.getFloat(cursor.getColumnIndex("total"));
                    dataMap.put(day, totalAmount);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }

        return dataMap;
    }

    // Update the total income based on selected month and year
    private void updateTotalIncome(String selectedMonth, String selectedYear) {
        double totalIncome = getTotalIncomeForMonthYear(selectedMonth, selectedYear);
        tvTotalIncome.setText("Total Income : " + String.format("%.2f", totalIncome) + " taka");
    }

    private void updateTotalExpense(String selectedMonth, String selectedYear) {
        double totalIncome = getTotalExpenseForMonthYear(selectedMonth, selectedYear);
        tvTotalExpense.setText("Total Expense : " + String.format("%.2f", totalIncome) + " taka");
    }


    private void updateTotalSaving(String selectedMonth, String selectedYear) {
        double totalIncome = getTotalIncomeForMonthYear(selectedMonth, selectedYear);
        double totalExpense = getTotalExpenseForMonthYear(selectedMonth, selectedYear);
        double totalSaving = totalIncome - totalExpense;
        tvTotalSaving.setText("Total Saving : " + String.format("%.2f", totalSaving) + " taka");
    }
    // Method to get total income for the selected month and year
    private double getTotalIncomeForMonthYear(String selectedMonth, String selectedYear) {
        double totalIncome = 0.0;
        Cursor cursor = null;
        try {
            String query = "SELECT SUM(amount) AS total FROM income WHERE SUBSTR(date, 4, 2) = ? AND SUBSTR(date, 7, 4) = ?";
            cursor = database.rawQuery(query, new String[]{selectedMonth, selectedYear});

            if (cursor != null && cursor.moveToFirst()) {
                totalIncome = cursor.getDouble(cursor.getColumnIndex("total"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }

        return totalIncome;
    }



    private double getTotalExpenseForMonthYear(String selectedMonth, String selectedYear) {
        double totalExpence = 0.0;
        Cursor cursor = null;
        try {
            String query = "SELECT SUM(amount) AS total FROM expence WHERE SUBSTR(date, 4, 2) = ? AND SUBSTR(date, 7, 4) = ?";
            cursor = database.rawQuery(query, new String[]{selectedMonth, selectedYear});

            if (cursor != null && cursor.moveToFirst()) {
                totalExpence = cursor.getDouble(cursor.getColumnIndex("total"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }

        return totalExpence;
    }



}
