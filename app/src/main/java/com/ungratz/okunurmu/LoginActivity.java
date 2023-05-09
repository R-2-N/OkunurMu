package com.ungratz.okunurmu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginActivity extends Activity {
    TextView imageViewLogIn;
    ImageView textViewForgotPassword;
    ImageView imageViewBackButton;

    @SuppressLint("WrongViewCast")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //TODO: Check if email and password match.
        super.onCreate(savedInstanceState);

        imageViewLogIn = findViewById(R.id.loginView);
        imageViewLogIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.ungratz.okunurmu.LoginActivity.this, HomeFragment.class);
                startActivity(intent);
            }
        });

        textViewForgotPassword = findViewById(R.id.forgotPasswordView);

        textViewForgotPassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.ungratz.okunurmu.LoginActivity.this, HomeFragment.class);
                //TODO: Shouldn't be home fragment but we don't have an activity for "Forgot password?" yet.
                startActivity(intent);
            }
        });

        imageViewBackButton = findViewById(R.id.backButton);

        imageViewBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.ungratz.okunurmu.LoginActivity.this, FirstPageActivity.class);
                startActivity((intent));
            }
        });
    }
}
