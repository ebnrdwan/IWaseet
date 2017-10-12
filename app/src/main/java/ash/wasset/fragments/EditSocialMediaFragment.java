package ash.wasset.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import ash.wasset.R;
import ash.wasset.models.StandardWebServiceResponse;
import ash.wasset.serverconnection.Url;
import ash.wasset.serverconnection.volley.AppController;
import ash.wasset.serverconnection.volley.ConnectionVolley;
import ash.wasset.utils.CheckResponse;

/**
 * Created by ahmed on 1/31/17.
 */

public class EditSocialMediaFragment extends Fragment implements View.OnClickListener, Response.Listener, Response.ErrorListener {

    View view;
    String getUrl, getId;
    Spinner socialMediaSpinner;
    EditText urlEditText;
    Button updateButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.edit_social_media_fragment, container, false);
        initialization();
        set();
        return view;
    }

    private void initialization(){
        getUrl = getArguments().getString("url");
        getId = getArguments().getString("id");
        updateButton = (Button) view.findViewById(R.id.updateButton);
        urlEditText = (EditText) view.findViewById(R.id.urlEditText);
        socialMediaSpinner = (Spinner) view.findViewById(R.id.socialMediaSpinner);
    }

    private void set(){
        updateButton.setOnClickListener(this);
        urlEditText.setText(getUrl);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.updateButton:
                callServerTo();
                break;
        }
    }

    private void callServerTo(){
        Map<String,String> params = new HashMap<String, String>();
        params.put("Id", getId);
        params.put("Url", urlEditText.getText().toString());
        Log.e("re", params.toString());
        ConnectionVolley connectionVolley = new ConnectionVolley(getActivity(), Request.Method.POST, Url.getInstance().updateSocialMediaURL, this, this, params, true);
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
            if (checkServiceName(response.toString()).equals("UpdateSpUserSocialMedia")){
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
