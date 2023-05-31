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
    private final int MAX_DOWNLOAD_SIZE = 2048*2048;
    private Intent photoPicking = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
    private Bitmap bm;
    private boolean photosAreClickable = false;


    ActivityResultLauncher<Intent> ppUploading =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
                    , new ActivityResultCallback<ActivityResult>() {
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK){
                                binding.currentUserProfilePic.setImageURI(result.getData().getData());
                                CurrentUser.changeProfilePicOnStorage(result.getData().getData());
                            }
                        }
                    });


    ActivityResultLauncher<Intent> personalPhotoUploading =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
                    , result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            CurrentUser.getStorageRef().child(CurrentUser.getID()+"/"+CurrentUser.getAmountOfPersonalPhotos())
                                    .putFile(result.getData().getData())
                                    .addOnSuccessListener(taskSnapshot -> {
                                        ImageView iv = new ImageView(getContext());
                                        iv.setImageURI(result.getData().getData());
                                        iv.setClickable(false);
                                        binding.linearLayoutForPhotos.addView(iv);
                                        CurrentUser.updatePhotoAmountBy(1);
                                    });
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
        binding.userName.setText("User Name: " + CurrentUser.getUserName()
            + "\nReal Name: " + CurrentUser.getRealName());
        binding.bio.setText(CurrentUser.getBio());
        if (CurrentUser.getAmountOfPersonalPhotos()!=0){showPersonalPhotos();}

        if (CurrentUser.getIsMentor()==true){
            binding.uniText.setText("University:\n" + CurrentUser.getUniversity());
            binding.departmentText.setText("Department:\n" + CurrentUser.getDepartment());

            binding.uniText.setVisibility(View.VISIBLE);
            binding.departmentText.setVisibility(View.VISIBLE);

        }


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

        binding.currentUserProfilePic.setOnClickListener(v -> setNewPP());

        binding.uploadPhoto.setOnClickListener(v -> addPersonalPhoto());

        binding.deletePhoto.setOnClickListener(v -> {
            if (binding.deletePhoto.getText().toString().equals("Delete Photo")) {
                changePhotoClickability(true);
                binding.deletePhoto.setText("Cancel");
            }
            else {
                changePhotoClickability(false);
                binding.deletePhoto.setText("Delete Photo");
            }

        });
    }

    public void putProfilePic(){

        CurrentUser.getStorageRef().child(CurrentUser.getID()+"/userProfilePic")
                .getBytes(MAX_DOWNLOAD_SIZE)
                .addOnSuccessListener(bytes -> {
                    bm = BitmapFactory.decodeByteArray(bytes,0, bytes.length);
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
    public void addImageToLinearLayoutAsByteArray(byte[] bytes){
        ImageView iv = new ImageView(getContext());
        iv.setImageBitmap(BitmapFactory.decodeByteArray(bytes,0,bytes.length));

        iv.setClickable(false);
        binding.linearLayoutForPhotos.addView(iv);
    }
    private int index = 0;
    public void showPersonalPhotos(){

        if (index == 0){
            binding.linearLayoutForPhotos.removeAllViews();
        }
        if (index==CurrentUser.getAmountOfPersonalPhotos()){
            index=0;
            return;
        }
        CurrentUser.getStorageRef().child(CurrentUser.getID()+"/"+index)
                .getBytes(MAX_DOWNLOAD_SIZE)
                .addOnSuccessListener(bytes -> {
                    addImageToLinearLayoutAsByteArray(bytes);
                    index++;

                    showPersonalPhotos();
                });
    }

    private int pos = 0;
    public void deleteClickedPhoto(int positionOfViewToDelete){

        if (pos == CurrentUser.getAmountOfPersonalPhotos()){
            resultOfDeletion();
            pos = 0;
            binding.linearLayoutForPhotos.removeViewAt(positionOfViewToDelete);
            return;
        }
        if (pos == 0){
            pos = positionOfViewToDelete+1;
        }
        CurrentUser.getStorageRef().child(CurrentUser.getID()+"/"+pos)
                .getBytes(MAX_DOWNLOAD_SIZE)
                .addOnSuccessListener(bytes -> CurrentUser.getStorageRef().child(CurrentUser.getID() + "/" + (pos - 1))
                        .putBytes(bytes)
                        .addOnSuccessListener(taskSnapshot -> {
                            pos++;
                            deleteClickedPhoto(positionOfViewToDelete);
                        }));
    }

    public void resultOfDeletion(){
        CurrentUser.getStorageRef().child(CurrentUser.getID() + "/" + (CurrentUser.getAmountOfPersonalPhotos()-1))
                .delete()
                .addOnSuccessListener(unused -> {
                    changePhotoClickability(false);
                    CurrentUser.updatePhotoAmountBy(-1);
                    binding.deletePhoto.setText("Deleted");
                    new Handler().postDelayed(() -> {
                        binding.deletePhoto.setText("Delete Photo");
                        binding.deletePhoto.setClickable(true);
                    }, 2700);
                })
                .addOnFailureListener(e -> resultOfDeletion());
    }

    public void changePhotoClickability(boolean b){
        for (int i = 0; i < binding.linearLayoutForPhotos.getChildCount(); i++){
            int finalI = i;
            binding.linearLayoutForPhotos.getChildAt(i).setOnClickListener(v -> {
                binding.deletePhoto.setText("Deleting");
                binding.deletePhoto.setClickable(false);
                deleteClickedPhoto(finalI);
            });
            binding.linearLayoutForPhotos.getChildAt(i).setClickable(b);
        }
        if (b == true){
            photosAreClickable = true;
        }
    }
}