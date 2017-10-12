package ash.wasset.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.firebase.FirebaseApp;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
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
import ash.wasset.utils.Validation.ValidationObject;
import ash.wasset.utils.Validation.ViewsValidation;
import ash.wasset.GeneralAccess.Genral;


public class LoginActivity extends Activity implements View.OnClickListener, Response.Listener, Response.ErrorListener {

    EditText mobileEditText, passwordEditText;
    Button loginButton,skip, forgotPasswordButton;
    LinearLayout toRegister;
    LoginSharedPreferences loginSharedPreferences;
    MyFirebaseInstanceIDService myFirebaseInstanceIDService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        FirebaseApp.initializeApp(this);
        initialization();
        onClick();

    }

    private void initialization(){
        mobileEditText = (EditText) findViewById(R.id.mobileEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        forgotPasswordButton = (Button) findViewById(R.id.forgotPasswordButton);
        loginButton = (Button) findViewById(R.id.loginButton);
        skip = (Button) findViewById(R.id.skipButton);
        toRegister = (LinearLayout) findViewById(R.id.to_register);

        myFirebaseInstanceIDService = new MyFirebaseInstanceIDService(LoginActivity.this);
    }

    private void onClick(){
        loginButton.setOnClickListener(this);
        skip.setOnClickListener(this);
        toRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.loginButton:
                loginClick();
                break;
            case R.id.skipButton:
                setSkipClick();
                break;
            case R.id.to_register:
                setToRegisterClick();
                break;
        }
    }

    private void setToRegisterClick(){
        finish();
        Intent toRegisterActivity = new Intent(LoginActivity.this, RegisterOneActivity.class);
        toRegisterActivity.putExtra("json", "");
        startActivity(toRegisterActivity);
    }

    private void setSkipClick() {
        //callServerToGetCategories();
        //toCategoriesActivity();
        //toMainActivity("");
        Genral.ShowCat= true;
        setToRegisterClick();
    }
    private void loginClick(){
        if (checkValidation()){
            callServerToLogin();
        }
    }


    private void callServerToLogin(){
        Map<String,String> parms = new HashMap<String,String>();
        Users users=new Users();
        users.setUserName(mobileEditText.getText().toString());
        users.setPassword(passwordEditText.getText().toString());
       // users.setMobileType("1");
        users.setPushKey(myFirebaseInstanceIDService.getToken());
        Gson gson = new Gson();
        parms = gson.fromJson(gson.toJson(users), parms.getClass());
        Log.e("Parm", parms.toString());
        ConnectionVolley connectionVolley = new ConnectionVolley(this, Request.Method.POST, Url.getInstance().loginURL, this, this, parms, true);
        AppController.getInstance().addToRequestQueue(connectionVolley, "");

    }

    public boolean checkValidation() {
        ArrayList<ValidationObject> list=new ArrayList<>();
        list.add(new ValidationObject(mobileEditText , 9, true, R.string.mobileErrorMsg));
        list.add(new ValidationObject(passwordEditText , 6, true, R.string.passwordErrorMsg));
        return ViewsValidation.getInstance().validate(this,list);
    }

    private void checkActivation(String jsonString) throws Exception{
        StandardWebServiceResponse standardWebServiceResponse = new StandardWebServiceResponse();
        Gson gson = new Gson();
        standardWebServiceResponse = gson.fromJson(jsonString, StandardWebServiceResponse.class);
        Users users = new Users();
        gson = new Gson();
        GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        users = gson.fromJson(builder.create().toJson(standardWebServiceResponse.getData()), Users.class);
        if (!standardWebServiceResponse.isSuccess()){
            toActivation(jsonString);
            return;
        }
        if (users.getBlocked()){
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.invalidUser), Toast.LENGTH_LONG).show();
        }
    }

    private void toActivation(String jsonString){
        finish();
        Intent toActivationActivity = new Intent(LoginActivity.this, ActivitionActivity.class);
        toActivationActivity.putExtra("json", jsonString);
        startActivity(toActivationActivity);
    }

    private void toCategoriesActivity(){
        finish();
        Intent CategoriesActivity = new Intent(LoginActivity.this, CategoriesActivity.class);
        CategoriesActivity.putExtra("json", "");
        startActivity(CategoriesActivity);
    }

    private void toMainActivity(String jsonString){
        finish();
        Intent toMainActivity = new Intent(LoginActivity.this, MainActivity.class);
        toMainActivity.putExtra("json", jsonString);
        startActivity(toMainActivity);
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

    private String checkServiceName(String jsonString){
        StandardWebServiceResponse standardWebServiceResponse = new StandardWebServiceResponse();
        Gson gson = new Gson();
        standardWebServiceResponse = gson.fromJson(jsonString, StandardWebServiceResponse.class);
        return standardWebServiceResponse.getServiceName();
    }

    private void toServiceProviderMainActivity(String jsonString){
        finish();
        Intent toServiceProviderMainActivity = new Intent(this, ServiceProviderMainActivity.class);
        toServiceProviderMainActivity.putExtra("json", jsonString);
        startActivity(toServiceProviderMainActivity);
    }

    void callServerToGetCategories(){
        Map<String,String> parms = new HashMap<String,String>();
        ConnectionVolley connectionVolley = new ConnectionVolley(this, Request.Method.POST, Url.getInstance().categoriesURL, this, this, parms, true);
        AppController.getInstance().addToRequestQueue(connectionVolley, "");
    }

    void callServerToShowLocationRequests(){
        Map<String,String> parms = new HashMap<String,String>();
        ConnectionVolley connectionVolley = new ConnectionVolley(this, Request.Method.POST, Url.getInstance().showLocationRequestsURL, this, this, parms, false);
        AppController.getInstance().addToRequestQueue(connectionVolley, "");
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
                    rememberUser(response.toString());
                    checkActivation(response.toString());
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
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.invalidUser), Toast.LENGTH_LONG).show();
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
