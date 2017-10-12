package ash.wasset.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import ash.wasset.adapters.LocationRequestsAdapter;
import ash.wasset.models.LocationRequestModel;
import ash.wasset.models.StandardWebServiceResponse;
import ash.wasset.serverconnection.Url;
import ash.wasset.serverconnection.volley.AppController;
import ash.wasset.serverconnection.volley.ConnectionVolley;
import ash.wasset.utils.CheckResponse;

/**
 * Created by ahmed on 12/29/16.
 */

public class ClientLocationRequestsFragment extends Fragment implements Response.Listener, Response.ErrorListener,
        AdapterView.OnItemClickListener {

    View view;
    ListView requestsListView;
    LocationRequestsAdapter locationRequestsAdapter;
    ArrayList<LocationRequestModel> locationRequestModels;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.client_location_requests_fragment, container, false);
        initialization();
        set();
        callServerToShowLocationRequests();
        return view;
    }

    private void initialization(){
        requestsListView = (ListView) view.findViewById(R.id.requestsListView);
    }

    private void set(){
        requestsListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TrackingFragment trackingFragment = new TrackingFragment();
        Bundle args = new Bundle();
        args.putString("lat", locationRequestModels.get(i).getMapLat()+"");
        args.putString("lng", locationRequestModels.get(i).getMapLng()+"");
        trackingFragment.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.contentView, trackingFragment).addToBackStack("").commit();
    }

    private void checkLenth(){
        if (locationRequestModels.size() == 0)
            requestsListView.setVisibility(View.GONE);
    }

    void callServerToShowLocationRequests(){
        Map<String,String> parms = new HashMap<String,String>();
        ConnectionVolley connectionVolley = new ConnectionVolley(getActivity(), Request.Method.POST, Url.getInstance().showLocationRequestsURL, this, this, parms, true);
        AppController.getInstance().addToRequestQueue(connectionVolley, "");
    }

    private String checkServiceName(String jsonString){
        StandardWebServiceResponse standardWebServiceResponse = new StandardWebServiceResponse();
        Gson gson = new Gson();
        standardWebServiceResponse = gson.fromJson(jsonString, StandardWebServiceResponse.class);
        return standardWebServiceResponse.getServiceName();
    }

    private void callAdapter(){
        locationRequestsAdapter = new LocationRequestsAdapter(getActivity(), locationRequestModels);
        requestsListView.setAdapter(locationRequestsAdapter);
    }

    private void decodeJson(String jsonString){
        StandardWebServiceResponse standardWebServiceResponse = new StandardWebServiceResponse();
        Gson gson = new Gson();
        standardWebServiceResponse = gson.fromJson(jsonString, StandardWebServiceResponse.class);
        locationRequestModels = new ArrayList<>();
        gson = new Gson();
        GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        locationRequestModels = gson.fromJson(builder.create().toJson(standardWebServiceResponse.getData()), new TypeToken<List<LocationRequestModel>>(){}.getType());
    }

    @Override
    public void onResponse(Object response) {
        Log.e("response", response.toString());
        try {
            if (checkServiceName(response.toString()).equals("ShowLocationRequests")){
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
