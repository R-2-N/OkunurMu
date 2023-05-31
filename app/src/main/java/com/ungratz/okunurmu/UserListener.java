package com.ungratz.okunurmu;

import com.google.firebase.firestore.auth.User;

public interface UserListener {
    void onUserClicked(User user);
}
