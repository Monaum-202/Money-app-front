package com.monaum.money;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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
    private Spinner monthSpinner;

    private TextView tvBalance;
    private Database dbHelper;
    private SQLiteDatabase database;

    private String selectedMonth = "03"; // Default to March

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
        monthSpinner = findViewById(R.id.monthSpinner);
        lineChart = findViewById(R.id.chart);

        dbHelper = new Database(this);
        database = openOrCreateDatabase("myDB", MODE_PRIVATE, null);

        updateBalance();

        setupChart();
        setupMonthSpinner();
        loadData(selectedMonth);

        // Open drawer on button click
        buttonDrawerToggle.setOnClickListener(v -> drawerLayout.open());

        // Navigation Drawer Item Clicks
        navigationView.setNavigationItemSelectedListener(item -> {
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
                startActivity(new Intent(MainActivity.this, BarChartActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if (itemId == R.id.navPlan) {
                startActivity(new Intent(MainActivity.this, LineChartActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if (itemId == R.id.navHistory) {
                startActivity(new Intent(MainActivity.this, History.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if (itemId == R.id.navEHistory) {
                startActivity(new Intent(MainActivity.this, ExpenseHistory.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
    private void setupMonthSpinner() {
        final String[] months = {"01 - January", "02 - February", "03 - March", "04 - April",
                "05 - May", "06 - June", "07 - July", "08 - August", "09 - September",
                "10 - October", "11 - November", "12 - December"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, months);
        monthSpinner.setAdapter(adapter);

        // Get current month (1-12) and adjust to match array index (0-11)
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH); // 0 = January, 11 = December

        // Set the default month in Spinner
        monthSpinner.setSelection(currentMonth);

        // Extract and store selected month
        selectedMonth = months[currentMonth].substring(0, 2);

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMonth = months[position].substring(0, 2); // Extract month number
                loadData(selectedMonth);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Load data for the default selected month
        loadData(selectedMonth);
    }

    // Load data for the selected month and display in the LineChart
    private void loadData(String selectedMonth) {
        Map<Integer, Float> incomeMap = getMonthlyData("income", selectedMonth);
        Map<Integer, Float> expenseMap = getMonthlyData("expence", selectedMonth);

        List<Entry> incomeEntries = new ArrayList<>();
        List<Entry> expenseEntries = new ArrayList<>();

        List<String> xValues = new ArrayList<>();
        float cumulativeIncome = 0f;
        float cumulativeExpense = 0f;
        int lastDay = 0;

        for (int i = 1; i <= 31; i++) {
            float dailyIncome = incomeMap.getOrDefault(i, 0f);
            float dailyExpense = expenseMap.getOrDefault(i, 0f);

            cumulativeIncome += dailyIncome;
            cumulativeExpense += dailyExpense;

            if (cumulativeIncome > 0 || cumulativeExpense > 0) {
                lastDay = i;
            }

            incomeEntries.add(new Entry(i, cumulativeIncome));
            expenseEntries.add(new Entry(i, cumulativeExpense));

            xValues.add(String.valueOf(i));
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

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xValues));
        xAxis.setGranularity(1f);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(true);

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setDrawAxisLine(true);
        yAxis.setDrawGridLines(true);

        lineChart.getAxisRight().setEnabled(false);
        lineChart.getDescription().setEnabled(false);

        if (lastDay > 0) {
            Description description = new Description();
            description.setText("End Value: " + cumulativeIncome);
            description.setTextSize(12f);
            description.setPosition(lineChart.getXChartMax() - 100, lineChart.getYChartMax() - 50);
            lineChart.setDescription(description);
        }

        lineChart.invalidate();
    }

    // Get the monthly data for either "income" or "expence"
    private Map<Integer, Float> getMonthlyData(String tableName, String selectedMonth) {
        Map<Integer, Float> dataMap = new HashMap<>();
        Cursor cursor = null;

        try {
            String query = "SELECT SUM(amount) AS total, SUBSTR(date, 1, 2) AS day FROM " + tableName +
                    " WHERE SUBSTR(date, 4, 2) = ? GROUP BY day ORDER BY day";

            cursor = database.rawQuery(query, new String[]{selectedMonth});

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int day = Integer.parseInt(cursor.getString(1));
                    float totalAmount = cursor.getFloat(0);
                    dataMap.put(day, totalAmount);
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
}
