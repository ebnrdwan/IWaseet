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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ash.wasset.R;
import ash.wasset.models.StandardWebServiceResponse;
import ash.wasset.models.Users;
import ash.wasset.serverconnection.Url;
import ash.wasset.serverconnection.volley.AppController;
import ash.wasset.serverconnection.volley.ConnectionVolley;
import ash.wasset.utils.CheckResponse;
import ash.wasset.utils.Validation.ValidationObject;
import ash.wasset.utils.Validation.ViewsValidation;

/**
 * Created by ahmed on 2/12/17.
 */

public class UpdateShopFragment extends Fragment implements View.OnClickListener, Response.Listener, Response.ErrorListener {

    View view;
    EditText nameEditText, detailsEditText;
    Button updateButton;
    String getJson;
    Users usersModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.update_shop_fragment, container, false);
        initialization();
        decodeJson(getJson);
        set();
        return view;
    }

    private void initialization(){
        nameEditText = (EditText) view.findViewById(R.id.nameEditText);
        detailsEditText = (EditText) view.findViewById(R.id.detailsEditText);
        updateButton = (Button) view.findViewById(R.id.updateButton);
        getJson = getArguments().getString("json");
    }

    private void decodeJson(String jsonString){
        StandardWebServiceResponse standardWebServiceResponse = new StandardWebServiceResponse();
        Gson gson = new Gson();
        standardWebServiceResponse = gson.fromJson(jsonString, StandardWebServiceResponse.class);
        gson = new Gson();
        GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        usersModel = gson.fromJson(builder.create().toJson(standardWebServiceResponse.getData()), Users.class);
    }

    private void set(){
        updateButton.setOnClickListener(this);
        nameEditText.setText(usersModel.getShopName());
        detailsEditText.setText(usersModel.getDescription());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.updateButton:
                updateClick();
                break;
        }
    }

    private void updateClick(){
        if (checkValidation()){
            callServerToUpdate();
        }
    }

    public boolean checkValidation()
    {
        ArrayList<ValidationObject> list=new ArrayList<>();
        list.add(new ValidationObject(nameEditText , 4, true, R.string.nameErrorMsg));
        list.add(new ValidationObject(detailsEditText , true, R.string.nameErrorMsg));
        return ViewsValidation.getInstance().validate(getActivity(),list);
    }

    private void callServerToUpdate(){
        Map<String,String> parms = new HashMap<String,String>();
        Users users=new Users();
        users.setName(usersModel.getName());
        users.setEmail(usersModel.getEmail());
        users.setShowPhone(usersModel.getShowPhone());
        users.setShopName(nameEditText.getText().toString());
        users.setDescription(detailsEditText.getText().toString());
        users.setMapLat(usersModel.getMapLat());
        users.setMapLng(usersModel.getMapLng());
        Gson gson = new Gson();
        parms = gson.fromJson(gson.toJson(users).toString(), parms.getClass());
        Log.e("Parm", parms.toString());
        ConnectionVolley connectionVolley = new ConnectionVolley(getActivity(), Request.Method.POST, Url.getInstance().updateUserProfileURL, this, this, parms, true);
        AppController.getInstance().addToRequestQueue(connectionVolley, "");
    }

    private void removeVeiw(){
        getActivity().getSupportFragmentManager().popBackStack();
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    @Override
    public void onResponse(Object response) {
        Log.e("response", response.toString());
        try {
            if (CheckResponse.getInstance().checkResponse(getActivity(), response.toString(), true)){
                removeVeiw();
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
