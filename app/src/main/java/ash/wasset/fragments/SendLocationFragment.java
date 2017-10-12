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
import android.widget.Toast;

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

import java.util.HashMap;
import java.util.Map;

import ash.wasset.R;
import ash.wasset.models.LocationVariable;
import ash.wasset.models.RequestModel;
import ash.wasset.models.StandardWebServiceResponse;
import ash.wasset.serverconnection.Url;
import ash.wasset.serverconnection.volley.AppController;
import ash.wasset.serverconnection.volley.ConnectionVolley;
import ash.wasset.utils.CheckResponse;
import ash.wasset.utils.GPSTracker;

/**
 * Created by shafeek on 24/07/16.
 */
public class SendLocationFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback, Response.Listener,
        Response.ErrorListener {

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
    String id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.send_location_fragment, container, false);
        id = getArguments().getString("id");
        markerOptions = new MarkerOptions();
        builder = new LatLngBounds.Builder();
        tracker = new GPSTracker(getActivity());
        googleMap = null;
        sendLocationButton = (FloatingActionButton) view.findViewById(R.id.sendLocationButton);
        currentLocationButton = (FloatingActionButton) view.findViewById(R.id.currentLocationButton);

        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        currentLocation();
        sendLocationButton.setOnClickListener(this);
        currentLocationButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()){
                case R.id.currentLocationButton:
                    currentLocation();
                    break;
                case R.id.sendLocationButton:
                    callServerToSendRequest();
                    break;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void callServerToSendRequest(){
        Map<String,String> parms = new HashMap<String,String>();
        RequestModel requestModel=new RequestModel();
        requestModel.setServiceProviderId(id);
        requestModel.setRquestState("3");
        requestModel.setRquestType("1");
        requestModel.setMapLat(lat+"");
        requestModel.setMapLng(lng+"");
        Gson gson = new Gson();
        parms = gson.fromJson(gson.toJson(requestModel), parms.getClass());
        Log.e("Parm", parms.toString());
        ConnectionVolley connectionVolley = new ConnectionVolley(getActivity(), Request.Method.POST, Url.getInstance().sendRequestURL, this, this, parms, true);
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
        LocationManager locationManager = (LocationManager)getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        lat=location.getLatitude();
        lng=location.getLongitude();
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
            if (checkServiceName(response.toString()).equals("SendLocation")){
                if (CheckResponse.getInstance().checkResponse(getActivity(), response.toString(), false)){
                    Toast.makeText(getActivity(), getResources().getString(R.string.requestSent), Toast.LENGTH_LONG).show();
                    getActivity().getSupportFragmentManager().popBackStack();
                    getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
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
}
