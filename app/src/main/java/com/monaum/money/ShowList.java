package com.monaum.money;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.monaum.money.adapters.UserAdapter;
import com.monaum.money.databinding.ActivityMainBinding;
import com.monaum.money.databinding.ActivityShowListBinding;
import com.monaum.money.dbUtill.Database;
import com.monaum.money.entity.Users;

import java.util.ArrayList;

public class ShowList extends AppCompatActivity {

    ActivityShowListBinding binding;

    ArrayList<Users> dataArrayList = new ArrayList<>();
    UserAdapter listAdapter;
    Users listData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

//        setContentView(R.layout.activity_main);
        binding = ActivityShowListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Database db = new Database(getApplicationContext());
        dataArrayList= (ArrayList<Users>) db.getAllUsers();

        listAdapter = new UserAdapter(ShowList.this, dataArrayList);
        binding.listview.setAdapter(listAdapter);
        binding.listview.setClickable(true);
        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText(ShowList.this, dataArrayList.get(i).toString(), Toast.LENGTH_LONG).show();

//                Intent intent = new Intent(MainActivity.this, Detailed.class);
//                intent.putExtra("name", nameList[i]);
//                intent.putExtra("time", timeList[i]);
//                intent.putExtra("ingredients", ingredientList[i]);
//                intent.putExtra("desc", descList[i]);
//                intent.putExtra("image", imageList[i]);
//                startActivity(intent);
            }
        });
    }
}