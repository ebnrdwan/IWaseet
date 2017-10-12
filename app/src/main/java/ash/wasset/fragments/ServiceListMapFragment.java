package ash.wasset.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ash.wasset.R;
import ash.wasset.models.ServiceListModel;
import ash.wasset.models.StandardWebServiceResponse;
import ash.wasset.serverconnection.Url;

/**
 * Created by ahmed on 12/5/16.
 */

public class ServiceListMapFragment extends Fragment implements
        GoogleMap.OnMarkerClickListener,
        OnMapReadyCallback,
        View.OnClickListener {

    View view;
    SupportMapFragment mMapFragment;
    FragmentTransaction fragmentTransaction;
    String getJson, getCategoryName;
    ArrayList<ServiceListModel> serviceListModelArrayList;
    TextView categoryNameTextView;
    private Marker mPerth;
    ImageView listImageView;
    Dialog infoDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.service_list_map_fragment, container, false);
        initialization();
        decodeJson(getJson);
        onClick();

        return view;
    }

    private void initialization(){
        mMapFragment = SupportMapFragment.newInstance();
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.mapFragment, mMapFragment);
        fragmentTransaction.commit();
        getJson = getArguments().getString("json");
        getCategoryName = getArguments().getString("categoryName");
        categoryNameTextView = (TextView) view.findViewById(R.id.categoryNameTextView);
        listImageView = (ImageView) view.findViewById(R.id.listImageView);
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

    private void onClick(){
        categoryNameTextView.setText(getCategoryName);
        mMapFragment.getMapAsync(this);
        listImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.listImageView:
                serviceList();
                break;
            case R.id.closeImageView:
                infoDialog.dismiss();
                break;
        }
    }

    private void serviceList(){
        ServiceListFragment serviceListFragment = new ServiceListFragment();
        Bundle args = new Bundle();
        args.putString("json", getJson);
        args.putString("categoryName", getCategoryName);
        serviceListFragment.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.contentView, serviceListFragment).addToBackStack("").commit();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        infoDialog((int) marker.getTag());
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            for (int i = 0; i < serviceListModelArrayList.size(); i++){
                LatLng latLng = new LatLng(serviceListModelArrayList.get(i).getMapLat(), serviceListModelArrayList.get(i).getMapLng());
                if (serviceListModelArrayList.get(i).getIsAdds().equals("true")){
                    mPerth = googleMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.location_star_icon))
                            .position(latLng));
                } else {
                    mPerth = googleMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.location_icon))
                            .position(latLng));
                }
                mPerth.setTag(i);
            }
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(serviceListModelArrayList.get(0).getMapLat(), serviceListModelArrayList.get(0).getMapLng()), 15.0f));
            googleMap.setOnMarkerClickListener(this);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void infoDialog(int position){
        ImageView serviceListImageView;
        TextView serviceNameTextView;
        TextView distanceTextView;
        ImageView locationImageView;
        ImageView callImageView;
        ImageView closeImageView;
        
        infoDialog = new Dialog(getActivity());
        infoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        infoDialog.setContentView(R.layout.service_list_map_view);
        infoDialog.setCancelable(true);
        infoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        serviceListImageView = (ImageView) infoDialog.findViewById(R.id.serviceListImageView);
        serviceNameTextView = (TextView) infoDialog.findViewById(R.id.serviceNameTextView);
        distanceTextView = (TextView) infoDialog.findViewById(R.id.distanceTextView);
        locationImageView = (ImageView) infoDialog.findViewById(R.id.locationImageView);
        callImageView = (ImageView) infoDialog.findViewById(R.id.callImageView);
        closeImageView = (ImageView) infoDialog.findViewById(R.id.closeImageView);

        Picasso.with(getActivity())
                .load(Url.getInstance().profilePicturesURL + serviceListModelArrayList.get(position).getPicturePath())
                .placeholder(R.mipmap.logo)
                .into(serviceListImageView);
        serviceNameTextView.setText(serviceListModelArrayList.get(position).getShopName());

        if (((Double)Double.parseDouble(serviceListModelArrayList.get(position).getDistance())).intValue() > 1000)
            distanceTextView.setText(getResources().getString(R.string.distance) + " " + ((Double)Double.parseDouble(serviceListModelArrayList.get(position).getDistance())).intValue()/1000 + " " + getResources().getString(R.string.km));
        else if (Integer.parseInt(serviceListModelArrayList.get(position).getDistance()) < 1000)
            distanceTextView.setText(getResources().getString(R.string.distance) + " " + ((Double)Double.parseDouble(serviceListModelArrayList.get(position).getDistance())).intValue() + " " + getResources().getString(R.string.m));

        closeImageView.setOnClickListener(this);

        infoDialog.show();
    }
}
