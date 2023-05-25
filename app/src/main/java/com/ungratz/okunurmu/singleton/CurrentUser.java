package com.ungratz.okunurmu.singleton;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class CurrentUser {
    public static CurrentUser u;
    public CurrentUser(){}

    public static CurrentUser getInstance(){
        if (u==null){
            u = new CurrentUser();
        }
        return u;
    }

    private static FirebaseFirestore ff = FirebaseFirestore.getInstance();
    private static FirebaseUser user;
    private static DocumentReference dr;

    // I actually cannot store DocumentSnapshot. I have to call .addOnCompleteListener
    // everytime on the DocumentReference
    private static DocumentSnapshot ds;
    private static FirebaseStorage fs = FirebaseStorage.getInstance();
    private static StorageReference sr;
    private static String id;
    private static String realName;
    private static String mail;
    private static String userName;
    private static boolean isMentor;
    private static String university;
    private static String department;
    private static String bio;


    public static void setFirebaseUser(FirebaseUser u){
        user = u;
        setID(user.getUid());
        ff = FirebaseFirestore.getInstance();
        /*
        Quick tutorial on how to get field data from Firestore
        Do what I do in lines 63 to 78
        */

        setUserDocumentRef(ff.collection("users").document(getID()));

        setStorageRef(fs.getReference());
        setMail(user.getEmail());

        setDefaultProfilePicOnStorage();
    }

    public static void setNewFirebaseUser
            (FirebaseUser u, String userRealName, String userName, boolean isItMentor, String university, String department){
        setIsMentor(isItMentor);

        //writing the info into the database
        ff.collection("users").document(u.getUid()).
                set(createUserHashMap(userRealName, userName, u.getEmail(), isMentor, university, department))
                .addOnSuccessListener(unused -> {
                    Log.d("W", "Data is written");
                    setFirebaseUser(u);
                })
                .addOnFailureListener(e -> Log.w("L", "DocumentSnapshot could not be retrieved"));
    }



    //setters
    public static void setID(String i){id = i;}
    public static void setUserDocumentRef(DocumentReference d){dr = d;}
    public static void setUserDocumentSnapshot(DocumentSnapshot d){ds = d;}
    public static void setStorageRef(StorageReference s){sr = s;}
    public static void setRealName(String n){realName = n;}
    public static void setUserName(String u){userName = u;}
    public static void setMail(String m){mail = m;}
    public static void setIsMentor(boolean b){isMentor = b;}
    public static void setUniversity(String u){university = u;}
    public static void setDepartment(String d){department = d;}
    public static void setBio(String b){bio=b;}


    //getters
    public static FirebaseUser getUser(){return user;}
    public static String getID(){return id;}
    public static DocumentReference getUserDocumentRef(){return dr;}
    public static DocumentSnapshot getUserDocumentSnapshot(){return ds;}
    public static StorageReference getStorageRef(){return sr;}
    public static String getRealName(){return realName;}
    public static String getUserName(){return userName;}
    public static String getMail(){return mail;}
    public static boolean getIsMentor(){return isMentor;}
    public static String getUniversity(){return university;}
    public static String getDepartment(){return department;}
    public static String getBio(){return bio;}

    public static void updateBio(String b){
        ff.collection("users").document(getID()).update("bio", b)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        setBio(b);
                        //code for writing that they were successfull in updating their bio
                    }
                });
    }

    //I think this is redundant bc of glide
    public static Uri getProfilePic(){
        AtomicReference<Uri> sUri = null;
        getStorageRef().child(getID()+"/userProfilePic").getDownloadUrl()
                .addOnSuccessListener(uri -> sUri.set(uri))
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        return sUri.get();
    }


    public static Map<String, Object>
    createUserHashMap(String rn, String un, String e, boolean b, String uniN, String d){
        Map<String, Object> newUserMap = new HashMap<>();
        newUserMap.put("realName", rn);
        newUserMap.put("userName", un);
        newUserMap.put("email", e);
        newUserMap.put("isMentor", b);
        newUserMap.put("bio", "");
        if (b==true){
            newUserMap.put("university", uniN);
            newUserMap.put("department", d);
        }

        return newUserMap;
    }

    public static void uploadPersonalPhotoToStorage(Uri u){

        String[] s = u.toString().split("/");
        getStorageRef().child(getID() + "/" + s[s.length-1]).putFile(u)
                .addOnCompleteListener(task -> Log.d("Upload:", "success"))
                .addOnFailureListener(e -> Log.w("Upload:", "failed"));
    }

    public static void setDefaultProfilePicOnStorage(){
        Uri uri = Uri.parse("android.resource://com.ungratz.okunurmu/drawable/aby_photo");
        getStorageRef().child(getID()+"/userProfilePic.png").putFile(uri);
    }
}