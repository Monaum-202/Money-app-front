package com.monaum.money;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class Login extends AppCompatActivity {


    private EditText name, password;
    private Button btnLogin;
    private TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);

        btnLogin = findViewById(R.id.login);

        tvRegister = findViewById(R.id.register);


        btnLogin.setOnClickListener(v -> {

            String username = name.getText().toString();
            String pass = password.getText().toString();


            // Validate fields
            if (username.isEmpty() || pass.isEmpty()) {
                Toast.makeText(Login.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                Database db = new Database(getApplicationContext());
                int val = db.loginUser(username, pass);

                if (val < 1) {
                    Toast.makeText(Login.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            // Example: After sign up is successful, move to login screen or home screen
            Toast.makeText(Login.this, "Log in successful!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Login.this, MainActivity.class));  // Assume HomeActivity is your landing page
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
            }
        });

    }
}