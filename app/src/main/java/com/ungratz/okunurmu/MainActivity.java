package com.ungratz.okunurmu;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ungratz.okunurmu.databinding.MainActivityBinding;
import com.ungratz.okunurmu.fragments.ProfileFragment;
import com.ungratz.okunurmu.fragments.SearchFragment;

public class MainActivity extends FragmentActivity {

    private MainActivity ma = this;

    private ProfileFragment pf;
    private SearchFragment sf;

    private FragmentManager fm = getSupportFragmentManager();
    private FragmentTransaction ft = fm.beginTransaction();

    private MainActivityBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = MainActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pf = new ProfileFragment();
        sf = new SearchFragment();
        ft.replace(R.id.main_fragment, pf).commit();

        binding.homeProfile.setOnClickListener(v -> ft.replace(R.id.main_fragment, pf));

    }

}
