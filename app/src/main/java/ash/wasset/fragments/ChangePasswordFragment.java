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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ash.wasset.R;
import ash.wasset.models.PasswordModel;
import ash.wasset.serverconnection.Url;
import ash.wasset.serverconnection.volley.AppController;
import ash.wasset.serverconnection.volley.ConnectionVolley;
import ash.wasset.utils.CheckResponse;
import ash.wasset.utils.LoginSharedPreferences;
import ash.wasset.utils.Validation.ValidationObject;
import ash.wasset.utils.Validation.ViewsValidation;

/**
 * Created by ahmed on 12/19/16.
 */

public class ChangePasswordFragment extends Fragment implements View.OnClickListener, Response.Listener, Response.ErrorListener{

    View view;
    EditText oldPasswordEditText, newPasswordEditText, reTypePasswordEditText;
    Button updateButton;
    LoginSharedPreferences loginSharedPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.change_password_fragment, container, false);
        initialization();
        set();
        return view;
    }

    private void initialization(){
        oldPasswordEditText = (EditText) view.findViewById(R.id.oldPasswordEditText);
        newPasswordEditText = (EditText) view.findViewById(R.id.newPasswordEditText);
        reTypePasswordEditText = (EditText) view.findViewById(R.id.reTypePasswordEditText);
        updateButton = (Button) view.findViewById(R.id.updateButton);
        loginSharedPreferences = new LoginSharedPreferences();
    }

    private void set(){
        updateButton.setOnClickListener(this);

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
        list.add(new ValidationObject(oldPasswordEditText , 6, true, R.string.passwordErrorMsg));
        list.add(new ValidationObject(newPasswordEditText , 6, true, R.string.passwordErrorMsg));
        list.add(new ValidationObject(newPasswordEditText, reTypePasswordEditText , true, R.string.passwordNotMatched));
        return ViewsValidation.getInstance().validate(getActivity(),list);
    }

    private void callServerToUpdate(){
        Map<String,String> parms = new HashMap<String,String>();
        PasswordModel passwordModel = new PasswordModel();
        passwordModel.setNewPassword(newPasswordEditText.getText().toString());
        passwordModel.setOldPassword(oldPasswordEditText.getText().toString());
        Gson gson = new Gson();
        parms = gson.fromJson(gson.toJson(passwordModel).toString(), parms.getClass());
        Log.e("Parm", parms.toString());
        ConnectionVolley connectionVolley = new ConnectionVolley(getActivity(), Request.Method.POST, Url.getInstance().updateUserProfilePasswordURL, this, this, parms, true);
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
                loginSharedPreferences.setPassword(newPasswordEditText.getText().toString());
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
