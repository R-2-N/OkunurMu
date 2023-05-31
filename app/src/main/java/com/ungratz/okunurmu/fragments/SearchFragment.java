package com.ungratz.okunurmu.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ungratz.okunurmu.R;
import com.ungratz.okunurmu.databinding.FragmentSearchBinding;

import org.checkerframework.checker.nullness.qual.NonNull;

public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;

    private final String[] filterOptions = {"Name", "University", "Department"};

    public SearchFragment(){
        super(R.layout.fragment_search);
    }

    @Nullable
    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){

        ArrayAdapter<String> filterAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, filterOptions);
        binding.searchSpinner.setAdapter(filterAdapter);
        binding.searchSpinner.setPrompt("Set Filter");


        /*
        ConstraintLayout cl = (ConstraintLayout) getLayoutInflater().inflate(R.layout.profile_preview, binding.linearLayoutForSearch, false);
        TextView t = cl.findViewById(R.id.mentorNameForSearch);
        t.setText("Amogus");

        binding.linearLayoutForSearch.addView(cl);

        ConstraintLayout cl2 = (ConstraintLayout) getLayoutInflater().inflate(R.layout.profile_preview, binding.linearLayoutForSearch, false);
        TextView t2 = cl2.findViewById(R.id.mentorNameForSearch);
        t2.setText("amogus2electricboogaloo");

        View v = cl2.findViewById(R.id.sendMessageToTutorView);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView t12 = cl2.findViewById(R.id.sendMeetingRequestToTutorText);
                t12.setText("amongusboogaloo");
            }
        });

        binding.linearLayoutForSearch.addView(cl2);
        */
    }




}
