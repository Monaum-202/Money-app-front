package com.monaum.money;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LineChartActivity extends AppCompatActivity {

    private LineChart lineChart;
    private SQLiteDatabase database;
    private List<String> xValues;
    private Spinner monthSpinner;
    private String selectedMonth = "03"; // Default to March

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);

        lineChart = findViewById(R.id.chart);
        monthSpinner = findViewById(R.id.monthSpinner);
        database = openOrCreateDatabase("myDB", MODE_PRIVATE, null);

        setupChart();
        setupMonthSpinner();
        loadData(selectedMonth);
    }

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


    private void loadData(String selectedMonth) {
        Map<Integer, Float> incomeMap = getMonthlyData("income", selectedMonth);
        Map<Integer, Float> expenseMap = getMonthlyData("expence", selectedMonth);

        List<Entry> incomeEntries = new ArrayList<>();
        List<Entry> expenseEntries = new ArrayList<>();

        xValues = new ArrayList<>();
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













//package com.monaum.money;
//
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.graphics.Color;
//import android.os.Bundle;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.github.mikephil.charting.charts.LineChart;
//import com.github.mikephil.charting.components.Description;
//import com.github.mikephil.charting.components.XAxis;
//import com.github.mikephil.charting.components.YAxis;
//import com.github.mikephil.charting.data.Entry;
//import com.github.mikephil.charting.data.LineData;
//import com.github.mikephil.charting.data.LineDataSet;
//import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class LineChartActivity extends AppCompatActivity {
//
//    private LineChart lineChart;
//    private SQLiteDatabase database;
//    private List<String> xValues;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_line_chart);
//
//        lineChart = findViewById(R.id.chart);
//        database = openOrCreateDatabase("myDB", MODE_PRIVATE, null); // Change "money_db" to your actual database name
//
//        setupChart();
//        loadData();
//    }
//
//    private void setupChart() {
//        Description description = new Description();
//        description.setText("Money Flow");
//        description.setTextSize(12f);
//        lineChart.setDescription(description);
//        lineChart.getAxisRight().setEnabled(false);
//
//        xValues = Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
//
//        XAxis xAxis = lineChart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setValueFormatter(new IndexAxisValueFormatter(xValues));
//        xAxis.setLabelCount(6);
//        xAxis.setGranularity(1f);
//
//        YAxis yAxis = lineChart.getAxisLeft();
//        yAxis.setAxisMinimum(0f);
//        yAxis.setLabelCount(5);
//        yAxis.setTextColor(Color.BLACK);
//    }
//
//    private void loadData() {
//        Map<Integer, Float> incomeMap = getMonthlyData("income");
//        Map<Integer, Float> expenseMap = getMonthlyData("expence");
//
//        List<Entry> incomeEntries = new ArrayList<>();
//        List<Entry> expenseEntries = new ArrayList<>();
//
//        for (int i = 0; i < 12; i++) {
//            float income = incomeMap.getOrDefault(i, 0f);
//            float expense = expenseMap.getOrDefault(i, 0f);
//
//            // Log values to debug
//            System.out.println("Month: " + (i + 1) + ", Income: " + income + ", Expense: " + expense);
//
//            incomeEntries.add(new Entry(i, income));
//            expenseEntries.add(new Entry(i, expense));
//        }
//
//        // Create datasets
//        LineDataSet incomeDataSet = new LineDataSet(incomeEntries, "Income");
//        incomeDataSet.setColor(Color.GREEN);
//        incomeDataSet.setCircleColor(Color.GREEN);
//        incomeDataSet.setLineWidth(2f);
//        incomeDataSet.setValueTextSize(10f);
//
//        LineDataSet expenseDataSet = new LineDataSet(expenseEntries, "Expense");
//        expenseDataSet.setColor(Color.RED);
//        expenseDataSet.setCircleColor(Color.RED);
//        expenseDataSet.setLineWidth(2f);
//        expenseDataSet.setValueTextSize(10f);
//
//        lineChart.setData(new LineData(incomeDataSet, expenseDataSet));
//        lineChart.invalidate();
//    }
//
//
//    private Map<Integer, Float> getMonthlyData(String tableName) {
//        Map<Integer, Float> dataMap = new HashMap<>();
//        Cursor cursor = null;
//
//        try {
//            cursor = database.rawQuery(
//                    "SELECT SUM(amount) AS total, SUBSTR(date, 4, 2) AS month FROM " + tableName + " GROUP BY month",
//                    null
//            );
//
//            if (cursor != null) {
//                while (cursor.moveToNext()) {
//                    int monthIndex = Integer.parseInt(cursor.getString(1)) - 1; // Convert month to 0-based index
//                    float totalAmount = cursor.getFloat(0);
//                    dataMap.put(monthIndex, totalAmount);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//
//        return dataMap;
//    }
//
//
//}