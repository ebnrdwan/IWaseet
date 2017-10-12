package ash.wasset.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;

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

public class SplashScreenActivity extends Activity implements Response.Listener, Response.ErrorListener  {

    Handler handler;
    LoginSharedPreferences loginSharedPreferences;
    MyFirebaseInstanceIDService myFirebaseInstanceIDService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);
        FirebaseApp.initializeApp(this);
        initialization();
       // checkSharedPreference();
        toLogin();
    }

    private void initialization(){
        handler = new Handler();
        loginSharedPreferences = new LoginSharedPreferences();
        myFirebaseInstanceIDService = new MyFirebaseInstanceIDService(SplashScreenActivity.this);
    }

    private void checkSharedPreference(){
        if (loginSharedPreferences.getActivated(this).equals("true")){
            callServerToLogin();
        } else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toHome();
                }
            }, 2000);
        }
    }

    private void callServerToLogin(){
        Map<String,String> parms = new HashMap<String,String>();
        Users users=new Users();
        users.setPhoneNo(loginSharedPreferences.getPhoneNumber(this));
        users.setPassword(loginSharedPreferences.getPassword(this));
        users.setMobileType("1");
        users.setPushKey(myFirebaseInstanceIDService.getToken());
        Gson gson = new Gson();
        parms = gson.fromJson(gson.toJson(users), parms.getClass());
        Log.e("Parm", parms.toString());
        ConnectionVolley connectionVolley = new ConnectionVolley(this, Request.Method.POST, Url.getInstance().loginURL, this, this, parms, false);
        AppController.getInstance().addToRequestQueue(connectionVolley, "");
    }

    void callServerToGetCategories(){
        Map<String,String> parms = new HashMap<String,String>();
        ConnectionVolley connectionVolley = new ConnectionVolley(this, Request.Method.POST, Url.getInstance().categoriesURL, this, this, parms, false);
        AppController.getInstance().addToRequestQueue(connectionVolley, "");
    }

    void callServerToShowLocationRequests(){
        Map<String,String> parms = new HashMap<String,String>();
        ConnectionVolley connectionVolley = new ConnectionVolley(this, Request.Method.POST, Url.getInstance().showLocationRequestsURL, this, this, parms, false);
        AppController.getInstance().addToRequestQueue(connectionVolley, "");
    }

    private void rememberUser(String jsonString){
        StandardWebServiceResponse standardWebServiceResponse = new StandardWebServiceResponse();
        Gson gson = new Gson();
        standardWebServiceResponse = gson.fromJson(jsonString, StandardWebServiceResponse.class);
        Users users = new Users();
        gson = new Gson();
        GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        users = gson.fromJson(builder.create().toJson(standardWebServiceResponse.getData()), Users.class);
        loginSharedPreferences = new LoginSharedPreferences(this, users.getAccessToken(), users.getMobileNumber(), users.getPassword(), users.getUserType(), Boolean.toString(standardWebServiceResponse.isSuccess()), users.getUserName(), users.getPicturePath());
    }

    private void toHome(){
        finish();
        Intent toHome = new Intent(this, RegisterOneActivity.class);
        startActivity(toHome);
    }

    private void toLogin(){
        finish();
        Intent toLogin = new Intent(this, LoginActivity.class);
        startActivity(toLogin);
    }

    private String checkServiceName(String jsonString){
        StandardWebServiceResponse standardWebServiceResponse = new StandardWebServiceResponse();
        Gson gson = new Gson();
        standardWebServiceResponse = gson.fromJson(jsonString, StandardWebServiceResponse.class);
        return standardWebServiceResponse.getServiceName();
    }

    private void toMainActivity(String jsonString){
        finish();
        Intent toMainActivity = new Intent(this, MainActivity.class);
        toMainActivity.putExtra("json", jsonString);
        startActivity(toMainActivity);
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

    @Override
    public void onResponse(Object response) {
        Log.e("response", response.toString());
        try {
            if (checkServiceName(response.toString()).equals("Login")){
                if (CheckResponse.getInstance().checkResponse(this, response.toString(), false)){
                    rememberUser(response.toString());
                    if (checkUsertype(response.toString()).equals("2")){
                        callServerToGetCategories();
                    } else {
                        callServerToShowLocationRequests();
                    }
                } else {
                    loginSharedPreferences.removeLogin(this);
                    toLogin();
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
            loginSharedPreferences.removeLogin(this);
            toLogin();
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
