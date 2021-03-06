package ash.wasset.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ash.wasset.R;
import ash.wasset.fragments.MapAddLocationFragment;
import ash.wasset.models.LocationVariable;
import ash.wasset.models.Users;
import ash.wasset.serverconnection.Url;
import ash.wasset.serverconnection.volley.AppController;
import ash.wasset.serverconnection.volley.ConnectionVolley;
import ash.wasset.utils.CheckResponse;
import ash.wasset.utils.Permissions;
import ash.wasset.utils.Validation.ValidationObject;
import ash.wasset.utils.Validation.ViewsValidation;

public class ServiceProviderRegistrationActivity extends FragmentActivity implements View.OnClickListener, Response.Listener, Response.ErrorListener {

    EditText nameEditText, mobileEditText, emailEditText, passwordEditText, reTypePasswordEditText, shopNameEditText;
    Button registrationButton, loginButton;
    LinearLayout photoLayer, locationLayout;
    Permissions permissions;
    final static int SELECT_IMAGE = 100;
    final static int IMAGE_CAPTURE = 200;
    final static int STORAGE_PERMISSION = 1111;
    final static int CAMERA_PERMISSION = 1888;
    String imageData;
    LocationVariable locationVariable;
    String getJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_provider_registration_activity);
        initialization();
        onClick();

    }

    private void initialization(){
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        mobileEditText = (EditText) findViewById(R.id.mobileEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        shopNameEditText = (EditText) findViewById(R.id.shopNameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        reTypePasswordEditText = (EditText) findViewById(R.id.reTypePasswordEditText);
        registrationButton = (Button) findViewById(R.id.registrationButton);
        photoLayer = (LinearLayout) findViewById(R.id.photoLayer);
        locationLayout = (LinearLayout) findViewById(R.id.locationLayout);
        imageData = "";
        locationVariable = new LocationVariable(0,0);
    }

    private void onClick(){
        registrationButton.setOnClickListener(this);
        photoLayer.setOnClickListener(this);
        locationLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.loginButton:
                loginClick();
                break;
            case R.id.registrationButton:
                registerClick();
                break;
            case R.id.photoLayer:
                takePhotoDialog();
                break;
            case R.id.locationLayout:
                openMap();
                break;
        }
    }

    private void openMap(){
        MapAddLocationFragment mapAddLocationFragment = new MapAddLocationFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.contentView, mapAddLocationFragment).addToBackStack("").commit();
    }

    void takePhotoDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.take_photo_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        Button cameraButton = (Button) dialog.findViewById(R.id.cameraButton);
        Button gallaryButton = (Button) dialog.findViewById(R.id.gallaryButton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                addImageFromCamera();
            }
        });
        gallaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                addImage();
            }
        });
        dialog.show();
    }

    private void addImageFromCamera(){
        if (permissions.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            if (permissions.hasPermission(this, Manifest.permission.CAMERA)) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, IMAGE_CAPTURE);
            } else {
                this.requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
            }
        } else {
            this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
        }
    }

    private void addImage(){
        if (permissions.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);//
            startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_IMAGE);
        } else {
            this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case STORAGE_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);//
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_IMAGE);
                } else {
//                    permission is denied
                }
                break;
            case CAMERA_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, IMAGE_CAPTURE);
                } else {
//                    permission is denied
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_IMAGE) {
                handleSmallPhoto(data);
            } else if (requestCode == IMAGE_CAPTURE) {
                handleSmallCameraPhoto(data);
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private void handleSmallCameraPhoto(Intent intent) {
        Bundle extras = intent.getExtras();
        Bitmap bitmap = (Bitmap) extras.get("data");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        imageData =  Base64.encodeToString(b, Base64.DEFAULT);
    }

    private void handleSmallPhoto(Intent intent){
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), intent.getData());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] b = byteArrayOutputStream.toByteArray();
            imageData = Base64.encodeToString(b, Base64.DEFAULT);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void loginClick(){
        finish();
        Intent toLogin = new Intent(ServiceProviderRegistrationActivity.this, LoginActivity.class);
        startActivity(toLogin);
    }

    void SPRegistartion(){
        finish();
        Intent toActivationActivity = new Intent(this, ActivitionActivity.class);
        startActivity(toActivationActivity);
    }


    private void registerClick(){
        if (checkValidation()){
            callServerToRegister();
            SPRegistartion();
        }
    }

    private void callServerToRegister(){
        Map<String,String> parms = new HashMap<String,String>();
        Users users=new Users();
        users.setUserName(nameEditText.getText().toString());
        users.setPhoneNo(mobileEditText.getText().toString());
        users.setEmail(emailEditText.getText().toString());
        users.setPassword(passwordEditText.getText().toString());
        users.setShopName(shopNameEditText.getText().toString());
        users.setMapLat(locationVariable.getLat()+"");
        users.setMapLng(locationVariable.getLng()+"");
        users.setPicturePath(imageData);
        Gson gson = new Gson();
        parms = gson.fromJson(gson.toJson(users), parms.getClass());
        Log.e("Parm", parms.toString());
        getJson= parms.toString();
        ConnectionVolley connectionVolley = new ConnectionVolley(this, Request.Method.POST, Url.getInstance().serviceProviderRegistrationURL, this, this, parms, true);
        AppController.getInstance().addToRequestQueue(connectionVolley, "");
    }

    public boolean checkValidation()
    {
        ArrayList<ValidationObject> list=new ArrayList<>();
        list.add(new ValidationObject(nameEditText , 4, true, R.string.nameErrorMsg));
        list.add(new ValidationObject(mobileEditText , 9, true, R.string.mobileErrorMsg));
        list.add(new ValidationObject(passwordEditText , 6, true, R.string.passwordErrorMsg));
        list.add(new ValidationObject(shopNameEditText , 1, true, R.string.passwordErrorMsg));
        list.add(new ValidationObject(passwordEditText, reTypePasswordEditText , true, R.string.passwordNotMatched));
        list.add(new ValidationObject(emailEditText , true, R.string.emailErrorMsg));
        return ViewsValidation.getInstance().validate(this,list);
    }

    @Override
    public void onResponse(Object response) {
        Log.e("response", response.toString());
        try {
            if (CheckResponse.getInstance().checkResponse(this, response.toString(), true)){
                loginClick();
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
