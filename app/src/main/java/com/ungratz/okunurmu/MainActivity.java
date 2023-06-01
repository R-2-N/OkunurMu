package com.ungratz.okunurmu;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ungratz.okunurmu.databinding.MainActivityBinding;
import com.ungratz.okunurmu.fragments.Chat;
import com.ungratz.okunurmu.fragments.ChatFragment;
import com.ungratz.okunurmu.fragments.MeetingsFragment;
import com.ungratz.okunurmu.fragments.ProfileFragment;
import com.ungratz.okunurmu.fragments.ProfileToExamineFragment;
import com.ungratz.okunurmu.fragments.SearchFragment;

public class MainActivity extends FragmentActivity {

    private MainActivity ma = this;

    private ProfileFragment pf;
    private SearchFragment sf;

    private MeetingsFragment mf;

    private ChatFragment cf;

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
        cf = new ChatFragment();
        mf = new MeetingsFragment();


        ft.replace(R.id.main_fragment, pf).commit();

        binding.homeProfile.setOnClickListener(v -> {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.main_fragment, pf).commit();
            binding.fragmentNameShowcase.setText("Profile");
        });

        binding.searchIcon.setOnClickListener(v -> {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.main_fragment, sf).commit();
            binding.fragmentNameShowcase.setText("Search");
        });

        binding.meetingsIcon.setOnClickListener(v -> {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.main_fragment, mf).commit();
            binding.fragmentNameShowcase.setText("Meets");
        });

        binding.chatIcon.setOnClickListener(v -> {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.main_fragment, cf).commit();
            binding.fragmentNameShowcase.setText("Chat");
        });
    }

    public void switchToProfileExamine(String idOfTutor){
        ProfileToExamineFragment ptef = new ProfileToExamineFragment(idOfTutor);
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.main_fragment, ptef).commit();
        binding.fragmentNameShowcase.setText("Tutor");
    }

    public void switchToChat(String idOfChatter){
        Chat c = new Chat(idOfChatter);
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.main_fragment, c).commit();
        binding.fragmentNameShowcase.setText("Tutor");
    }

}
