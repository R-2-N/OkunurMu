package com.ungratz.okunurmu.InAndUps;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ungratz.okunurmu.LoginActivity;
import com.ungratz.okunurmu.databinding.FirstPageActivityBinding;

public class FirstPageActivity extends AppCompatActivity {

    private FirstPageActivity fpa = this;
    private FirstPageActivityBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FirstPageActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.createStudent.setOnClickListener(v -> {
            Intent intent = new Intent(fpa, StudentSignUpActivity.class);
            startActivity(intent);
        });

        binding.createMentor.setOnClickListener(v -> {
            Intent intent = new Intent(fpa, MentorSignUpActivity.class);
            startActivity(intent);
        });

        binding.loginView.setOnClickListener(v -> {
            Intent intent = new Intent(fpa, LoginActivity.class);
            startActivity(intent);
        });
    }
}