package ash.wasset.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ash.wasset.R;
import ash.wasset.activities.LoginActivity;
import ash.wasset.adapters.SocialMediaAdapter;
import ash.wasset.models.RequestModel;
import ash.wasset.models.SendCommentModel;
import ash.wasset.models.ServiceListModel;
import ash.wasset.models.SocialMediaModel;
import ash.wasset.models.StandardWebServiceResponse;
import ash.wasset.serverconnection.Url;
import ash.wasset.serverconnection.volley.AppController;
import ash.wasset.serverconnection.volley.ConnectionVolley;
import ash.wasset.utils.CheckResponse;
import ash.wasset.utils.GPSTracker;
import ash.wasset.utils.LoginSharedPreferences;
import ash.wasset.utils.Permissions;

/**
 * Created by ahmed on 12/7/16.
 */

public class ServiceListDetailsFragment extends Fragment implements View.OnClickListener, Response.Listener,
        Response.ErrorListener, AdapterView.OnItemClickListener {

    View view;
    String getJson, getCategoryName, getSocialMedia;
    TextView categoryNameTextView;
    ServiceListModel serviceListModel;
    ImageView serviceProviderImageView;
    TextView serviceProviderTextView, serviceProviderDetailsTextView;
    RatingBar serviceProviderRatingBar;
    LinearLayout sendRequestLayout, reviewsLayout, complainLayout, callLayout, rateLayout, workingHoursLayout;
    LoginSharedPreferences loginSharedPreferences;
    GPSTracker gpsTracker;
    Permissions permissions;
    final static int CALL_PERMISSION = 1111;
    int callStatus;
    ListView socialMediaListView;
    SocialMediaAdapter serviceListAdapter;
    ArrayList<SocialMediaModel> socialMediaModelArrayList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        view = inflater.inflate(R.layout.service_list_details_fragment, container, false);
        initialization();
        decodeJson(getJson);
        setData();
        callAdapter();
        socialMediaListView.setOnItemClickListener(this);

        return view;
    }

    private void initialization(){
        categoryNameTextView = (TextView) view.findViewById(R.id.categoryNameTextView);
        serviceProviderImageView = (ImageView) view.findViewById(R.id.serviceProviderImageView);
        serviceProviderTextView = (TextView) view.findViewById(R.id.serviceProviderTextView);
        serviceProviderRatingBar = (RatingBar) view.findViewById(R.id.serviceProviderRatingBar);
        serviceProviderDetailsTextView = (TextView) view.findViewById(R.id.serviceProviderDetailsTextView);
        sendRequestLayout = (LinearLayout) view.findViewById(R.id.sendRequestLayout);
        reviewsLayout = (LinearLayout) view.findViewById(R.id.reviewsLayout);
        complainLayout = (LinearLayout) view.findViewById(R.id.complainLayout);
        callLayout = (LinearLayout) view.findViewById(R.id.callLayout);
        rateLayout = (LinearLayout) view.findViewById(R.id.rateLayout);
        workingHoursLayout =  (LinearLayout) view.findViewById(R.id.workingHoursLayout);
        getJson = getArguments().getString("json");
        getCategoryName = getArguments().getString("categoryName");
        getSocialMedia = getArguments().getString("socialMedia");
        permissions = new Permissions();
        callStatus = 0;
        loginSharedPreferences = new LoginSharedPreferences();
        socialMediaListView = (ListView) view.findViewById(R.id.socialMediaListView);
        socialMediaModelArrayList = new ArrayList<>();
    }

    private void decodeJson(String jsonString){
        Gson gson = new Gson();
        serviceListModel = gson.fromJson(jsonString, ServiceListModel.class);
        StandardWebServiceResponse standardWebServiceResponse = new StandardWebServiceResponse();
        gson = new Gson();
        standardWebServiceResponse = gson.fromJson(getSocialMedia, StandardWebServiceResponse.class);
        gson = new Gson();
        GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        socialMediaModelArrayList = gson.fromJson(builder.create().toJson(standardWebServiceResponse.getData()), new TypeToken<List<SocialMediaModel>>(){}.getType());
    }

    private void callAdapter(){
        serviceListAdapter = new SocialMediaAdapter(getActivity(), socialMediaModelArrayList);
        socialMediaListView.setAdapter(serviceListAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(socialMediaModelArrayList.get(i).getUrl()));
            startActivity(browserIntent);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setData(){
        Picasso.with(getActivity())
                .load(Url.getInstance().profilePicturesURL + serviceListModel.getPicturePath())
                .placeholder(R.mipmap.logo)
                .into(serviceProviderImageView);
        serviceProviderRatingBar.setRating(Float.parseFloat(serviceListModel.getTotalRate()));
        serviceProviderTextView.setText(serviceListModel.getShopName());
        serviceProviderDetailsTextView.setText(serviceListModel.getDescription());
        categoryNameTextView.setText(getCategoryName);
        sendRequestLayout.setOnClickListener(this);
        reviewsLayout.setOnClickListener(this);
        complainLayout.setOnClickListener(this);
        callLayout.setOnClickListener(this);
        rateLayout.setOnClickListener(this);
        workingHoursLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sendRequestLayout:
                gpsTracker = new GPSTracker(getActivity());
                if(gpsTracker.canGetLocation()){
                    //callServerToSendRequest();
                    SendLocationFragment sendLocationFragment = new SendLocationFragment();
                    Bundle args = new Bundle();
                    args.putString("id", serviceListModel.getId());
                    sendLocationFragment.setArguments(args);
                    getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("").add(R.id.contentView, sendLocationFragment).commit();
                } else {
                    gpsTracker.showSettingsAlert();
                }
                break;
            case R.id.reviewsLayout:
                callServerToGetComments();
                break;
            case R.id.complainLayout:
                if (loginSharedPreferences.getActivated(getActivity()).equals("true")){
                    complainDialog();
                } else {
                    toLogin();
                }
                break;
            case R.id.callLayout:
                callAction();
                break;
            case R.id.rateLayout:
                if (loginSharedPreferences.getActivated(getActivity()).equals("true")){
                    rateDialog();
                } else {
                    toLogin();
                }
                break;
            case R.id.workingHoursLayout:
                callServerToGetWorkingHours();
                break;
        }
    }

    private void toLogin(){
        Intent toLogin = new Intent(getActivity(), LoginActivity.class);
        startActivity(toLogin);
    }

    private void callServerToGetWorkingHours(){
        Map<String,String> parms = new HashMap<String,String>();
        RequestModel requestModel=new RequestModel();
        requestModel.setServiceProviderId(serviceListModel.getId());
        Gson gson = new Gson();
        parms = gson.fromJson(gson.toJson(requestModel), parms.getClass());
        Log.e("Parm", parms.toString());
        ConnectionVolley connectionVolley = new ConnectionVolley(getActivity(), Request.Method.POST, Url.getInstance().workHoursURL, this, this, parms, true);
        AppController.getInstance().addToRequestQueue(connectionVolley, "");
    }

    private void rateDialog(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.send_rate_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final RatingBar serviceProviderRatingBar = (RatingBar)  dialog.findViewById(R.id.serviceProviderRatingBar);
        Button sendButton = (Button)  dialog.findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callServerToSendRate(serviceProviderRatingBar.getRating()+"");
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void callServerToSendRate(String rate){
        Map<String,String> parms = new HashMap<String,String>();
        SendCommentModel sendCommentModel=new SendCommentModel();
        sendCommentModel.setServiceProviderId(serviceListModel.getId());
        sendCommentModel.setRate(rate);
        Gson gson = new Gson();
        parms = gson.fromJson(gson.toJson(sendCommentModel), parms.getClass());
        Log.e("Parm", parms.toString());
        ConnectionVolley connectionVolley = new ConnectionVolley(getActivity(), Request.Method.POST, Url.getInstance().addReviewURL, this, this, parms, true);
        AppController.getInstance().addToRequestQueue(connectionVolley, "");
    }

    private void callAction(){
        if (serviceListModel.getShowPhone().equals("true")){
            if (permissions.hasPermission(getActivity(), Manifest.permission.CALL_PHONE)) {
                callStatus = 1;
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + serviceListModel.getPhoneNumber()));
                startActivityForResult(intent, 1);
            } else {
                this.requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSION);
            }
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.notAllowToCall), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case CALL_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callStatus = 1;
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + serviceListModel.getPhoneNumber()));
                    startActivityForResult(intent, 1);
                } else {
//                    permission is denied
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onResume() {
        if (callStatus != 0){
            callStatus = 0;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    callResultDialog();
                }
            }, 1000);
        }
        super.onResume();
    }

    private void callResultDialog(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.call_result_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button answeredButton = (Button)  dialog.findViewById(R.id.answeredButton);
        Button notAnsweredButton = (Button)  dialog.findViewById(R.id.notAnsweredButton);
        Button busyButton = (Button)  dialog.findViewById(R.id.busyButton);
        answeredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callServerToSendCallResult("0");
                dialog.dismiss();
            }
        });

        notAnsweredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callServerToSendCallResult("1");
                dialog.dismiss();
            }
        });

        busyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callServerToSendCallResult("2");
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void callServerToSendCallResult(String state){
        Map<String,String> parms = new HashMap<String,String>();
        RequestModel requestModel=new RequestModel();
        requestModel.setServiceProviderId(serviceListModel.getId());
        requestModel.setRquestState(state);
        requestModel.setRquestType("0");
        Gson gson = new Gson();
        parms = gson.fromJson(gson.toJson(requestModel), parms.getClass());
        Log.e("Parm", parms.toString());
        ConnectionVolley connectionVolley = new ConnectionVolley(getActivity(), Request.Method.POST, Url.getInstance().callMeURL, this, this, parms, false);
        AppController.getInstance().addToRequestQueue(connectionVolley, "");
    }

    private void callServerToSendRequest(){
        Map<String,String> parms = new HashMap<String,String>();
        RequestModel requestModel=new RequestModel();
        requestModel.setServiceProviderId(serviceListModel.getId());
        requestModel.setRquestState("3");
        requestModel.setRquestType("1");
        requestModel.setMapLat(gpsTracker.getLatitude()+"");
        requestModel.setMapLng(gpsTracker.getLongitude()+"");
        Gson gson = new Gson();
        parms = gson.fromJson(gson.toJson(requestModel), parms.getClass());
        Log.e("Parm", parms.toString());
        ConnectionVolley connectionVolley = new ConnectionVolley(getActivity(), Request.Method.POST, Url.getInstance().sendRequestURL, this, this, parms, true);
        AppController.getInstance().addToRequestQueue(connectionVolley, "");
    }

    private void callServerToRefresh(){
        Map<String,String> parms = new HashMap<String,String>();
        parms.put("Id", serviceListModel.getId());
        Log.e("Parm", parms.toString());
        ConnectionVolley connectionVolley = new ConnectionVolley(getActivity(), Request.Method.POST, Url.getInstance().getServiceProviderByidURL, this, this, parms, false);
        AppController.getInstance().addToRequestQueue(connectionVolley, "");
    }

    private void complainDialog(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.send_complain_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final EditText complainEditText = (EditText)  dialog.findViewById(R.id.complainEditText);
        Button sendButton = (Button)  dialog.findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (complainEditText.length() != 0){
                    callServerToSendComplain(complainEditText.getText().toString());
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    private void callServerToSendComplain(String complain){
        Map<String,String> parms = new HashMap<String,String>();
        RequestModel requestModel=new RequestModel();
        requestModel.setRquestDetails(complain);
        requestModel.setServiceProviderId(serviceListModel.getId());
        requestModel.setRquestState("4");
        requestModel.setRquestType("4");
        Gson gson = new Gson();
        parms = gson.fromJson(gson.toJson(requestModel), parms.getClass());
        Log.e("Parm", parms.toString());
        ConnectionVolley connectionVolley = new ConnectionVolley(getActivity(), Request.Method.POST, Url.getInstance().sendComplainsURL, this, this, parms, true);
        AppController.getInstance().addToRequestQueue(connectionVolley, "");
    }

    private void callServerToGetComments(){
        Map<String,String> parms = new HashMap<String,String>();
        RequestModel requestModel=new RequestModel();
        requestModel.setServiceProviderId(serviceListModel.getId());
        Gson gson = new Gson();
        parms = gson.fromJson(gson.toJson(requestModel), parms.getClass());
        Log.e("Parm", parms.toString());
        ConnectionVolley connectionVolley = new ConnectionVolley(getActivity(), Request.Method.POST, Url.getInstance().getServiceProviderReviewsURL, this, this, parms, true);
        AppController.getInstance().addToRequestQueue(connectionVolley, "");
    }

    private String checkServiceName(String jsonString){
        StandardWebServiceResponse standardWebServiceResponse = new StandardWebServiceResponse();
        Gson gson = new Gson();
        standardWebServiceResponse = gson.fromJson(jsonString, StandardWebServiceResponse.class);
        return standardWebServiceResponse.getServiceName();
    }

    private void toCommentsFragment(String result){
        CommentsServiceProviderFragment commentsServiceProviderFragment = new CommentsServiceProviderFragment();
        Bundle args = new Bundle();
        args.putString("json", result);
        args.putString("serviceProviderName", serviceListModel.getShopName());
        args.putString("serviceList", getJson);
        commentsServiceProviderFragment.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.contentView, commentsServiceProviderFragment).addToBackStack("").commit();
    }

    private void toWorkHoursFragment(String result){
        ServiceProviderHoursWorkFragment serviceProviderHoursWorkFragment = new ServiceProviderHoursWorkFragment();
        Bundle args = new Bundle();
        args.putString("json", result);
        serviceProviderHoursWorkFragment.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.contentView, serviceProviderHoursWorkFragment).addToBackStack("").commit();
    }

    private void updateRate(String jsonString){
        StandardWebServiceResponse standardWebServiceResponse = new StandardWebServiceResponse();
        Gson gson = new Gson();
        standardWebServiceResponse = gson.fromJson(jsonString, StandardWebServiceResponse.class);
        gson = new Gson();
        GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        ArrayList<ServiceListModel> serviceListModel2 = gson.fromJson(builder.create().toJson(standardWebServiceResponse.getData()), new TypeToken<List<ServiceListModel>>(){}.getType());
        serviceProviderRatingBar.setRating(Float.parseFloat(serviceListModel2.get(0).getTotalRate()));
    }

    @Override
    public void onResponse(Object response) {
        Log.e("response", response.toString());
        try {
            if (checkServiceName(response.toString()).equals("SendLocation")){
                if (CheckResponse.getInstance().checkResponse(getActivity(), response.toString(), false)){
                    Toast.makeText(getActivity(), getResources().getString(R.string.requestSent), Toast.LENGTH_LONG).show();
                }
            } else if (checkServiceName(response.toString()).equals("GetServiceProviderReviews")){
                if (CheckResponse.getInstance().checkResponse(getActivity(), response.toString(), false)){
                    toCommentsFragment(response.toString());
                }
            } else if (checkServiceName(response.toString()).equals("SendComplains")){
                if (CheckResponse.getInstance().checkResponse(getActivity(), response.toString(), false)){
                    Toast.makeText(getActivity(), getResources().getString(R.string.complainSent), Toast.LENGTH_LONG).show();
                }
            } else if (checkServiceName(response.toString()).equals("AddReview")){
                if (CheckResponse.getInstance().checkResponse(getActivity(), response.toString(), false)){
                    callServerToRefresh();
                    Toast.makeText(getActivity(), getResources().getString(R.string.rateSent), Toast.LENGTH_LONG).show();
                }
            } else if (checkServiceName(response.toString()).equals("GetServiceProviderHourWorks")){
                if (CheckResponse.getInstance().checkResponse(getActivity(), response.toString(), false)){
                    toWorkHoursFragment(response.toString());
                }
            } else if (checkServiceName(response.toString()).equals("GetServiceProviderByid")){
                if (CheckResponse.getInstance().checkResponse(getActivity(), response.toString(), false)){
                    updateRate(response.toString());
                }
            }
        } catch (Exception e){
            Toast.makeText(getActivity(), getResources().getString(R.string.connectionError), Toast.LENGTH_LONG).show();
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
