package com.ungratz.okunurmu.fragments;

import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.ungratz.okunurmu.R;
import com.ungratz.okunurmu.databinding.FragmentCurrentuserprofileBinding;
import com.ungratz.okunurmu.singleton.CurrentUser;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.units.qual.Current;

public class ProfileFragment extends Fragment {

    private FragmentCurrentuserprofileBinding binding;

    public ProfileFragment(){
        super(R.layout.fragment_currentuserprofile);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){

        binding.userName.setText(CurrentUser.getUserDocumentSnapshot().getString("userName"));

        Glide.with(getContext()).
                load(CurrentUser.getStorageRef().child(CurrentUser.getID()+"/userProfilePic")).
                into(binding.currentUserProfilePic);

    }


}