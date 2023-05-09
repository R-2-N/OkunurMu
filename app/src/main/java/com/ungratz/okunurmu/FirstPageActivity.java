package com.ungratz.okunurmu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class FirstPageActivity extends AppCompatActivity {


        ImageView imageViewStudent;
        ImageView imageViewMentor;
        ImageView imageViewLogIn;

        @SuppressLint("MissingInflatedId")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.signup_formentors_page);

            // initialize imageView
            // with method findViewById()
            imageViewStudent = findViewById(R.id.createStudent);

            // Apply OnClickListener  to imageView to
            // switch from one activity to another
            imageViewStudent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Intent class will help to go to next activity using
                    // it's object named intent.
                    // SecondActivty is the name of new created EmptyActivity.
                    Intent intent = new Intent(com.ungratz.okunurmu.FirstPageActivity.this, StudentSignUpActivity.class);
                    startActivity(intent);
                }
            });

            imageViewMentor = findViewById(R.id.createMentor);

            imageViewMentor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(com.ungratz.okunurmu.FirstPageActivity.this, MentorSignUpActivity.class);
                    startActivity(intent);
                }
            });

            imageViewLogIn = findViewById(R.id.loginView);

            imageViewLogIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(com.ungratz.okunurmu.FirstPageActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
        }
}