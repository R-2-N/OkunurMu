package com.ungratz.okunurmu.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.ungratz.okunurmu.R;
import com.ungratz.okunurmu.databinding.FragmentCurrentuserprofileBinding;
import com.ungratz.okunurmu.singleton.CurrentUser;

import org.checkerframework.checker.nullness.qual.NonNull;

public class ProfileFragment extends Fragment {

    private FragmentCurrentuserprofileBinding binding;

    public ProfileFragment(){

        super(R.layout.fragment_currentuserprofile);

    }


    @Nullable
    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCurrentuserprofileBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){

        binding.userName.setText(CurrentUser.getUserName());

        Glide.with(this)
                .load(CurrentUser.getStorageRef().child(CurrentUser.getID()+"/userProfilePic.png"))
                .placeholder(R.drawable.chat_avatar)
                .into(binding.currentUserProfilePic);

    }
}