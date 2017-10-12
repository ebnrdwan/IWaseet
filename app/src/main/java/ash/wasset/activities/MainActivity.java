package ash.wasset.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ash.wasset.R;
import ash.wasset.adapters.MainMenuAdapter;
import ash.wasset.fragments.CategoriesFragment;
import ash.wasset.fragments.ChangePasswordFragment;
import ash.wasset.fragments.UpdateUserProfileFragment;
import ash.wasset.models.MainMenuModel;
import ash.wasset.models.StandardWebServiceResponse;
import ash.wasset.models.Users;
import ash.wasset.serverconnection.Url;
import ash.wasset.serverconnection.volley.AppController;
import ash.wasset.serverconnection.volley.ConnectionVolley;
import ash.wasset.utils.CheckResponse;
import ash.wasset.utils.GeneralClass;
import ash.wasset.utils.LoginSharedPreferences;
import ash.wasset.utils.Permissions;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
        //, SearchView.OnQueryTextListener,
        ,Response.Listener, Response.ErrorListener, AdapterView.OnItemClickListener {

    String getJson;
    ImageView menuImageView;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ListView menuListView;
    ArrayList<MainMenuModel> mainMenuModels;
    MainMenuAdapter mainMenuAdapter;
    LoginSharedPreferences loginSharedPreferences;
    View mainMenuHeader;
    ImageView userImageView;
    FloatingActionButton chooseImageButton;
    Permissions permissions;
    final static int SELECT_IMAGE = 100;
    final static int IMAGE_CAPTURE = 200;
    final static int STORAGE_PERMISSION = 1111;
    final static int CAMERA_PERMISSION = 1888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialization();
        menuContent();
        onClick();
        callAdapter();
       categoriesFragment(getJson);
    }

    private void initialization(){
        getJson = getIntent().getStringExtra("json");
        menuImageView = (ImageView) findViewById(R.id.menuImageView);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        menuListView = (ListView) findViewById(R.id.menuListView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mainMenuModels = new ArrayList<>();
        loginSharedPreferences = new LoginSharedPreferences();
        permissions = new Permissions();
        mainMenuHeader = (View) getLayoutInflater().inflate(R.layout.main_menu_header, null);
        userImageView = (ImageView) mainMenuHeader.findViewById(R.id.userImageView);
        chooseImageButton = (FloatingActionButton) mainMenuHeader.findViewById(R.id.chooseImageButton);
    }

    private void onClick(){
        menuImageView.setOnClickListener(this);
        menuListView.setOnItemClickListener(this);
        userImageView.setOnClickListener(this);
        chooseImageButton.setOnClickListener(this);
        menuListView.addHeaderView(mainMenuHeader);
        setSupportActionBar(toolbar);
        Picasso.with(this)
                .load(Url.getInstance().profilePicturesURL + loginSharedPreferences.getPicturePath(this))
                .placeholder(R.mipmap.logo)
                .into(userImageView);
    }

    private void menuContent(){
        mainMenuModels.add(new MainMenuModel(getResources().getString(R.string.updateProfile), R.mipmap.profile_circle_icon));
        mainMenuModels.add(new MainMenuModel(getResources().getString(R.string.changePassword), R.mipmap.password_circle_icon));
        mainMenuModels.add(new MainMenuModel(getResources().getString(R.string.shareApp), R.mipmap.share_icon));
        mainMenuModels.add(new MainMenuModel(getResources().getString(R.string.language), R.mipmap.share_icon));
        mainMenuModels.add(new MainMenuModel(getResources().getString(R.string.signOut), R.mipmap.sign_out_icon));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        drawerLayout.closeDrawers();
        switch (i){
            case 1:
                if (loginSharedPreferences.getActivated(this).equals("true")){
                    callServerToLogin();
                } else {
                    toLogin();
                }
                break;
            case 2:
                toChangePasswordFragment();
                break;
            case 3:
                shareApp();
                break;
            case 4:
                checkkLanguage();
                break;
            case 5:
                signOut();
                break;
        }
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

    private void shareApp(){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Here is the share content body";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.shareVia)));
    }

    private void signOut(){
        loginSharedPreferences.removeLogin(this);
        finish();
        Intent toHomeActivity = new Intent(this, RegisterOneActivity.class);
        startActivity(toHomeActivity);
    }

    private void toChangePasswordFragment(){
        ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.contentView, changePasswordFragment).addToBackStack("").commit();
    }

    private void callServerToLogin(){
        Map<String,String> parms = new HashMap<String,String>();
        Users users=new Users();
        users.setPhoneNo(loginSharedPreferences.getPhoneNumber(this));
        users.setPassword(loginSharedPreferences.getPassword(this));
        Gson gson = new Gson();
        parms = gson.fromJson(gson.toJson(users), parms.getClass());
        Log.e("Parm", parms.toString());
        ConnectionVolley connectionVolley = new ConnectionVolley(this, Request.Method.POST, Url.getInstance().loginURL, this, this, parms, true);
        AppController.getInstance().addToRequestQueue(connectionVolley, "");
    }

    private void callAdapter(){
        mainMenuAdapter = new MainMenuAdapter(this, mainMenuModels);
        menuListView.setAdapter(mainMenuAdapter);
    }

    void callServerToGetCategories(){
        Map<String,String> parms = new HashMap<String,String>();
        ConnectionVolley connectionVolley = new ConnectionVolley(this, Request.Method.POST, Url.getInstance().categoriesURL, this, this, parms, false);
        AppController.getInstance().addToRequestQueue(connectionVolley, "");
    }
    private void categoriesFragment(String json){
        CategoriesFragment categoriesFragment = new CategoriesFragment();
        Bundle args = new Bundle();
        args.putString("json", json);
        categoriesFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().add(R.id.contentView, categoriesFragment).commit();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.seach_menu, menu);
//        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
//        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setOnQueryTextListener(this);
//
//        MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem item) {
//                if (getSupportActionBar() != null) {
//                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.lieghtBlue)));
//                }
//                return true;
//            }
//
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem item) {
//                if (getSupportActionBar() != null) {
//                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
//                }
//                return true;
//            }
//        });
//
//        return true;
//    }
//
//    @Override
//    public boolean onQueryTextSubmit(String query) {
//        //Toast.makeText(this, query, Toast.LENGTH_LONG).show();
//        return false;
//    }
//
//    @Override
//    public boolean onQueryTextChange(String newText) {
//        return false;
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.menuImageView:
                openAndCloseDrawer();
                break;
            case R.id.userImageView:
                if (loginSharedPreferences.getActivated(this).equals("true")){
                    takePhotoDialog();
                } else {
                    toLogin();
                }
                break;
            case R.id.chooseImageButton:
                if (loginSharedPreferences.getActivated(this).equals("true")){
                    takePhotoDialog();
                } else {
                    toLogin();
                }
                break;
        }
    }

    private void toLogin(){
        Intent toLogin = new Intent(this, LoginActivity.class);
        startActivity(toLogin);
    }

    void takePhotoDialog(){
        drawerLayout.closeDrawer(menuListView);
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
        callServerToChangePic(Base64.encodeToString(b, Base64.DEFAULT));
    }

    private void handleSmallPhoto(Intent intent){
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), intent.getData());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] b = byteArrayOutputStream.toByteArray();
            callServerToChangePic(Base64.encodeToString(b, Base64.DEFAULT));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void callServerToChangePic(String imageData){
        Log.e("Image", imageData);
        Map<String,String> params = new HashMap<String, String>();
        params.put("ImageBase64", imageData);
        ConnectionVolley connectionVolley = new ConnectionVolley(this, Request.Method.POST, Url.getInstance().updateUserProfilePicURL, this, this, params, true);
        AppController.getInstance().addToRequestQueue(connectionVolley, "");
    }

    private void openAndCloseDrawer(){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(menuListView)){
            drawerLayout.closeDrawer(menuListView);
        } else{
            if(getSupportFragmentManager().getBackStackEntryCount() != 0){
                FragmentManager.BackStackEntry backEntry = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1);
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.contentView)).commit();
            } else {
                finish();
            }
        }
    }

    private String checkServiceName(String jsonString){
        StandardWebServiceResponse standardWebServiceResponse = new StandardWebServiceResponse();
        Gson gson = new Gson();
        standardWebServiceResponse = gson.fromJson(jsonString, StandardWebServiceResponse.class);
        return standardWebServiceResponse.getServiceName();
    }

    private void toUpdateUserProfileFragment(String result){
        UpdateUserProfileFragment updateUserProfileFragment = new UpdateUserProfileFragment();
        Bundle args = new Bundle();
        args.putString("json", result);
        updateUserProfileFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().add(R.id.contentView, updateUserProfileFragment).addToBackStack("").commit();
    }

    @Override
    public void onResponse(Object response) {
        Log.e("response", response.toString());
        try {
            if (checkServiceName(response.toString()).equals("Login")){
                if (CheckResponse.getInstance().checkResponse(this, response.toString(), false)){
                    toUpdateUserProfileFragment(response.toString());
                } else {

                }
            } else if (checkServiceName(response.toString()).equals("UpdateUserProfilePic")){
                if (CheckResponse.getInstance().checkResponse(this, response.toString(), true)){

                } else {

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
