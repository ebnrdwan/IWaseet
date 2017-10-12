package ash.wasset.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ash.wasset.R;
import ash.wasset.utils.GMapV2Direction;
import ash.wasset.utils.GPSTracker;

/**
 * Created by ahmed on 3/1/17.
 */

public class TrackingFragment extends Fragment implements  GoogleMap.OnMarkerClickListener,
        OnMapReadyCallback {

    View view;
    double lat, lng, myLat, myLng;
    SupportMapFragment mMapFragment;
    FragmentTransaction fragmentTransaction;
    CameraPosition cameraPositionn;
    Calendar calendar;
    String date;
    GPSTracker gpsTracker;
    GoogleMap googleMap;
    GMapV2Direction gMapV2Direction;
    Document document;
    Dialog dialog;
    PolylineOptions polylineOptions;
    LatLng src, des;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tracking_fragment, container, false);
        initialization();
        set();
        return view;
    }

    private void initialization(){
        mMapFragment = SupportMapFragment.newInstance();
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.mapFragment, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync(this);
        lat = Double.parseDouble(getArguments().getString("lat"));
        lng = Double.parseDouble(getArguments().getString("lng"));
        calendar = Calendar.getInstance();
        gpsTracker = new GPSTracker(getActivity());
        myLat = gpsTracker.getLatitude();
        myLng = gpsTracker.getLongitude();
    }

    private void set() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateandTime = sdf.format(new Date());
        date = currentDateandTime;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return true;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.clear();
        this.googleMap = googleMap;
        try {
            LatLng latLngSrc = new LatLng(lat, lng);
            googleMap.addMarker(new MarkerOptions()
                    //.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher))
                    .position(latLngSrc));

            LatLng latLngDes = new LatLng(myLat, myLng);
            googleMap.addMarker(new MarkerOptions()
                    //.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher))
                    .position(latLngDes));

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngSrc, 15.0f));
            src = latLngSrc;
            des = latLngDes;
            new GetDirection().execute();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private class GetDirection extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            gMapV2Direction = new GMapV2Direction();
            document = gMapV2Direction.getDocument(src, des,
                    GMapV2Direction.MODE_DRIVING);
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            try {
                ArrayList<LatLng> directionPoint = gMapV2Direction.getDirection(document);
                polylineOptions = new PolylineOptions().width(5).color(Color.RED);
                for (int i = 0; i < directionPoint.size(); i++)
                    polylineOptions.add(directionPoint.get(i));
                googleMap.addPolyline(polylineOptions);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
            dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.progress_dialog);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }
    }
}
