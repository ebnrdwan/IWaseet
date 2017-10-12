package ash.wasset.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
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
import ash.wasset.adapters.SocialMediaAdapter;
import ash.wasset.models.SocialMediaModel;
import ash.wasset.models.StandardWebServiceResponse;
import ash.wasset.serverconnection.Url;
import ash.wasset.serverconnection.volley.AppController;
import ash.wasset.serverconnection.volley.ConnectionVolley;
import ash.wasset.utils.CheckResponse;

/**
 * Created by ahmed on 1/30/17.
 */

public class ShowSocialMediaFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener, Response.Listener, Response.ErrorListener{

    View view;
    ListView socialMediaListView;
    SocialMediaAdapter socialMediaAdapter;
    ArrayList<SocialMediaModel> socialMediaModelArrayList;
    String getSocialMedia;
    FloatingActionButton addButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.show_social_media_fragment, container, false);
        initialization();
        decodeJson();
        callAdapter();
        set();

        return view;
    }

    private void initialization(){
        getSocialMedia = getArguments().getString("socialMedia");
        socialMediaListView = (ListView) view.findViewById(R.id.socialMediaListView);
        addButton = (FloatingActionButton) view.findViewById(R.id.addButton);
        socialMediaModelArrayList = new ArrayList<>();
    }

    private void decodeJson(){
        Gson gson = new Gson();
        StandardWebServiceResponse standardWebServiceResponse = new StandardWebServiceResponse();
        gson = new Gson();
        standardWebServiceResponse = gson.fromJson(getSocialMedia, StandardWebServiceResponse.class);
        gson = new Gson();
        GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        socialMediaModelArrayList = gson.fromJson(builder.create().toJson(standardWebServiceResponse.getData()), new TypeToken<List<SocialMediaModel>>(){}.getType());
    }

    private void callAdapter(){
        socialMediaAdapter = new SocialMediaAdapter(getActivity(), socialMediaModelArrayList);
        socialMediaListView.setAdapter(socialMediaAdapter);
    }

//    @Override
//    public void onResponse(Object response) {
//        Log.e("response", response.toString());
//        try {
//            if (checkServiceName(response.toString()).equals("GetServiceListSummary")){
//                if (CheckResponse.getInstance().checkResponse(getActivity(), response.toString(), false)){
//                    decodeJson(response.toString());
//                    callAdapter();
//                }
//            } else if (checkServiceName(response.toString()).equals("SearchServiceListSummary")){
//                if (CheckResponse.getInstance().checkResponse(getActivity(), response.toString(), false)){
//                    decodeJson(response.toString());
//                    callAdapter();
//                }
//            } else if (checkServiceName(response.toString()).equals("ShowSocialMedia")){
//                if (CheckResponse.getInstance().checkResponse(getActivity(), response.toString(), false)){
//                    serviceListDetails(index, response.toString());
//                }
//            }
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onErrorResponse(VolleyError error) {
//        VolleyLog.d("responseError", "Error: " + error.getMessage());
//        try {
//            ConnectionVolley.dialog.dismiss();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }

    private void set(){
        addButton.setOnClickListener(this);
        socialMediaListView.setOnItemClickListener(this);
        socialMediaListView.setOnItemLongClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        EditSocialMediaFragment editSocialMediaFragment = new EditSocialMediaFragment();
        Bundle args = new Bundle();
        args.putString("id", socialMediaModelArrayList.get(i).getId());
        args.putString("url", socialMediaModelArrayList.get(i).getUrl());
        editSocialMediaFragment.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.contentView, editSocialMediaFragment).addToBackStack("").commit();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        deleteDialog(i);
        return true;
    }

    private void deleteDialog(final int index){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.delete_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button yesButton = (Button)  dialog.findViewById(R.id.yesButton);
        Button noButton = (Button)  dialog.findViewById(R.id.noButton);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callServerToDeleteSociaMedia(index);
                dialog.dismiss();
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.addButton:
                callServerToGetSociaMedia();
                break;
        }
    }

    private void callServerToDeleteSociaMedia(int index){
        Map<String,String> parms = new HashMap<String,String>();
        parms.put("Id", socialMediaModelArrayList.get(index).getId());
        ConnectionVolley connectionVolley = new ConnectionVolley(getActivity(), Request.Method.POST, Url.getInstance().deleteSocialMediaURL, this, this, parms, true);
        AppController.getInstance().addToRequestQueue(connectionVolley, "");
    }

    private void callServerToGetSociaMedia(){
        Map<String,String> parms = new HashMap<String,String>();
        ConnectionVolley connectionVolley = new ConnectionVolley(getActivity(), Request.Method.POST, Url.getInstance().getAllSocialMediaURL, this, this, parms, true);
        AppController.getInstance().addToRequestQueue(connectionVolley, "");
    }

    private String checkServiceName(String jsonString){
        StandardWebServiceResponse standardWebServiceResponse = new StandardWebServiceResponse();
        Gson gson = new Gson();
        standardWebServiceResponse = gson.fromJson(jsonString, StandardWebServiceResponse.class);
        return standardWebServiceResponse.getServiceName();
    }

    private void toAddSocialMediaFragment(String result){
        AddSocialMediaFragment addSocialMediaFragment = new AddSocialMediaFragment();
        Bundle args = new Bundle();
        args.putString("json", result);
        addSocialMediaFragment.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.contentView, addSocialMediaFragment).addToBackStack("").commit();
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
            if (checkServiceName(response.toString()).equals("ShowSocialMedia")){
                if (CheckResponse.getInstance().checkResponse(getActivity(), response.toString(), false)){
                    toAddSocialMediaFragment(response.toString());
                }
            }
            else if (checkServiceName(response.toString()).equals("DeleteSpUserSocialMedia")){
                if (CheckResponse.getInstance().checkResponse(getActivity(), response.toString(), false)){
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
