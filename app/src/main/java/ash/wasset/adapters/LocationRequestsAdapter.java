package ash.wasset.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ash.wasset.R;
import ash.wasset.models.LocationRequestModel;
import ash.wasset.serverconnection.Url;

/**
 * Created by ahmed on 11/27/16.
 */

public class LocationRequestsAdapter extends BaseAdapter {

    Activity activity;
    ArrayList<LocationRequestModel> locationRequestModels;
    ViewHolder holder;

    public LocationRequestsAdapter() {
    }

    public LocationRequestsAdapter(Activity activity, ArrayList<LocationRequestModel> locationRequestModels) {
        this.activity = activity;
        this.locationRequestModels = locationRequestModels;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = activity.getLayoutInflater().inflate(R.layout.location_request_custom, parent, false);
        holder = new ViewHolder();
        holder.clientImageView = (ImageView) convertView.findViewById(R.id.clientImageView);
        holder.clientNameTextView = (TextView) convertView.findViewById(R.id.clientNameTextView);
        holder.requestDetailsTextView = (TextView) convertView.findViewById(R.id.requestDetailsTextView);
        holder.requestDateTextView = (TextView) convertView.findViewById(R.id.requestDateTextView);

        Picasso.with(activity)
                .load(Url.getInstance().profilePicturesURL + locationRequestModels.get(position).getClientPic())
                .placeholder(R.mipmap.logo)
                .into(holder.clientImageView);
        holder.clientNameTextView.setText(locationRequestModels.get(position).getClientName());
        holder.requestDetailsTextView.setText(locationRequestModels.get(position).getRequestDetails());
        holder.requestDateTextView.setText(locationRequestModels.get(position).getRequestDate());

        return convertView;
    }

    public int getCount() {
        return locationRequestModels.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        private ImageView clientImageView;
        private TextView clientNameTextView;
        private TextView requestDetailsTextView;
        private TextView requestDateTextView;
    }
}
