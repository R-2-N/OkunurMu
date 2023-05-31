package com.ungratz.okunurmu.singleton;

import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.typesense.api.Client;
import org.typesense.api.Configuration;
import org.typesense.resources.Node;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
    private static CollectionReference uc;
    private static DocumentReference dr;

    // I actually cannot store DocumentSnapshot. I have to call .addOnCompleteListener
    // everytime on the DocumentReference
    private static DocumentSnapshot ds;
    private static FirebaseStorage fs = FirebaseStorage.getInstance();
    private static StorageReference sr = fs.getReference();
    private static String id;
    private static String realName;
    private static String mail;
    private static String userName;
    private static boolean isMentor;
    private static String university;
    private static String department;
    private static String bio;
    private static int amountOfPersonalPhotos;


    private static ArrayList<Node> nodes = new ArrayList<>();
    private static Configuration typesenseConfiguration;
    private static Client typensenseClient;


    public static void setFirebaseUser(FirebaseUser u){
        user = u;
        setID(user.getUid());

        /*
        Quick tutorial on how to get field data from Firestore
        Do what I do in lines 63 to 78
        */

        setUsersCollectionReference(ff.collection("users"));
        setUserDocumentRef(ff.collection("users").document(getID()));
        setStorageRef(fs.getReference());
        setMail(user.getEmail());



        Map<String, Object> typesenseHashMap = new HashMap<>();
        typesenseHashMap.put("trigger", true);
        ff.collection("typesense_sync").document("backfill").set(typesenseHashMap);

        nodes.add(new Node("https","i8w0qd72xsjz4u63p-1.a1.typesense.net","443"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setTypesenseConfiguration(new Configuration(nodes, Duration.ofSeconds(2), "DswDykKECeI3u0YZCs3FaPX2yB7BJfww"));
        }
        setTypensenseClient(new Client(getTypesenseConfiguration()));
    }

    public static void setNewFirebaseUser
            (FirebaseUser u, String userRealName, String userName, boolean isItMentor, String university, String department){
        setStorageRef(fs.getReference());
        setID(u.getUid());
        setDefaultProfilePicOnStorage();

        setIsMentor(isItMentor);


        //writing the info into the database
        ff.collection("users").document(u.getUid()).
                set(createUserHashMap(u.getUid(), userRealName, userName, u.getEmail(), isMentor, university, department))
                .addOnSuccessListener(unused -> {
                    Log.d("W", "Data is written");
                    setFirebaseUser(u);
                })
                .addOnFailureListener(e -> Log.w("L", "DocumentSnapshot could not be retrieved"));
    }



    //setters
    public static void setID(String i){id = i;}
    public static void setUsersCollectionReference(CollectionReference cr){uc = cr;}
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
    public static void setAmountOfPersonalPhotos(int a){amountOfPersonalPhotos = a;}

    public static void setTypesenseConfiguration(Configuration tc){typesenseConfiguration = tc;}
    public static void setTypensenseClient(Client c){typensenseClient = c;}


    //getters
    public static FirebaseUser getUser(){return user;}
    public static String getID(){return id;}
    public static FirebaseFirestore getFirebaseFirestore(){return ff;}
    public static CollectionReference getUsersCollectionReference(){return uc;};
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
    public static int getAmountOfPersonalPhotos(){return amountOfPersonalPhotos;}

    public static Configuration getTypesenseConfiguration(){return typesenseConfiguration;}
    public static Client getTypensenseClient(){return typensenseClient;}


    public static void updateBio(String b){
        ff.collection("users").document(getID()).update("bio", b)
                .addOnSuccessListener(unused -> {
                    setBio(b);
                    //code for writing that they were successfull in updating their bio
                });
    }

    public static void updatePhotoAmountBy(int change){
        ff.collection("users").document(getID()).update("photoAmount", getAmountOfPersonalPhotos()+change)
                .addOnSuccessListener(unused -> {
                    setAmountOfPersonalPhotos(getAmountOfPersonalPhotos()+change);
                    //code for writing that they were successfull in updating their bio
                });
    }

    public static Map<String, Object>
    createUserHashMap(String id, String rn, String un, String e, boolean b, String uniN, String d){
        Map<String, Object> newUserMap = new HashMap<>();
        newUserMap.put("id", id);
        newUserMap.put("realName", rn);
        newUserMap.put("userName", un);
        newUserMap.put("email", e);
        newUserMap.put("isMentor", b);
        newUserMap.put("bio", "");
        newUserMap.put("photoAmount",0);
        if (b==true){
            newUserMap.put("university", uniN);
            newUserMap.put("department", d);
            newUserMap.put("meetingRequests", Arrays.asList());
        }

        return newUserMap;
    }

    // Probably won't be used since I need the success listener of these
    public static void uploadPersonalPhotoToStorage(Uri u){
        getStorageRef().child(getID()+"/"+(getAmountOfPersonalPhotos())).putFile(u);
        updatePhotoAmountBy(1);
    }

    public static void changeProfilePicOnStorage(Uri uri){
        getStorageRef().child(getID()+"/userProfilePic").putFile(uri);
    }

    public static void setDefaultProfilePicOnStorage(){
        Uri uri = Uri.parse("android.resource://com.ungratz.okunurmu/drawable/aby_photo");
        getStorageRef().child(getID()+"/userProfilePic").putFile(uri);
    }
}