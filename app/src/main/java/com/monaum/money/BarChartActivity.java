package com.monaum.money;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BarChartActivity extends AppCompatActivity {

    private BarChart barChart;
    private SQLiteDatabase database;
    private List<String> xValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        barChart = findViewById(R.id.chart);
        database = openOrCreateDatabase("myDB", MODE_PRIVATE, null); // Use your actual database name

        setupChart();
        loadData();
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

    private void loadData() {
        Map<Integer, Float> incomeMap = getMonthlyData("income");
        Map<Integer, Float> expenseMap = getMonthlyData("expence");

        List<BarEntry> incomeEntries = new ArrayList<>();
        List<BarEntry> expenseEntries = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            float income = incomeMap.getOrDefault(i, 0f);
            float expense = expenseMap.getOrDefault(i, 0f);

            // Log values for debugging
            System.out.println("Month: " + (i + 1) + ", Income: " + income + ", Expense: " + expense);

            incomeEntries.add(new BarEntry(i, income));
            expenseEntries.add(new BarEntry(i, expense));
        }

        // Create datasets
        BarDataSet incomeDataSet = new BarDataSet(incomeEntries, "Income");
        incomeDataSet.setColor(Color.GREEN);
        incomeDataSet.setValueTextSize(10f);

        BarDataSet expenseDataSet = new BarDataSet(expenseEntries, "Expense");
        expenseDataSet.setColor(Color.RED);
        expenseDataSet.setValueTextSize(10f);

        BarData barData = new BarData(incomeDataSet, expenseDataSet);
        barData.setBarWidth(0.4f);

        barChart.setData(barData);
        barChart.groupBars(0f, 0.2f, 0.05f); // Adjusts bar spacing
        barChart.invalidate();
    }

    private Map<Integer, Float> getMonthlyData(String tableName) {
        Map<Integer, Float> dataMap = new HashMap<>();
        Cursor cursor = null;

        try {
            cursor = database.rawQuery(
                    "SELECT SUM(amount) AS total, SUBSTR(date, 4, 2) AS month FROM " + tableName + " GROUP BY month",
                    null
            );

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int monthIndex = Integer.parseInt(cursor.getString(1)) - 1; // Convert month to 0-based index
                    float totalAmount = cursor.getFloat(0);
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
}
