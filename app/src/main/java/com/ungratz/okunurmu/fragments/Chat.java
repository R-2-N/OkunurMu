package com.ungratz.okunurmu.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.ungratz.okunurmu.R;
import com.ungratz.okunurmu.databinding.ChatBinding;
import com.ungratz.okunurmu.singleton.CurrentUser;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class Chat extends Fragment {

    private ChatBinding binding;
    private String enemyID;
    public Chat(String idOfChatter){
        super(R.layout.fragment_meetings);
        enemyID = idOfChatter;
    }

    @Nullable
    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ChatBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    private int iterationNo = 1;
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){

        CurrentUser.getFirebaseFirestore().collection("chat").document(CurrentUser.getID())
                .collection("chatters").document(enemyID)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                     @Override
                     public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                         if (error != null) {
                             return;
                         }

                         if (value != null && value.exists()) {
                            // everythingMessages();
                         } else {

                         }
                     }
                });

        everythingMessages();

    }

    public void everythingMessages(){

        CurrentUser.getFirebaseFirestore().collection("chat").document(CurrentUser.getID())
                .collection("chatters").document(enemyID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<DocumentSnapshot> task) {

                        List<String> messages = (List<String>) task.getResult().get("messages");

                        for (int i = 1; i < messages.size(); i++){

                            TextView t = new TextView(getContext());
                            t.setText((String)messages.get(i));
                            binding.linearLayoutForChat.addView(t);
                        }


                        binding.sendView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String newMessage = binding.chatBar.getText().toString();
                                if (!(newMessage.equals(""))){

                                    CurrentUser.getFirebaseFirestore().collection("chat").document(CurrentUser.getID())
                                            .collection("chatters").document(enemyID).update("messages", FieldValue.arrayUnion(CurrentUser.getRealName()+": "+ newMessage));

                                    CurrentUser.getFirebaseFirestore().collection("chat").document(enemyID)
                                            .collection("chatters").document(CurrentUser.getID()).update("messages", FieldValue.arrayUnion(CurrentUser.getRealName()+ ": " +newMessage));

                                    TextView t2 = new TextView(getContext());
                                    t2.setText(CurrentUser.getRealName()+ ": "+newMessage);
                                    binding.linearLayoutForChat.addView(t2);
                                    binding.chatBar.setText("");
                                }
                                else{
                                    binding.chatBar.setHint("Message cannot be empty");
                                }
                            }
                        });


                    }
                });

    }




}
