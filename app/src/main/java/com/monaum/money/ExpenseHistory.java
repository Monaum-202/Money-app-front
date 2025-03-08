package com.monaum.money;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.monaum.money.adapters.ExpenseHistoryAdapters;
import com.monaum.money.databinding.ActivityExpenseHistoryBinding;
import com.monaum.money.databinding.ActivityHistoryBinding;
import com.monaum.money.dbUtill.Database;
import com.monaum.money.entity.AddExpence1;


import java.util.ArrayList;
import java.util.List;

public class ExpenseHistory extends AppCompatActivity {

    private ActivityExpenseHistoryBinding binding;
    private ArrayList<AddExpence1> dataArrayList;
    private ExpenseHistoryAdapters listAdapter;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_history);
        // Initialize ViewBinding
        binding = ActivityExpenseHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Database
        db = new Database(this);

        // Initialize Data
        dataArrayList = new ArrayList<>();
        listAdapter = new ExpenseHistoryAdapters(this, dataArrayList);
        binding.listview.setAdapter(listAdapter);

        // Load and set data
        loadExpenseData();
    }

    private void loadExpenseData() {
        new Thread(() -> {
            List<AddExpence1> data = db.getAllExpence(); // Fetch data from DB
            ArrayList<AddExpence1> finalData = new ArrayList<>(data);

            runOnUiThread(() -> {
                dataArrayList.clear();
                dataArrayList.addAll(finalData);
                listAdapter.notifyDataSetChanged(); // Update the list
            });
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadExpenseData(); // Refresh data when resuming
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close(); // Close the database to prevent memory leaks
        }
        binding = null; // Prevent memory leaks
    }
}
