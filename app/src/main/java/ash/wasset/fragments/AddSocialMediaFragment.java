package ash.wasset.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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
import ash.wasset.models.SocialMediaModel;
import ash.wasset.models.StandardWebServiceResponse;
import ash.wasset.serverconnection.Url;
import ash.wasset.serverconnection.volley.AppController;
import ash.wasset.serverconnection.volley.ConnectionVolley;
import ash.wasset.utils.CheckResponse;

/**
 * Created by ahmed on 1/30/17.
 */

public class AddSocialMediaFragment extends Fragment implements View.OnClickListener, Response.Listener, Response.ErrorListener {

    View view;
    String getJson;
    ArrayList<SocialMediaModel> socialMediaModelArrayList;
    Spinner socialMediaSpinner;
    EditText urlEditText;
    Button addButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_social_media_fragment, container, false);
        initialization();
        decodeJson();
        set();
        spinnerAdapter();
        return view;
    }

    private void initialization(){
        getJson = getArguments().getString("json");
        socialMediaModelArrayList = new ArrayList<>();
        addButton = (Button) view.findViewById(R.id.addButton);
        urlEditText = (EditText) view.findViewById(R.id.urlEditText);
        socialMediaSpinner = (Spinner) view.findViewById(R.id.socialMediaSpinner);
    }

    private void decodeJson(){
        Gson gson = new Gson();
        StandardWebServiceResponse standardWebServiceResponse = new StandardWebServiceResponse();
        gson = new Gson();
        standardWebServiceResponse = gson.fromJson(getJson, StandardWebServiceResponse.class);
        gson = new Gson();
        GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        socialMediaModelArrayList = gson.fromJson(builder.create().toJson(standardWebServiceResponse.getData()), new TypeToken<List<SocialMediaModel>>(){}.getType());
    }

    private void set(){
        addButton.setOnClickListener(this);
    }

    private void spinnerAdapter(){
        ArrayAdapter<SocialMediaModel> spinnerAdapter = new ArrayAdapter<SocialMediaModel>(getActivity(), android.R.layout.simple_spinner_item, socialMediaModelArrayList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        socialMediaSpinner.setAdapter(spinnerAdapter);
        spinnerAdapter.notifyDataSetChanged();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.addButton:
                callServerTo();
                break;
        }
    }

    private void callServerTo(){
        Map<String,String> params = new HashMap<String, String>();
        params.put("Id", "8");
        params.put("ServiceProviderId", "8");
        params.put("SocialMediaId", socialMediaModelArrayList.get(socialMediaSpinner.getSelectedItemPosition()).getId());
        params.put("Url", urlEditText.getText().toString());
        Log.e("re", params.toString());
        ConnectionVolley connectionVolley = new ConnectionVolley(getActivity(), Request.Method.POST, Url.getInstance().addSocialMediaURL, this, this, params, true);
        AppController.getInstance().addToRequestQueue(connectionVolley, "");
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
            try {
                ConnectionVolley.dialog.dismiss();
            } catch (Exception e){
                e.printStackTrace();
            }
            if (checkServiceName(response.toString()).equals("AddSpUserSocialMedia")){
                if (CheckResponse.getInstance().checkResponse(getActivity(), response.toString(), true)){
                    getActivity().getSupportFragmentManager().popBackStack();
                    getActivity().getSupportFragmentManager().popBackStack();
                    getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
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
