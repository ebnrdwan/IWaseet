package ash.wasset.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.firebase.FirebaseApp;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import ash.wasset.R;
import ash.wasset.models.StandardWebServiceResponse;
import ash.wasset.models.Users;
import ash.wasset.notifications.MyFirebaseInstanceIDService;
import ash.wasset.serverconnection.Url;
import ash.wasset.serverconnection.volley.AppController;
import ash.wasset.serverconnection.volley.ConnectionVolley;
import ash.wasset.utils.CheckResponse;
import ash.wasset.utils.LoginSharedPreferences;

public class ActivitionActivity extends Activity implements View.OnClickListener, Response.Listener, Response.ErrorListener {

    String getJson;
    EditText comfirmationCodeEditText;
    Button checkButton, resendButton;
    Users users;
    LoginSharedPreferences loginSharedPreferences;
    MyFirebaseInstanceIDService myFirebaseInstanceIDService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activition_activity);
        FirebaseApp.initializeApp(this);
        initialization();
        if(getJson!=null)
        decodeJson(getJson);
        onClick();
    }

    private void initialization(){
        getJson = getIntent().getStringExtra("json");
        comfirmationCodeEditText = (EditText) findViewById(R.id.comfirmationCodeEditText);
        checkButton = (Button) findViewById(R.id.checkButton);
        resendButton = (Button) findViewById(R.id.resendButton);
        loginSharedPreferences = new LoginSharedPreferences();
        myFirebaseInstanceIDService = new MyFirebaseInstanceIDService(ActivitionActivity.this);
    }

    private void decodeJson(String jsonString){
        StandardWebServiceResponse standardWebServiceResponse = new StandardWebServiceResponse();
        Gson gson = new Gson();
        standardWebServiceResponse = gson.fromJson(jsonString, StandardWebServiceResponse.class);
        users = new Users();
        gson = new Gson();
        GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        users = gson.fromJson(builder.create().toJson(standardWebServiceResponse.getData()), Users.class);
    }

    private void onClick(){

        checkButton.setOnClickListener(this);
        resendButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.checkButton:
                checkClick();
                break;
            case R.id.resendButton:
                loginClick();
                break;
        }
    }

    private void checkClick(){
        if (comfirmationCodeEditText.getText().toString().equals(users.getActivationCode())){
            callServerToActivation();
        } else {
            Toast.makeText(ActivitionActivity.this, getResources().getString(R.string.invalidActivationCode), Toast.LENGTH_LONG).show();
        }
    }

    private void toMainActivity(String jsonString){
        finish();
        Intent toMainActivity = new Intent(this, MainActivity.class);
        toMainActivity.putExtra("json", jsonString);
        startActivity(toMainActivity);
    }

    private void loginClick(){
        callServerToLogin();
    }

    private void callServerToLogin(){
        Map<String,String> parms = new HashMap<String,String>();
        users.setMobileNumber(users.getMobileNumber());
        users.setPassword(users.getPassword());
        users.setMobileType("1");
        users.setPushKey(myFirebaseInstanceIDService.getToken());
        Gson gson = new Gson();
        parms = gson.fromJson(gson.toJson(users), parms.getClass());
        Log.e("Parm", parms.toString());
        ConnectionVolley connectionVolley = new ConnectionVolley(this, Request.Method.POST, Url.getInstance().loginURL, this, this, parms, true);
        AppController.getInstance().addToRequestQueue(connectionVolley, "");
    }

    private void callServerToActivation(){
        Map<String,String> parms = new HashMap<String,String>();
        users.setRegActivationCode(users.getActivationCode());
        Gson gson = new Gson();
        parms = gson.fromJson(gson.toJson(users), parms.getClass());
        Log.e("Parm", parms.toString());
        ConnectionVolley connectionVolley = new ConnectionVolley(this, Request.Method.POST, Url.getInstance().activationURL, this, this, parms, true);
        AppController.getInstance().addToRequestQueue(connectionVolley, "");
    }

    private String checkServiceName(String jsonString){
        StandardWebServiceResponse standardWebServiceResponse = new StandardWebServiceResponse();
        Gson gson = new Gson();
        standardWebServiceResponse = gson.fromJson(jsonString, StandardWebServiceResponse.class);
        return standardWebServiceResponse.getServiceName();
    }

    void callServerToGetCategories(){
        Map<String,String> parms = new HashMap<String,String>();
        ConnectionVolley connectionVolley = new ConnectionVolley(this, Request.Method.POST, Url.getInstance().categoriesURL, this, this, parms, false);
        AppController.getInstance().addToRequestQueue(connectionVolley, "");
    }

    private void toServiceProviderMainActivity(String jsonString){
        finish();
        Intent toServiceProviderMainActivity = new Intent(this, ServiceProviderMainActivity.class);
        toServiceProviderMainActivity.putExtra("json", jsonString);
        startActivity(toServiceProviderMainActivity);
    }

    private String checkUsertype(String jsonString){
        StandardWebServiceResponse standardWebServiceResponse = new StandardWebServiceResponse();
        Gson gson = new Gson();
        standardWebServiceResponse = gson.fromJson(jsonString, StandardWebServiceResponse.class);
        Users users = new Users();
        gson = new Gson();
        GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        users = gson.fromJson(builder.create().toJson(standardWebServiceResponse.getData()), Users.class);
        return users.getUserType();
    }

    void callServerToShowLocationRequests(){
        Map<String,String> parms = new HashMap<String,String>();
        ConnectionVolley connectionVolley = new ConnectionVolley(this, Request.Method.POST, Url.getInstance().showLocationRequestsURL, this, this, parms, false);
        AppController.getInstance().addToRequestQueue(connectionVolley, "");
    }

    @Override
    public void onResponse(Object response) {
        Log.e("response", response.toString());
        try {
            if (checkServiceName(response.toString()).equals("Login")){
                Toast.makeText(ActivitionActivity.this, getResources().getString(R.string.activationCodeHasBeenSent), Toast.LENGTH_LONG).show();
            } else if (checkServiceName(response.toString()).equals("Activation")){
                loginSharedPreferences.setActivated(this, "true");
                if (checkUsertype(response.toString()).equals("2")){
                    callServerToGetCategories();
                } else {
                    callServerToShowLocationRequests();
                }
            } else if (checkServiceName(response.toString()).equals("GetCategories")){
                if (CheckResponse.getInstance().checkResponse(this, response.toString(), false)){
                    toMainActivity(response.toString());
                }
            } else if (checkServiceName(response.toString()).equals("ShowLocationRequests")){
                if (CheckResponse.getInstance().checkResponse(this, response.toString(), false)){
                    toServiceProviderMainActivity(response.toString());
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        VolleyLog.d("responseError", "Error: " + error.getMessage());
        try {
            ConnectionVolley.dialog.dismiss();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
