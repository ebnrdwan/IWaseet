package ash.wasset.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ash.wasset.R;
import ash.wasset.activities.LoginActivity;
import ash.wasset.adapters.ServiceProviderCommentsAdapter;
import ash.wasset.models.CommentsModel;
import ash.wasset.models.SendCommentModel;
import ash.wasset.models.ServiceListModel;
import ash.wasset.models.StandardWebServiceResponse;
import ash.wasset.serverconnection.Url;
import ash.wasset.serverconnection.volley.AppController;
import ash.wasset.serverconnection.volley.ConnectionVolley;
import ash.wasset.utils.CheckResponse;
import ash.wasset.utils.LoginSharedPreferences;

/**
 * Created by ahmed on 12/12/16.
 */

public class CommentsServiceProviderFragment extends Fragment implements View.OnClickListener, Response.Listener, Response.ErrorListener{

    View view;
    ListView commentsListView;
    TextView serviceProvicerNameTextView;
    EditText commentEditText;
    ImageView sendImageView;
    String getJson, getServiceProviderName, getServiceList;
    ServiceProviderCommentsAdapter serviceProviderCommentsAdapter;
    ArrayList<CommentsModel> commentsModelArrayList;
    ServiceListModel serviceListModel;
    LoginSharedPreferences loginSharedPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.comments_for_service_provider_fragment, container, false);
        initialization();
        set();
        decodeJson(getJson);
        decodeJsonserviceList(getServiceList);
        checkLenth();
        callAdapter();

        return view;
    }

    private void checkLenth(){
        if (commentsModelArrayList.size() == 0)
            commentsListView.setVisibility(View.GONE);
    }

    private void initialization(){
        commentsListView = (ListView) view.findViewById(R.id.commentsListView);
        serviceProvicerNameTextView = (TextView) view.findViewById(R.id.serviceProvicerNameTextView);
        commentEditText = (EditText) view.findViewById(R.id.commentEditText);
        sendImageView = (ImageView) view.findViewById(R.id.sendImageView);
        getJson = getArguments().getString("json");
        getServiceList = getArguments().getString("serviceList");
        getServiceProviderName = getArguments().getString("serviceProviderName");
        serviceProviderCommentsAdapter = new ServiceProviderCommentsAdapter();
        commentsModelArrayList = new ArrayList<>();
        loginSharedPreferences = new LoginSharedPreferences();
    }

    private void decodeJsonserviceList(String jsonString){
        Gson gson = new Gson();
        serviceListModel = gson.fromJson(jsonString, ServiceListModel.class);
    }

    private void decodeJson(String jsonString){
        StandardWebServiceResponse standardWebServiceResponse = new StandardWebServiceResponse();
        Gson gson = new Gson();
        standardWebServiceResponse = gson.fromJson(jsonString, StandardWebServiceResponse.class);
        commentsModelArrayList = new ArrayList<>();
        gson = new Gson();
        GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        commentsModelArrayList = gson.fromJson(builder.create().toJson(standardWebServiceResponse.getData()), new TypeToken<List<CommentsModel>>(){}.getType());
    }

    private void set(){
        serviceProvicerNameTextView.setText(getServiceProviderName);
        sendImageView.setOnClickListener(this);
    }

    private void callAdapter(){
        serviceProviderCommentsAdapter = new ServiceProviderCommentsAdapter(getActivity(), commentsModelArrayList);
        commentsListView.setAdapter(serviceProviderCommentsAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sendImageView:
                if (loginSharedPreferences.getActivated(getActivity()).equals("true")){
                    if (commentEditText.length() != 0)
                        callServerToSendComment();
                } else {
                    toLogin();
                }
                break;
        }
    }

    private void toLogin(){
        Intent toLogin = new Intent(getActivity(), LoginActivity.class);
        startActivity(toLogin);
    }

    private void callServerToSendComment(){
        Map<String,String> parms = new HashMap<String,String>();
        SendCommentModel sendCommentModel=new SendCommentModel();
        sendCommentModel.setServiceProviderId(serviceListModel.getId());
        sendCommentModel.setComment(commentEditText.getText().toString());
        Gson gson = new Gson();
        parms = gson.fromJson(gson.toJson(sendCommentModel), parms.getClass());
        Log.e("Parm", parms.toString());
        ConnectionVolley connectionVolley = new ConnectionVolley(getActivity(), Request.Method.POST, Url.getInstance().addReviewURL, this, this, parms, true);
        AppController.getInstance().addToRequestQueue(connectionVolley, "");
    }

    private String checkServiceName(String jsonString){
        StandardWebServiceResponse standardWebServiceResponse = new StandardWebServiceResponse();
        Gson gson = new Gson();
        standardWebServiceResponse = gson.fromJson(jsonString, StandardWebServiceResponse.class);
        return standardWebServiceResponse.getServiceName();
    }

    private void addNewMessage(){
        commentsModelArrayList.add(new CommentsModel(loginSharedPreferences.getUserName(getActivity()),
                loginSharedPreferences.getPicturePath(getActivity()), "", commentEditText.getText().toString(),
                "0", "0"));
        serviceProviderCommentsAdapter.notifyDataSetChanged();
        commentEditText.setText("");
    }

    @Override
    public void onResponse(Object response) {
        Log.e("response", response.toString());
        try {
            if (checkServiceName(response.toString()).equals("AddReview")){
                if (CheckResponse.getInstance().checkResponse(getActivity(), response.toString(), false)){
                    addNewMessage();
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
