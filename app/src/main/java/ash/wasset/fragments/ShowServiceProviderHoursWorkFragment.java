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
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ash.wasset.R;
import ash.wasset.adapters.WorkingHoursAdapter;
import ash.wasset.models.StandardWebServiceResponse;
import ash.wasset.models.WorkHoursModel;
import ash.wasset.serverconnection.Url;
import ash.wasset.serverconnection.volley.AppController;
import ash.wasset.serverconnection.volley.ConnectionVolley;
import ash.wasset.utils.CheckResponse;

/**
 * Created by ahmed on 12/22/16.
 */

public class ShowServiceProviderHoursWorkFragment extends Fragment implements AdapterView.OnItemClickListener, Response.Listener, Response.ErrorListener{

    View view;
    ListView workingHoursListView;
    String getJson;
    ArrayList<WorkHoursModel> workHoursModelArrayList;
    WorkingHoursAdapter workingHoursAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.service_provider_hours_work_fragment, container, false);
        initialization();
        set();
        decodeJson(getJson);
        checkLenth();
        callAdapter();

        return view;
    }

    private void set(){
        workingHoursListView.setOnItemClickListener(this);
    }

    private void initialization(){
        workingHoursListView = (ListView) view.findViewById(R.id.workingHoursListView);
        getJson = getArguments().getString("json");
    }

    private void checkLenth(){
        if (workHoursModelArrayList.size() == 0)
            workingHoursListView.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        UpdateWorkHoursFragment updateWorkHoursFragment = new UpdateWorkHoursFragment();
        Bundle args = new Bundle();
        Gson gson = new GsonBuilder().create();
        JsonArray workHoursModelJsonArray = gson.toJsonTree(workHoursModelArrayList).getAsJsonArray();
        args.putString("json", workHoursModelJsonArray.get(i).toString());
        updateWorkHoursFragment.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.contentView, updateWorkHoursFragment).addToBackStack("").commit();
    }

    private void decodeJson(String jsonString){
        StandardWebServiceResponse standardWebServiceResponse = new StandardWebServiceResponse();
        Gson gson = new Gson();
        standardWebServiceResponse = gson.fromJson(jsonString, StandardWebServiceResponse.class);
        workHoursModelArrayList = new ArrayList<>();
        gson = new Gson();
        GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        workHoursModelArrayList = gson.fromJson(builder.create().toJson(standardWebServiceResponse.getData()), new TypeToken<List<WorkHoursModel>>(){}.getType());
    }

    private void callAdapter(){
        workingHoursAdapter = new WorkingHoursAdapter(getActivity(), workHoursModelArrayList);
        workingHoursListView.setAdapter(workingHoursAdapter);
    }

    @Override
    public void onResume() {
        callServerToGetWorkingHours();
        super.onResume();
    }

    private void callServerToGetWorkingHours(){
        Map<String,String> parms = new HashMap<String,String>();
        ConnectionVolley connectionVolley = new ConnectionVolley(getActivity(), Request.Method.POST, Url.getInstance().getWorkHoursURL, this, this, parms, false);
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
            if (checkServiceName(response.toString()).equals("GetWorkHours")){
                if (CheckResponse.getInstance().checkResponse(getActivity(), response.toString(), false)){
                    decodeJson(response.toString());
                    callAdapter();
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
