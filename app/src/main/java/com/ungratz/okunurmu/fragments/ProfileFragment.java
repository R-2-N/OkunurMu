package com.ungratz.okunurmu.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ungratz.okunurmu.R;
import com.ungratz.okunurmu.databinding.FragmentCurrentuserprofileBinding;
import com.ungratz.okunurmu.singleton.CurrentUser;

import org.checkerframework.checker.nullness.qual.NonNull;

public class ProfileFragment extends Fragment {

    private final int GALLERY_CODE = 1000;
    private FragmentCurrentuserprofileBinding binding;
    private Uri profilePicUri;
    private final int MAX_DOWNLOAD_SIZE = 2048*2048;
    private Intent photoPicking = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);


    ActivityResultLauncher<Intent> ppUploading =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
                    , new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK){
                                binding.currentUserProfilePic.setImageURI(result.getData().getData());
                                CurrentUser.changeProfilePicOnStorage(result.getData().getData());
                            }
                        }
                    });



    ActivityResultLauncher<Intent> personalPhotoUploading =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
                    , new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK){
                                CurrentUser.uploadPersonalPhotoToStorage(result.getData().getData());
                                showPersonalPhotos();
                            }
                        }
                    });



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

        binding.currentUserProfilePic.setImageURI(
                Uri.parse("android.resource://com.ungratz.okunurmu/drawable/aby_photo"));
        putProfilePic();
        binding.userName.setText(CurrentUser.getUserName());
        binding.bio.setText(CurrentUser.getBio());
        if (CurrentUser.getAmountOfPersonalPhotos()!=0){showPersonalPhotos();}
/*      Glide would be good but I am not a good programmer
        Glide.with(this)
                .load(CurrentUser.getStorageRef().child(CurrentUser.getID()+"/userProfilePic"))
                .placeholder(R.drawable.chat_avatar)
                .into(binding.currentUserProfilePic);
*/
        binding.saveProfile.setOnClickListener(v -> {
            CurrentUser.updateBio(binding.bio.getText().toString());
            binding.savedText.setVisibility(View.VISIBLE);
            new Handler().postDelayed(() -> binding.savedText.setVisibility(View.INVISIBLE), 3000);
        });
        binding.currentUserProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNewPP();
            }
        });

        binding.uploadPhoto.setOnClickListener(v -> addPersonalPhoto());
    }

    public void putProfilePic(){

        CurrentUser.getStorageRef().child(CurrentUser.getID()+"/userProfilePic")
                .getBytes(MAX_DOWNLOAD_SIZE)
                .addOnSuccessListener(bytes -> {
                    Bitmap bm = BitmapFactory.decodeByteArray(bytes,0, bytes.length);
                    binding.currentUserProfilePic.setImageBitmap(bm);
                })
                .addOnFailureListener(e -> binding.currentUserProfilePic.setImageURI(Uri.parse("android.resource://com.ungratz.okunurmu/drawable/chat_avatar")));
    }

    public void setNewPP(){
        ppUploading.launch(photoPicking);
    }


    // Stuff related to personal photos is not the hard but long
    // and rest of the class is dedicated to that

    public void addPersonalPhoto(){
        personalPhotoUploading.launch(photoPicking);
    }

    private int index = 0;
    public void showPersonalPhotos(){
        if (index==CurrentUser.getAmountOfPersonalPhotos()){
            index=0;
            return;
        }
        CurrentUser.getStorageRef().child(CurrentUser.getID()+"/"+index)
                .getBytes(MAX_DOWNLOAD_SIZE)
                .addOnSuccessListener(bytes -> {
                    ImageView iv = new ImageView(getContext());
                    iv.setImageBitmap(BitmapFactory.decodeByteArray(bytes,0,bytes.length));
                    binding.linearLayoutForPhotos.addView(iv);
                    index++;

                    showPersonalPhotos();
                });
    }
}