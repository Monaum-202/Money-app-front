package com.monaum.money;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import com.monaum.money.dbUtill.Database;
import com.monaum.money.entity.AddExpence1;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddExpence extends AppCompatActivity {

    private EditText etExpenseAmount, etNotes;
    private Spinner spinnerCategory, spinnerPaymentMethod;
    private Button btnDate, btnTime, btnSave;
    private ImageButton btnBack;
    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;

    private List<String> categoryList, paymentMethodList;
    private boolean isEditMode = false;

    private Database expenseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expence);

        // Initialize Database
        expenseDatabase = new Database(this);

        // Initialize UI elements
        etExpenseAmount = findViewById(R.id.et_expence_amount);
        etNotes = findViewById(R.id.et_notes);
        spinnerCategory = findViewById(R.id.spinner_category);
        spinnerPaymentMethod = findViewById(R.id.spinner_payment_method);
        btnDate = findViewById(R.id.btn_date);
        btnTime = findViewById(R.id.btn_time);
        btnSave = findViewById(R.id.btn_save);
        btnBack = findViewById(R.id.back);

        // Get current date and time
        Calendar calendar = Calendar.getInstance();
        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        selectedHour = calendar.get(Calendar.HOUR_OF_DAY);
        selectedMinute = calendar.get(Calendar.MINUTE);

        // Populate Category List
        categoryList = new ArrayList<>();
        categoryList.add("Rent");
        categoryList.add("Groceries");
        categoryList.add("Utilities");
        categoryList.add("Transportation");
        categoryList.add("Healthcare");
        categoryList.add("Entertainment");
        categoryList.add("Dining Out");
        categoryList.add("Shopping");
        categoryList.add("Insurance");
        categoryList.add("Education");
        categoryList.add("Debt Payments");
        categoryList.add("Travel");
        categoryList.add("Personal Care");
        categoryList.add("Subscriptions");
        categoryList.add("Charity");
        categoryList.add("Other");

        // Populate Payment Method List
        paymentMethodList = new ArrayList<>();
        paymentMethodList.add("Cash");
        paymentMethodList.add("Bank Transfer");
        paymentMethodList.add("Credit Card");
        paymentMethodList.add("PayPal");
        paymentMethodList.add("Other");

        // Set up category spinner
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categoryList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        // Set up payment method spinner
        ArrayAdapter<String> paymentAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, paymentMethodList);
        paymentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPaymentMethod.setAdapter(paymentAdapter);

        // Date Picker
        btnDate.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (datePicker, year, month, day) -> {
                        selectedYear = year;
                        selectedMonth = month;
                        selectedDay = day;
                        btnDate.setText(String.format("%02d-%02d-%d", day, month + 1, year));
                    }, selectedYear, selectedMonth, selectedDay);
            datePickerDialog.show();
        });

        // Time Picker
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
        btnSave.setOnClickListener(view -> saveExpense());

        // Back button action
        btnBack.setOnClickListener(v -> finish()); // Closes the activity
    }

    private void saveExpense() {
        // Get expense amount
        String amountStr = etExpenseAmount.getText().toString().trim();
        double amount = 0;

        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount entered!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get selected category
        String category = spinnerCategory.getSelectedItem().toString();

        // Get selected payment method
        String wallet = spinnerPaymentMethod.getSelectedItem().toString();

        // Get notes
        String notes = etNotes.getText().toString().trim();

        // Get selected date and time
        String date = btnDate.getText().toString();
        String time = btnTime.getText().toString();

        if (date.equals("Select Date") || time.equals("Select Time")) {
            Toast.makeText(this, "Please select a valid date and time", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isEditMode) {
            // Update existing expense record
            AddExpence1 updatedExpense = new AddExpence1(amount, category, wallet, notes, date, time);
            expenseDatabase.updateExpence(updatedExpense);
            Toast.makeText(this, "Expense Updated!", Toast.LENGTH_SHORT).show();
        } else {
            // Create new expense record
            AddExpence1 newExpense = new AddExpence1(amount, category, wallet, notes, date, time);
            expenseDatabase.insertExpence(newExpense);
            Toast.makeText(this, "Expense Added!", Toast.LENGTH_SHORT).show();
        }

        // Finish activity
        finish();
    }
}
