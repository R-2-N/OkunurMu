package com.ungratz.okunurmu.InAndUps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ungratz.okunurmu.MainActivity;
import com.ungratz.okunurmu.databinding.SignupFormentorsPageBinding;
import com.ungratz.okunurmu.singleton.CurrentUser;

public class MentorSignUpActivity extends Activity {

    private static FirebaseUser user;
    private MentorSignUpActivity msa = this;
    private SignupFormentorsPageBinding binding;

    private static final String TAG = "EmailPassword";
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private String name;
    private String userName;
    private String uniName;
    private String departmentName;
    private String email;
    private String password;
    private String passwordAgain;

    private String[] uniMailNameCheck =
            {"ug.bilkent.edu.tr", "sabaniuniv.edu", "itü.edu.tr",
            "hacettepe.edu.tr", "ku.edu.tr"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SignupFormentorsPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        binding.mentorSignUpView.setOnClickListener(v -> {

            name = binding.nameMentorSignUp.getText().toString();
            userName = binding.usernameMentorSignUp.getText().toString();
            uniName = binding.uniMentorSignUp.getText().toString();
            departmentName = binding.departmentMentorSignUp.getText().toString();
            email = binding.emailMentorSignUp.getText().toString();
            password = binding.passwordMentorSignUp.getText().toString();
            passwordAgain = binding.passwordAgainMentorSignUp.getText().toString();

            boolean uniReal = checkIfUniExists(email);

            if (((password.equals(passwordAgain)) && (password!="")) && uniReal) {
                createAccount(email, password);
            }

        });

        binding.backButtonMentor.setOnClickListener(v -> {

            Intent intent = new Intent(msa, FirstPageActivity.class);
            startActivity(intent);
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
                            Toast.makeText(MentorSignUpActivity.this, "Authentication failed.",
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
                    CurrentUser.setNewFirebaseUser(user, name, userName, true, uniName, departmentName);
                    updateUI(user);
                });
        // [END send_email_verification]
    }

    private boolean checkIfUniExists(String e){
        for (int i = 0; i < uniMailNameCheck.length; i++){
            if (e.contains(uniMailNameCheck[i])){
                return true;
            }
        }
        return false;
    }

    private void reload() { }

    private void updateUI(FirebaseUser user) {
        Intent intent = new Intent(msa, MainActivity.class);
        startActivity(intent);
    }
}
