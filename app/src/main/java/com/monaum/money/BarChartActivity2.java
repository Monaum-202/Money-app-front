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

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.navigation.NavigationView;
import com.monaum.money.dbUtill.Database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BarChartActivity2 extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private BarChart barChart;
    private SQLiteDatabase database;
    private Database dbHelper;
    private List<String> xValues;
    private ImageButton buttonDrawerToggle, btnAdd, btnExpense, btnTransfer, btnLoan, btnRefresh;
    private TextView tvBalance,tvTotalIncome, tvTotalExpense, tvTotalSaving;
    private Spinner spinnerYear;

    NavigationView navigationView;


    private String selectedYear = "2025";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart2);

        spinnerYear = findViewById(R.id.spinner_year);
        btnAdd = findViewById(R.id.btn_add);
        drawerLayout = findViewById(R.id.drawerLayout);
        buttonDrawerToggle = findViewById(R.id.buttonDrawerToggle);
        navigationView = findViewById(R.id.navigationView);
        btnExpense = findViewById(R.id.btn_remove);
        btnTransfer = findViewById(R.id.btn_transfer);
        btnLoan = findViewById(R.id.btn_loan);
        btnRefresh = findViewById(R.id.btn_refresh);
        tvBalance = findViewById(R.id.tv_balance);
        tvTotalIncome = findViewById(R.id.tv_total_income);
        tvTotalExpense = findViewById(R.id.tv_total_expense);
        tvTotalSaving = findViewById(R.id.tv_total_saving);
        dbHelper = new Database(this);


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
                startActivity( new Intent(BarChartActivity2.this, MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if (itemId == R.id.navWalletStatus) {
                startActivity( new Intent(BarChartActivity2.this, WalletActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);            } else if (itemId == R.id.navIncomeStatus) {
                startActivity(new Intent(BarChartActivity2.this, IncomeChart.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if (itemId == R.id.navExpenseStatus) {
                startActivity(new Intent(BarChartActivity2.this, ExpenseChart.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if (itemId == R.id.navBudget) {
                startActivity(new Intent(BarChartActivity2.this, BarChartActivity2.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if (itemId == R.id.navPlan) {
                Toast.makeText(BarChartActivity2.this, "Plan clicked", Toast.LENGTH_SHORT).show();
            } else if (itemId == R.id.navHistory) {
                startActivity(new Intent(BarChartActivity2.this, History.class));  // Assume HomeActivity is your landing page
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
            else if (itemId == R.id.navLogout) {
                // Clear shared preferences to remove stored username
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear(); // Clears all saved data in "myData"
                editor.apply();

                // Redirect to Login Activity
                Intent intent = new Intent(BarChartActivity2.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
                startActivity(intent);
                finish(); // Close MainActivity
            }


            drawerLayout.close();
            return true;
        });

        // Button Listeners
        btnAdd.setOnClickListener(v -> startActivity(new Intent(BarChartActivity2.this, AddIncome.class)));
        btnExpense.setOnClickListener(v -> startActivity(new Intent(BarChartActivity2.this, AddExpence.class)));
        btnRefresh.setOnClickListener(v -> recreate());



        barChart = findViewById(R.id.chart);
        database = openOrCreateDatabase("myDB", MODE_PRIVATE, null);

        setupYearSpinner();

        btnAdd.setOnClickListener(v -> startActivity(new Intent(BarChartActivity2.this, AddIncome.class)));
        btnExpense.setOnClickListener(v -> startActivity(new Intent(BarChartActivity2.this, AddExpence.class)));
        btnRefresh.setOnClickListener(v -> recreate());

        updateBalance();
        setupChart();

        database = openOrCreateDatabase("myDB", MODE_PRIVATE, null);
        setupYearSpinner();
        selectedYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        loadData(selectedYear);
    }

    private void setupYearSpinner() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        List<String> years = new ArrayList<>();
        for (int i = currentYear; i >= 2000; i--) {
            years.add(String.valueOf(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, years);
        spinnerYear.setAdapter(adapter);
        spinnerYear.setSelection(0);
        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedYear = parent.getItemAtPosition(position).toString();
                loadData(selectedYear);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupChart() {
        Description description = new Description();
        description.setText("Money Flow");
        description.setTextSize(12f);
        barChart.setDescription(description);
        barChart.getAxisRight().setEnabled(false);

        xValues = Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xValues));
        xAxis.setLabelCount(6);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);

        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setLabelCount(5);
        yAxis.setTextColor(Color.BLACK);
    }

    private void loadData(String year) {
        Map<Integer, Float> incomeMap = getyearlyData("income", year);
        Map<Integer, Float> expenseMap = getyearlyData("expence", year);

        List<BarEntry> incomeEntries = new ArrayList<>();
        List<BarEntry> expenseEntries = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            float income = incomeMap.getOrDefault(i, 0f);
            float expense = expenseMap.getOrDefault(i, 0f);

            incomeEntries.add(new BarEntry(i, income));
            expenseEntries.add(new BarEntry(i, expense));
        }

        BarDataSet incomeDataSet = new BarDataSet(incomeEntries, "Income");
        incomeDataSet.setColor(Color.GREEN);
        incomeDataSet.setValueTextSize(10f);

        BarDataSet expenseDataSet = new BarDataSet(expenseEntries, "Expense");
        expenseDataSet.setColor(Color.RED);
        expenseDataSet.setValueTextSize(10f);

        BarData barData = new BarData(incomeDataSet, expenseDataSet);
        barData.setBarWidth(0.4f);

        barChart.setData(barData);
        barChart.groupBars(0f, 0.2f, 0.05f);
        barChart.invalidate();

        updateTotalIncome(selectedYear);
        updateTotalExpense(selectedYear);
        updateTotalSaving(selectedYear);
    }

    private Map<Integer, Float> getyearlyData(String tableName, String year) {
        Map<Integer, Float> dataMap = new HashMap<>();
        Cursor cursor = null;

        try {
            cursor = database.rawQuery(
                    "SELECT SUBSTR(date, 4, 2) AS month, SUM(amount) AS total FROM " + tableName +
                            " WHERE SUBSTR(date, 7, 4) = ? GROUP BY month",
                    new String[]{year}
            );

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int monthIndex = Integer.parseInt(cursor.getString(0)) - 1; // Convert month to 0-based index
                    float totalAmount = cursor.getFloat(1);
                    dataMap.put(monthIndex, totalAmount);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return dataMap;
    }


    private void updateBalance() {
        double balance = dbHelper.getCurrentBalance();
        tvBalance.setText("Balance: " + String.format("%.2f", balance) + " taka");
    }


    private void updateTotalIncome(String year) {
        double totalIncome = getTotalIncomeForYear(year);
        tvTotalIncome.setText("Total Income: " + String.format("%.2f", totalIncome) + " taka");
    }

    private void updateTotalExpense(String year) {
        double totalExpense = getTotalExpenseForYear(year);
        tvTotalExpense.setText("Total Expense: " + String.format("%.2f", totalExpense) + " taka");
    }

    private void updateTotalSaving(String year) {
        double totalIncome = getTotalIncomeForYear(year);
        double totalExpense = getTotalExpenseForYear(year);
        double totalSaving = totalIncome - totalExpense;
        tvTotalSaving.setText("Total Saving: " + String.format("%.2f", totalSaving) + " taka");
    }

    private double getTotalIncomeForYear(String year) {
        double totalIncome = 0.0;
        Cursor cursor = database.rawQuery("SELECT SUM(amount) FROM income WHERE SUBSTR(date, 7, 4) = ?", new String[]{year});
        if (cursor != null && cursor.moveToFirst()) {
            totalIncome = cursor.getDouble(0);
            cursor.close();
        }
        return totalIncome;
    }

    private double getTotalExpenseForYear(String year) {
        double totalExpense = 0.0;
        Cursor cursor = database.rawQuery("SELECT SUM(amount) FROM expence WHERE SUBSTR(date, 7, 4) = ?", new String[]{year});
        if (cursor != null && cursor.moveToFirst()) {
            totalExpense = cursor.getDouble(0);
            cursor.close();
        }
        return totalExpense;
    }


}
