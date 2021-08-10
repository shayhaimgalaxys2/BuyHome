package il.co.buyhome;

import android.app.Application;

import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.messaging.FirebaseMessaging;
import com.pushwoosh.Pushwoosh;

public class App extends Application {

    private static String deviceId = "";
    static String deviceToken = "";
    private static String deviceAdvertiserId = "";

    @Override
    public void onCreate() {
        super.onCreate();
        initialFCM();
        Pushwoosh.getInstance().registerForPushNotifications();

    }

    private void initialFCM() {
        FirebaseInstallations.getInstance().getId().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                deviceId = task.getResult();
            }
        });
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                deviceToken = task.getResult();
            }
        });
    }
}
