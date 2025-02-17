package com.monaum.money;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddIncome extends AppCompatActivity {

    private EditText etIncomeAmount, etNotes;
    private Spinner spinnerCategory, spinnerPaymentMethod;
    private Button btnDate, btnTime, btnSave;
    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;

    private List<String> categoryList, paymentMethodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);

        etIncomeAmount = findViewById(R.id.et_income_amount);
        etNotes = findViewById(R.id.et_notes);
        spinnerCategory = findViewById(R.id.spinner_category);
        spinnerPaymentMethod = findViewById(R.id.spinner_payment_method);
        btnDate = findViewById(R.id.btn_date);
        btnTime = findViewById(R.id.btn_time);
        btnSave = findViewById(R.id.btn_save);

        // Get current date and time
        Calendar calendar = Calendar.getInstance();
        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        selectedHour = calendar.get(Calendar.HOUR_OF_DAY);
        selectedMinute = calendar.get(Calendar.MINUTE);

        // Populate Category List
        categoryList = new ArrayList<>();
        categoryList.add("Salary");
        categoryList.add("Allowance");
        categoryList.add("Freelance");
        categoryList.add("Investment");
        categoryList.add("Other");

        // Populate Payment Method List
        paymentMethodList = new ArrayList<>();
        paymentMethodList.add("Cash");
        paymentMethodList.add("Bank Transfer");
        paymentMethodList.add("Credit Card");
        paymentMethodList.add("PayPal");
        paymentMethodList.add("Other");

        // Create Adapter for Category Spinner
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, categoryList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set Adapter to Category Spinner
        spinnerCategory.setAdapter(categoryAdapter);

        // Handle Category Spinner Item Selection
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = parent.getItemAtPosition(position).toString();
                Toast.makeText(AddIncome.this, "Selected Category: " + selectedCategory, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Create Adapter for Payment Method Spinner
        ArrayAdapter<String> paymentAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, paymentMethodList);
        paymentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set Adapter to Payment Method Spinner
        spinnerPaymentMethod.setAdapter(paymentAdapter);

        // Handle Payment Method Spinner Item Selection
        spinnerPaymentMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedPaymentMethod = parent.getItemAtPosition(position).toString();
                Toast.makeText(AddIncome.this, "Selected Payment Method: " + selectedPaymentMethod, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Date picker dialog
        btnDate.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (datePicker, year, month, day) -> {
                        selectedYear = year;
                        selectedMonth = month;
                        selectedDay = day;
                        btnDate.setText(day + "-" + (month + 1) + "-" + year);
                    }, selectedYear, selectedMonth, selectedDay);
            datePickerDialog.show();
        });

        // Time picker dialog
        btnTime.setOnClickListener(view -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (timePicker, hour, minute) -> {
                        selectedHour = hour;
                        selectedMinute = minute;
                        btnTime.setText(String.format("%02d:%02d", hour, minute));
                    }, selectedHour, selectedMinute, true);
            timePickerDialog.show();
        });

        // Save button action
        btnSave.setOnClickListener(view -> saveIncome());
    }

    private void saveIncome() {
        String incomeAmount = etIncomeAmount.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();
        String paymentMethod = spinnerPaymentMethod.getSelectedItem().toString();
        String date = btnDate.getText().toString();
        String time = btnTime.getText().toString();

        if (incomeAmount.isEmpty()) {
            Toast.makeText(this, "Please enter income amount", Toast.LENGTH_SHORT).show();
            return;
        }

        // Here you can save the data to a database or API
        Toast.makeText(this, "Income Saved Successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
