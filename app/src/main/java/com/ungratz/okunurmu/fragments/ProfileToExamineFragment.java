package com.ungratz.okunurmu.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ungratz.okunurmu.R;
import com.ungratz.okunurmu.databinding.FragmentCurrentuserprofileBinding;
import com.ungratz.okunurmu.singleton.CurrentUser;

import org.checkerframework.checker.nullness.qual.NonNull;

public class ProfileToExamineFragment extends Fragment {

    private FragmentCurrentuserprofileBinding binding;
    private Bitmap bm;
    private String id;

    private final int MAX_DOWNLOAD_BYTES = 1024*1024;

    public ProfileToExamineFragment(String idOfThisTutor){
        super(R.layout.fragment_currentuserprofile);
        id = idOfThisTutor;
    }

    @Nullable
    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCurrentuserprofileBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        binding.uploadPhoto.setVisibility(View.INVISIBLE);
        binding.deletePhoto.setVisibility(View.INVISIBLE);
        binding.saveProfile.setVisibility(View.INVISIBLE);
        binding.bio.setEnabled(false);

        CurrentUser.getStorageRef().child(id+"/userProfilePic")
                .getBytes(MAX_DOWNLOAD_BYTES)
                .addOnSuccessListener(bytes -> {
                    bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    binding.currentUserProfilePic.setImageBitmap(bm);
                });

        CurrentUser.getUsersCollectionReference().document(id).get()
                .addOnCompleteListener(task -> {
                    binding.userName.setText("Real Name: " + task.getResult().getString("realName")
                    + "\nUser Name: " + task.getResult().getString("userName"));
                    binding.uniText.setVisibility(View.VISIBLE);
                    binding.uniText.setText("University: "+task.getResult().getString("university").toString());
                    binding.departmentText.setVisibility(View.VISIBLE);
                    binding.departmentText.setText("Department: " + task.getResult().getString("department").toString());
                    binding.bio.setText(task.getResult().getString("bio").toString());
                });

        showPersonalPhotos();
    }

    private int index = 0;
    private ImageView iv;
    public void showPersonalPhotos(){

        CurrentUser.getUsersCollectionReference().document(id).get()
                .addOnCompleteListener(task -> {

                    if (index != task.getResult().get("photoAmount", Integer.class)){

                        CurrentUser.getStorageRef().child(CurrentUser.getID()+"/"+index)
                                .getBytes(MAX_DOWNLOAD_BYTES)
                                .addOnSuccessListener(bytes -> {
                                    bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    iv = new ImageView(getContext());
                                    iv.setImageBitmap(bm);
                                    binding.linearLayoutForPhotos.addView(iv);
                                    index++;
                                    showPersonalPhotos();
                                });
                    }

                });

    }
}
