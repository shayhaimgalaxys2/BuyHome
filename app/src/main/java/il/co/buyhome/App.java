package il.co.buyhome;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.messaging.FirebaseMessaging;
import com.onesignal.OneSignal;
import com.pushwoosh.Pushwoosh;

public class App extends Application {

    public static final String ONESIGNAL_APP_ID = "04f6f3eb-fc98-4d0e-b6f6-79497bda3145";
    private static String deviceId = "";
    static String deviceToken = "";
    private static String deviceAdvertiserId = "";

    @Override
    public void onCreate() {
        super.onCreate();
//        initialFCM();
    }


    private void initialFCM() {
        FirebaseApp.initializeApp(this);
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
