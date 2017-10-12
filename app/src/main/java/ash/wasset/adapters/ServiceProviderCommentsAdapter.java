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
import ash.wasset.models.CommentsModel;
import ash.wasset.serverconnection.Url;

/**
 * Created by ahmed on 11/27/16.
 */

public class ServiceProviderCommentsAdapter extends BaseAdapter {

    Activity activity;
    ArrayList<CommentsModel> commentsModelArrayList;
    ViewHolder holder;

    public ServiceProviderCommentsAdapter() {
    }

    public ServiceProviderCommentsAdapter(Activity activity, ArrayList<CommentsModel> commentsModelArrayList) {
        this.activity = activity;
        this.commentsModelArrayList = commentsModelArrayList;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = activity.getLayoutInflater().inflate(R.layout.comments_custom, parent, false);
        holder = new ViewHolder();
        holder.userImageView = (ImageView) convertView.findViewById(R.id.userImageView);
        holder.userNameTextView = (TextView) convertView.findViewById(R.id.userNameTextView);
        holder.commentTextView = (TextView) convertView.findViewById(R.id.commentTextView);
        holder.timeTextView = (TextView) convertView.findViewById(R.id.timeTextView);

        Picasso.with(activity)
                .load(Url.getInstance().categoriesImageURL + commentsModelArrayList.get(position).getClientPic())
                .placeholder(R.mipmap.logo)
                .into(holder.userImageView);
        holder.userNameTextView.setText(commentsModelArrayList.get(position).getClientName());
        holder.commentTextView.setText(commentsModelArrayList.get(position).getReviewText());
        holder.timeTextView.setText(commentsModelArrayList.get(position).getReviewDate());

        return convertView;
    }

    public int getCount() {
        return commentsModelArrayList.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        private ImageView userImageView;
        private TextView userNameTextView;
        private TextView commentTextView;
        private TextView timeTextView;
    }
}
