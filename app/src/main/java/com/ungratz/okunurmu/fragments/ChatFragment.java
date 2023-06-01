package com.ungratz.okunurmu.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ungratz.okunurmu.MainActivity;
import com.ungratz.okunurmu.R;
import com.ungratz.okunurmu.databinding.FragmentChatBinding;
import com.ungratz.okunurmu.singleton.CurrentUser;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.typesense.model.SearchParameters;
import org.typesense.model.SearchResult;

public class ChatFragment extends Fragment {
    private FragmentChatBinding binding;

    private SearchParameters sp;

    private SearchResult sResult;


    private final int MAX_DOWNLOAD_SIZE = 2048*2048;
    private Bitmap bm;

    public ChatFragment(){
        super(R.layout.fragment_chat);
    }

    @Nullable
    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    private int iterationNo = 1;
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        CurrentUser.getFirebaseFirestore().collection("chat").document(CurrentUser.getID())
                .collection("chatters").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<QuerySnapshot> task) {

                        for (QueryDocumentSnapshot snapshot: task.getResult()){

                            ConstraintLayout chatter = (ConstraintLayout) getLayoutInflater().inflate(R.layout.chat_preview, binding.linearLayoutForChatPreview, false);
                            TextView name = chatter.findViewById(R.id.chatPreviewName);
                            name.setText(snapshot.getString("name"));

                            name.setOnClickListener(v -> ((MainActivity)ChatFragment.this.getActivity()).switchToChat(snapshot.getId()));

                            binding.linearLayoutForChatPreview.addView(chatter);

                        }
                    }
                });

    }

}
