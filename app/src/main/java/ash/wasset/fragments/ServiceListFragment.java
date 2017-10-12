package ash.wasset.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import ash.wasset.R;
import ash.wasset.models.CommentsModel;
import ash.wasset.models.RequestModel;
import ash.wasset.models.ServiceListModel;
import ash.wasset.models.ServiceListRequestModel;
import ash.wasset.models.StandardWebServiceResponse;
import ash.wasset.serverconnection.Url;
import ash.wasset.serverconnection.volley.AppController;
import ash.wasset.serverconnection.volley.ConnectionVolley;
import ash.wasset.utils.CheckResponse;
import ash.wasset.utils.GPSTracker;
import ash.wasset.utils.Permissions;
//import ServiceListAdapter;

/**
 * Created by ahmed on 11/30/16.
 */

public class ServiceListFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener
        , SearchView.OnQueryTextListener ,Response.Listener, Response.ErrorListener {

    View view;
    ListView serviceListListView;
    ServiceListAdapter serviceListAdapter;
    ArrayList<ServiceListModel> serviceListModelArrayList;
    String getJson, getCategoryName, getCategoryId;
    TextView categoryNameTextView;
    ImageView locationImageView, filterImageView;
    ArrayList<CommentsModel> commentsModelArrayList;
    Permissions permissions;
    final static int CALL_PERMISSION = 1111;
    int callStatus;
    GPSTracker gpsTracker;
    String phone;
    int index;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.service_list_fragment, container, false);
        setHasOptionsMenu(true);
        initialization();
        decodeJson(getJson);
        callAdapter();
        onClick();

        return view;
    }

    private void initialization(){
        serviceListListView = (ListView) view.findViewById(R.id.serviceListListView);
        categoryNameTextView = (TextView) view.findViewById(R.id.categoryNameTextView);
        locationImageView = (ImageView) view.findViewById(R.id.locationImageView);
        filterImageView = (ImageView) view.findViewById(R.id.filterImageView);
        getJson = getArguments().getString("json");
        getCategoryName = getArguments().getString("categoryName");
        getCategoryId = getArguments().getString("categoryId");
        serviceListAdapter = new ServiceListAdapter();
        commentsModelArrayList = new ArrayList<>();
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.locationImageView:
                serviceListMap();
                break;
            case R.id.filterImageView:
                filterDialog();
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.seach_menu, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                if (((AppCompatActivity)getActivity()).getSupportActionBar() != null) {
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.lieghtBlue)));
                }
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                if (((AppCompatActivity)getActivity()).getSupportActionBar() != null) {
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
                }
                return true;
            }
        });
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        callServerToSearchServiesList("", "", query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void callServerToSearchServiesList(String lat, String lng, String key){
        Map<String,String> parms = new HashMap<String,String>();
        ServiceListRequestModel serviceListRequestModel = new ServiceListRequestModel();
        serviceListRequestModel.setCatId(getCategoryId);
        serviceListRequestModel.setLat("30.1");
        serviceListRequestModel.setLnd("31.2");
        serviceListRequestModel.setPageNo("0");
        serviceListRequestModel.setSortBy("0");
        serviceListRequestModel.setSearchKey(key);
        Gson gson = new Gson();
        parms = gson.fromJson(gson.toJson(serviceListRequestModel), parms.getClass());
        Log.e("Parm", parms.toString());
        ConnectionVolley connectionVolley = new ConnectionVolley(getActivity(), Request.Method.POST, Url.getInstance().searchServiceListSummaryURL, this, this, parms, true);
        AppController.getInstance().addToRequestQueue(connectionVolley, "");
    }

    private void serviceListMap(){
        ServiceListMapFragment serviceListMapFragment = new ServiceListMapFragment();
        Bundle args = new Bundle();
        args.putString("json", getJson);
        args.putString("categoryName", getCategoryName);
        serviceListMapFragment.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.contentView, serviceListMapFragment).addToBackStack("").commit();
    }

    private void serviceListDetails(int i, String socialMedia){
        ServiceListDetailsFragment serviceListDetailsFragment = new ServiceListDetailsFragment();
        Bundle args = new Bundle();
        Gson gson = new GsonBuilder().create();
        JsonArray serviceListModelJsonArray = gson.toJsonTree(serviceListModelArrayList).getAsJsonArray();
        args.putString("json", serviceListModelJsonArray.get(i).toString());
        args.putString("categoryName", getCategoryName);
        args.putString("socialMedia", socialMedia);
        serviceListDetailsFragment.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.contentView, serviceListDetailsFragment).addToBackStack("").commit();
    }

    private void onClick(){
        locationImageView.setOnClickListener(this);
        categoryNameTextView.setText(getCategoryName);
        serviceListListView.setOnItemClickListener(this);
        filterImageView.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        callServerToGetSociaMedia(serviceListModelArrayList.get(i).getId());
        index = i;
    }

    private void decodeJson(String jsonString){
        StandardWebServiceResponse standardWebServiceResponse = new StandardWebServiceResponse();
        Gson gson = new Gson();
        standardWebServiceResponse = gson.fromJson(jsonString, StandardWebServiceResponse.class);
        serviceListModelArrayList = new ArrayList<>();
        gson = new Gson();
        GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        serviceListModelArrayList = gson.fromJson(builder.create().toJson(standardWebServiceResponse.getData()), new TypeToken<List<ServiceListModel>>(){}.getType());
    }

    private void callAdapter(){
        serviceListAdapter = new ServiceListAdapter(getActivity(), serviceListModelArrayList);
        serviceListListView.setAdapter(serviceListAdapter);
    }

    private void filterDialog(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.filter_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button distanceButton = (Button)  dialog.findViewById(R.id.distanceButton);
        Button rateButton = (Button)  dialog.findViewById(R.id.rateButton);
        distanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gpsTracker = new GPSTracker(getActivity());
                if(gpsTracker.canGetLocation()){
                    callServerToGetServiesList("0", getCategoryId, gpsTracker.getLatitude()+"", gpsTracker.getLongitude()+"");
                } else {
                    gpsTracker.showSettingsAlert();
                }
                dialog.dismiss();
            }
        });

        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gpsTracker = new GPSTracker(getActivity());
                if(gpsTracker.canGetLocation()){
                    callServerToGetServiesList("1", getCategoryId, gpsTracker.getLatitude()+"", gpsTracker.getLongitude()+"");
                } else {
                    gpsTracker.showSettingsAlert();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void callServerToGetServiesList(String sortBy, String categoryId, String lat, String lng){
        Map<String,String> parms = new HashMap<String,String>();
        ServiceListRequestModel serviceListRequestModel = new ServiceListRequestModel();
        serviceListRequestModel.setCatId(categoryId);
        serviceListRequestModel.setLat("30.1");
        serviceListRequestModel.setLnd("31.2");
        serviceListRequestModel.setPageNo("0");
        serviceListRequestModel.setSortBy(sortBy);
        Gson gson = new Gson();
        parms = gson.fromJson(gson.toJson(serviceListRequestModel), parms.getClass());
        Log.e("Parm", parms.toString());
        ConnectionVolley connectionVolley = new ConnectionVolley(getActivity(), Request.Method.POST, Url.getInstance().serviceListURL, this, this, parms, true);
        AppController.getInstance().addToRequestQueue(connectionVolley, "");
    }

    private void callServerToGetSociaMedia(String serviceProviderId){
        Map<String,String> parms = new HashMap<String,String>();
        parms.put("Id", serviceProviderId);
        ConnectionVolley connectionVolley = new ConnectionVolley(getActivity(), Request.Method.POST, Url.getInstance().socialMediaURL, this, this, parms, true);
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
            if (checkServiceName(response.toString()).equals("GetServiceListSummary")){
                if (CheckResponse.getInstance().checkResponse(getActivity(), response.toString(), false)){
                    decodeJson(response.toString());
                    callAdapter();
                }
            } else if (checkServiceName(response.toString()).equals("SearchServiceListSummary")){
                if (CheckResponse.getInstance().checkResponse(getActivity(), response.toString(), false)){
                    decodeJson(response.toString());
                    callAdapter();
                }
            } else if (checkServiceName(response.toString()).equals("GetSpUserSocialMedias")){
                if (CheckResponse.getInstance().checkResponse(getActivity(), response.toString(), false)){
                    serviceListDetails(index, response.toString());
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

    public ServiceListFragment() {
        super();
    }

    public class ServiceListAdapter extends BaseAdapter{

        Activity activity;
        ArrayList<ServiceListModel> serviceListModels;
        ViewHolder holder;

        public ServiceListAdapter(){
        }

        public ServiceListAdapter(Activity activity, ArrayList<ServiceListModel> serviceListModels) {
            this.activity = activity;
            this.serviceListModels = serviceListModels;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = activity.getLayoutInflater().inflate(R.layout.service_list_custom, parent, false);
            holder = new ViewHolder();
            holder.serviceListImageView = (ImageView) convertView.findViewById(R.id.serviceListImageView);
            holder.serviceNameTextView = (TextView) convertView.findViewById(R.id.serviceNameTextView);
            holder.distanceTextView = (TextView) convertView.findViewById(R.id.distanceTextView);
            holder.locationImageView = (ImageView) convertView.findViewById(R.id.locationImageView);
            holder.callImageView = (ImageView) convertView.findViewById(R.id.callImageView);
            holder.serviceProviderRatingBar = (RatingBar) convertView.findViewById(R.id.serviceProviderRatingBar);

            Picasso.with(activity)
                    .load(Url.getInstance().profilePicturesURL + serviceListModels.get(position).getPicturePath())
                    .placeholder(R.mipmap.logo)
                    .into(holder.serviceListImageView);
            holder.serviceNameTextView.setText(serviceListModels.get(position).getShopName());
            holder.serviceProviderRatingBar.setRating(Float.parseFloat(serviceListModels.get(position).getTotalRate()));
            if (((Double)Double.parseDouble(serviceListModels.get(position).getDistance())).intValue() > 1000)
                holder.distanceTextView.setText(activity.getResources().getString(R.string.distance) + " " + ((Double)Double.parseDouble(serviceListModels.get(position).getDistance())).intValue()/1000 + " " + activity.getResources().getString(R.string.km));
            else if (Integer.parseInt(serviceListModels.get(position).getDistance()) < 1000)
                holder.distanceTextView.setText(activity.getResources().getString(R.string.distance) + " " + ((Double)Double.parseDouble(serviceListModels.get(position).getDistance())).intValue() + " " + activity.getResources().getString(R.string.m));

            holder.callImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    index = position;
                    if (serviceListModels.get(position).getShowPhone().equals("true")){
                        phone = serviceListModels.get(position).getPhoneNumber();
                        callAction();
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.notAllowToCall), Toast.LENGTH_LONG).show();
                    }
                }
            });

            holder.locationImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String uri = String.format(Locale.ENGLISH, "geo:%f,%f", serviceListModels.get(position).getMapLat(), serviceListModels.get(position).getMapLng());
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    activity.startActivity(intent);
                }
            });

            return convertView;
        }

        public int getCount() {
            return serviceListModels.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {
            private ImageView serviceListImageView;
            private TextView serviceNameTextView;
            private TextView distanceTextView;
            private ImageView locationImageView;
            private ImageView callImageView;
            private RatingBar serviceProviderRatingBar;
        }
    }

    private void callAction(){
        if (permissions.hasPermission(getActivity(), Manifest.permission.CALL_PHONE)) {
            callStatus = 1;
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            startActivityForResult(intent, 1);
        } else {
            this.requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case CALL_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callStatus = 1;
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                    startActivityForResult(intent, 1);
                } else {
//                    permission is denied
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onResume() {
        if (callStatus != 0){
            callStatus = 0;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    callResultDialog();
                }
            }, 1000);
        }
        super.onResume();
    }

    private void callResultDialog(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.call_result_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button answeredButton = (Button)  dialog.findViewById(R.id.answeredButton);
        Button notAnsweredButton = (Button)  dialog.findViewById(R.id.notAnsweredButton);
        Button busyButton = (Button)  dialog.findViewById(R.id.busyButton);
        answeredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callServerToSendCallResult("0");
                dialog.dismiss();
            }
        });

        notAnsweredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callServerToSendCallResult("1");
                dialog.dismiss();
            }
        });

        busyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callServerToSendCallResult("2");
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void callServerToSendCallResult(String state){
        Map<String,String> parms = new HashMap<String,String>();
        RequestModel requestModel=new RequestModel();
        requestModel.setServiceProviderId(serviceListModelArrayList.get(index).getId());
        requestModel.setRquestState(state);
        requestModel.setRquestType("0");
        Gson gson = new Gson();
        parms = gson.fromJson(gson.toJson(requestModel), parms.getClass());
        Log.e("Parm", parms.toString());
        ConnectionVolley connectionVolley = new ConnectionVolley(getActivity(), Request.Method.POST, Url.getInstance().callMeURL, this, this, parms, false);
        AppController.getInstance().addToRequestQueue(connectionVolley, "");
    }
}
