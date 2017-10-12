package ash.wasset.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ash.wasset.R;
import ash.wasset.models.PriceListModel;

/**
 * Created by ahmed on 11/27/16.
 */

public class PriceListAdapter extends BaseAdapter {

    Activity activity;
    ArrayList<PriceListModel> priceListModels;
    ViewHolder holder;

    public PriceListAdapter() {
    }

    public PriceListAdapter(Activity activity, ArrayList<PriceListModel> priceListModels) {
        this.activity = activity;
        this.priceListModels = priceListModels;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = activity.getLayoutInflater().inflate(R.layout.price_list_custom, parent, false);
        holder = new ViewHolder();
        holder.titleTextView = (TextView) convertView.findViewById(R.id.titleTextView);
        holder.detailsTextView = (TextView) convertView.findViewById(R.id.detailsTextView);
        holder.priceTextView = (TextView) convertView.findViewById(R.id.priceTextView);

        holder.titleTextView.setText(priceListModels.get(position).getOfferName());
        holder.detailsTextView.setText(priceListModels.get(position).getOfferDetails());
        holder.priceTextView.setText(priceListModels.get(position).getPrice() + " " + activity.getResources().getString(R.string.rs));

        return convertView;
    }

    public int getCount() {
        return priceListModels.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        private TextView titleTextView;
        private TextView detailsTextView;
        private TextView priceTextView;
    }
}
