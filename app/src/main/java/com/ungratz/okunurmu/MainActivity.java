package com.ungratz.okunurmu;

import android.app.Activity;
import android.os.Bundle;

import com.ungratz.okunurmu.databinding.LaunchPageBinding;

public class MainActivity extends Activity {

    private MainActivity ma = this;

    private LaunchPageBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LaunchPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



    }



}
