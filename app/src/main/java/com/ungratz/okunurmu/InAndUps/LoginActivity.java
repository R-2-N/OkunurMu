package com.ungratz.okunurmu.InAndUps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.ungratz.okunurmu.MainActivity;
import com.ungratz.okunurmu.databinding.LoginPageBinding;
import com.ungratz.okunurmu.singleton.CurrentUser;

public class LoginActivity extends Activity {
    private CurrentUser currentUser = CurrentUser.getInstance();
    private LoginActivity la = this;
    private FirebaseAuth mAuth;
    private LoginPageBinding binding;

    private static final String TAG = "EmailPassword";

    private String email;
    private String password;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LoginPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.loginView.setOnClickListener(v -> {
            email = binding.emailLogIn.getText().toString();
            password = binding.passwordLogIn.getText().toString();
            signIn(email, password);
        });

        binding.backButtonLogin.setOnClickListener(v -> {
            Intent intent = new Intent(la, FirstPageActivity.class);
            startActivity(intent);
        });

    }

    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        currentUser.setFirebaseUser(user);
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        // [END sign_in_with_email]
    }

    public void updateUI(FirebaseUser user){

        CurrentUser.getUserDocumentRef().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {

                    CurrentUser.setRealName(task.getResult().getString("realName"));
                    CurrentUser.setUserName(task.getResult().getString("userName"));
                    CurrentUser.setBio(task.getResult().getString("bio"));
                    CurrentUser.setAmountOfPersonalPhotos(task.getResult().get("photoAmount", Integer.class));

                    if (task.getResult().getBoolean("isMentor")) {
                        CurrentUser.setUniversity(task.getResult().getString("university"));
                        CurrentUser.setDepartment(task.getResult().getString("department"));
                    }

                    Intent intent = new Intent(la, MainActivity.class);
                    startActivity(intent);

                } else {
                    Log.d("Bazinga", "No such document");
                }
            } else {
                Log.d("Bazinga", "get failed with ", task.getException());
            }
        });
    }
}
