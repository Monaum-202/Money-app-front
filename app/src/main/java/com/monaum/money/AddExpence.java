package com.monaum.money;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.monaum.money.dbUtill.Database;
import com.monaum.money.entity.AddExpence1;
import com.monaum.money.entity.AddIncome1;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddExpence extends AppCompatActivity {

    private EditText etExpenceAmount, etNotes;
    private Spinner spinnerCategory, spinnerPaymentMethod;
    private Button btnDate, btnTime, btnSave;
    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;

    private ImageButton btnBack;

    private List<String> categoryList, paymentMethodList;

    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expence);

        etExpenceAmount = findViewById(R.id.et_expence_amount);
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
        btnSave.setOnClickListener(view -> saveExpence());


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Closes the current activity and goes back to the previous one
            }
        });
    }


    Database expenceDatabase = new Database(this);

    private void saveExpence() {
        // Get income amount
        String amountStr = etExpenceAmount.getText().toString().trim();
        double amount = Double.parseDouble(amountStr); // Ensure to handle NumberFormatException as needed

        // Get selected category
        String category = spinnerCategory.getSelectedItem().toString();

        // Get selected wallet (payment method)
        String wallet = spinnerPaymentMethod.getSelectedItem().toString();

        // Get notes
        String notes = etNotes.getText().toString().trim();

        // Get selected date and time
        String date = btnDate.getText().toString(); // Assuming the button text is set to the selected date
        String time = btnTime.getText().toString(); // Assuming the button text is set to the selected time

        if (isEditMode) {
            // Update existing income record
//            currentIncome.setAmount(amount);
//            currentIncome.setCategory(category);
//            currentIncome.setWallet(wallet);
//            currentIncome.setNotes(notes);
//            currentIncome.setDate(date);
//            currentIncome.setTime(time);
//
//            // Update the income in the database
//            incomeDatabase.updateIncome(currentIncome);
            Toast.makeText(this, "Expense Updated!", Toast.LENGTH_SHORT).show();
        } else {
            // Create new income record
            AddExpence1 newExpence = new AddExpence1(amount, category, wallet, notes, date, time);
            expenceDatabase.insertExpence(newExpence);
            Toast.makeText(this, "Expense Added!", Toast.LENGTH_SHORT).show();
        }

        // Finish activity
        finish();
    }
}