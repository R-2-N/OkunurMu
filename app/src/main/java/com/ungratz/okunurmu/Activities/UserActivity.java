package com.ungratz.okunurmu.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.google.firebase.firestore.FirebaseFirestore;
import com.ungratz.okunurmu.R;
import com.ungratz.okunurmu.databinding.ActivityUserBinding;

public class UserActivity extends AppCompatActivity {

    private ActivityUserBinding binding;
    private PreferenceManager preferenceManager;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new androidx.preference.PreferenceManager(getApplicationContext());
        getUsers();
        setListeners();
    }

    protected void setListeners() { binding. }

    protected void getUsers() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(StringsNet.USERS_KEY)
            .get().addOnCompleteListener(task -> {
                String currentUserID = preferenceManager.getStr
                        }

                )
    }
}