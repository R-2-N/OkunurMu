package com.ungratz.okunurmu.singleton;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

    private static FirebaseFirestore fs = FirebaseFirestore.getInstance();
    private static FirebaseUser user;
    private static DocumentReference dr;
    private static DocumentSnapshot ds;
    private static String id;
    private static String realName;
    private static String mail;
    private static String userName;
    private static boolean isMentor;


    public static void setFirebaseUser(FirebaseUser u){
        user = u;
        setID(user.getUid());

        /*
        Quick tutorial on how to get field data from Firestore
        First by using get() you have to turn document reference into Task<DocumentSnapshot>
        After which you can turn that into a documentSnapshot with getResult()
        and finally you could get the field using a string to indicate its name
        */

        setUserDocumentRef(fs.collection("users").document(getID()));
        setUserDocumentSnapshot(dr.get().getResult());
        setRealName(ds.getString("realName"));
        setUserName(ds.getString("userName"));
        setMail(user.getEmail());
    }

    public static void setNewFirebaseUser(FirebaseUser u, String userRealName, String userName, boolean b){

        setIsMentor(b);

        //writing the info into the database
        fs.collection("users").document(u.getUid()).
                set(createUserHashMap(userRealName, userName, u.getEmail(), isMentor))
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
    public static void setRealName(String n){realName = n;}
    public static void setUserName(String u){userName = u;}
    public static void setMail(String m){mail = m;}
    public static void setIsMentor(boolean b){isMentor = b;}



    //getters
    public static FirebaseUser getUser(){return user;}
    public static String getID(){return id;}
    public static DocumentReference getUserDocumentRef(){return dr;}
    public static DocumentSnapshot getUserDocumentSnapshot(){return ds;}
    public static String getRealName(){return ds.getString("realName");}
    public static String getUserName(){return ds.getString("userName");}
    public static String getMail(){return ds.getString("email");}
    public static boolean getIsMentor(){return isMentor;}


    public static Map<String, Object>
    createUserHashMap(String rn, String un, String e, boolean b){
        Map<String, Object> newUserMap = new HashMap<>();
        newUserMap.put("realName", rn);
        newUserMap.put("userName", un);
        newUserMap.put("email", e);
        newUserMap.put("isMentor", b);

        return newUserMap;
    }
}