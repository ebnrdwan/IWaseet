package ash.wasset.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by shafeek on 04/08/16.
 */
public class LoginSharedPreferences {

    private Context context;
    private String phoneNumber, password, accessToken, userType, activated, UserName, PicturePath;
    SharedPreferences.Editor editor;
    SharedPreferences settings;

    public LoginSharedPreferences() {
    }

    public LoginSharedPreferences(Context context, String accessToken, String phoneNumber, String password, String userType, String activated, String UserName, String PicturePath) {
        this.accessToken = accessToken;
        this.context = context;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.userType = userType;
        this.activated = activated;
        this.UserName = UserName;
        this.PicturePath = PicturePath;
        settings = context.getSharedPreferences("login", 0);
        editor = settings.edit();
        editor.putString("accessToken", accessToken);
        editor.putString("phoneNumber", phoneNumber);
        editor.putString("password", password);
        editor.putString("userType", userType);
        editor.putString("activated", activated);
        editor.putString("UserName", UserName);
        editor.putString("PicturePath", PicturePath);
        editor.commit();
    }

    public String getAccessToken(Context context) {
        settings = context.getSharedPreferences("login", 0);
        return settings.getString("accessToken", "");
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getPhoneNumber(Context context) {
        settings = context.getSharedPreferences("login", 0);
        return settings.getString("phoneNumber", "");
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword(Context context) {
        settings = context.getSharedPreferences("login", 0);
        return settings.getString("password", "");
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType(Context context) {
        settings = context.getSharedPreferences("login", 0);
        return settings.getString("userType", "");
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getActivated(Context context) {
        settings = context.getSharedPreferences("login", 0);
        return settings.getString("activated", "");
    }

    public void setActivated(Context context, String activated) {
        this.activated = activated;
        settings = context.getSharedPreferences("login", 0);
        editor = settings.edit();
        editor.putString("activated", activated);
        editor.commit();
    }

    public String getUserName(Context context) {
        settings = context.getSharedPreferences("login", 0);
        return settings.getString("UserName", "");
    }

    public void setUserName(Context context, String UserName) {
        this.UserName = UserName;
        settings = context.getSharedPreferences("login", 0);
        editor = settings.edit();
        editor.putString("UserName", UserName);
        editor.commit();
    }

    public String getPicturePath(Context context) {
        settings = context.getSharedPreferences("login", 0);
        return settings.getString("PicturePath", "");
    }

    public void setPicturePath(Context context, String PicturePath) {
        this.PicturePath = PicturePath;
        settings = context.getSharedPreferences("login", 0);
        editor = settings.edit();
        editor.putString("PicturePath", PicturePath);
        editor.commit();
    }

    public void removeLogin(Context context){
        settings = context.getSharedPreferences("login", 0);
        editor = settings.edit();
        editor.remove("accessToken");
        editor.remove("phoneNumber");
        editor.remove("password");
        editor.remove("userType");
        editor.remove("activated");
        editor.remove("UserName");
        editor.remove("PicturePath");
        editor.commit();
    }
}
