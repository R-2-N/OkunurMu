/**
 * Copyright 2021 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ungratz.okunurmu.InAndUps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ungratz.okunurmu.MainActivity;
import com.ungratz.okunurmu.R;

import com.ungratz.okunurmu.databinding.SignupForstudentsPageBinding;
import com.ungratz.okunurmu.singleton.CurrentUser;

public class StudentSignUpActivity extends Activity {

    private static FirebaseUser user;
    private StudentSignUpActivity ssa = this;
    private SignupForstudentsPageBinding binding;

    private static final String TAG = "EmailPassword";
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private String name;
    private String userName;
    private String email;
    private String password;
    private String passwordAgain;

    private EditText passwordAgainText;
    private EditText passwordText;
    private EditText emailText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SignupForstudentsPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        passwordAgainText = findViewById(R.id.passwordAgainStudentSignUp);
        passwordText = findViewById(R.id.passwordStudentSignUp);
        emailText = findViewById(R.id.emailStudentSignUp);

        binding.studentSignUpView.setOnClickListener(v -> {

            name = binding.nameStudentSignUp.getText().toString();
            userName = binding.usernameStudentSignUp.getText().toString();
            email = binding.emailStudentSignUp.getText().toString();
            password = binding.passwordStudentSignUp.getText().toString();
            passwordAgain = binding.passwordAgainStudentSignUp.getText().toString();


            if(password.trim().length()<8){
                passwordText.setError("Too short for a password");
            }
            if(password.trim().length()==0)
            {
                passwordText.setError("Field cannot be left blank!");
            }
            if(passwordAgain.trim().length()==0) {
                passwordAgainText.setError("Field cannot be left blank!");
            }
            if(!password.trim().equals(passwordAgain.trim())){
                passwordAgainText.setError("Passwords do not match!");
            }

            if ((password.equals(passwordAgain)) && (password.trim().length()!=0)&& password.trim().length()>=8 && passwordAgain.trim().length()>=8) {
                createAccount(email, password);
            }

        });

        binding.backButtonStudent.setOnClickListener(v -> {

            updateUI(null);

        });
    }


    // [START on_start_check_user]
    /*
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }
    // [END on_start_check_user]
    */



    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            user = mAuth.getCurrentUser();
                            sendEmailVerification(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(StudentSignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END create_user_with_email]
    }


    private void sendEmailVerification(FirebaseUser user) {
        // Send verification email
        // [START send_email_verification]
        user.sendEmailVerification()
                .addOnCompleteListener(this, task -> {
                    CurrentUser.getInstance();
                    CurrentUser.setNewFirebaseUser(user, name, userName, false, "", "");
                    updateUI(user);
                });
        // [END send_email_verification]
    }


    private void reload() { }

    private void updateUI(FirebaseUser user) {

        try {
            Intent intent = new Intent(ssa, FirstPageActivity.class);
            startActivity(intent);
        }
        catch (Exception e){
            updateUI(user);
        }
    }
}
