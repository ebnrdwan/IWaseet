package ash.wasset.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
import ash.wasset.adapters.CallRequestsAdapter;
import ash.wasset.models.CallRequestModel;
import ash.wasset.models.StandardWebServiceResponse;
import ash.wasset.serverconnection.Url;
import ash.wasset.serverconnection.volley.AppController;
import ash.wasset.serverconnection.volley.ConnectionVolley;
import ash.wasset.utils.CheckResponse;

/**
 * Created by ahmed on 1/2/17.
 */

public class ClientCallRequestsFragment extends Fragment implements Response.Listener, Response.ErrorListener {

    View view;
    ListView requestsListView;
    ArrayList<CallRequestModel> callRequestModels;
    CallRequestsAdapter callRequestsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.client_call_requests_fragment, container, false);
        initialization();
        callServerToShowCallRequests();
        return view;
    }

    private void initialization(){
        requestsListView = (ListView) view.findViewById(R.id.requestsListView);
    }

    private void checkLenth(){
        if (callRequestModels.size() == 0)
            requestsListView.setVisibility(View.GONE);
    }

    private String checkServiceName(String jsonString){
        StandardWebServiceResponse standardWebServiceResponse = new StandardWebServiceResponse();
        Gson gson = new Gson();
        standardWebServiceResponse = gson.fromJson(jsonString, StandardWebServiceResponse.class);
        return standardWebServiceResponse.getServiceName();
    }

    void callServerToShowCallRequests(){
        Map<String,String> parms = new HashMap<String,String>();
        ConnectionVolley connectionVolley = new ConnectionVolley(getActivity(), Request.Method.POST, Url.getInstance().ShowCallRequeststsURL, this, this, parms, false);
        AppController.getInstance().addToRequestQueue(connectionVolley, "");
    }

    private void callAdapter(){
        callRequestsAdapter = new CallRequestsAdapter(getActivity(), callRequestModels);
        requestsListView.setAdapter(callRequestsAdapter);
    }

    private void decodeJson(String jsonString){
        StandardWebServiceResponse standardWebServiceResponse = new StandardWebServiceResponse();
        Gson gson = new Gson();
        standardWebServiceResponse = gson.fromJson(jsonString, StandardWebServiceResponse.class);
        callRequestModels = new ArrayList<>();
        gson = new Gson();
        GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        callRequestModels = gson.fromJson(builder.create().toJson(standardWebServiceResponse.getData()), new TypeToken<List<CallRequestModel>>(){}.getType());
    }

    @Override
    public void onResponse(Object response) {
        Log.e("response", response.toString());
        try {
            if (checkServiceName(response.toString()).equals("ShowCallRequests")){
                if (CheckResponse.getInstance().checkResponse(getActivity(), response.toString(), false)){
                    decodeJson(response.toString());
                    callAdapter();
                    checkLenth();
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
