package ash.wasset.notifications;

import android.content.Context;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";
    Context context;
    String token;

    public MyFirebaseInstanceIDService() {
    }

    public MyFirebaseInstanceIDService(Context context) {
        this.context = context;
        onTokenRefresh();
    }

    @Override
    public void onTokenRefresh() {
        FirebaseApp.initializeApp(context);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        this.token = token;
    }

    public String getToken(){
        return token;
    }

}
