package com.ungratz.okunurmu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ungratz.okunurmu.databinding.LaunchPageBinding;
import com.ungratz.okunurmu.databinding.MainActivityBinding;
import com.ungratz.okunurmu.fragments.ProfileFragment;
import com.ungratz.okunurmu.fragments.SearchFragment;

public class MainActivity extends FragmentActivity {


    private MainActivity ma = this;

    private ProfileFragment pf = new ProfileFragment();
    private SearchFragment sf = new SearchFragment();

    private FragmentManager fm = getSupportFragmentManager();
    private FragmentTransaction ft = fm.beginTransaction();

    private MainActivityBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MainActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ft.replace(R.id.main_fragment, pf);
        ft.commit();

        binding.homeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ft.replace(R.id.main_fragment, pf);
            }
        });

    }



}
