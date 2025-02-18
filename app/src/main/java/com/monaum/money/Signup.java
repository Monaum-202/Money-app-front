package com.monaum.money;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.monaum.money.dbUtill.Database;
import com.monaum.money.entity.Users;

import java.util.Calendar;

public class Signup extends AppCompatActivity {

    private EditText username, email,password,confirmPassword,DOB;
    private Button signup;

    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);


        username =  findViewById(R.id.name);
        email =  findViewById(R.id.email);
        password =  findViewById(R.id.password);
        confirmPassword =  findViewById(R.id.confirmPassword);
        DOB =  findViewById(R.id.DOB);
        signup = findViewById(R.id.signin);
        login = findViewById(R.id.login);


        DOB.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            // Open the DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(Signup.this,
                    (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                        // Set the selected date in the EditText
                        String selectedDate = selectedDayOfMonth + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        DOB.setText(selectedDate);
                    }, year, month, dayOfMonth);
            datePickerDialog.show();
        });


        signup.setOnClickListener(v -> {
            String name = username.getText().toString();
            String mail = email.getText().toString();
            String pass = password.getText().toString();
            String cpass = confirmPassword.getText().toString();
            String dateofbirth = DOB.getText().toString();


            // Validate fields
            if (name.isEmpty() || mail.isEmpty() || pass.isEmpty()|| cpass.isEmpty() || dateofbirth.isEmpty()) {
                Toast.makeText(Signup.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            try {

                Database db = new Database(getApplicationContext());
                Users users =  new Users(name,mail,pass,cpass,dateofbirth);
                db.insertUser(users);
                
            }catch (Exception e){
                e.printStackTrace();
            }


            // Example: After sign up is successful, move to login screen or home screen
            Toast.makeText(Signup.this, "Sign up successful!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Signup.this, Login.class));  // Assume HomeActivity is your landing page
        });

        // Handle Login button click (navigate to login page)
        login.setOnClickListener(v -> {
            startActivity(new Intent(Signup.this, Login.class));
        });
    }
    }