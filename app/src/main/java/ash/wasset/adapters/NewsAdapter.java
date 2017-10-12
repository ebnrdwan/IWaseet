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
import ash.wasset.models.NewsModel;
import ash.wasset.serverconnection.Url;
import ash.wasset.utils.GeneralClass;

/**
 * Created by ahmed on 11/27/16.
 */

public class NewsAdapter extends BaseAdapter {

    Activity activity;
    ArrayList<NewsModel> newsModels;
    ViewHolder holder;

    public NewsAdapter() {
    }

    public NewsAdapter(Activity activity, ArrayList<NewsModel> newsModels) {
        this.activity = activity;
        this.newsModels = newsModels;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = activity.getLayoutInflater().inflate(R.layout.news_custom, parent, false);
        holder = new ViewHolder();
        holder.newsImageView = (ImageView) convertView.findViewById(R.id.newsImageView);
        holder.titleTextView = (TextView) convertView.findViewById(R.id.titleTextView);
        holder.detailsTextView = (TextView) convertView.findViewById(R.id.detailsTextView);
        holder.dateTextView = (TextView) convertView.findViewById(R.id.dateTextView);

        Picasso.with(activity)
                .load(Url.getInstance().newsPicturesURL + newsModels.get(position).getPicturePath())
                .placeholder(R.mipmap.logo)
                .into(holder.newsImageView);

        if (GeneralClass.language.equals("en")){
            holder.titleTextView.setText(newsModels.get(position).getEnglishName());
            holder.detailsTextView.setText(newsModels.get(position).getEnglishDescription());
            holder.dateTextView.setText(newsModels.get(position).getDateOfNew());
        } else if (GeneralClass.language.equals("ar")){
            holder.titleTextView.setText(newsModels.get(position).getArabicName());
            holder.detailsTextView.setText(newsModels.get(position).getArabicDescription());
            holder.dateTextView.setText(newsModels.get(position).getDateOfNew());
        }

        return convertView;
    }

    public int getCount() {
        return newsModels.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        private ImageView newsImageView;
        private TextView titleTextView;
        private TextView detailsTextView;
        private TextView dateTextView;
    }
}
