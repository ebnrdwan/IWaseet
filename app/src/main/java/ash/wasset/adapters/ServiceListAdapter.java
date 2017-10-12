package ash.wasset.adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import ash.wasset.R;
import ash.wasset.models.ServiceListModel;
import ash.wasset.serverconnection.Url;

/**
 * Created by ahmed on 11/27/16.
 */

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
        holder.serviceProviderRatingBar.setVisibility(View.GONE);
        holder.serviceNameTextView.setText(serviceListModels.get(position).getShopName());
        holder.serviceProviderRatingBar.setRating(Float.parseFloat(serviceListModels.get(position).getTotalRate()));
        if (((Double)Double.parseDouble(serviceListModels.get(position).getDistance())).intValue() > 1000)
            holder.distanceTextView.setText(activity.getResources().getString(R.string.distance) + " " + ((Double)Double.parseDouble(serviceListModels.get(position).getDistance())).intValue()/1000 + " " + activity.getResources().getString(R.string.km));
        else if (Integer.parseInt(serviceListModels.get(position).getDistance()) < 1000)
            holder.distanceTextView.setText(activity.getResources().getString(R.string.distance) + " " + ((Double)Double.parseDouble(serviceListModels.get(position).getDistance())).intValue() + " " + activity.getResources().getString(R.string.m));

        holder.callImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
