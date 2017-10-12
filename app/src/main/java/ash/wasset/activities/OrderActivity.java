package ash.wasset.activities;


import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import ash.wasset.R;
import ash.wasset.models.Users;
import ash.wasset.notifications.MyFirebaseInstanceIDService;
import ash.wasset.utils.LoginSharedPreferences;

public class OrderActivity extends Activity {

    String getJson;
    EditText comfirmationCodeEditText;
    Button checkButton, resendButton;
    Users Vendor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        initialization();
    }


    private void initialization(){
        String getJson;
        EditText comfirmationCodeEditText;
        Button checkButton, resendButton;
        Users users;
        LoginSharedPreferences loginSharedPreferences;
        MyFirebaseInstanceIDService myFirebaseInstanceIDService;
    }
}
