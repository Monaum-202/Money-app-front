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
import com.monaum.money.entity.Users;

import java.util.ArrayList;

public class UserAdapter extends ArrayAdapter<Users> {
    public UserAdapter(@NonNull Context context, ArrayList<Users> dataArrayList) {
        super(context, R.layout.list_item, dataArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        Users listData = getItem(position);
        if (view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        TextView listName = view.findViewById(R.id.listName);
        TextView listEmail = view.findViewById(R.id.listEmail);
        TextView listDob = view.findViewById(R.id.listDob);

        listName.setText(listData != null ? listData.username : "");
        listEmail.setText(listData != null ? listData.email : "");
        listDob.setText(listData != null ? listData.dob : "");

        return view;
    }
}