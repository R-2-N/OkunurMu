package com.ungratz.okunurmu.Activities;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ungratz.okunurmu.R;
import com.ungratz.okunurmu.User;
import com.ungratz.okunurmu.databinding.ChatActivityBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private ChatActivityBinding binding;
    private User receiverUser;
    private List<ChatMessage> chatMessages;
    private ChatAdapter chatAdapter;
    protected PreferenceManager preferenceManager;
    private FirebaseFirestore firestore;
    private DatabaseReference messagesRef;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    public ChatActivity() {
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ChatActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        setReceiver();
        preferenceManager = new PreferenceManager(getApplicationContext());
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages, preferenceManager.getString);
        binding.chatView.setAdapter(chatAdapter);
        firestore = FirebaseFirestore.getInstance();


    }

    private void sendMessage() {
        HashMap<String, Object> message = new HashMap<>();
        message.put(currentUser.getUid(), preferenceManager.getString())
    }

    private void setReceiver() {
        //receiverUser = (User) getIntent().getSerializableExtra(currentUser.get);
        binding.nameText.setText(receiverUser.name);
    }

    private Bitmap getBitmap(String bitmap) {
        byte[] bytes = android.util.Base64.decode(bitmap, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    private void setListeners() {
        binding.goBack.setOnClickListener(v -> onBackPressed());
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // User is not signed in, redirect to sign-in activity
            // Implement your sign-in flow or redirect to the authentication activity
        }
    }
}