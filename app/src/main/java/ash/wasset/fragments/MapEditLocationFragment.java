package ash.wasset.fragments;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import ash.wasset.R;
import ash.wasset.models.LocationVariable;
import ash.wasset.models.StandardWebServiceResponse;
import ash.wasset.models.Users;
import ash.wasset.serverconnection.Url;
import ash.wasset.serverconnection.volley.AppController;
import ash.wasset.serverconnection.volley.ConnectionVolley;
import ash.wasset.utils.CheckResponse;
import ash.wasset.utils.GPSTracker;

/**
 * Created by shafeek on 24/07/16.
 */
public class MapEditLocationFragment extends Fragment implements Response.Listener, Response.ErrorListener, View.OnClickListener, OnMapReadyCallback {

    FloatingActionButton sendLocationButton, currentLocationButton;
    double lat, lng;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    MarkerOptions markerOptions;
    GoogleMap googleMap;
    LatLngBounds.Builder builder;
    GPSTracker tracker;
    LocationVariable locationVariable;
    TextView activityTitle;
    String getJson;
    Users usersModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_location_fragment, container, false);
        markerOptions = new MarkerOptions();
        builder = new LatLngBounds.Builder();
        tracker = new GPSTracker(getActivity());
        googleMap = null;
        sendLocationButton = (FloatingActionButton) view.findViewById(R.id.sendLocationButton);
        currentLocationButton = (FloatingActionButton) view.findViewById(R.id.currentLocationButton);
        getJson = getArguments().getString("json");
        decodeJson(getJson);
        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        currentLocation();
        sendLocationButton.setOnClickListener(this);
        currentLocationButton.setOnClickListener(this);

        return view;
    }

    private void decodeJson(String jsonString){
        StandardWebServiceResponse standardWebServiceResponse = new StandardWebServiceResponse();
        Gson gson = new Gson();
        standardWebServiceResponse = gson.fromJson(jsonString, StandardWebServiceResponse.class);
        gson = new Gson();
        GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        usersModel = gson.fromJson(builder.create().toJson(standardWebServiceResponse.getData()), Users.class);
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()){
                case R.id.currentLocationButton:
                    currentLocation();
                    break;
                case R.id.sendLocationButton:
                    callServerToUpdate();
                    break;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void callServerToUpdate(){
        Map<String,String> parms = new HashMap<String,String>();
        Users users=new Users();
        users.setName("0");
        users.setEmail(usersModel.getEmail());
        users.setShowPhone(usersModel.getShowPhone());
        users.setShopName(usersModel.getShopName());
        users.setMapLat(tracker.getLatitude()+"");
        users.setMapLng(tracker.getLongitude()+"");
        Gson gson = new Gson();
        parms = gson.fromJson(gson.toJson(users).toString(), parms.getClass());
        Log.e("Parm", parms.toString());
        ConnectionVolley connectionVolley = new ConnectionVolley(getActivity(), Request.Method.POST, Url.getInstance().updateUserProfileURL, this, this, parms, true);
        AppController.getInstance().addToRequestQueue(connectionVolley, "");
    }

    private void currentLocation(){
        LocationManager locationManager = (LocationManager)getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        // getting network status
        isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        try
        {
            if(isNetworkEnabled || isGPSEnabled){


//                //googleMap = ((SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map)).getMap();
//                lat=location.getLatitude();
//                lng=location.getLongitude();
//                markerOptions.position(new LatLng(lat,lng)).getPosition();
//                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.location_icon));
//                builder.include(new LatLng(lat, lng));
//                markerOptions.draggable(true);
//                markerOptions.isDraggable();
//                builder.include(new LatLng(lat, lng));
//                googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lng)));
//                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
//                locationVariable = new LocationVariable(lat, lng);
//                googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
//                    @Override
//                    public void onCameraChange(CameraPosition cameraPosition) {
//                        locationVariable.setLat(lat);
//                        locationVariable.setLng(lng);
//                    }
//                });
            }
            else {
                tracker.showSettingsAlert();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {

        lat=Double.parseDouble(usersModel.getMapLat());
        lng=Double.parseDouble(usersModel.getMapLng());
        markerOptions.position(new LatLng(lat,lng)).getPosition();
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.location_icon));
        builder.include(new LatLng(lat, lng));
        markerOptions.draggable(true);
        markerOptions.isDraggable();
        builder.include(new LatLng(lat, lng));
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lng)));
        map.animateCamera(CameraUpdateFactory.zoomTo(15));
        locationVariable = new LocationVariable(lat, lng);
        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                locationVariable.setLat(lat);
                locationVariable.setLng(lng);
            }
        });
    }

//    public void setupMap(double lat, double lng)
//    {
//        googleMap = ((SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map)).getMap();
//        markerOptions.position(new LatLng(lat,lng)).getPosition();
//        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.logo_icon));
//        markerOptions.draggable(true);
//        markerOptions.isDraggable();
//        builder.include(new LatLng(lat, lng));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lng)));
//        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
////        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
////            @Override
////            public void onMarkerDragStart(Marker marker) {
////
////            }
////
////            @Override
////            public void onMarkerDrag(Marker marker) {
////                //Toast.makeText(getActivity(), Double.toString(branchLat) + " " + Double.toString(branchLng), Toast.LENGTH_SHORT).show();
////            }
////
////            @Override
////            public void onMarkerDragEnd(Marker marker) {
////                selectedLat = marker.getPosition().latitude;
////                selectedLng = marker.getPosition().longitude;
////            }
////        });
//        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
//            @Override
//            public void onCameraChange(CameraPosition cameraPosition) {
//                ViewChatFragment.currentLocation = cameraPosition.target.latitude + "-" + cameraPosition.target.longitude + "-1";
//            }
//        });
////        googleMap.addMarker(markerOptions);
//    }

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
