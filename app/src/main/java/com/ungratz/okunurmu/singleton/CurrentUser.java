package com.ungratz.okunurmu.singleton;

import android.net.Uri;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class CurrentUser extends AppCompatActivity {
    private static CurrentUser u;
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


    public static void setFirebaseUser(FirebaseUser u){
        user = u;
        setID(user.getUid());


        /*
        Quick tutorial on how to get field data from Firestore
        First by using get() you have to turn document reference into Task<DocumentSnapshot>
        After which you can turn that into a documentSnapshot with getResult()
        and finally you could get the field using a string to indicate its name
        */

        setUserDocumentRef(ff.collection("users").document(getID()));
        setUserDocumentSnapshot(dr.get().getResult());
        setStorageRef(fs.getReference());
        setRealName(ds.getString("realName"));
        setUserName(ds.getString("userName"));
        setMail(user.getEmail());
        if(ds.getBoolean("isMentor")){
            setUniversity(ds.getString("university"));
            setDepartment(ds.getString("department"));
        }
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
    private static void setStorageRef(StorageReference s){sr = s;}
    public static void setRealName(String n){realName = n;}
    public static void setUserName(String u){userName = u;}
    public static void setMail(String m){mail = m;}
    public static void setIsMentor(boolean b){isMentor = b;}
    public static void setUniversity(String u){university = u;}
    public static void setDepartment(String d){department = d;}


    //getters
    public static FirebaseUser getUser(){return user;}
    public static String getID(){return id;}
    public static DocumentReference getUserDocumentRef(){return dr;}
    public static DocumentSnapshot getUserDocumentSnapshot(){return ds;}
    private static StorageReference getStorageRef(){return sr;}
    public static String getRealName(){return ds.getString("realName");}
    public static String getUserName(){return ds.getString("userName");}
    public static String getMail(){return ds.getString("email");}
    public static boolean getIsMentor(){return isMentor;}
    public static String getUniversity(){return university;}
    public static String getDepartment(){return department;}


    public static Map<String, Object>
    createUserHashMap(String rn, String un, String e, boolean b, String uniN, String d){
        Map<String, Object> newUserMap = new HashMap<>();
        newUserMap.put("realName", rn);
        newUserMap.put("userName", un);
        newUserMap.put("email", e);
        newUserMap.put("isMentor", b);
        if (b==true){
            newUserMap.put("university", uniN);
            newUserMap.put("department", d);
        }

        return newUserMap;
    }

    public static void uploadFileToStorage(Uri u){

        String[] s = u.toString().split("/");
        getStorageRef().child(getID() + "/" + s[s.length-1]).putFile(u)
                .addOnCompleteListener(task -> Log.d("Upload:", "success"))
                .addOnFailureListener(e -> Log.w("Upload:", "failed"));

    }
}