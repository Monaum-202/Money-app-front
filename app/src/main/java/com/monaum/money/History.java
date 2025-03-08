package com.monaum.money;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.monaum.money.databinding.ActivityHistoryBinding;
import com.monaum.money.entity.Transaction;
import com.monaum.money.adapters.TransactionAdapter;
import com.monaum.money.dbUtill.Database;
import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity {

    private ActivityHistoryBinding binding;
    private ArrayList<Transaction> transactionList;
    private TransactionAdapter transactionAdapter;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ViewBinding
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Database
        db = new Database(this);

        // Initialize Data
        transactionList = new ArrayList<>();
        transactionAdapter = new TransactionAdapter(this, transactionList);
        binding.listview.setAdapter(transactionAdapter);

        // Load and set data
        loadTransactionData();
    }

    private void loadTransactionData() {
        new Thread(() -> {
            List<Transaction> data = db.getAllTransactions(); // Fetch combined data
            ArrayList<Transaction> finalData = new ArrayList<>(data);

            runOnUiThread(() -> {
                transactionList.clear();
                transactionList.addAll(finalData);
                transactionAdapter.notifyDataSetChanged(); // Update the list
            });
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTransactionData(); // Refresh data when resuming
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





//package com.monaum.money;
//
//import android.os.Bundle;
//import androidx.appcompat.app.AppCompatActivity;
//import com.monaum.money.databinding.ActivityHistoryBinding;
//import com.monaum.money.entity.AddIncome1;
//import com.monaum.money.adapters.HistoryAdapter;
//import com.monaum.money.dbUtill.Database;
//import java.util.ArrayList;
//import java.util.List;
//
//public class History extends AppCompatActivity {
//
//    private ActivityHistoryBinding binding;
//    private ArrayList<AddIncome1> dataArrayList;
//    private HistoryAdapter listAdapter;
//    private Database db;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_history);
//        // Initialize ViewBinding
//        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        // Initialize Database
//        db = new Database(this);
//
//        // Initialize Data
//        dataArrayList = new ArrayList<>();
//        listAdapter = new HistoryAdapter(this, dataArrayList);
//        binding.listview.setAdapter(listAdapter);
//
//        // Load and set data
//        loadIncomeData();
//    }
//
//    private void loadIncomeData() {
//        new Thread(() -> {
//            List<AddIncome1> data = db.getAllIncome(); // Fetch data from DB
//            ArrayList<AddIncome1> finalData = new ArrayList<>(data);
//
//            runOnUiThread(() -> {
//                dataArrayList.clear();
//                dataArrayList.addAll(finalData);
//                listAdapter.notifyDataSetChanged(); // Update the list
//            });
//        }).start();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        loadIncomeData(); // Refresh data when resuming
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (db != null) {
//            db.close(); // Close the database to prevent memory leaks
//        }
//        binding = null; // Prevent memory leaks
//    }
//}
