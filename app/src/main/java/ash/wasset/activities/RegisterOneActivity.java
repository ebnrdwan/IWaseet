package ash.wasset.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ash.wasset.R;
import ash.wasset.models.StandardWebServiceResponse;
import ash.wasset.serverconnection.Url;
import ash.wasset.serverconnection.volley.AppController;
import ash.wasset.serverconnection.volley.ConnectionVolley;
import ash.wasset.utils.CheckResponse;
import ash.wasset.utils.GeneralClass;
import ash.wasset.GeneralAccess.Genral;

public class RegisterOneActivity extends Activity implements View.OnClickListener, Response.Listener, Response.ErrorListener {

    Button clientRegistrationButton, serviceProviderRegistrationButton, loginButton, skipButton;
    ImageView langImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        clientRegistrationButton = (Button) findViewById(R.id.clientRegistrationButton);
        serviceProviderRegistrationButton = (Button) findViewById(R.id.serviceProviderRegistrationButton);
        loginButton = (Button) findViewById(R.id.loginButton);
        skipButton = (Button) findViewById(R.id.skipButton);
      langImageView = (ImageView) findViewById(R.id.langImageView);

        clientRegistrationButton.setOnClickListener(this);
        serviceProviderRegistrationButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        skipButton.setOnClickListener(this);
        langImageView.setOnClickListener(this);
        if(Genral.ShowCat)
        {
            Genral.ShowCat= false;
            callServerToGetCategories();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.clientRegistrationButton:
                clientRegistartion();
                break;
            case R.id.serviceProviderRegistrationButton:
                serviceProviderRegistartion();
                break;
            case R.id.loginButton:
                Login();
                break;
            case R.id.skipButton:
                callServerToGetCategories();
                break;
            case R.id.langImageView:
                checkkLanguage();
                break;
        }
    }

    private void categoriesFragment(String json){

    }
    public void checkkLanguage (){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("language", 0);
        if (pref.getString("language", "en").equals("en")){
            checkLanguage("ar");
        } else {
            checkLanguage("en");
        }
    }

    public void checkLanguage (String languageToLoad){
        Locale locale = new Locale(languageToLoad);
        rememberLanguage(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getApplicationContext().getResources().updateConfiguration(config, getApplicationContext().getResources().getDisplayMetrics());
    }

    private void rememberLanguage(String language){
        GeneralClass.language = language;
        SharedPreferences pref = getApplicationContext().getSharedPreferences("language", 0);
        SharedPreferences.Editor ed = pref.edit();
        ed.putString("language", language);
        ed.commit();
        restartActivity();
    }

    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    void clientRegistartion(){
        Intent toClientRegistartion = new Intent(this, ClientRegistrationActivity.class);
        startActivity(toClientRegistartion);
    }

    void serviceProviderRegistartion(){
        Intent toServiceProviderRegistartion = new Intent(this, ServiceProviderRegistrationActivity.class);
        startActivity(toServiceProviderRegistartion);
    }

    void Login(){
        Intent toLogin = new Intent(this, LoginActivity.class);
        startActivity(toLogin);
    }

    void callServerToGetCategories(){
        Map<String,String> parms = new HashMap<String,String>();
        ConnectionVolley connectionVolley = new ConnectionVolley(this, Request.Method.POST, Url.getInstance().categoriesURL, this, this, parms, true);
        AppController.getInstance().addToRequestQueue(connectionVolley, "");
    }

    private void toMainActivity(String jsonString){
        finish();
        Intent toMainActivity = new Intent(this, MainActivity.class);
        toMainActivity.putExtra("json", jsonString);
        startActivity(toMainActivity);
    }

    private String checkServiceName(String jsonString){
        StandardWebServiceResponse standardWebServiceResponse = new StandardWebServiceResponse();
        Gson gson = new Gson();
        standardWebServiceResponse = gson.fromJson(jsonString, StandardWebServiceResponse.class);
        return standardWebServiceResponse.getServiceName();
    }

    @Override
    public void onResponse(Object response) {
        Log.e("response", response.toString());
        try {
            if (checkServiceName(response.toString()).equals("GetCategories")){
                if (CheckResponse.getInstance().checkResponse(this, response.toString(), false)){
                    toMainActivity(response.toString());
                }
            }
        } catch (Exception e){
            Toast.makeText(this, getResources().getString(R.string.connectionError), Toast.LENGTH_LONG).show();
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
