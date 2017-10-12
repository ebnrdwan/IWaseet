package ash.wasset.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

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
import ash.wasset.adapters.CategoriesAdapter;
import ash.wasset.models.Categories;
import ash.wasset.models.ServiceListRequestModel;
import ash.wasset.models.StandardWebServiceResponse;
import ash.wasset.serverconnection.Url;
import ash.wasset.serverconnection.volley.AppController;
import ash.wasset.serverconnection.volley.ConnectionVolley;
import ash.wasset.utils.CheckResponse;
import ash.wasset.utils.GPSTracker;
import ash.wasset.utils.GeneralClass;

/**
 * Created by ahmed on 11/27/16.
 */

public class CategoriesFragment extends Fragment implements AdapterView.OnItemClickListener, Response.Listener,
        Response.ErrorListener, SearchView.OnQueryTextListener  {

    View view;
    GridView categoriesGridView;
    CategoriesAdapter categoriesAdapter;
    String getJson;
    ArrayList<Categories> categories;
    GPSTracker gpsTracker;
    int index;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        view = inflater.inflate(R.layout.categories_fragment, container, false);
        initialization();
        decodeJson(getJson);
        callAdapter();
        onClick();
        return view;
    }

    private void initialization(){
        categoriesGridView = (GridView) view.findViewById(R.id.categoriesGridView);
        getJson = getArguments().getString("json");
        categoriesAdapter = new CategoriesAdapter();
        gpsTracker = new GPSTracker(getActivity());
    }

    private void onClick(){
        categoriesGridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        gpsTracker = new GPSTracker(getActivity());
        if(gpsTracker.canGetLocation()){
            callServerToGetServiesList(i, gpsTracker.getLatitude()+"", gpsTracker.getLongitude()+"");
        } else {
            gpsTracker.showSettingsAlert();
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
    public void onResume() {
        setHasOptionsMenu(true);
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        callServerToSearchCategories(query);
        return false;
    }

    private void callServerToSearchCategories(String key){
        Map<String,String> parms = new HashMap<String,String>();
        parms.put("SearchKey", key);
        Log.e("Parm", parms.toString());
        ConnectionVolley connectionVolley = new ConnectionVolley(getActivity(), Request.Method.POST, Url.getInstance().searchGetCategoriesURL, this, this, parms, true);
        AppController.getInstance().addToRequestQueue(connectionVolley, "");
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void callServerToGetServiesList(int index, String lat, String lng){
        this.index = index;
        Map<String,String> parms = new HashMap<String,String>();
        ServiceListRequestModel serviceListRequestModel = new ServiceListRequestModel();
        serviceListRequestModel.setCatId(categories.get(index).getId()+"");
        serviceListRequestModel.setLat("30.1");
        serviceListRequestModel.setLnd("31.2");
        serviceListRequestModel.setPageNo("0");
        serviceListRequestModel.setSortBy("0");
        Gson gson = new Gson();
        parms = gson.fromJson(gson.toJson(serviceListRequestModel), parms.getClass());
        Log.e("Parm", parms.toString());
        ConnectionVolley connectionVolley = new ConnectionVolley(getActivity(), Request.Method.POST, Url.getInstance().serviceListURL, this, this, parms, true);
        AppController.getInstance().addToRequestQueue(connectionVolley, "");
    }

    private void decodeJson(String jsonString){
        StandardWebServiceResponse standardWebServiceResponse = new StandardWebServiceResponse();
        Gson gson = new Gson();
        standardWebServiceResponse = gson.fromJson(jsonString, StandardWebServiceResponse.class);
        categories = new ArrayList<>();
        gson = new Gson();
        GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        categories = gson.fromJson(builder.create().toJson(standardWebServiceResponse.getData()), new TypeToken<List<Categories>>(){}.getType());
    }

    private void callAdapter(){
        categoriesAdapter = new CategoriesAdapter(getActivity(), categories);
        categoriesGridView.setAdapter(categoriesAdapter);
    }

    private String checkServiceName(String jsonString){
        StandardWebServiceResponse standardWebServiceResponse = new StandardWebServiceResponse();
        Gson gson = new Gson();
        standardWebServiceResponse = gson.fromJson(jsonString, StandardWebServiceResponse.class);
        return standardWebServiceResponse.getServiceName();
    }

    private void toServiceListFragment(String result){
        setHasOptionsMenu(false);
        ServiceListFragment serviceListFragment = new ServiceListFragment();
        Bundle args = new Bundle();
        args.putString("json", result);
        args.putString("categoryId", categories.get(index).getId()+"");
        if (GeneralClass.language.equals("en")){
            args.putString("categoryName", categories.get(index).getEnglishName());
        } else if (GeneralClass.language.equals("ar")){
            args.putString("categoryName", categories.get(index).getArabicName());
        }
        serviceListFragment.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contentView, serviceListFragment).addToBackStack("").commit();
    }

    @Override
    public void onResponse(Object response) {
        Log.e("response", response.toString());
        try {
            if (checkServiceName(response.toString()).equals("GetServiceListSummary")){
                if (CheckResponse.getInstance().checkResponse(getActivity(), response.toString(), false)){
                    toServiceListFragment(response.toString());
                }
            } else if (checkServiceName(response.toString()).equals("SearchGetCategories")){
                if (CheckResponse.getInstance().checkResponse(getActivity(), response.toString(), false)){
                    decodeJson(response.toString());
                    callAdapter();
                }
            }
        } catch (Exception e){
            Toast.makeText(getActivity(), getResources().getString(R.string.connectionError), Toast.LENGTH_LONG).show();
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