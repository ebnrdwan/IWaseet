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
import ash.wasset.models.SocialMediaModel;
import ash.wasset.serverconnection.Url;

/**
 * Created by ahmed on 11/27/16.
 */

public class SocialMediaAdapter extends BaseAdapter {

    Activity activity;
    ArrayList<SocialMediaModel> socialMediaModelArrayList;
    ViewHolder holder;

    public SocialMediaAdapter() {
    }

    public SocialMediaAdapter(Activity activity, ArrayList<SocialMediaModel> socialMediaModelArrayList) {
        this.activity = activity;
        this.socialMediaModelArrayList = socialMediaModelArrayList;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = activity.getLayoutInflater().inflate(R.layout.social_media_custom, parent, false);
        holder = new ViewHolder();
        holder.socialMediaIconImageView = (ImageView) convertView.findViewById(R.id.socialMediaIconImageView);
        holder.socialMediaNameTextView = (TextView) convertView.findViewById(R.id.socialMediaNameTextView);

        holder.socialMediaNameTextView.setText(socialMediaModelArrayList.get(position).getName());
        Picasso.with(activity)
                .load(Url.getInstance().socialMediasImageURL + socialMediaModelArrayList.get(position).getIcon())
                .placeholder(R.mipmap.logo)
                .into(holder.socialMediaIconImageView);
        return convertView;
    }

    public int getCount() {
        return socialMediaModelArrayList.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        private ImageView socialMediaIconImageView;
        private TextView socialMediaNameTextView;
    }
}
