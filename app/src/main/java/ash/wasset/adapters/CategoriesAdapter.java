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
import ash.wasset.models.Categories;
import ash.wasset.serverconnection.Url;
import ash.wasset.utils.GeneralClass;

/**
 * Created by ahmed on 11/27/16.
 */

public class CategoriesAdapter extends BaseAdapter {

    Activity activity;
    ArrayList<Categories> categoriesModel;
    ViewHolder holder;

    public CategoriesAdapter() {
    }

    public CategoriesAdapter(Activity activity, ArrayList<Categories> categoriesModel) {
        this.activity = activity;
        this.categoriesModel = categoriesModel;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = activity.getLayoutInflater().inflate(R.layout.categories_grid_custom, parent, false);
        holder = new ViewHolder();
        holder.categoryImageView = (ImageView) convertView.findViewById(R.id.categoryImageView);
        holder.categoryNameTextView = (TextView) convertView.findViewById(R.id.categoryNameTextView);

        Picasso.with(activity)
                .load(Url.getInstance().categoriesImageURL + categoriesModel.get(position).getImagePath())
                .placeholder(R.mipmap.logo)
                .into(holder.categoryImageView);
        if (GeneralClass.language.equals("en")){
            holder.categoryNameTextView.setText(categoriesModel.get(position).getEnglishName());
        } else if (GeneralClass.language.equals("ar")){
            holder.categoryNameTextView.setText(categoriesModel.get(position).getArabicName());
        }

        return convertView;
    }

    public int getCount() {
        return categoriesModel.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        private ImageView categoryImageView;
        private TextView categoryNameTextView;
    }
}
