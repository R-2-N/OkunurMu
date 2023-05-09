package com.ungratz.okunurmu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class FirstPageActivity extends AppCompatActivity {

    FirstPageActivity fpa = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_page_activity);

        ImageView createStudent = findViewById(R.id.createStudent);
        ImageView createMentor = findViewById(R.id.createMentor);
        ImageView login = findViewById(R.id.loginView);

        createStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fpa, StudentSignUpActivity.class);
                startActivity(intent);
            }
        });

        createMentor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fpa, MentorSignUpActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fpa, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}