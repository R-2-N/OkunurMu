package com.ungratz.okunurmu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.ungratz.okunurmu.databinding.LaunchPageBinding;
import com.ungratz.okunurmu.databinding.MainActivityBinding;

public class MainActivity extends Activity {

    private MainActivity ma = this;

    private MainActivityBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MainActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.homeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }



}
