package io.flutter.plugins.firebasemessaging;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.firebase.messaging.RemoteMessage;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class CustomFlutterFirebaseMessagingService extends FlutterFirebaseMessagingService {
  @SuppressLint("ApplySharedPref")
  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {
    try {
      if (remoteMessage.getData().get("message").contains("\"type\":\"voip_incoming_call\"")) {

        // Write data to shared preferences
        final Context backgroundContext = getApplicationContext();
        final String PREF_REMOTE_MESSAGE_DATA_KEY = "flutter.pref_remote_message_data_key";
        SharedPreferences sharedPref = backgroundContext.getSharedPreferences("FlutterSharedPreferences", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREF_REMOTE_MESSAGE_DATA_KEY, remoteMessage.getData().toString());
        editor.commit();

        // Start application
        Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        startActivity(intent);

        // In-case application on pause
        try {
          Intent intent2 = new Intent(ACTION_REMOTE_MESSAGE);
          intent2.putExtra(EXTRA_REMOTE_MESSAGE, remoteMessage);
          LocalBroadcastManager.getInstance(this).sendBroadcast(intent2);
        } catch (Exception e) {
          // Ignore
        }

      } else {
        super.onMessageReceived(remoteMessage);
      }
    } catch(Exception e) {
      super.onMessageReceived(remoteMessage);
    }

  }
}
