package com.ungratz.okunurmu.InAndUps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.ungratz.okunurmu.databinding.FirstPageActivityBinding;

public class FirstPageActivity extends AppCompatActivity {

    private FirstPageActivity fpa = this;
    private FirstPageActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FirstPageActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.createStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fpa, StudentSignUpActivity.class);
                FirstPageActivity.this.startActivity(intent);
            }
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