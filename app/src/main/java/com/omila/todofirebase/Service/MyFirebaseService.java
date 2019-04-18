package com.omila.todofirebase.Service;

import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.omila.todofirebase.FileHelper;
import com.omila.todofirebase.MainActivity;

public class MyFirebaseService extends FirebaseMessagingService {

    MainActivity main;
    @Override
    public void onNewToken(String token) {
        Log.d("NEWTOKEN", "Refreshed token: " + token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
       String s=remoteMessage.getNotification().getBody();
      // MainActivity.Companion.getAdapter().addAll(s);
      // MainActivity.Companion.setItemT(null);
        //FileHelper.writeData(MainActivity.Companion.getItems()MainActivity);
        MainActivity.Companion.writeData(s);
        Log.d("Poruka: ",s);
        FileHelper.writeData(MainActivity.Companion.getItems(), this);
    }
}
