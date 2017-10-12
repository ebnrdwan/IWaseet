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
import ash.wasset.adapters.ClientReviewsAdapter;
import ash.wasset.models.ReviewModel;
import ash.wasset.models.StandardWebServiceResponse;
import ash.wasset.serverconnection.Url;
import ash.wasset.serverconnection.volley.AppController;
import ash.wasset.serverconnection.volley.ConnectionVolley;
import ash.wasset.utils.CheckResponse;

/**
 * Created by ahmed on 1/3/17.
 */

public class ClientReviewsFragment extends Fragment implements Response.Listener, Response.ErrorListener {

    View view;
    ListView reviewsListView;
    ArrayList<ReviewModel> reviewModels;
    ClientReviewsAdapter clientReviewsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.client_reviews_fragment, container, false);
        initialization();
        callServerToShowReviews();
        return view;
    }

    private void initialization(){
        reviewsListView = (ListView) view.findViewById(R.id.reviewsListView);
    }

    private void checkLenth(){
        if (reviewModels.size() == 0)
            reviewsListView.setVisibility(View.GONE);
    }

    private String checkServiceName(String jsonString){
        StandardWebServiceResponse standardWebServiceResponse = new StandardWebServiceResponse();
        Gson gson = new Gson();
        standardWebServiceResponse = gson.fromJson(jsonString, StandardWebServiceResponse.class);
        return standardWebServiceResponse.getServiceName();
    }

    void callServerToShowReviews(){
        Map<String,String> parms = new HashMap<String,String>();
        ConnectionVolley connectionVolley = new ConnectionVolley(getActivity(), Request.Method.POST, Url.getInstance().showClientReviewsURL, this, this, parms, false);
        AppController.getInstance().addToRequestQueue(connectionVolley, "");
    }

    private void callAdapter(){
        clientReviewsAdapter = new ClientReviewsAdapter(getActivity(), reviewModels);
        reviewsListView.setAdapter(clientReviewsAdapter);
    }

    private void decodeJson(String jsonString){
        StandardWebServiceResponse standardWebServiceResponse = new StandardWebServiceResponse();
        Gson gson = new Gson();
        standardWebServiceResponse = gson.fromJson(jsonString, StandardWebServiceResponse.class);
        reviewModels = new ArrayList<>();
        gson = new Gson();
        GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        reviewModels = gson.fromJson(builder.create().toJson(standardWebServiceResponse.getData()), new TypeToken<List<ReviewModel>>(){}.getType());
    }

    @Override
    public void onResponse(Object response) {
        Log.e("response", response.toString());
        try {
            if (checkServiceName(response.toString()).equals("ShowClientReview")){
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
