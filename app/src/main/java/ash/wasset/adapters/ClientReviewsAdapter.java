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
import ash.wasset.models.ReviewModel;
import ash.wasset.serverconnection.Url;

/**
 * Created by ahmed on 11/27/16.
 */

public class ClientReviewsAdapter extends BaseAdapter {

    Activity activity;
    ArrayList<ReviewModel> reviewModels;
    ViewHolder holder;

    public ClientReviewsAdapter() {
    }

    public ClientReviewsAdapter(Activity activity, ArrayList<ReviewModel> reviewModels) {
        this.activity = activity;
        this.reviewModels = reviewModels;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = activity.getLayoutInflater().inflate(R.layout.review_custom, parent, false);
        holder = new ViewHolder();
        holder.clientImageView = (ImageView) convertView.findViewById(R.id.clientImageView);
        holder.clientNameTextView = (TextView) convertView.findViewById(R.id.clientNameTextView);
        holder.requestDetailsTextView = (TextView) convertView.findViewById(R.id.requestDetailsTextView);
        holder.requestDateTextView = (TextView) convertView.findViewById(R.id.requestDateTextView);

        Picasso.with(activity)
                .load(Url.getInstance().profilePicturesURL + reviewModels.get(position).getClientPic())
                .placeholder(R.mipmap.logo)
                .into(holder.clientImageView);
        holder.clientNameTextView.setText(reviewModels.get(position).getClientName());
        holder.requestDetailsTextView.setText(reviewModels.get(position).getComment());
        holder.requestDateTextView.setText(reviewModels.get(position).getSubmissionDate());

        return convertView;
    }

    public int getCount() {
        return reviewModels.size();
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
