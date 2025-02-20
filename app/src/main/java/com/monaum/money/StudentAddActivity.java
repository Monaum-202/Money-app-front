package com.monaum.money;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.monaum.money.entity.Student;

import java.util.Arrays;
import java.util.List;

public class StudentAddActivity extends AppCompatActivity {

    private EditText editTextName, editTextUsername, editTextPassword;
    private Spinner spinnerGrade;
    private RadioGroup radioGroupGender;
    private CheckBox checkBoxSports, checkBoxMusic, checkBoxArt;
    private Button buttonSubmit;

    private String selectedGrade;
    private String selectedGender;

    private Student currentStudent; // Holds the student if editing
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_add);




        editTextName = findViewById(R.id.editTextName);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        spinnerGrade = findViewById(R.id.spinnerGrade);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        checkBoxSports = findViewById(R.id.checkBoxSports);
        checkBoxMusic = findViewById(R.id.checkBoxMusic);
        checkBoxArt = findViewById(R.id.checkBoxArt);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        // Define a List of grades
        List<String> gradesList = Arrays.asList("Grade 1", "Grade 2", "Grade 3", "Grade 4", "Grade 5");

        // Load grades into Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gradesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGrade.setAdapter(adapter);

        spinnerGrade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGrade = gradesList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedGrade = null;
            }
        });

        // Check if editing an existing student
        if (getIntent().hasExtra("student")) {
            isEditMode = true;
            currentStudent = (Student) getIntent().getSerializableExtra("student");
            populateFields();
            buttonSubmit.setText("Update Student"); // Change button text for edit mode
        }

        buttonSubmit.setOnClickListener(view -> submitForm());
    }

    private void populateFields() {
        if (currentStudent != null) {
            editTextName.setText(currentStudent.getName());
            editTextUsername.setText(currentStudent.getUsername());
            editTextPassword.setText(currentStudent.getPassword());

            // Set Spinner selection
            List<String> gradesList = Arrays.asList("Grade 1", "Grade 2", "Grade 3", "Grade 4", "Grade 5");
            int gradePosition = gradesList.indexOf(currentStudent.getUsername());
            if (gradePosition != -1) {
                spinnerGrade.setSelection(gradePosition);
            }

            // Set Gender selection
            for (int i = 0; i < radioGroupGender.getChildCount(); i++) {
                RadioButton radioButton = (RadioButton) radioGroupGender.getChildAt(i);
                if (radioButton.getText().toString().equals(currentStudent.getUsername())) {
                    radioButton.setChecked(true);
                    break;
                }
            }

            // Set checkboxes (assuming you store hobbies as comma-separated values)
            String[] hobbies = currentStudent.getUsername().split(",");
            for (String hobby : hobbies) {
                if (hobby.trim().equalsIgnoreCase("Sports")) checkBoxSports.setChecked(true);
                if (hobby.trim().equalsIgnoreCase("Music")) checkBoxMusic.setChecked(true);
                if (hobby.trim().equalsIgnoreCase("Art")) checkBoxArt.setChecked(true);
            }
        }
    }

    private void submitForm() {
        String name = editTextName.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Get selected gender
        int selectedId = radioGroupGender.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedId);
            selectedGender = selectedRadioButton.getText().toString();
        }

        // Get selected hobbies
        StringBuilder hobbies = new StringBuilder();
        if (checkBoxSports.isChecked()) hobbies.append("Sports, ");
        if (checkBoxMusic.isChecked()) hobbies.append("Music, ");
        if (checkBoxArt.isChecked()) hobbies.append("Art, ");

        // Trim trailing comma
        String hobbiesStr = hobbies.toString().replaceAll(", $", "");

        if (isEditMode) {
            // Update existing student
            currentStudent.setName(name);
            currentStudent.setUsername(username);
            currentStudent.setPassword(password);

            Toast.makeText(this, "Student Updated!", Toast.LENGTH_SHORT).show();
        } else {
            // Create new student
            Student newStudent = new Student(name, username, password);
            System.out.println("New Student: " + newStudent);
            Toast.makeText(this, "Student Added!", Toast.LENGTH_SHORT).show();
        }

        // Finish activity
//        finish();
    }
}