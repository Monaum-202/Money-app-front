package com.monaum.money.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.monaum.money.R;
import com.monaum.money.entity.AddIncome1;
import java.util.ArrayList;

public class HistoryAdapter extends ArrayAdapter<AddIncome1> {

    public HistoryAdapter(@NonNull Context context, ArrayList<AddIncome1> dataArrayList) {
        super(context, R.layout.history_item, dataArrayList); // Ensure correct layout reference
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        AddIncome1 listData = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.history_item, parent, false);
        }

        // Find views safely
        TextView listTime = convertView.findViewById(R.id.time);
        TextView listAmount = convertView.findViewById(R.id.amount);
        TextView listCategory = convertView.findViewById(R.id.category);
        TextView listWallet = convertView.findViewById(R.id.wallet);

        if (listData != null) {
            listTime.setText(listData.time != null ? listData.time : "--");
            listAmount.setText(listData.amount != null ? String.format("$%.2f", listData.amount) : "$0.00");
            listCategory.setText(listData.category != null ? listData.category : "N/A");
            listWallet.setText(listData.wallet != null ? listData.wallet : "N/A");
        }

        return convertView;
    }
}
