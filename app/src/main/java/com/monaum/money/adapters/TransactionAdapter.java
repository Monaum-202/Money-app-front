package com.monaum.money.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.monaum.money.R;
import com.monaum.money.entity.Transaction;

import java.util.ArrayList;

public class TransactionAdapter extends ArrayAdapter<Transaction> {

    public TransactionAdapter(@NonNull Context context, ArrayList<Transaction> transactionList) {
        super(context, R.layout.history_item, transactionList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Transaction transaction = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.history_item, parent, false);
        }

        // Find views safely
        TextView listDate = convertView.findViewById(R.id.date);
        TextView listTime = convertView.findViewById(R.id.time);
        TextView listAmount = convertView.findViewById(R.id.amount);
        TextView listCategory = convertView.findViewById(R.id.category);
        TextView listWallet = convertView.findViewById(R.id.wallet);

        if (transaction != null) {
            listDate.setText(transaction.date != null ? transaction.date : "--");
            listTime.setText(transaction.time != null ? transaction.time : "--");
            listAmount.setText(transaction.amount != 0 ? String.format("$%.2f", transaction.amount) : "$0.00");
            listCategory.setText(transaction.category != null ? transaction.category : "N/A");
            listWallet.setText(transaction.wallet != null ? transaction.wallet : "N/A");

            // 🔴 Change text color based on transaction type
            if ("income".equals(transaction.type)) {
                listAmount.setTextColor(ContextCompat.getColor(getContext(), R.color.green)); // Green for income
            } else {
                listAmount.setTextColor(ContextCompat.getColor(getContext(), R.color.red)); // Red for expense
            }
        }

        return convertView;
    }
}
