package com.ungratz.okunurmu.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.ungratz.okunurmu.R;
import com.ungratz.okunurmu.databinding.FragmentCurrentuserprofileBinding;
import com.ungratz.okunurmu.singleton.CurrentUser;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.units.qual.Current;

import java.util.concurrent.atomic.AtomicReference;

public class ProfileFragment extends Fragment {

    private FragmentCurrentuserprofileBinding binding;
    private Uri profilePicUri;
    private final int MAX_DOWNLOAD_SIZE = 2048*2048;

    public ProfileFragment(){

        super(R.layout.fragment_currentuserprofile);

    }


    @Nullable
    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCurrentuserprofileBinding.inflate(getLayoutInflater());
        //profilePicUri = this.getProfilePic();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){

        binding.currentUserProfilePic.setImageURI(
                Uri.parse("android.resource://com.ungratz.okunurmu/drawable/aby_photo"));
        getProfilePic();
        binding.userName.setText(CurrentUser.getUserName());
        binding.bio.setText(CurrentUser.getBio());

/*      Glide would be good but I am not a good programmer
        Glide.with(this)
                .load(CurrentUser.getStorageRef().child(CurrentUser.getID()+"/userProfilePic"))
                .placeholder(R.drawable.chat_avatar)
                .into(binding.currentUserProfilePic);
*/
        binding.saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentUser.updateBio(binding.bio.getText().toString());
            }
        });
    }

    public void getProfilePic(){

        CurrentUser.getStorageRef().child(CurrentUser.getID()+"/userProfilePic")
                .getBytes(MAX_DOWNLOAD_SIZE)
                .addOnSuccessListener(bytes -> {
                    Bitmap bm = BitmapFactory.decodeByteArray(bytes,0, bytes.length);
                    binding.currentUserProfilePic.setImageBitmap(bm);
                })
                .addOnFailureListener(e -> binding.currentUserProfilePic.setImageURI(Uri.parse("android.resource://com.ungratz.okunurmu/drawable/chat_avatar")));
    }

    public void getPersonalPhotos(){

    }
}